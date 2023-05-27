package mss.pfe.fileIntegrator.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mss.pfe.fileIntegrator.entities.Config;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConfigMapper {
    public ConfigMapper() {
    }

    public Config convertConfigStringToConfig(String configString) {
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
}
