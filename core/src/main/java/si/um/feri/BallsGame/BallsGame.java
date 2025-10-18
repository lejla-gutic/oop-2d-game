package si.um.feri.BallsGame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture;

public class BallsGame extends ApplicationAdapter  {
    // crtanje
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Texture background;

    // polje svih loptica
    private Array<Ball> balls;

    // konstante igrice
    private static final float GAME_WIDTH = 800;
    private static final float GAME_HEIGHT = 800;
    private static final float GRAVITY = -600f; // px/s^2 (negativno = prema dolje)
    private static final float BOUNCE_LOSS = 0.8f; // KOLIKI DIO BRZINE ZADRZI PRI ODBIJANJU (80%)

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT); // false znaci y osi ide prema gore (da se ne crta naopako)

        background = new Texture("images/BallsGame/background.png");
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        balls = new Array<>();
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, GAME_WIDTH, GAME_HEIGHT);
        batch.end();

        // ako kliknem misem => stvori se nova loptica
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = GAME_HEIGHT - Gdx.input.getY();
            balls.add(new Ball(x, y));
        }

        // azuriranje fizike svake loptice
        for (Ball i : balls) {
            i.update(dt);
        }

        // crtanje
        shapeRenderer.setProjectionMatrix(camera.combined); // nacrtaj onaj dio koji kamera vidi
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Ball i : balls) {
            shapeRenderer.setColor(i.color);
            shapeRenderer.circle(i.x, i.y, i.radius);
        }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        background.dispose();
    }

    class Ball {
        float x, y;
        float radius;
        float speedY;
        float accelerationY;
        Color color;

        Ball(float x, float y) {
            this.x = x;
            this.y = y;
            this.radius = MathUtils.random(15f, 50f);
            this.color = new Color (MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
            this.speedY = 0f; // poskoci prema gore random brzinom
            this.accelerationY = MathUtils.random(-800f, -400f);; // sve kuglice imaju istu gravitaciju
        }

        void update(float dt) {
            speedY += accelerationY * dt;

            y += speedY * dt;

            if (y - radius < 0) {
                y = radius;
                speedY = -speedY * BOUNCE_LOSS; // obrne smjer i izgubi energiju
            }

            if (x - radius < 0) {
                x = radius;
            }
            if (x + radius > GAME_WIDTH) {
                x = GAME_WIDTH - radius;
            }
        }

    }



}
