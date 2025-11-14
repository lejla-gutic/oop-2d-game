package si.um.feri.oo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public abstract class FallingObject extends DynamicGameObject implements Pool.Poolable{
    public boolean activeItem = false;

    public FallingObject(TextureRegion texture, float x, float y, float w, float h, float speed) {
        super(texture, x, y, w, h, speed);
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
        activeItem = false;
    }

    public abstract void onCollision(GuticGameOO game);
}
