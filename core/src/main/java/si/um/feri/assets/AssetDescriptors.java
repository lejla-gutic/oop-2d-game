package si.um.feri.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetDescriptors {

    public static final AssetDescriptor<TextureAtlas> ATLAS =
        new AssetDescriptor<>(AssetPaths.GAMEPLAY_ATLAS, TextureAtlas.class);
    public static final AssetDescriptor<Sound> YUM_SOUND =
        new AssetDescriptor<>(AssetPaths.SOUND_YUM, Sound.class);

    public static final AssetDescriptor<Sound> EW_SOUND =
        new AssetDescriptor<>(AssetPaths.SOUND_EW, Sound.class);

    public static final AssetDescriptor<Sound> SHOOT_SOUND =
        new AssetDescriptor<>(AssetPaths.SOUND_SHOOT, Sound.class);

    public static final AssetDescriptor<BitmapFont> UI_FONT_SMALL =
        new AssetDescriptor<>(AssetPaths.UI_FONT_SMALL, BitmapFont.class);

    private AssetDescriptors() {
    }
}
