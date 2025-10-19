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
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Texture background;

    private Array<Ball> balls;

    private static final float GAME_WIDTH = 800;
    private static final float GAME_HEIGHT = 800;
   // private static final float GRAVITY = -600f; // prema dolje
    private static final float BOUNCE_LOSS = 0.8f; // brzina koju obdrzi pri odbijanju (80%)

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT); // false -> y raste ka gore, (0,0) je donji lijevi ugao

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

        // ustvari se ball
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = GAME_HEIGHT - Gdx.input.getY();
            balls.add(new Ball(x, y));
        }

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



}
