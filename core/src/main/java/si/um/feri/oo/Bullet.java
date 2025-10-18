package si.um.feri.oo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;

public class Bullet extends DynamicGameObject implements Pool.Poolable{
    public boolean activeBullet = false;    // da znam da li se koristi metak
    public Bullet(Texture texture, float x, float y, float w, float h, float speed) {
        super(texture, x, y, w, h, speed);
    }

    @Override
    public void update(float dt) {
        if (!activeBullet) return;  // ako nije aktivan, ne interesuje me
        y += speed * dt;
        rect.setPosition(x, y);
    }

    @Override
    public void reset() {
        // resetujem kada se metak oslobodi u pool
        x = -1000;
        y = -1000;
        activeBullet = false;
    }
}
