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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Component

public class fileController {
    @CrossOrigin("*")
    @PostMapping("/api/v1/file_integrator")
    public void processFile(@RequestParam("File") MultipartFile file,
                            @RequestParam("entitiesString") String entitiesString,
                            @RequestParam("configString") String configString){

        Config config=convertConfigStringToConfig(configString);
        Map<String,ArrayList<Champ>> entities = convertEntitiesStringToMap(entitiesString);
        String initialRepositorySourceCode=DefaultCode.RepositorySourceCode;
        String initialEntitySourceCode =DefaultCode.EntitySourceCode;
        String entityName = "";
        String[] codes=prepareEntityCode(entities,initialRepositorySourceCode,initialEntitySourceCode,config);

        initialRepositorySourceCode=codes[0];
        initialEntitySourceCode=codes[1];
        entityName=codes[2];

        createFilesFromCode(entityName,initialRepositorySourceCode,initialEntitySourceCode);

        List<String> fileContent=getFileString(file);
        processFile(fileContent,entities);
    }
    private Config convertConfigStringToConfig(String configString){
        ObjectMapper objectMapper = new ObjectMapper();
        Config config= new Config();
        try {
            config = objectMapper.readValue(configString,new TypeReference<Config>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }
    private  Map<String, ArrayList<Champ>> convertEntitiesStringToMap(String entitiesString){
        Map<String,ArrayList<Champ>> entities=new HashMap<String,ArrayList<Champ>>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, ArrayList<Champ>> map = objectMapper.readValue(entitiesString,new TypeReference<Map<String, ArrayList<Champ>>>() {});
            for (String entity : map.keySet()){
                entities.put(entity, (map.get(entity)));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }
    private String[] prepareEntityCode(Map<String,ArrayList<Champ>> entities,String initialRepositorySourceCode,String initialEntitySourceCode,Config config ){
        String[] code=new String[3];
        String className="";
        for(String entity: entities.keySet()){
            className=entity;
            initialEntitySourceCode=String.format(initialEntitySourceCode,config.getSchema(), entity,entity);
            Champ champ;
            for(int i=0;i<entities.get(entity).size();i++){
                champ = entities.get(entity).get(i);
                initialEntitySourceCode+="private "+champ.getType()+" "+champ.getValeur()+";\n" +
                        "public void "+"set"+champ.getValeur()+"("+champ.getType() + " "+ champ.getValeur()+"){\n" +
                        "this."+champ.getValeur()+"=" +champ.getValeur()+";\n" +
                        "}\n";
            }
            initialEntitySourceCode+="}";
        }
        initialRepositorySourceCode=String.format(initialRepositorySourceCode,className,className);
        code[0]=initialRepositorySourceCode;
        code[1]=initialEntitySourceCode;
        code[2]=className;
        return code;
    }
    public void createFilesFromCode(String entityName,String RepositorySourceCode,String EntitySourceCode ){
        String filePath="C:\\Users\\bilel\\Desktop\\entityCreator\\executable\\src\\main\\java\\mss\\pfe\\fileIntegrator\\entities\\";
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        File sourceFile1 = new File(entityName + ".java");
        File sourceFile2 = new File(entityName + "Repo" +".java");
        try {
            Writer writer1 = new FileWriter(filePath+sourceFile1);
            Writer writer2 = new FileWriter(filePath+sourceFile2);
            writer1.write(EntitySourceCode);
            writer2.write(RepositorySourceCode);
            writer1.close();
            writer2.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void processFile(List<String> fileContent,Map<String,ArrayList<Champ>> entities){
        for (String table : entities.keySet()){

            String className= table;
            String object =table.toLowerCase();
            int fileLineIndex =0 ;
            String processFileSourceCode=DefaultCode.processFileSourceCode;
            String code ="";

            for(String line:fileContent){
                fileLineIndex++;
                ArrayList<Champ> champs=entities.get(table);
                code+=className+ " "+ object + fileLineIndex + "= new "+table+ "();\n";

                for(int i=0;i<champs.size();i++){
                    Champ champ = champs.get(i);
                    String champContent=line.substring(champ.getValeur_min(),champ.getValeur_max());

                    code+=object + fileLineIndex+".set"+champ.getValeur()+"("+champContent+");\n";
                    System.out.println(champContent);
                }
                code+=object + fileLineIndex+".save("+object + fileLineIndex+");\n";

            }
            processFileSourceCode=String.format(processFileSourceCode,code);
            System.out.println(processFileSourceCode);
        }
    }
    private List<String> getFileString(MultipartFile file){
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
    public void createFileProcessingCode(){

    }
    @PostMapping("/api/v1/compile")
    public void compile(){
        try {
            String path="C:\\Users\\bilel\\Desktop\\entityCreator\\executable ";
            Runtime rt =Runtime.getRuntime();
            Process p1= rt.exec("cmd /c cd "+path+" && mvn clean install > mvn.txt");
            p1.waitFor(20, TimeUnit.SECONDS);

            Process p2= Runtime.getRuntime().exec("cmd /c cd "+path+"\\target && java -jar fileIntegrator-0.0.1-SNAPSHOT.jar  >ok.txt");
            p2.waitFor(40, TimeUnit.SECONDS);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    }
