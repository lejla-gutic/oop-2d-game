package si.um.feri.oo;

import com.badlogic.gdx.graphics.Texture;
public abstract class DynamicGameObject extends GameObject{
    public float speed;

    public DynamicGameObject(Texture texture, float x, float y, float w, float h, float speed) {
        super(texture, x, y, w, h);
        this.speed = speed;
    }

    // poziva se svakog frame-a, dt => koliko je vremena proslo od zadnjeg frame-a
    public abstract void update(float dt);
}
