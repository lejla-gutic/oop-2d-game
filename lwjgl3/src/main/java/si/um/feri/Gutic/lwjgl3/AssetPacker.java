package si.um.feri.Gutic.lwjgl3;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AssetPacker {

    private static final boolean DEBUG = false;

    private static final String RAW_ASSETS_PATH = "lwjgl3/assets-raw";
    private static final String ASSETS_PATH = "core/assets";

    public static void main(String[] args) {

        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.debug = DEBUG;
        settings.maxWidth = 8192;
        settings.maxHeight = 8192;

        TexturePacker.process(
            settings,
            RAW_ASSETS_PATH + "/images",
            ASSETS_PATH + "/gameplay",
            "game"
        );

        System.out.println("✔ Atlas generated in core/assets/game.atlas");
    }
}
