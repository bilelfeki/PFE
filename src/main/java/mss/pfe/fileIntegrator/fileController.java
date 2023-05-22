package mss.pfe.fileIntegrator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mss.pfe.fileIntegrator.entities.Champ;
import mss.pfe.fileIntegrator.entities.Config;
import org.springframework.stereotype.Component;
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
    String filePath = "C:\\Users\\bilel\\Desktop\\entityCreator\\executable\\src\\main\\java\\mss\\pfe\\fileIntegrator\\entities\\";

    @CrossOrigin("*")
    @PostMapping("/api/v1/file_integrator")
    public void processFile(@RequestParam("File") MultipartFile file, @RequestParam("entitiesString") String entitiesString, @RequestParam("configString") String configString) {
        System.out.println(entitiesString);
        Config config = convertConfigStringToConfig(configString);
        Map<String, ArrayList<Champ>> entities = convertEntitiesStringToMap(entitiesString);
        String initialRepositorySourceCode = DefaultCode.RepositorySourceCode;
        String initialEntitySourceCode = DefaultCode.EntitySourceCode;
        String entityName = "";
        String[] codes = prepareEntityCode(entities, initialRepositorySourceCode, initialEntitySourceCode, config);

        initialRepositorySourceCode = codes[0];
        initialEntitySourceCode = codes[1];
        entityName = codes[2];

        createFilesFromCode(entityName, initialRepositorySourceCode, initialEntitySourceCode);

        List<String> fileContent = getFileString(file);
        processFile(fileContent, entities);
    }

    private Config convertConfigStringToConfig(String configString) {
        ObjectMapper objectMapper = new ObjectMapper();
        Config config = new Config();
        try {
            config = objectMapper.readValue(configString, new TypeReference<Config>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }

    private Map<String, ArrayList<Champ>> convertEntitiesStringToMap(String entitiesString) {
        Map<String, ArrayList<Champ>> entities = new HashMap<String, ArrayList<Champ>>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, ArrayList<Champ>> map = objectMapper.readValue(entitiesString, new TypeReference<Map<String, ArrayList<Champ>>>() {
            });
            for (String entity : map.keySet()) {
                entities.put(entity, (map.get(entity)));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }

    private String[] prepareEntityCode(Map<String, ArrayList<Champ>> entities, String initialRepositorySourceCode, String initialEntitySourceCode, Config config) {
        String[] code = new String[3];
        String className = "";
        for (String entity : entities.keySet()) {
            className = entity;
            initialEntitySourceCode = String.format(initialEntitySourceCode, config.getSchema(), entity, entity);
            Champ champ;
            for (int i = 0; i < entities.get(entity).size(); i++) {
                champ = entities.get(entity).get(i);
                initialEntitySourceCode += "private " + champ.getType() + " " + champ.getValeur() + ";\n" + "public void " + "set" + champ.getValeur() + "(" + champ.getType() + " " + champ.getValeur() + "){\n" + "this." + champ.getValeur() + "=" + champ.getValeur() + ";\n" + "}\n";
            }
            initialEntitySourceCode += "}";
        }
        initialRepositorySourceCode = String.format(initialRepositorySourceCode, className, className);
        code[0] = initialRepositorySourceCode;
        code[1] = initialEntitySourceCode;
        code[2] = className;
        return code;
    }

    public void createFilesFromCode(String entityName, String RepositorySourceCode, String EntitySourceCode) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        File sourceFile1 = new File(entityName + ".java");
        File sourceFile2 = new File(entityName + "Repo" + ".java");
        try {
            Writer writer1 = new FileWriter(filePath + sourceFile1);
            Writer writer2 = new FileWriter(filePath + sourceFile2);
            writer1.write(EntitySourceCode);
            writer2.write(RepositorySourceCode);
            writer1.close();
            writer2.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processFile(List<String> fileContent, Map<String, ArrayList<Champ>> entities) {
        for (String className : entities.keySet()) {
            String repoDeclaration = DefaultCode.repoDeclaration;
            String saveObjectLine1 = DefaultCode.saveObjectLine1;
            String saveObjectLine2 = DefaultCode.saveObjectLine2;
            String saveObjectLine3 = DefaultCode.saveObjectLine3;
            String processorConstructor = DefaultCode.processorConstructor;
            String repoName = className + "Repo";
            int fileLineIndex = 1;
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
                    var = "line.substring(" + champ.getValeur_min() + "," + champ.getValeur_max() + ")";
                    var2 += handleSaveObjectLine2(saveObjectLine2, var, champ, currentObjectName) +");\n";
                    saveObjectLine2 = DefaultCode.saveObjectLine2;
                }

                saveObjectLine3 = String.format(saveObjectLine3, repoName, className+"1");

                    var2+=saveObjectLine3;
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

    private List<String> getFileString(MultipartFile file) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
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
            p2.waitFor(40, TimeUnit.SECONDS);
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
}
