package si.um.feri.oo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class DynamicGameObject extends GameObject{
    public float speed;

    public DynamicGameObject(TextureRegion texture, float x, float y, float w, float h, float speed) {
        super(texture, x, y, w, h);
        this.speed = speed;
    }

    public abstract void update(float dt);
}
