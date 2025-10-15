package si.um.feri.oo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
public class Player extends GameObject {
    public float moveSpeed;
    public float gameWidth;

    public Player(Texture texture, float x, float y, float w, float h, float moveSpeed, float gameWidth) {
        super(texture, x, y, w, h);
        this.moveSpeed = moveSpeed;
        this.gameWidth = gameWidth;
    }

    public void moveLeft(float dt) {
        x -= moveSpeed * dt;
        if (x < 0) {
            x = 0;
        }
        rect.setPosition(x, y);
    }

    public void moveRight(float dt) {
        x += moveSpeed * dt;
        if (x + w > gameWidth) {
            x = gameWidth - w;
        }
        rect.setPosition(x, y);
    }

    // pravougaonik za sudar
    public Rectangle getRect() {
        return rect;
    }

}
