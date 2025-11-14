package si.um.feri.oo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PowerUpItem extends FallingObject{
    private ParticleEffect sparkleEffect;
    public PowerUpItem(TextureRegion texture, float x, float y, float w, float h, float speed) {
        super(texture, x, y, w, h, speed);

        sparkleEffect = new ParticleEffect();
        sparkleEffect.load(Gdx.files.internal("particles/sparkle.p"), Gdx.files.internal("particles"));
        sparkleEffect.start();
    }

    @Override
    public void reset() {
        super.reset();
        sparkleEffect.reset();
        sparkleEffect.start();
    }

    @Override
    public void onCollision(GuticGameOO game) {
        game.activateShield(5f);
        game.playYumSound();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        sparkleEffect.setPosition(x + w / 2f, y + h / 2f);
        sparkleEffect.update(dt);
    }

    public void drawEffect(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        sparkleEffect.draw(batch);
    }

    public void dispose() {
        sparkleEffect.dispose();
    }
}
