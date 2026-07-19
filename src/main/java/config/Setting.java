package config;

public class Setting {
    private static final Setting INSTANCE = new Setting();

    public static final String APP_NAME = "Codev";

    private Setting() {
        ThemeConfig.getInstance();
    }

    public static Setting getInstance() {
        return INSTANCE;
    }
}
