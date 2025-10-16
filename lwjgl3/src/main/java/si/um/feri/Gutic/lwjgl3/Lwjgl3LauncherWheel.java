package si.um.feri.Gutic.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import si.um.feri.WheelGame.WheelGame;

public class Lwjgl3LauncherWheel {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new WheelGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Wheel Game");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setWindowedMode(800, 800);
        configuration.setResizable(true);
        configuration.setBackBufferConfig(8, 8, 8, 8, 16, 0, 4);
        configuration.setWindowIcon(
            "libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png"
        );
        return configuration;
    }
}
