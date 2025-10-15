package si.um.feri.Gutic.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import si.um.feri.oo.GuticGameOO;

/** Launches the desktop (LWJGL3) application for the object-oriented version. */
public class Lwjgl3LauncherOO {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        // 🔸 pokrećemo novu OO verziju igre
        return new Lwjgl3Application(new GuticGameOO(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("GuticGame OO Version");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setWindowedMode(800, 800);
        configuration.setResizable(true);
        configuration.setWindowIcon(
            "libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png"
        );
        return configuration;
    }
}
