package mss.pfe.fileIntegrator;

import mss.pfe.fileIntegrator.code_generation.CodeGenerator;
import mss.pfe.fileIntegrator.code_generation.FileInsertionCode;
import mss.pfe.fileIntegrator.code_generation.SpringBatchCode;
import mss.pfe.fileIntegrator.code_writer.CodeWriter;
import mss.pfe.fileIntegrator.entities.Champ;
import mss.pfe.fileIntegrator.entities.CodeGenerated;
import mss.pfe.fileIntegrator.mapper.ConfigMapper;
import mss.pfe.fileIntegrator.mapper.EntityMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class fileControllerTest {
    EntityMapper entityMapper =new EntityMapper();
    ConfigMapper configMapper = new ConfigMapper();
    CodeWriter codeWriter =new CodeWriter() ;
    FileInsertionCode fileInsertionCode = new FileInsertionCode();
    CodeGenerator codeGenerator =new CodeGenerator(new SpringBatchCode());

    CodeGeneratorController CodeGeneratorController =new CodeGeneratorController(entityMapper,configMapper,codeWriter,fileInsertionCode,codeGenerator);

    @Test
    public void  getEntityNameTest(){
        HashMap<String , ArrayList<Champ>> entity=new HashMap<>();
        entity.put("entityName",new ArrayList<>());
        entity.put("bilel",new ArrayList<>());
        assertTrue( this.CodeGeneratorController.getEntityName(entity)=="entityName");
    }

}