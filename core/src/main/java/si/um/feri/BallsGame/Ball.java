package si.um.feri.BallsGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class Ball {
    float x, y;
    float radius;
    float speedY;
    float accelerationY;
    Color color;

    private static final float BOUNCE_LOSS = 0.8f;
    private static final float GAME_WIDTH = 800;

    public Ball(float x, float y) {
        this.x = x;
        this.y = y;
        this.radius = MathUtils.random(15f, 50f);
        this.color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
        this.speedY = 0f;
        this.accelerationY = MathUtils.random(-800f, -400f);
    }

    public void update(float dt) {
        speedY += accelerationY * dt;  // gravitacija
        y += speedY * dt;

        if (y - radius < 0) {
            y = radius;
            speedY = -speedY * BOUNCE_LOSS; // odbijanje od tla
        }

        if (x - radius < 0) {
            x = radius;
        }
        if (x + radius > GAME_WIDTH) {
            x = GAME_WIDTH - radius;
        }
    }
}
