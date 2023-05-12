package mss.pfe.fileIntegrator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mss.pfe.fileIntegrator.entities.Champ;
import mss.pfe.fileIntegrator.entities.Config;
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
    String filePath = "C:\\Users\\bilel\\Desktop\\entityCreator\\executable\\src\\main\\java\\mss\\pfe\\fileIntegrator\\entities\\";

    @CrossOrigin("*")
    @PostMapping("/api/v1/file_integrator")
    public void processFile(@RequestParam("File") MultipartFile file,
                            @RequestParam("entitiesString") String entitiesString,
                            @RequestParam("configString") String configString) {

        Config config = convertConfigStringToConfig(configString);
        Map<String, ArrayList<Champ>> entities = convertEntitiesStringToMap(entitiesString);
        String initialRepositorySourceCode = DefaultCode.RepositorySourceCode;
        String initialEntitySourceCode = DefaultCode.EntitySourceCode;
        String entityName = "";
        String[] codes = prepareEntityCode(entities, initialRepositorySourceCode, initialEntitySourceCode, config);

        saveFileIntoExecutable(file) ;
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
            config = objectMapper.readValue(configString, new TypeReference<Config>() {});
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
                initialEntitySourceCode += "private " + champ.getType() + " " + champ.getValeur() + ";\n" +
                        "public void " + "set" + champ.getValeur() + "(" + champ.getType() + " " + champ.getValeur() + "){\n" +
                        "this." + champ.getValeur() + "=" + champ.getValeur() + ";\n" +
                        "}\n";
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
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
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
            String saveObjectLine1 =DefaultCode.saveObjectLine1;
            String saveObjectLine2 =DefaultCode.saveObjectLine2;
            String saveObjectLine3 = DefaultCode.saveObjectLine3;
            String processorConstructor = DefaultCode.processorConstructor  ;
            String processFileSourceCode = DefaultCode.processFileSourceCode;
            String repoName= className+"Repo";
            String champContent="" ;
            String codeOfALine="";
            int fileLineIndex = 0;
            int fileNumber=0;
            //customer1
            String currentObjectName;

            repoDeclaration=handleRepoDeclaration(repoDeclaration,repoName);
            System.out.println(repoDeclaration);

            for (String line : fileContent) {
                currentObjectName=setCurrentObjectName(className,fileLineIndex);
                System.out.println(fileLineIndex);
                processorConstructor=String.format(processorConstructor,fileLineIndex,repoName,repoName,repoName,repoName);
                System.out.println(processorConstructor);


                saveObjectLine1=handleSaveObjectLine1(saveObjectLine1,className,currentObjectName);

                codeOfALine+=saveObjectLine1+"\n";
                //reinitialise line
                ArrayList<Champ> champs = entities.get(className);
                for (int i = 0; i < champs.size(); i++) {
                    Champ champ = champs.get(i);
                    champContent = line.substring(champ.getValeur_min(), champ.getValeur_max());
                    saveObjectLine2=handleSaveObjectLine2(saveObjectLine2,champContent,champ,currentObjectName);
                    codeOfALine+=saveObjectLine2+"\n";
                    saveObjectLine2=DefaultCode.saveObjectLine2;

                }
                saveObjectLine3=String.format(saveObjectLine3,repoName,currentObjectName);
                codeOfALine+=saveObjectLine3+"\n";
                System.out.println("*****\n"+codeOfALine+"*****\n");
                saveObjectLine1=DefaultCode.saveObjectLine1;
                saveObjectLine3=DefaultCode.saveObjectLine3;
                processFileSourceCode=String.format(processFileSourceCode,fileLineIndex,repoDeclaration,processorConstructor,fileLineIndex,codeOfALine);
                processorConstructor=DefaultCode.processorConstructor;
                codeOfALine="";

                try {
                    Writer writer1 = new FileWriter(filePath + "Processor"+fileLineIndex+".java");
                    writer1.write(processFileSourceCode);
                    writer1.close();
                    processFileSourceCode=DefaultCode.processFileSourceCode;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                fileLineIndex++;
            }


            System.out.println(processFileSourceCode);
        }
    }

    private String handleSaveObjectLine2(String saveObjectLine2, String champContent, Champ champ,String currentObjectName ) {
       return String.format(saveObjectLine2,currentObjectName,champ.getValeur(),champContent);
    }


    private String setCurrentObjectName(String className, int fileLineIndex) {
        return className+fileLineIndex;
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
    public void compile() {
        try {
            String path = "C:\\Users\\bilel\\Desktop\\entityCreator\\executable ";
            Runtime rt = Runtime.getRuntime();
            Process p1 = rt.exec("cmd /c cd " + path + " && mvn clean install > mvn.txt");
            p1.waitFor(20, TimeUnit.SECONDS);

            Process p2 = Runtime.getRuntime().exec("cmd /c cd " + path + "\\target && java -jar fileIntegrator-0.0.1-SNAPSHOT.jar  >ok.txt");
            p2.waitFor(40, TimeUnit.SECONDS);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void saveFileIntoExecutable(MultipartFile file){
        String filePath="C:\\Users\\bilel\\Desktop\\entityCreator\\executable\\src\\main\\java\\mss\\pfe\\fileIntegrator\\File\\";
        String fileName = file.getOriginalFilename();
        File FileInExecutable= new File(filePath+fileName);
        try {
            FileCopyUtils.copy(file.getBytes(), FileInExecutable);
        } catch (IOException e) {
            System.out.println("cannot create File");
        }
    }
    public String handleRepoDeclaration(String repoDeclarationDefaultCode,String repoName){
        return String.format(repoDeclarationDefaultCode,repoName,repoName);
    }
    public String handleSaveObjectLine1(String saveObjectLine1,String className,String currentObjectName){
        return saveObjectLine1=String.format(saveObjectLine1,className,currentObjectName,className);
    }
}
