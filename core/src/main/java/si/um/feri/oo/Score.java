package si.um.feri.oo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
public class Score {
    private int points;
    private int lives;
    private boolean gameOver;

    private Texture heartFull;
    private Texture heartEmpty;
    private ParticleEffect[] heartGlows;

    private static final float HEART_SIZE = 36f;
    private static final float SPACING = 8f;
    private static final float MARGIN_RIGHT = 20f;
    private static final float MARGIN_TOP = 48f;
    private static final int START_LIVES = 3;
    private static final float GAME_AREA_W = 800f;
    private static final float GAME_AREA_H = 800f;

    public Score(int startLives) {
        this.points = 0;
        this.lives = startLives;
        this.gameOver = false;

        heartFull = new Texture(Gdx.files.internal("images/GuticGame/full_heart.png"));
        heartEmpty = new Texture(Gdx.files.internal("images/GuticGame/empty_heart.png"));

        heartGlows = new ParticleEffect[startLives];
        for (int i = 0; i < startLives; i++) {
            heartGlows[i] = new ParticleEffect();
            heartGlows[i].load(Gdx.files.internal("particles/glow.p"), Gdx.files.internal("particles"));
            heartGlows[i].start();
        }
    }

    public void render(SpriteBatch batch) {
        float totalWidth = (HEART_SIZE + SPACING) * START_LIVES;
        float startX = GAME_AREA_W - totalWidth - MARGIN_RIGHT;
        float y = GAME_AREA_H - MARGIN_TOP;

        for (int i = 0; i < START_LIVES; i++) {
            float hx = startX + i * (HEART_SIZE + SPACING) + HEART_SIZE / 2f;
            float hy = y + HEART_SIZE / 2f;

            // Glow samo za aktivna srca
            if (i < lives) {
                heartGlows[i].setPosition(hx, hy);
                heartGlows[i].update(Gdx.graphics.getDeltaTime());
                heartGlows[i].draw(batch);
            }

            Texture current = (i < lives) ? heartFull : heartEmpty;
            batch.draw(current, startX + i * (HEART_SIZE + SPACING), y, HEART_SIZE, HEART_SIZE);
        }
    }

    public void addPoints(int amount) {
        points += amount;
    }

    public void loseLife() {
        if (!gameOver) {
            lives--;
            if (lives <= 0) {
                lives = 0;
                gameOver = true;
            }
        }
    }

    public int getPoints() {
        return points;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void reset(int startLives) {
        points = 0;
        lives = startLives;
        gameOver = false;

        for (ParticleEffect effect : heartGlows) {
            effect.reset();
            effect.start();
        }
    }

    public void dispose() {
        heartFull.dispose();
        heartEmpty.dispose();
        for (ParticleEffect effect : heartGlows) {
            effect.dispose();
        }
    }

}
