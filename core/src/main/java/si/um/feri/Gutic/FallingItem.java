package si.um.feri.Gutic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
public class FallingItem {
    public float x, y, w, h;
    public float fallSpeed;
    public boolean isFood;
    public Texture texture;
    public Rectangle rect = new Rectangle(); // za provjeru sudara

    public FallingItem(Texture texture, float x, float y, float w, float h, float fallSpeed, boolean isFood) {
        this.texture = texture;
        this.x = x; this.y = y;
        this.w = w; this.h = h;
        this.fallSpeed = fallSpeed;
        this.isFood = isFood;
        rect.set(x, y, w, h);
    }

    public void update(float dt) {
        y -= fallSpeed * dt; // pomjeri se na dole brzinom fallSpeed skalirano s vremenom
        rect.setPosition(x, y); // azuriramo rectangle da htbox prati objekt
    }
}
