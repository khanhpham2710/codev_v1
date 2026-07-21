package config;

import enums.EPropertyKey;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {
    private final Properties properties = new Properties();
    private static final ConfigProperties INSTANCE = new ConfigProperties();

    private ConfigProperties(){
        loadProperties();
    }

    private void loadProperties(){
        try (InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConfigProperties getInstance(){
        return INSTANCE;
    }

    public String getProperties(EPropertyKey propertyKey){
        return properties.getProperty(propertyKey.getKey());
    }
}
