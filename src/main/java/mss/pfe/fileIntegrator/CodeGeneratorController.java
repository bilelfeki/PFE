package mss.pfe.fileIntegrator;

import mss.pfe.fileIntegrator.code_generation.*;
import mss.pfe.fileIntegrator.code_writer.CodeWriter;
import mss.pfe.fileIntegrator.entities.Champ;
import mss.pfe.fileIntegrator.entities.CodeGenerated;
import mss.pfe.fileIntegrator.entities.Config;
import mss.pfe.fileIntegrator.mapper.ConfigMapper;
import mss.pfe.fileIntegrator.mapper.EntityMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Component

public class CodeGeneratorController {
    private final EntityMapper entityMapper;
    private final ConfigMapper configMapper;
    private final CodeWriter codeWriter;
    private final FileInsertionCode fileInsertionCode;
    private  final CodeGenerator codeGenerator;

    public CodeGeneratorController(EntityMapper entityMapper,
                                   ConfigMapper configMapper,
                                   CodeWriter codeWriter,
                                   FileInsertionCode fileInsertionCode,
                                   CodeGenerator codeGenerator) {
        this.entityMapper = entityMapper;
        this.configMapper = configMapper;
        this.codeWriter = codeWriter;
        this.fileInsertionCode = fileInsertionCode;
        this.codeGenerator=codeGenerator;
    }


    String userHome= System.getProperty("user.home");

    String filePath = userHome +"\\Desktop\\executable\\src\\main\\java\\mss\\pfe\\fileIntegrator\\entities\\";

    @CrossOrigin("*")
    @PostMapping("/api/v1/file_integrator")
    public CodeGenerated generateFileInsertionCode(@RequestParam("File") MultipartFile file, @RequestParam("entitiesString") String entitiesString, @RequestParam("configString") String configString) {
        System.out.println(userHome);

        System.out.println(entitiesString);
        Config config = configMapper.convertConfigStringToConfig(configString);
        Map<String, ArrayList<Champ>> entities = entityMapper.convertEntitiesStringToMap(entitiesString);
        String repositorySourceCode ;
        String entitySourceCode ;
        String entityName;

        CodeGenerated codeGenerated = new CodeGenerated();

        saveFileIntoExecutable(file);
        repositorySourceCode = this.codeGenerator.generateRepositoryCode(entities, RepositoryCode.RepositorySourceCode);
        entitySourceCode = this.codeGenerator.generateEntitySourceCode(entities, EntityCode.DefaultEntitySourceCode, config);

        entityName = this.getEntityName(entities);
        writeCodeIntoFiles(entityName, repositorySourceCode, entitySourceCode);
        generateFileInsertionCode(entities);

        codeGenerated.setEntityCode(entitySourceCode);
        codeGenerated.setRepoCode(repositorySourceCode);
        codeGenerated.setSpringBatchCode(codeGenerator.generateSpringBatchCode(this.getEntityName(entities),file.getName()));

        return codeGenerated;
    }

    public String getEntityName(Map<String, ArrayList<Champ>> entities) {
        return entities.keySet().stream().findFirst().get();
    }

    public void writeCodeIntoFiles(String entityName, String RepositorySourceCode, String EntitySourceCode) {
        this.codeWriter.writeContentIntoPath(EntitySourceCode, filePath, entityName);
        this.codeWriter.writeContentIntoPath(RepositorySourceCode, filePath, entityName + "Repo");
    }


    private void generateFileInsertionCode(Map<String, ArrayList<Champ>> entities) {
        String className = this.getEntityName(entities);
        String repoDeclaration = FileInsertionCode.repoDeclaration;
        String objectInstantiation = FileInsertionCode.instantiateObject;
        String objectSetter = FileInsertionCode.setObjectCode;
        String saveObjectWithRepoCode = FileInsertionCode.saveObjectWithRepoCode;
        String fileInsertionClassCode;
        String fileInsertionMethodCode;
        String FileInsertionConstructorCode = FileInsertionCode.FileInsertionConstructorCode;
        String repoName = className + "Repo";
        String objectName;
        repoDeclaration = this.fileInsertionCode.formatRepoDeclaration(repoDeclaration, repoName);

        objectName = setObjectName(className);
        FileInsertionConstructorCode = fileInsertionCode.formatFileInsertionConstructorCode(FileInsertionConstructorCode,repoName);
        objectInstantiation = handleObjectInstantiation(objectInstantiation, className, objectName);

        String lineSubstring = "";
        String forLoopCode = "";
        forLoopCode += objectInstantiation;
        ArrayList<Champ> champs = entities.get(className);
        for (int i = 0; i < champs.size(); i++) {
            Champ champ = champs.get(i);
            if (champ.getType().trim().equals("String")) {
                lineSubstring = "line.substring(" + champ.getValeur_min() + "," + champ.getValeur_max() + ")";
            } else {
                lineSubstring = "Integer.valueOf(line.substring(" + champ.getValeur_min() + "," + champ.getValeur_max() + "))";
            }
            forLoopCode += this.fileInsertionCode.formatObjectSetter(objectSetter, lineSubstring, champ, objectName) + ");\n";
            objectSetter = FileInsertionCode.setObjectCode;
        }
        saveObjectWithRepoCode = String.format(saveObjectWithRepoCode, repoName, objectName);

        forLoopCode += saveObjectWithRepoCode;
        fileInsertionMethodCode=fileInsertionCode.formatFileInsertionMethodCode(FileInsertionCode.fileInsertionMethodCode,forLoopCode);
        fileInsertionClassCode = String.format(FileInsertionCode.fileInsertionClassCode, repoDeclaration, FileInsertionConstructorCode,fileInsertionMethodCode );
        this.codeWriter.writeContentIntoPath(fileInsertionClassCode, filePath, "Processor1");
    }



    private String setObjectName(String className) {
        return className + "_";
    }


    @PostMapping("/api/v1/compile")
    public void compileExecutableProject() {
        String userHome= System.getProperty("user.home");
        String ProjectPath = userHome +"\\Desktop\\executable ";
        String cleanInstallCommand = "cmd /c cd " + ProjectPath + " && mvn clean install > mvn.txt";
        String runJarCommand = "cmd /c cd " + ProjectPath + "\\target && java -jar fileIntegrator-0.0.1-SNAPSHOT.jar  >ok.txt";

        try {
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



    public String handleObjectInstantiation(String objectSetter, String className, String objectName) {
        return String.format(objectSetter, className, objectName);
    }

    public void saveFileIntoExecutable(MultipartFile file) {
        String userHome= System.getProperty("user.home");

        String filePath = userHome +"\\Desktop\\executable\\src\\main\\java\\mss\\pfe\\fileIntegrator\\File\\";
        String fileName = file.getOriginalFilename();
        File FileInExecutable = new File(filePath + fileName);
        try {
            FileCopyUtils.copy(file.getBytes(), FileInExecutable);
        } catch (IOException e) {
            System.out.println("cannot create File");
        }

    }
}
