package mss.pfe.fileIntegrator;

import mss.pfe.fileIntegrator.code_writer.CodeWriter;
import mss.pfe.fileIntegrator.entities.Champ;
import mss.pfe.fileIntegrator.mapper.ConfigMapper;
import mss.pfe.fileIntegrator.mapper.EntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class fileControllerTest {
    EntityMapper entityMapper =new EntityMapper();
    ConfigMapper configMapper = new ConfigMapper();
    CodeWriter codeWriter =new CodeWriter() ;

    fileController fileController=new fileController(entityMapper,configMapper,codeWriter);

    @Test
    public void  getEntityNameTest(){
        HashMap<String , ArrayList<Champ>> entity=new HashMap<>();
        entity.put("entityName",new ArrayList<>());
        entity.put("bilel",new ArrayList<>());
        assertTrue( this.fileController.getEntityName(entity)=="entityName");
    }

}