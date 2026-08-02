import app.App;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import config.Storage;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args){
        Storage storage = Storage.getInstance();

        try {
            UIManager.setLookAndFeel(
                    storage.getDarkTheme()
                            ? new FlatMacDarkLaf()
                            : new FlatMacLightLaf()
            );
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        FlatJetBrainsMonoFont.install();
        FlatInterFont.install();

        UIManager.put("defaultFont", new Font(FlatInterFont.FAMILY, Font.PLAIN, 13));

        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.init();
            app.addComponent();
        });
    }
}