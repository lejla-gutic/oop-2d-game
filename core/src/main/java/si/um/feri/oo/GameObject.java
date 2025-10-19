package si.um.feri.oo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
public class GameObject {
    public float x, y;
    public float w, h;

    public Texture texture;

    public Rectangle rect = new Rectangle();

    public GameObject(Texture texture, float x, float y, float w, float h) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        rect.set(x, y, w, h);
    }




}

