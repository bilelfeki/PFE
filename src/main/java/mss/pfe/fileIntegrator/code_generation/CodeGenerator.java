package mss.pfe.fileIntegrator.code_generation;

import mss.pfe.fileIntegrator.entities.Champ;
import mss.pfe.fileIntegrator.entities.Config;
import mss.pfe.fileIntegrator.entities.SpringBatchCodeGenerated;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class CodeGenerator {
    private final SpringBatchCode springBatchCode;
    public CodeGenerator(SpringBatchCode springBatchCode) {
        this.springBatchCode = springBatchCode;
    }

    public String generateRepositoryCode(Map<String, ArrayList<Champ>> entities, String initialRepositorySourceCode) {
        String entityName = this.getEntityName(entities);
        return String.format(initialRepositorySourceCode, entityName);
    }
    public String getEntityName(Map<String, ArrayList<Champ>> entities) {
        return entities.keySet().stream().findFirst().get();
    }
    public String generateEntitySourceCode(Map<String, ArrayList<Champ>> entities, String initialEntitySourceCode, Config config) {
        String updatedEntitySourceCode;
        String entityName = this.getEntityName(entities);
        updatedEntitySourceCode = String.format(initialEntitySourceCode, config.getSchema(), entityName, entityName);
        ArrayList<Champ> champs = entities.get(entityName);
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
    public SpringBatchCodeGenerated generateSpringBatchCode(String objectName,String fileName){
        return this.springBatchCode.formatAll(objectName,fileName);
    }
}
