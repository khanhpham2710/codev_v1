package config;

import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import enums.ETheme;

import java.awt.*;

public class Setting {

    private static final Setting INSTANCE = new Setting();

    public static final String APP_NAME = "Codev";

    private ETheme currentTheme = ETheme.MONOKAI;

    private final Font editorFont = new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 18);


    private Setting() {
        ThemeConfig.getInstance();
    }

    public static Setting getInstance() {
        return INSTANCE;
    }


    public ETheme getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(ETheme currentTheme) {
        this.currentTheme = currentTheme;
    }

    public Font getEditorFont() {
        return editorFont;
    }
}
