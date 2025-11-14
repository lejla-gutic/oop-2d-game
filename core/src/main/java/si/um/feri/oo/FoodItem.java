package si.um.feri.oo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class FoodItem extends FallingObject {
    public FoodItem(TextureRegion texture, float x, float y, float w, float h, float speed) {
        super(texture, x, y, w, h, speed);
    }

    @Override
    public void onCollision(GuticGameOO game) {
        game.getScoreSystem().addPoints(1);
        game.playYumSound();
    }
}
