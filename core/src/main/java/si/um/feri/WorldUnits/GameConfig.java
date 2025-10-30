package si.um.feri.WorldUnits;

class GameConfig {

    public static final float WIDTH = 800f;
    public static final float HEIGHT = 480f;

    // HUD
    public static final float HUD_WIDTH = 800f;
    public static final float HUD_HEIGHT = 680f;

    // Game world
    public static final float WORLD_WIDTH = 10f;
    public static final float WORLD_HEIGHT = 8f;

    // Player
    public static final float PLAYER_WIDTH = 1.5f;
    public static final float PLAYER_HEIGHT = 1.5f;
    public static final float PLAYER_START_X = WORLD_WIDTH / 2f - PLAYER_WIDTH / 2f;

    private GameConfig() {
    }
}
