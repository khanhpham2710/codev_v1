package config;

import enums.ETheme;
import org.fife.ui.rsyntaxtextarea.Theme;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class ThemeConfig {
    private static final ThemeConfig INSTANCE = new ThemeConfig();

    private final Map<ETheme, Theme> themes = new EnumMap<>(ETheme.class);

    private ThemeConfig() {
        loadThemes();
    }

    public static ThemeConfig getInstance() {
        if (Objects.isNull(INSTANCE)){
            return new ThemeConfig();
        }

        return INSTANCE;
    }

    private void loadThemes() {
        loadTheme(ETheme.MONOKAI, "/themes/monokai.xml");
        loadTheme(ETheme.ECLIPSE, "/themes/eclipse.xml");
        loadTheme(ETheme.NIGHT, "/themes/night.xml");
        loadTheme(ETheme.RED, "/themes/red.xml");
        loadTheme(ETheme.BLUE, "/themes/blue.xml");
        loadTheme(ETheme.PURPLE, "/themes/purple.xml");
    }

    private void loadTheme(ETheme themeName, String resourcePath) {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {

            if (is == null) {
                throw new IllegalArgumentException("Theme not found: " + resourcePath);
            }

            themes.put(themeName, Theme.load(is));

        } catch (IOException e) {
            throw new RuntimeException("Cannot load theme: " + resourcePath, e);
        }
    }

    public Theme getTheme(ETheme themeName) {
        return themes.getOrDefault(themeName, themes.get(ETheme.MONOKAI));
    }

}
