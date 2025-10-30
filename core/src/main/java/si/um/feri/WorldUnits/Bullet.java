package si.um.feri.WorldUnits;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {
    public float x, y, w, h;
    public float bulletSpeed;
    public Texture texture;
    public Rectangle rect = new Rectangle();

    public Bullet(Texture texture, float x, float y, float w, float h, float bulletSpeed) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.bulletSpeed = bulletSpeed;
        rect.set(x, y, w, h);
    }

    public void update(float dt){
        y += bulletSpeed * dt;
        rect.setPosition(x, y);
    }
}
