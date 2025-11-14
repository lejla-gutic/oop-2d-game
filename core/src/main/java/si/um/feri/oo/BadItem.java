package si.um.feri.oo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BadItem extends FallingObject {
    public BadItem(TextureRegion texture, float x, float y, float w, float h, float speed) {
        super(texture, x, y, w, h, speed);
    }

    @Override
    public void onCollision(GuticGameOO game) {
        if (!game.hasShield()) {
            game.getScoreSystem().loseLife();
            game.playEwSound();
        }
    }
}
