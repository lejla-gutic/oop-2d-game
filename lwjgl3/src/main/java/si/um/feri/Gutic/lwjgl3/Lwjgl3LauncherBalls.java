package si.um.feri.Gutic.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import si.um.feri.BallsGame.BallsGame;

/** Launches the desktop (LWJGL3) application for the BallsGame. */
public class Lwjgl3LauncherBalls {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new BallsGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Balls Game");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setWindowedMode(800, 800);
        configuration.setResizable(true);
        configuration.setWindowIcon(
            "libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png"
        );

        configuration.setBackBufferConfig(8, 8, 8, 8, 16, 0, 8);
        return configuration;
    }
}
