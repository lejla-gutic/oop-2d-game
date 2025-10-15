package si.um.feri.oo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Bullet extends DynamicGameObject{
    public Bullet(Texture texture, float x, float y, float w, float h, float speed) {
        super(texture, x, y, w, h, speed);
    }

    @Override
    public void update(float dt) {
        y += speed * dt;
        rect.setPosition(x, y);
    }
}
