package mss.pfe.fileIntegrator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mss.pfe.fileIntegrator.entities.Champ;
import mss.pfe.fileIntegrator.entities.Config;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        String[] codes=prepareEntityCode(entities,initialRepositorySourceCode,initialEntitySourceCode);

        initialRepositorySourceCode=codes[0];
        initialEntitySourceCode=codes[1];
        entityName=codes[2];

        createFilesFromCode(entityName,initialRepositorySourceCode,initialEntitySourceCode);
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
    private String[] prepareEntityCode(Map<String,ArrayList<Champ>> entities,String initialRepositorySourceCode,String initialEntitySourceCode){
        String[] code=new String[3];
        String className="";
        for(String entity: entities.keySet()){
            className=entity;
            initialEntitySourceCode=String.format(initialEntitySourceCode, entity,entity);
            Champ champ;
            for(int i=0;i<entities.get(entity).size();i++){
                champ = entities.get(entity).get(i);
                initialEntitySourceCode+="private "+champ.getType()+" "+champ.getValeur()+";\n";
            }
            initialEntitySourceCode+="}";
        }
        initialRepositorySourceCode=String.format(initialRepositorySourceCode,className,className);
        code[0]=initialRepositorySourceCode;
        code[1]=initialEntitySourceCode;
        code[2]=className;
        return code;
    }
    public void createFilesFromCode(String className,String RepositorySourceCode,String EntitySourceCode ){
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        File sourceFile1 = new File(className + ".java");
        File sourceFile2 = new File(className + "Repo" +".java");
        Writer writer1 = null;
        Writer writer2=null;
        try {
            String filePath="C:\\Users\\bilel\\Desktop\\entityCreator\\fileIntegrator\\src\\main\\java\\mss\\pfe\\fileIntegrator/";
            writer1 = new FileWriter(filePath+sourceFile1);
            writer2= new FileWriter(filePath+sourceFile2);
            writer1.write(EntitySourceCode);
            writer2.write(RepositorySourceCode);
            writer1.close();
            writer2.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }
