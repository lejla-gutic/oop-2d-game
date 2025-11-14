package si.um.feri.assets;

import com.badlogic.gdx.assets.AssetManager;

public class Assets {
    private static final AssetManager INSTANCE = new AssetManager();

    public static AssetManager get() {
        return INSTANCE;
    }

    private Assets() {}
}
