package mss.pfe.fileIntegrator.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mss.pfe.fileIntegrator.entities.Champ;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class EntityMapper {
    public EntityMapper() {
    }

    public Map<String, ArrayList<Champ>> convertEntitiesStringToMap(String entitiesString) {
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
}
