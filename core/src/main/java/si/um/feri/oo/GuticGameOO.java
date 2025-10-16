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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GuticGameOO extends ApplicationAdapter {
    private static final float GAME_AREA_W = 800f, GAME_AREA_H = 800f;

    private SpriteBatch batch;  // crtanje svih tekstura na ekranu
    private OrthographicCamera camera;
    private Viewport viewport;  // razlicitim dimenzije

    private Texture background, playerPic, strawberry, hamburger, shoe, cd, heartFull, heartEmpty, bullet; // slike koje prikazujem

    private Player player;
    private Array<FallingItem> items;
    private Array<Bullet> bullets;

    private int score = 0;
    private int hits = 0;
    private int lives = 3;
    private int maxLives = 3;
    private boolean gameOver = false;

    private float spawnTimer = 0f;
    private float spawnInterval = 0.9f;   // na početku ~svakih 0.9 s
    private float baseFallSpeed = 140f;   // početna brzina padanja

    private BitmapFont font, fontBig, fontSmall;
    private Sound soundYum, soundEw, soundShoot;


    @Override public void create() {
        batch = new SpriteBatch();  // batch - paket, SpriteBatech - crta sve slike u jednom potezu

        background = new Texture("images/GuticGame/background.png");
        playerPic = new Texture("images/GuticGame/player.png");
        playerPic.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); // za ljepsi izgled slike

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

        camera = new OrthographicCamera();
        viewport = new FitViewport(GAME_AREA_W, GAME_AREA_H, camera);
        viewport.apply();

        //player
        float playerW = 180, playerH = 180;
        float startX = (GAME_AREA_W - playerW) / 2f;
        float startY = 40f;
        float playerSpeed = 300f;
        player = new Player(playerPic, startX, startY, playerW, playerH, playerSpeed, GAME_AREA_W);

        items = new Array<>();
        bullets = new Array<>();


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
    }

    private void spawnItem(){
        boolean isFood = MathUtils.randomBoolean(0.6f); // // 70% hrana, 30% ne-hrana

        float w = 64f, h =64f;
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
        float bx = player.x + player.w / 2f - bw / 2f;
        float by = player.y + player.h;
        float bulletSpeed = 400f;

        bullets.add(new Bullet(bullet, bx, by, bw, bh, bulletSpeed));
    }

    private void restartGame() {
        score = 0;
        lives = 3;
        hits = 0;
        gameOver = false;
        items.clear();
        bullets.clear();
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

    @Override public void render() {
        if (gameOver) {
            drawGameOver();
            return; // prekini ostatak render metode
        }

        ScreenUtils.clear(0,0,0,1);
        float dt = Gdx.graphics.getDeltaTime(); // vrijeme koje je proslo od prethodnog frame - pomnozit cemo brzine s dt da bude neodvisno kretanje (spori i brzi racunar)

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.moveLeft(dt);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.moveRight(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !gameOver) {
            soundShoot.play(0.5f);
            spawnBullet();
        }

        spawnTimer += dt; // mjeri koliko je proslo od zadnjeg spawna (dodamo dt, vrijeme izmedju frameova)
        if (spawnTimer >= spawnInterval && items.size < 10) {
            spawnTimer = 0f;
            spawnItem();
        }

        // objekti (azuriranje i sudari)
        for (int i = items.size - 1; i >= 0; i--) {
            FallingItem item = items.get(i);
            item.update(dt); // padanje objekta

            if (item.rect.overlaps(player.rect)) {
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


        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(background, 0, 0, GAME_AREA_W, GAME_AREA_H);

        for (FallingItem item : items)
            batch.draw(item.texture, item.x, item.y, item.w, item.h);
        for (Bullet b : bullets) {
            batch.draw(b.texture, b.x, b.y, b.w, b.h);
        }
        batch.draw(player.texture, player.x, player.y, player.w, player.h);

        String scoreText = "Score: " + score;
       // GlyphLayout scoreLayout = new GlyphLayout(fontSmall, scoreText);
        fontSmall.draw(batch, scoreText, 20, GAME_AREA_H - 20);
       // fontSmall.draw(batch, "Hits: " + hits,  20, GAME_AREA_H - 50);

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
        viewport.update(width, height, true);
    }

    @Override public void dispose() {
        batch.dispose();
        background.dispose();
        playerPic.dispose();
        if (strawberry != null) strawberry.dispose();
        if (hamburger != null) hamburger.dispose();
        if (shoe != null) shoe.dispose();
        if (cd != null) cd.dispose();
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
    }
}

