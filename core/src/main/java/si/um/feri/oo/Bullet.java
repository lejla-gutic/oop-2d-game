package si.um.feri.oo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class Bullet extends DynamicGameObject implements Pool.Poolable{
    public boolean activeBullet = false;
    public Bullet(TextureRegion texture, float x, float y, float w, float h, float speed) {
        super(texture, x, y, w, h, speed);
    }

    @Override
    public void update(float dt) {
        if (!activeBullet) return;
        y += speed * dt;
        rect.setPosition(x, y);
    }

    @Override
    public void reset() {
        x = -1000;
        y = -1000;
        speed = 400f;
        activeBullet = false;
    }
}
