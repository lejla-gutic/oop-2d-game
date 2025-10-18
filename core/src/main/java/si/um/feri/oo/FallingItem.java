package si.um.feri.oo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;

public class FallingItem extends DynamicGameObject implements Pool.Poolable{
    public boolean isFood;
    public boolean activeItem = false;

    public FallingItem(Texture texture, float x, float y, float w, float h, float speed, boolean isFood) {
        super(texture, x, y, w, h, speed);
        this.isFood = isFood;
    }

    @Override
    public void update(float dt) {
        if (!activeItem) return;
        y -= speed * dt;
        rect.setPosition(x, y);
    }

    @Override
    public void reset() {
        x = 0;
        y = 0;
        isFood = false;
        activeItem = false;
    }
}
