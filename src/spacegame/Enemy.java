package spacegame;

import javax.swing.ImageIcon;

public class Enemy extends Entity{
    private Bomb bomb;
    private PowerUp power;
    private final String enemyImg = "/enemy.png";

    public Enemy(int x, int y) {

        initEnemy(x, y);
    }

    private void initEnemy(int x, int y) {

        this.x = x;
        this.y = y;
        
        bomb = new Bomb(x, y);
        power = new PowerUp(x, y);
        ImageIcon ii = new ImageIcon(getClass().getResource(enemyImg));
        setImage(ii.getImage());
    }

    public void act(int direction) {
        
        this.x += direction;
    }
    
    public PowerUp getPower(){
        return power;
    }

    public Bomb getBomb() {
        
        return bomb;
    }

    public class Bomb extends Entity {

        private final String bombImg = "/bomb.png";
        private boolean destroyed;

        public Bomb(int x, int y) {

            initBomb(x, y);
        }

        private void initBomb(int x, int y) {

            setDestroyed(true);
            this.x = x;
            this.y = y;
            ImageIcon ii = new ImageIcon(getClass().getResource(bombImg));
            setImage(ii.getImage());

        }

        public void setDestroyed(boolean destroyed) {
        
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
        
            return destroyed;
        }
    }
}
