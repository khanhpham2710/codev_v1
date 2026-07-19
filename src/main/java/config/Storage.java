package config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import enums.ETheme;
import helpers.PasswordHelper;
import helpers.TokenHelper;

import java.io.File;
import java.io.IOException;

public class Storage {

    private static final File CONFIG_FILE = new File("config.json");

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private static Storage instance;

    private ETheme theme = ETheme.MONOKAI;
    private String lastProjectPath = null;
    private String userName;
    private String passwordHash;
    private String token;
    private Boolean rememberMe = false;

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

            if (storage.userName == null || storage.userName.isBlank()) {
                storage.userName = null;
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

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return userName;
    }

    public void setPasswordHash(String passwordHash){
        this.passwordHash= passwordHash;
    }

    public String getPasswordHash(){
        return passwordHash;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setLastProjectPath(String lastProjectPath) {
        if (lastProjectPath == null || lastProjectPath.isBlank()){
            this.lastProjectPath = null;
        } else {
            this.lastProjectPath = lastProjectPath;
        }
    }

    public void setRememberMe(boolean rememberMe){
        this.rememberMe = rememberMe;
    }

    public Boolean getRememberMe(){
        return rememberMe;
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

    @JsonIgnore
    public boolean isValidToken(){
        return TokenHelper.isValidToken(this.token);
    }
}