package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import enums.ETheme;

import java.io.File;
import java.io.IOException;

public class Storage {

    private static final File CONFIG_FILE = new File("config.json");

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private static Storage instance;

    private ETheme theme = ETheme.MONOKAI;
    private String lastProjectPath = null;

    private Storage() {

    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    private static Storage load() {
        if (!CONFIG_FILE.exists()) {
            Storage storage = new Storage();
            storage.save();
            return storage;
        }

        try {
            Storage storage = MAPPER.readValue(CONFIG_FILE, Storage.class);

            if (storage.theme == null || !isValidTheme(storage.theme.toString())) {
                storage.theme = ETheme.MONOKAI;
            }

            if (storage.lastProjectPath == null || storage.lastProjectPath.isBlank()) {
                storage.lastProjectPath = System.getProperty("user.home");
            }

            return storage;

        } catch (IOException e) {
            e.printStackTrace();

            Storage storage = new Storage();
            storage.save();

            return storage;
        }
    }

    public void save() {
        try {
            File parent = CONFIG_FILE.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            if (!CONFIG_FILE.exists()) {
                CONFIG_FILE.createNewFile();
            }

            MAPPER.writeValue(CONFIG_FILE, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ETheme getTheme() {
        return theme;
    }

    public void setTheme(ETheme theme) {
        this.theme = theme;
    }

    public String getLastProjectPath() {
        return lastProjectPath;
    }

    public void setLastProjectPath(String lastProjectPath) {
        if (lastProjectPath == null || lastProjectPath.isBlank()){
            this.lastProjectPath = null;
        } else {
            this.lastProjectPath = lastProjectPath;
        }
    }

    public static File getConfigFile() {
        return CONFIG_FILE;
    }

    private static boolean isValidTheme(String value) {
        for (ETheme theme : ETheme.values()) {
            if (theme.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}