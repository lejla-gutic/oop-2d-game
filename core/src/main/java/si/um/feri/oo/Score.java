package si.um.feri.oo;

public class Score {
    private int points;
    private int lives;
    private boolean gameOver;

    public Score(int startLives) {
        this.points = 0;
        this.lives = startLives;
        this.gameOver = false;
    }

    public void addPoints(int amount) {
        points += amount;
    }

    public void loseLife() {
        if (!gameOver) {
            lives--;
            if (lives <= 0) {
                lives = 0;
                gameOver = true;
            }
        }
    }

    public int getPoints() {
        return points;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void reset(int startLives) {
        points = 0;
        lives = startLives;
        gameOver = false;
    }

}
