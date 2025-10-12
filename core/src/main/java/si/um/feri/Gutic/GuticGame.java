package si.um.feri.Gutic;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.audio.Sound;

public class GuticGame extends ApplicationAdapter {
    private static final float GAME_AREA_W = 800f, GAME_AREA_H = 800f;

    private SpriteBatch batch;  // crtanje svih tekstura na ekranu
    private Texture background, player, strawberry, hamburger, shoe, cd, heartFull, heartEmpty, bullet; // slike koje prikazujem
    private OrthographicCamera camera;
    private Viewport viewport;  // razlicitim dimenzije

    private float playerX, playerY;
    private float playerW = 180, playerH = 180;
    private float playerSpeed = 300f;   // f -> float vrijednost

    private int score = 0;
    private int lives = 3;
    private int maxLives = 3;
    private int hits = 0;
    private boolean gameOver = false;

    private Array<FallingItem> items; // objekti koji padaju
    private Array<Bullet> bullets = new Array<>();

    private float spawnTimer = 0f;
    private float spawnInterval = 0.9f;   // na početku ~svakih 0.9 s
    private float baseFallSpeed = 140f;   // početna brzina padanja

    private BitmapFont font;
    private BitmapFont fontBig;
    private BitmapFont fontSmall;

    private Sound soundYum;
    private Sound soundEw;
    private Sound soundShoot;

    private void spawnItem(){
        boolean isFood = MathUtils.randomBoolean(0.6f); // // 70% hrana, 30% ne-hrana

        float w=64f, h=64f;
        float x = MathUtils.random(16f, GAME_AREA_W - w - 16f); // // slučajna X pozicija
        float y = GAME_AREA_H + h; // kreni malo iznad vrha

        float fallSpeed = baseFallSpeed + MathUtils.random(-20f, 30f); // random brzina padanja

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

        items.add(new FallingItem(texture, x, y, w, h, fallSpeed, isFood));
    }

    private void spawnBullet() {
        float bw = 70f, bh = 70f;
        float bx = playerX + playerW/2f - bw/2f; // centriraj iznad igraca
        float by = playerY + playerH;
        float bulletSpeed = 400f;
        bullets.add(new Bullet(bullet, bx, by, bw, bh, bulletSpeed));
    }

    private void restartGame() {
        score = 0;
        lives = 3;
        hits = 0;
        items.clear();
        gameOver = false;
    }

    private void drawGameOver() {
        ScreenUtils.clear(0.85f, 0.93f, 0.94f, 1f); // boja pozadine

        batch.begin();

        batch.draw(background, 0, 0, GAME_AREA_W, GAME_AREA_H);

        font.getData().setScale(2f);
        String overText = "GAME OVER";
        GlyphLayout layout = new GlyphLayout(fontBig, overText);
        float x = (GAME_AREA_W - layout.width) / 2f;
        float y = GAME_AREA_H / 2f + 60f;
        fontBig.draw(batch, layout, x, y);

        // centriranje
        String restartText = "Press ENTER to Restart";
        layout.setText(fontSmall, restartText);
        x = (GAME_AREA_W - layout.width) / 2f;
        y = GAME_AREA_H / 2f - 20f;
        fontSmall.draw(batch, layout, x, y);

        batch.end();

        // ako pritisneš ENTER → restart igre
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            restartGame();
        }
    }

    @Override public void create() {
        batch = new SpriteBatch();  // batch - paket, SpriteBatech - crta sve slike u jednom potezu

        background = new Texture("images/background.png");

        player = new Texture("images/player.png");
        player.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); // za ljepsi izgled slike

        strawberry = new Texture("images/strawberry.png");
        strawberry.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        hamburger = new Texture("images/hamburger.png");
        hamburger.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        shoe = new Texture("images/shoe.png");
        shoe.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        cd = new Texture("images/cd.png");
        cd.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        heartFull = new Texture("images/full_heart.png");
        heartFull.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        heartEmpty = new Texture("images/empty_heart.png");
        heartEmpty.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        bullet = new Texture("images/bullet.png");
        bullet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Baloo.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // veliki font za "Game Over"
        parameter.size = 72;
        parameter.color = Color.valueOf("3b2e2a"); // topla tamnosmeđa nijansa
        fontBig = generator.generateFont(parameter);

        // manji font za tekst i score
        parameter.size = 32;
        parameter.color = Color.valueOf("4b3f39");
        fontSmall = generator.generateFont(parameter);

        generator.dispose();

        font = new BitmapFont();
        font.setColor(Color.BLACK);

        soundYum = Gdx.audio.newSound(Gdx.files.internal("sounds/yum.wav"));
        soundEw = Gdx.audio.newSound(Gdx.files.internal("sounds/ew.wav"));
        soundShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"));

        items = new Array<>();

        camera = new OrthographicCamera();
        viewport = new FitViewport(GAME_AREA_W, GAME_AREA_H, camera);
        viewport.apply();


        playerX = (GAME_AREA_W - playerW) / 2f; // centar po sirini
        playerY = 40f; // 40 piksela od dna ekrana (kao da stoji na stolu)
    }

    @Override public void render() {
        if (gameOver) {
            drawGameOver();
            return; // prekini ostatak render metode
        }

        ScreenUtils.clear(0,0,0,1);
        float dt = Gdx.graphics.getDeltaTime(); // vrijeme koje je proslo od prethodnog frame - pomnozit cemo brzine s dt da bude neodvisno kretanje (spori i brzi racunar)

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) playerX -= playerSpeed * dt;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) playerX += playerSpeed * dt;
        playerX = Math.max(0, Math.min(playerX, GAME_AREA_W - playerW)); // ne da liku da ide van granica

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !gameOver) {
            soundShoot.play(0.5f);
            spawnBullet();
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        spawnTimer += dt; // mjeri koliko je proslo od zadnjeg spawna (dodamo dt, vrijeme izmedju frameova)
        if (spawnTimer >= spawnInterval && items.size < 10) {
            spawnTimer = 0f;
            spawnItem();
        }

        Rectangle playerRect = new Rectangle(playerX, playerY, playerW, playerH);
        // objekti
        for (int i = items.size - 1; i >= 0; i--) {
            FallingItem item = items.get(i);
            item.update(dt); // padanje objekta

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

            if (item.y + item.h < 0) { // ispod dna
                if (!item.isFood) {
                    lives -= 1; // isto prvo oduzmi
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
            Bullet bullet = bullets.get(i);
            bullet.update(dt);

            // provjerim sudar s padajucim objektima
            for (int j = items.size - 1; j >= 0; j--) {
                FallingItem item = items.get(j);

                if (!item.isFood && bullet.rect.overlaps(item.rect)) {
                    hits += 1;
                    score += 2;

                    bullets.removeIndex(i);
                    items.removeIndex(j);
                    break;
                }
            }

            // ako metak izadje iz ekrana, uklonim ga
            if (bullet.y > GAME_AREA_H) {
                bullets.removeIndex(i);
            }
        }

        batch.begin();
        batch.draw(background, 0, 0, GAME_AREA_W, GAME_AREA_H);

        for (Bullet b : bullets) {
            batch.draw(b.texture, b.x, b.y, b.w, b.h);
        }

        for (int i = 0; i < items.size; i++) {
            FallingItem item = items.get(i);
            batch.draw(item.texture, item.x, item.y, item.w, item.h);
        }

        batch.draw(player, playerX, playerY, playerW, playerH);
        batch.end();

        batch.begin();

        String scoreText = "Score: " + score;
        GlyphLayout scoreLayout = new GlyphLayout(fontSmall, scoreText);
        fontSmall.draw(batch, scoreText, 20, GAME_AREA_H - 20);
        fontSmall.draw(batch, "Hits: " + hits,  20, GAME_AREA_H - 50);

        // --- LIVES (srca) ---
        float heartSize = 36f;             // veličina svakog srca (px)
        float spacing = 8f;                // razmak između srca
        float totalWidth = (heartSize + spacing) * maxLives;
        float startX = GAME_AREA_W - totalWidth - 20f; // malo odmaknuto od desnog ruba
        float y = GAME_AREA_H - 48f;       // vertikalna pozicija (blizu vrha)

        for (int i = 0; i < maxLives; i++) {
            Texture current = (i < lives) ? heartFull : heartEmpty;
            batch.draw(current, startX + i * (heartSize + spacing), y, heartSize, heartSize);
        }
        batch.end();

    }

    @Override public void resize(int width, int height) {
        viewport.update(width, height, true); // zadrži centar i 1:1 omjer
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

