package si.um.feri.oo;

import com.badlogic.gdx.graphics.Texture;
public class PowerUpItem extends FallingObject{
    public PowerUpItem(Texture texture, float x, float y, float w, float h, float speed) {
        super(texture, x, y, w, h, speed);
    }

    @Override
    public void onCollision(GuticGameOO game) {
        game.activateShield(5f);
        game.playYumSound();
    }
}
