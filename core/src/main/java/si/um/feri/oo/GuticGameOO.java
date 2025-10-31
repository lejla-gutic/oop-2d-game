package si.um.feri.oo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.Pool;
import si.um.feri.util.debug.DebugCameraController;
import si.um.feri.util.debug.MemoryInfo;
import si.um.feri.util.ViewportUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GuticGameOO extends ApplicationAdapter {
    private static final float GAME_AREA_W = 800f, GAME_AREA_H = 800f;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Texture background, playerPic, strawberry, hamburger, shoe, cd, heartFull, heartEmpty, bullet, powerStar, bubble;

    private Player player;
    private Array<FallingObject> items;
    private Array<Bullet> bullets;
    private Pool<Bullet> bulletPool;
    private Pool<FoodItem> foodItemPool;
    private Pool<BadItem> badItemPool;
    private Pool<PowerUpItem> powerUpPool;
    private Score scoreSystem;
    private final int START_LIVES= 3;
    private int hits = 0;

    private boolean paused = false;

    private boolean powerUpActive = false;
    private float powerUpTimer = 0f;
    private float powerUpDuration = 5f;

    private float spawnTimer = 0f;
    private float spawnInterval = 0.9f;   // vsake 0.9 s
    private float baseFallSpeed = 140f;   // zacetna hitrost padanje

    private BitmapFont font, fontBig, fontSmall;
    private Sound soundYum, soundEw, soundShoot;

    private boolean debug = false;
    private DebugCameraController debugCameraController;
    private MemoryInfo memoryInfo;
    private ShapeRenderer shapeRenderer;

    public Score getScoreSystem() {
        return scoreSystem;
    }
    public boolean hasShield() {
        return powerUpActive;
    }

    public void activateShield(float duration) {
        powerUpActive = true;
        powerUpTimer = duration;
    }

    public void playYumSound() {
        soundYum.play(0.6f);
    }

    public void playEwSound() {
        soundEw.play(0.8f);
    }

    @Override public void create() {
        batch = new SpriteBatch();

        background = new Texture("images/GuticGame/background.png");

        playerPic = new Texture("images/GuticGame/player.png");
        playerPic.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

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

        powerStar = new Texture("images/GuticGame/powerup.png");
        powerStar.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        bubble = new Texture("images/GuticGame/bubble.png");
        bubble.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        camera = new OrthographicCamera();
        viewport = new FitViewport(GAME_AREA_W, GAME_AREA_H, camera);
        viewport.apply();

        // player
        float playerW = 180, playerH = 180;
        float startX = (GAME_AREA_W - playerW) / 2f;
        float startY = 40f;
        float playerSpeed = 300f;
        player = new Player(playerPic, startX, startY, playerW, playerH, playerSpeed, GAME_AREA_W);

        scoreSystem = new Score(START_LIVES);
        items = new Array<>();
        bullets = new Array<>();

        bulletPool = new Pool<Bullet>() {
            @Override
            protected Bullet newObject() {
                float bw = 70f, bh = 70f;
                float bx = 0, by = 0;
                float bulletSpeed = 400f;
                return new Bullet(bullet, bx, by, bw, bh, bulletSpeed);
            }
        };

        foodItemPool = new Pool<FoodItem>() {
            @Override
            protected FoodItem newObject() {
                return new FoodItem(strawberry, 0, 0, 64, 64, baseFallSpeed);
            }
        };

        badItemPool = new Pool<BadItem>() {
            @Override
            protected BadItem newObject() {
                return new BadItem(cd, 0, 0, 64, 64, baseFallSpeed);
            }
        };

        powerUpPool = new Pool<PowerUpItem>() {
            @Override
            protected PowerUpItem newObject() {
                return new PowerUpItem(powerStar, 0, 0, 64, 64, baseFallSpeed);
            }
        };

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Baloo.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // big font
        parameter.size = 72;
        parameter.color = Color.valueOf("3b2e2a");
        fontBig = generator.generateFont(parameter);

        // small font
        parameter.size = 32;
        parameter.color = Color.valueOf("4b3f39");
        fontSmall = generator.generateFont(parameter);
        generator.dispose();

        font = new BitmapFont();
        font.setColor(Color.BLACK);

        soundYum = Gdx.audio.newSound(Gdx.files.internal("sounds/yum.wav"));
        soundEw = Gdx.audio.newSound(Gdx.files.internal("sounds/ew.wav"));
        soundShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"));

        // DEBUG
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GAME_AREA_W / 2f, GAME_AREA_H / 2f);
        memoryInfo = new MemoryInfo(500);
        shapeRenderer = new ShapeRenderer();
    }

    @Override public void render() {
        if (scoreSystem.isGameOver()) {
            drawGameOver();
            return;
        }

        ScreenUtils.clear(0,0,0,1);
        float dt = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) debug = !debug;

        if (debug) {
            debugCameraController.handleDebugInput(dt);
            memoryInfo.update();
        }

        if (powerUpActive) {
            powerUpTimer -= dt; // vsaki frame odstejem cas ki je poteko
            if (powerUpTimer <= 0f) {
                powerUpActive = false;
                powerUpTimer = 0f;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.moveLeft(dt);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.moveRight(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !scoreSystem.isGameOver()) {
            soundShoot.play(0.5f);
            spawnBullet();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            paused = !paused;
        }

        if (paused) {
            ScreenUtils.clear(0, 0, 0, 1);
            batch.begin();
            batch.draw(background, 0, 0, GAME_AREA_W, GAME_AREA_H);

            String pausedText = "PAUSED";
            GlyphLayout layout = new GlyphLayout(fontBig, pausedText);
            float x = (GAME_AREA_W - layout.width) / 2f;
            float y = GAME_AREA_H / 2f;
            fontBig.draw(batch, layout, x, y);

            batch.end();
            return;
        }

        spawnTimer += dt;
        if (spawnTimer >= spawnInterval && items.size < 10) {
            spawnTimer = 0f;
            spawnItem();
        }

        // objekti
        for (int i = items.size - 1; i >= 0; i--) {
            FallingObject item = items.get(i);
            item.update(dt); // padanje objekta

            if (item.rect.overlaps(player.rect)) {
                item.onCollision(this);

                items.removeIndex(i);

                if (item instanceof FoodItem) {
                    foodItemPool.free((FoodItem) item);
                } else if (item instanceof BadItem) {
                    badItemPool.free((BadItem) item);
                } else if (item instanceof PowerUpItem) {
                    powerUpPool.free((PowerUpItem) item);
                }

                continue;
            }

            if (item.y + item.h < 0) {
                if (item instanceof BadItem && !powerUpActive) {
                    scoreSystem.loseLife();
                }
                items.removeIndex(i);

                if (item instanceof FoodItem) {
                    foodItemPool.free((FoodItem) item);
                } else if (item instanceof BadItem) {
                    badItemPool.free((BadItem) item);
                } else if (item instanceof PowerUpItem) {
                    powerUpPool.free((PowerUpItem) item);
                }

            }
        }

        // bulltes
        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(dt);

            for (int j = items.size - 1; j >= 0; j--) {
                FallingObject item = items.get(j);

                if (item instanceof BadItem && bullet.rect.overlaps(item.rect)) {
                    hits += 1;
                    scoreSystem.addPoints(2);

                    items.removeIndex(j);
                    bullets.removeIndex(i);
                    bulletPool.free(bullet);
                    break;
                }
            }

            // ako metak izadje izvan, uklonim ga
            if (bullet.y > GAME_AREA_H) {
                bullets.removeIndex(i);
                bulletPool.free(bullet);
            }
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(background, 0, 0, GAME_AREA_W, GAME_AREA_H);

        for (FallingObject item : items) {
            batch.draw(item.texture, item.x, item.y, item.w, item.h);

            if (item instanceof PowerUpItem) {
                ((PowerUpItem) item).drawEffect(batch);
            }
        }
        for (Bullet b : bullets) {
            batch.draw(b.texture, b.x, b.y, b.w, b.h);
        }

        batch.draw(player.texture, player.x, player.y, player.w, player.h);

        if (powerUpActive) {
            float pulse = 1.0f + 0.1f * MathUtils.sin(Gdx.graphics.getFrameId() * 0.1f);

            float bubbleW = player.w * 1.4f * pulse;
            float bubbleH = player.h * 1.4f * pulse;
            float bubbleX = player.x + player.w / 2f - bubbleW / 2f;
            float bubbleY = player.y + player.h / 2f - bubbleH / 2f;

            batch.setColor(1f, 1f, 1f, 0.8f);
            batch.draw(bubble, bubbleX, bubbleY, bubbleW, bubbleH);
            batch.setColor(1f, 1f, 1f, 1f);
        }

        String scoreText = "Score: " + scoreSystem.getPoints();
        // GlyphLayout scoreLayout = new GlyphLayout(fontSmall, scoreText);
        fontSmall.draw(batch, scoreText, 20, GAME_AREA_H - 20);
        // fontSmall.draw(batch, "Hits: " + hits,  20, GAME_AREA_H - 50);

        float heartSize = 36f;
        float spacing = 8f;
        float totalWidth = (heartSize + spacing) * START_LIVES;
        float startX = GAME_AREA_W - totalWidth - 20f;
        float y = GAME_AREA_H - 48f;

        for (int i = 0; i < START_LIVES; i++) {
            Texture current = (i < scoreSystem.getLives()) ? heartFull : heartEmpty;
            batch.draw(current, startX + i * (heartSize + spacing), y, heartSize, heartSize);
        }

        batch.end();

        if (debug) {
            debugCameraController.applyTo(camera);

            // tekstualne informacije
            batch.begin();
            fontSmall.setColor(Color.YELLOW);
            fontSmall.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), GAME_AREA_W - 120, GAME_AREA_H - 20);
            memoryInfo.render(batch, fontSmall);
            batch.end();

            // mreža
            ViewportUtils.drawGrid(viewport, shapeRenderer, 50);

            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 1, 0, 1);
            shapeRenderer.rect(player.x, player.y, player.w, player.h);

            for (FallingObject item : items)
                shapeRenderer.rect(item.x, item.y, item.w, item.h);
            for (Bullet b : bullets)
                shapeRenderer.rect(b.x, b.y, b.w, b.h);

            shapeRenderer.end();
        }
    }

    private void spawnItem() {
        float w = 64f, h = 64f;
        float x = MathUtils.random(16f, GAME_AREA_W - w - 16f);
        float y = GAME_AREA_H + h;
        float speed = baseFallSpeed + MathUtils.random(-20f, 30f);

        float random = MathUtils.random();
        FallingObject item;

        if (random < 0.1f) {
            item = powerUpPool.obtain();
            item.texture = powerStar;
        } else if (random < 0.7f) {
            item = foodItemPool.obtain();
            item.texture = MathUtils.randomBoolean() ? strawberry : hamburger;
        } else {
            item = badItemPool.obtain();
            item.texture = MathUtils.randomBoolean() ? cd : shoe;
        }

        item.x = x;
        item.y = y;
        item.w = w;
        item.h = h;
        item.speed = speed;
        item.rect.setPosition(x, y);
        item.activeItem = true;
        items.add(item);
    }

    private void spawnBullet() {
        Bullet b = bulletPool.obtain(); // uzamem en bullet iz poola
        b.activeBullet = true;
        b.x = player.x + player.w / 2f - b.w / 2f;
        b.y = player.y + player.h;
        b.speed = 400f;
        b.rect.setPosition(b.x, b.y);
        bullets.add(b);
    }

    private void restartGame() {
        scoreSystem.reset(START_LIVES);
        items.clear();
        bullets.clear();
    }

    private void drawGameOver() {
        ScreenUtils.clear(0.85f, 0.93f, 0.94f, 1f);
        batch.begin();

        batch.draw(background, 0, 0, GAME_AREA_W, GAME_AREA_H);

        font.getData().setScale(2f);
        String overText = "GAME OVER";
        GlyphLayout layout = new GlyphLayout(fontBig, overText);
        float x = (GAME_AREA_W - layout.width) / 2f;
        float y = GAME_AREA_H / 2f + 60f;
        fontBig.draw(batch, layout, x, y);

        String restartText = "Press ENTER to Restart";
        layout.setText(fontSmall, restartText);
        x = (GAME_AREA_W - layout.width) / 2f;
        y = GAME_AREA_H / 2f - 20f;
        fontSmall.draw(batch, layout, x, y);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            restartGame();
        }
    }

    @Override public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void dispose() {
        batch.dispose();
        background.dispose();
        playerPic.dispose();
        strawberry.dispose();
        hamburger.dispose();
        shoe.dispose();
        cd.dispose();
        powerStar.dispose();
        bubble.dispose();
        font.dispose();
        heartFull.dispose();
        heartEmpty.dispose();
        bullet.dispose();
        soundYum.dispose();
        soundEw.dispose();
        soundShoot.dispose();
        font.dispose();
        fontBig.dispose();
        fontSmall.dispose();
        shapeRenderer.dispose();
        for (FallingObject item : items) {
            if (item instanceof PowerUpItem) {
                ((PowerUpItem) item).dispose();
            }
        }
    }
}

