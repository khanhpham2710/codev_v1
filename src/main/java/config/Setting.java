package config;

import enums.ETheme;
import org.fife.ui.rsyntaxtextarea.Theme;

import java.io.IOException;
import java.io.InputStream;

public class Setting {
    private static final Setting INSTANCE = new Setting();

    private Theme monokaiTheme;
    private Theme eclipseTheme;
    private Theme nightTheme;
    private Theme redTheme;
    private Theme blueTheme;
    private Theme purpleTheme;

    private Setting() {
        loadThemes();
    }

    public static Setting getInstance() {
        return INSTANCE;
    }

    private void loadThemes() {
        try (InputStream monokai = getClass().getResourceAsStream("/themes/monokai.xml");
             InputStream eclipse = getClass().getResourceAsStream("/themes/eclipse.xml");
             InputStream night = getClass().getResourceAsStream("/themes/night.xml");
             InputStream red = getClass().getResourceAsStream("/themes/red.xml");
             InputStream blue = getClass().getResourceAsStream("/themes/blue.xml");
             InputStream purple = getClass().getResourceAsStream("/themes/purple.xml")) {
            monokaiTheme = Theme.load(monokai);
            eclipseTheme = Theme.load(eclipse);
            nightTheme = Theme.load(night);
            redTheme = Theme.load(red);
            blueTheme = Theme.load(blue);
            purpleTheme = Theme.load(purple);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load themes", e);
        }
    }

    public Theme getTheme(ETheme themeName) {
        return switch (themeName) {
            case ETheme.ECLIPSE -> eclipseTheme;
            case ETheme.NIGHT -> nightTheme;
            case ETheme.RED -> redTheme;
            case ETheme.BLUE -> blueTheme;
            case ETheme.PURPLE -> purpleTheme;
            default -> monokaiTheme;
        };
    }
}
