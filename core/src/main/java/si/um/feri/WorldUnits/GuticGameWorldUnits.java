package si.um.feri.WorldUnits;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.audio.Sound;

import si.um.feri.Gutic.Bullet;
import si.um.feri.Gutic.FallingItem;

public class GuticGameWorldUnits extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture background, player, strawberry, hamburger, shoe, cd, heartFull, heartEmpty, bullet;
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;
    private Viewport hudViewport;
    private GlyphLayout layout;

    private float playerX, playerY;
    private float playerW = 1.5f, playerH = 1.5f;
    private float playerSpeed = 3f;

    private int score = 0;
    private int lives = 3;
    private int maxLives = 3;
    private int hits = 0;
    private boolean gameOver = false;

    private Array<si.um.feri.Gutic.FallingItem> items;
    private Array<si.um.feri.Gutic.Bullet> bullets = new Array<>();

    private float spawnTimer = 0f;
    private float spawnInterval = 0.9f;
    private float baseFallSpeed = 2f;

    private BitmapFont font;
    private BitmapFont fontBig;
    private BitmapFont fontSmall;

    private Sound soundYum;
    private Sound soundEw;
    private Sound soundShoot;

    private void spawnItem(){
        boolean isFood = MathUtils.randomBoolean(0.6f);

        float w=0.6f, h=0.6f;
        float x = MathUtils.random(0.5f, GameConfig.WORLD_WIDTH - w - 0.5f);
        float y = GameConfig.WORLD_HEIGHT + h;

        float fallSpeed = baseFallSpeed + MathUtils.random(-0.2f, 0.3f);

        Texture texture;
        if (isFood) {
            if (MathUtils.randomBoolean(0.5f)) {
                texture = strawberry;
            } else {
                texture = hamburger;
            }
        }
        else {
            if (MathUtils.randomBoolean(0.5f)) {
                texture = cd;
            } else {
                texture = shoe;
            }
        }

        items.add(new si.um.feri.Gutic.FallingItem(texture, x, y, w, h, fallSpeed, isFood));
    }

    private void spawnBullet() {
        float bw = 0.5f, bh = 0.6f;
        float bx = playerX + playerW/2f - bw/2f;
        float by = playerY + playerH;
        float bulletSpeed = 8f;
        bullets.add(new si.um.feri.Gutic.Bullet(bullet, bx, by, bw, bh, bulletSpeed));
    }

    private void restartGame() {
        score = 0;
        lives = 3;
        hits = 0;
        items.clear();
        gameOver = false;
    }

    private void drawGameOver() {
        ScreenUtils.clear(0.78f, 0.87f, 0.87f, 1f);
        hudViewport.apply();
        batch.setProjectionMatrix(hudViewport.getCamera().combined);
        batch.begin();

        batch.draw(background, 0, 0, GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);

        String overText = "GAME OVER";
        layout.setText(fontBig, overText);
        float x = GameConfig.HUD_WIDTH / 2f - layout.width / 2f;
        float y = GameConfig.HUD_HEIGHT / 2f + layout.height;
        fontBig.draw(batch, layout, x, y);

        String restartText = "Press ENTER to Restart";
        layout.setText(fontSmall, restartText);
        x = GameConfig.HUD_WIDTH / 2f - layout.width / 2f;
        y = GameConfig.HUD_HEIGHT / 2f - 2 * layout.height;
        fontSmall.draw(batch, layout, x, y);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            restartGame();
        }
    }


    @Override public void create() {
        batch = new SpriteBatch();

        layout = new GlyphLayout();

        background = new Texture("images/GuticGame/background.png");

        player = new Texture("images/GuticGame/player.png");
        player.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        strawberry = new Texture("images/GuticGame/strawberry.png");
        strawberry.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        hamburger = new Texture("images/GuticGame/hamburger.png");
        hamburger.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        shoe = new Texture("images/GuticGame/shoe.png");
        shoe.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        cd = new Texture("images/GuticGame/cd.png");
        cd.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        heartFull = new Texture("images/GuticGame/full_heart.png");
        heartFull.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        heartEmpty = new Texture("images/GuticGame/empty_heart.png");
        heartEmpty.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        bullet = new Texture("images/GuticGame/bullet.png");
        bullet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Baloo.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 72;
        parameter.color = Color.valueOf("3b2e2a");
        fontBig = generator.generateFont(parameter);

        parameter.size = 32;
        parameter.color = Color.valueOf("4b3f39");
        fontSmall = generator.generateFont(parameter);
        fontSmall.getData().setScale(0.9f);

        generator.dispose();

        font = new BitmapFont();
        font.setColor(Color.BLACK);

        soundYum = Gdx.audio.newSound(Gdx.files.internal("sounds/yum.wav"));
        soundEw = Gdx.audio.newSound(Gdx.files.internal("sounds/ew.wav"));
        soundShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"));

        items = new Array<>();

        gameCamera = new OrthographicCamera();
        gameViewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, gameCamera);
        gameViewport.apply();

        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        hudViewport.apply();

        layout = new GlyphLayout();

        heartFull.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        heartEmpty.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        playerX = (GameConfig.WORLD_WIDTH - playerW) / 2f;
        playerY = 0.8f;
    }

    @Override public void render() {
        if (gameOver) {
            drawGameOver();
            return;

        }

        ScreenUtils.clear(0.78f, 0.87f, 0.87f, 1f);

        float dt = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) playerX -= playerSpeed * dt;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) playerX += playerSpeed * dt;
        playerX = Math.max(0, Math.min(playerX, GameConfig.WORLD_WIDTH - playerW));

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !gameOver) {
            soundShoot.play(0.5f);
            spawnBullet();
        }

        gameCamera.update();
        batch.setProjectionMatrix(gameCamera.combined);

        spawnTimer += dt;
        if (spawnTimer >= spawnInterval && items.size < 10) {
            spawnTimer = 0f;
            spawnItem();
        }

        Rectangle playerRect = new Rectangle(playerX, playerY, playerW, playerH);
        // objekti
        for (int i = items.size - 1; i >= 0; i--) {
            si.um.feri.Gutic.FallingItem item = items.get(i);
            item.update(dt);

            if (item.rect.overlaps(playerRect)) {
                if (item.isFood) {
                    score += 1;
                    soundYum.play(0.6f);
                } else {
                    lives -= 1;
                    soundEw.play(0.8f);
                    if (lives <= 0) {
                        lives = 0;
                        gameOver = true;
                    }
                }
                items.removeIndex(i);
                continue;
            }

            if (item.y + item.h < 0) {
                if (!item.isFood) {
                    lives -= 1;
                    if (lives <= 0) {
                        lives = 0;
                        gameOver = true;
                    }
                }
                items.removeIndex(i);
            }
        }

        // update metaka
        for (int i = bullets.size - 1; i >= 0; i--) {
            si.um.feri.Gutic.Bullet bullet = bullets.get(i);
            bullet.update(dt);

            for (int j = items.size - 1; j >= 0; j--) {
                si.um.feri.Gutic.FallingItem item = items.get(j);

                if (!item.isFood && bullet.rect.overlaps(item.rect)) {
                    hits += 1;
                    score += 2;

                    bullets.removeIndex(i);
                    items.removeIndex(j);
                    break;
                }
            }

            if (bullet.y > GameConfig.WORLD_HEIGHT) {
                bullets.removeIndex(i);
            }
        }

        gameViewport.apply();
        batch.setProjectionMatrix(gameCamera.combined);
        batch.begin();

        batch.draw(background, 0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        for (Bullet b : bullets) {
            batch.draw(b.texture, b.x, b.y, b.w, b.h);
        }

        for (int i = 0; i < items.size; i++) {
            FallingItem item = items.get(i);
            batch.draw(item.texture, item.x, item.y, item.w, item.h);
        }

        batch.draw(player, playerX, playerY, playerW, playerH);
        batch.end();

        hudViewport.apply();
        batch.setProjectionMatrix(hudViewport.getCamera().combined);
        batch.begin();

        String scoreText = "Score: " + score;
        layout.setText(fontSmall, scoreText);
        float scoreX = 10f;
        float scoreY = GameConfig.HUD_HEIGHT - 30f;
        fontSmall.draw(batch, layout, scoreX, scoreY);

        float heartSize = 30f;
        float spacing = 8f;
        float totalWidth = (heartSize + spacing) * maxLives;
        float startX = GameConfig.HUD_WIDTH - totalWidth - 10f;
        float heartY = GameConfig.HUD_HEIGHT - heartSize - 30f;

        for (int i = 0; i < maxLives; i++) {
            Texture current = (i < lives) ? heartFull : heartEmpty;
            batch.draw(current,
                startX + i * (heartSize + spacing),
                heartY,
                heartSize, heartSize);
        }

        batch.end();

    }

    @Override public void resize(int width, int height) {
        gameViewport.update(width, height, true);
        hudViewport.update(width, height, true);
    }

    @Override public void dispose() {
        batch.dispose();
        background.dispose();
        player.dispose();

        if (strawberry != null) strawberry.dispose();
        if (hamburger != null) hamburger.dispose();
        if (shoe != null)      shoe.dispose();

        font.dispose();

        heartFull.dispose();
        heartEmpty.dispose();

        bullet.dispose();

        soundYum.dispose();
        soundEw.dispose();
        soundShoot.dispose();
    }
}
