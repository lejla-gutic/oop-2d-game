package si.um.feri.oo;

import com.badlogic.gdx.graphics.Texture;
import com.sun.tools.javac.code.Symbol;

public class FallingItem extends DynamicGameObject {
    public boolean isFood;

    public FallingItem(Texture texture, float x, float y, float w, float h, float speed, boolean isFood) {
        super(texture, x, y, w, h, speed);
        this.isFood = isFood;
    }

    @Override
    public void update(float dt) {
        y -= speed * dt;
        rect.setPosition(x, y);
    }
}
