package si.um.feri.oo;

import com.badlogic.gdx.graphics.Texture;
public class BadItem extends FallingObject {
    public BadItem(Texture texture, float x, float y, float w, float h, float speed) {
        super(texture, x, y, w, h, speed);
    }

    @Override
    public void onCollision(GuticGameOO game) {
        if (!game.hasShield()) {
            game.decreaseLife();
            game.playEwSound();
        }
    }
}
