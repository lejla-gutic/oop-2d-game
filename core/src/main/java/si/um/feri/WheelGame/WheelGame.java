package si.um.feri.WheelGame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class WheelGame extends ApplicationAdapter{
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture wheelTexture, background;
    private OrthographicCamera camera;

    private static final float GAME_WIDTH = 800;
    private static final float GAME_HEIGHT = 800;

    private float x, y;
    private float radius;
    private float speed; // korizontalna brzina
    private float angle; // ugao rotacije

    private int direction = 1;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        background = new Texture("images/WheelGame/background.png");
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        wheelTexture = new Texture("images/WheelGame/wheel.png");
        wheelTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        radius = 100;
        x = GAME_WIDTH - radius;
        y = 200;
        speed = 200;
        angle = 0;
    }

    @Override
    public void render() {
        float dt  = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, GAME_WIDTH, GAME_HEIGHT);
        batch.end();

        // kretanje tocka po x osi u zavisnosti od smijera
        x += direction * speed * dt;

        // ako predje ekran, vrati se na pocetak
        if (x + radius >= GAME_WIDTH) {
            x = GAME_WIDTH - radius;
            direction = -1;
        }

        // kada udari u lijevi zid
        if (x - radius <= 0) {
            x = radius;
            direction = 1;
        }

        // Rotacija (sinhronizovana s kretanjem)
        angle -= direction * (speed * dt * 360) / (2 * MathUtils.PI * radius);

        // crtanje
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        //  x - radius, y - radius,   // pomjeri sliku tako da centar slike bude na (x, y)
        batch.draw(wheelTexture, x - radius, y - radius, radius, radius, radius * 2, radius * 2, 1, 1, angle, 0, 0, wheelTexture.getWidth(), wheelTexture.getHeight(), false, false);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        wheelTexture.dispose();
    }

}
