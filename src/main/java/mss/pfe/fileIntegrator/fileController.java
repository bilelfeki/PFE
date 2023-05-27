package mss.pfe.fileIntegrator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mss.pfe.fileIntegrator.code_generation.DefaultCode;
import mss.pfe.fileIntegrator.code_generation.EntityCode;
import mss.pfe.fileIntegrator.code_generation.RepositoryCode;
import mss.pfe.fileIntegrator.code_writer.CodeWriter;
import mss.pfe.fileIntegrator.entities.Champ;
import mss.pfe.fileIntegrator.entities.Config;
import mss.pfe.fileIntegrator.mapper.ConfigMapper;
import mss.pfe.fileIntegrator.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.tools.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Component

public class fileController {
    private final EntityMapper entityMapper;
    private final ConfigMapper configMapper;
    private final CodeWriter codeWriter ;

    public fileController(EntityMapper entityMapper, ConfigMapper configMapper,CodeWriter codeWriter) {
        this.entityMapper = entityMapper;
        this.configMapper = configMapper;
        this.codeWriter=codeWriter;
    }


    String filePath = "C:\\Users\\bilel\\Desktop\\entityCreator\\executable\\src\\main\\java\\mss\\pfe\\fileIntegrator\\entities\\";

    @CrossOrigin("*")
    @PostMapping("/api/v1/file_integrator")
    public void processFile(@RequestParam("File") MultipartFile file, @RequestParam("entitiesString") String entitiesString, @RequestParam("configString") String configString) {
        saveFileIntoExecutable(file);

        System.out.println(entitiesString);
        Config config = configMapper.convertConfigStringToConfig(configString);
        Map<String, ArrayList<Champ>> entities = entityMapper.convertEntitiesStringToMap(entitiesString);
        String initialRepositorySourceCode = RepositoryCode.RepositorySourceCode;
        String initialEntitySourceCode = EntityCode.DefaultEntitySourceCode;
        String entityName;

        initialRepositorySourceCode = this.updateInitialRepositoryCode(entities,initialRepositorySourceCode);
        initialEntitySourceCode = this.updateInitialEntitySourceCode(entities,initialEntitySourceCode,config);
        entityName = this.getEntityName(entities);
        createFilesFromCode(entityName, initialRepositorySourceCode, initialEntitySourceCode);
        processFile(entities);
    }
    public String updateInitialRepositoryCode(Map<String, ArrayList<Champ>> entities, String initialRepositorySourceCode){
        String entityName=this.getEntityName(entities);
        return  String.format(initialRepositorySourceCode,entityName, entityName);
    }
    public String updateInitialEntitySourceCode(Map<String, ArrayList<Champ>> entities,String initialEntitySourceCode,Config config){
        String updatedEntitySourceCode;
        String entityName=this.getEntityName(entities);
        updatedEntitySourceCode = String.format(initialEntitySourceCode, config.getSchema(), entityName, entityName);
        ArrayList<Champ> champs =entities.get(entityName);
        Champ champ;
        for (int i = 0; i < champs.size(); i++) {
                champ = champs.get(i);
                updatedEntitySourceCode += "private " + champ.getType() + " "
                        + champ.getValeur() + ";\n" + "public void "
                        + "set" + champ.getValeur() + "("
                        + champ.getType() + " " + champ.getValeur()
                        + "){\n" + "this." + champ.getValeur() + "="
                        + champ.getValeur() + ";\n" + "}\n";
            }
            updatedEntitySourceCode += "}";
        return updatedEntitySourceCode;
    }

    public String getEntityName(Map<String, ArrayList<Champ>> entities) {
        return entities.keySet().stream().findFirst().get();
    }

    public void createFilesFromCode(String entityName, String RepositorySourceCode, String EntitySourceCode) {
        this.codeWriter.writeContentIntoPath(EntitySourceCode,filePath,entityName);
        this.codeWriter.writeContentIntoPath(RepositorySourceCode,filePath,entityName+"Repo");
    }


    private void processFile( Map<String, ArrayList<Champ>> entities) {
        for (String className : entities.keySet()) {
            String repoDeclaration = DefaultCode.repoDeclaration;
            String saveObjectLine1 = DefaultCode.saveObjectLine1;
            String saveObjectLine2 = DefaultCode.saveObjectLine2;
            String saveObjectLine3 = DefaultCode.saveObjectLine3;
            String processorConstructor = DefaultCode.processorConstructor;
            String repoName = className + "Repo";
            Integer fileLineIndex = 1;
            String currentObjectName;
            repoDeclaration = handleRepoDeclaration(repoDeclaration, repoName);

            currentObjectName = setCurrentObjectName(className, fileLineIndex);
            processorConstructor = String.format(processorConstructor, fileLineIndex, repoName, repoName, repoName, repoName);
            saveObjectLine1 = handleSaveObjectLine1(saveObjectLine1, className, currentObjectName);

            String var = "";
            String var2 = saveObjectLine1;
            ArrayList<Champ> champs = entities.get(className);
            for (int i = 0; i < champs.size(); i++) {
                Champ champ = champs.get(i);
                if (champ.getType().trim().equals("String")) {
                    var = "line.substring(" + champ.getValeur_min() + "," + champ.getValeur_max() + ")";
                } else {
                    var = "Integer.valueOf(" + "line.substring(" + champ.getValeur_min() + "," + champ.getValeur_max() + "))";
                }
                var2 += handleSaveObjectLine2(saveObjectLine2, var, champ, currentObjectName) + ");\n";
                saveObjectLine2 = DefaultCode.saveObjectLine2;

            }

            saveObjectLine3 = String.format(saveObjectLine3, repoName, className + "1");

            var2 += saveObjectLine3;
            try {
                Writer writer1 = new FileWriter(filePath + "Processor" + fileLineIndex + ".java");
                writer1.write(
                        String.format(DefaultCode.finalProcessor, fileLineIndex, repoDeclaration, processorConstructor, fileLineIndex, String.format(DefaultCode.forLoop, var2))
                );
                writer1.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private String handleSaveObjectLine2(String saveObjectLine2, String champContent, Champ champ, String currentObjectName) {
        return String.format(saveObjectLine2, currentObjectName, champ.getValeur(), champContent);
    }

    private String setCurrentObjectName(String className, int fileLineIndex) {
        return className + fileLineIndex;
    }


    @PostMapping("/api/v1/compile")
    public void compileExecutableProject() {
        try {
            String ProjectPath = "C:\\Users\\bilel\\Desktop\\entityCreator\\executable ";
            String cleanInstallCommand = "cmd /c cd " + ProjectPath + " && mvn clean install > mvn.txt";
            String runJarCommand = "cmd /c cd " + ProjectPath + "\\target && java -jar fileIntegrator-0.0.1-SNAPSHOT.jar  >ok.txt";
            Runtime rt = Runtime.getRuntime();
            Process p1 = rt.exec(cleanInstallCommand);
            p1.waitFor(20, TimeUnit.SECONDS);
            Process p2 = Runtime.getRuntime().exec(runJarCommand);
            //
            // p2.waitFor(40, TimeUnit.SECONDS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String handleRepoDeclaration(String repoDeclarationDefaultCode, String repoName) {
        return String.format(repoDeclarationDefaultCode, repoName, repoName);
    }

    public String handleSaveObjectLine1(String saveObjectLine1, String className, String currentObjectName) {
        return saveObjectLine1 = String.format(saveObjectLine1, className, currentObjectName, className);
    }

    public void saveFileIntoExecutable(MultipartFile file) {
        String filePath = "C:\\Users\\bilel\\Desktop\\entityCreator\\executable\\src\\main\\java\\mss\\pfe\\fileIntegrator\\File\\";
        String fileName = file.getOriginalFilename();
        File FileInExecutable = new File(filePath + fileName);
        try {
            FileCopyUtils.copy(file.getBytes(), FileInExecutable);
        } catch (IOException e) {
            System.out.println("cannot create File");
        }

    }
}
