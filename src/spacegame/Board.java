package spacegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable, Constants {

    private Dimension d;
    private ArrayList<Enemy> enemies;
    private Stack<Life> lives;
    private Stack<Shield> shield;
    private Player player;
    //shots
    private Shot shot;
    private Shot shot2;
    private Shot shot3;

    //enemy coordinates
    private final int ENEMY_INIT_X = 150;
    private final int ENEMY_INIT_Y = 5;
    
    //health coordinates
    private final int LIFE_INIT_X = 4;
    private final int LIFE_INIT_Y = 300;
    
    //shield coordinates
    private final int SHIELD_INIT_X = 43;
    private final int SHIELD_INIT_Y = 300;
    
    //Gun powerUp display coordinates
    private final int TEXT_X = 110;
    private final int TEXT_Y = 310;
    
    private int direction = -1;//initial direction the enemies move
    private int deaths = 0;//counts enemy deaths
    private int pDeaths = 3;//if this is 0, then player has no more lives
    private int powCount = 0; //counter for gun PowerUp; if 0, then shoot only one shot.
    private boolean hasShield = false;//true if shield powerUp is taken
    private boolean hasGuns = false;//true if gun power up is taken
    
    private boolean ingame = true;
    private final String explImg = "src/images/explosion.png";
    private String message = "Game Over";

    private Thread animator;

    public Board() {
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.black);

        gameInit();
        setDoubleBuffered(true);
    }

    @Override
    public void addNotify() {

        super.addNotify();
        gameInit();
    }
    
    //initialize game components
    public void gameInit() {
        enemies = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {

                Enemy enemy = new Enemy(ENEMY_INIT_X + 18 * j, ENEMY_INIT_Y + 18 * i);
                enemies.add(enemy);
            }
        }
           
        lives = new Stack<>();
        
        for (int i = 0; i < 3; i++) {
            Life life = new Life(LIFE_INIT_X + 13 * i, LIFE_INIT_Y);
            lives.push(life);
        }        
        
        shield = new Stack<>();
        
        player = new Player();
        shot = new Shot();
        shot2 = new Shot();
        shot3 = new Shot();

        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }
    }

    public void drawAliens(Graphics g) {
//        Iterator it = enemies.iterator();
        for (Enemy enemy: enemies) {
            if (enemy.isVisible()) {
                g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
            }
            if (enemy.isDying()) {
                enemy.die();
            }
        }
    }

    public void drawPlayer(Graphics g) {
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }
        //check if player has 0 lives
        if (player.isDying()) { 
            if (pDeaths <= 0){
                player.die();
                ingame = false;
            }
        }
    }

    public void drawShot(Graphics g){
        if (hasGuns == true){
            if (shot.isVisible() && shot2.isVisible() && shot3.isVisible()) {
                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
                g.drawImage(shot2.getImage(), shot2.getX(), shot2.getY(), this);
                g.drawImage(shot3.getImage(), shot3.getX(), shot3.getY(), this);
            }
        }else {
            if (shot.isVisible()) {
                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
            }
        }
        
    }
    
    public void drawLife(Graphics g) {
        for (Life life: lives){
            if (life.isVisible()){
                g.drawImage(life.getImage(), life.getX(), life.getY(), this);
            }
        }
    }

    public void drawBombing(Graphics g) {

        for (Enemy a : enemies) {
            
            Enemy.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }
    
    
    public void drawPowerUps(Graphics g) {

        for (Enemy a : enemies) {
            if (!a.isVisible()){
                PowerUp p = a.getPower();

                if (!p.isTaken()) {
                    g.drawImage(p.getImage(), p.getX(), p.getY(), this);
                }
            }
        }
    }
    
    public void drawShield(Graphics g) {
        for (Shield s: shield){
            if (s.isVisible()){
                g.drawImage(s.getImage(), s.getX(), s.getY(), this);
            }
        }
    }
    
    public void drawText(Graphics g){
        if (hasGuns == true && (powCount - 1) != 0 ){
            Font small = new Font("Helvetica", Font.BOLD, 14);
            FontMetrics metr = this.getFontMetrics(small);
            String text = "PowerUp Ammo: " + (powCount - 1);
            g.setColor(Color.white);
            g.setFont(small);
            g.drawString(text, TEXT_X, TEXT_Y);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.blue);

        if (ingame) {

            g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);
            drawLife(g);
            drawPowerUps(g);
            drawShield(g);
            drawText(g);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void gameOver() {

        Graphics g = this.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2, BOARD_WIDTH / 2);
        Sound.GG.play();
    }

    public void animationCycle() {
        //check if enemy deaths is equal to num of enemies to destroy
        if (deaths == NUMBER_OF_ENEMIES_TO_DESTROY) {

            ingame = false;//end the game 
            message = "You Win!";
        }

        // player
        player.act();//do player actions

        // shot
        //if gun powerUp is taken
        if (hasGuns == true){
            if (shot.isVisible() && shot2.isVisible() && shot3.isVisible()) {

                int shotX = shot.getX();
                int shotY = shot.getY();
                int shot2X = shot2.getX();
                int shot2Y = shot2.getY();
                int shot3X = shot3.getX();
                int shot3Y = shot3.getY();

                for (Enemy enemy: enemies) {

                    int enemyX = enemy.getX();
                    int enemyY = enemy.getY();

                    if (enemy.isVisible() && shot.isVisible()) {
                        if (shotX >= (enemyX) && shotX <= (enemyX + ENEMY_WIDTH) && shotY >= (enemyY) && shotY <= (enemyY + ENEMY_HEIGHT)){
                            ImageIcon ii = new ImageIcon(explImg);
                            enemy.setImage(ii.getImage());
                            enemy.setDying(true);
                            Sound.BOOM.play();
                            deaths++;
                            shot.die();
                        }
                    }
                    if (enemy.isVisible() && shot2.isVisible()) {
                        if (shot2X >= (enemyX) && shot2X <= (enemyX + ENEMY_WIDTH) && shot2Y >= (enemyY) && shot2Y <= (enemyY + ENEMY_HEIGHT)){
                            ImageIcon ii = new ImageIcon(explImg);
                            enemy.setImage(ii.getImage());
                            enemy.setDying(true);
                            Sound.BOOM.play();
                            deaths++;
                            shot2.die();
                        }
                    }
                    if (enemy.isVisible() && shot3.isVisible()) {
                        if (shot3X >= (enemyX) && shot3X <= (enemyX + ENEMY_WIDTH) && shot3Y >= (enemyY) && shot3Y <= (enemyY + ENEMY_HEIGHT)){
                            ImageIcon ii = new ImageIcon(explImg);
                            enemy.setImage(ii.getImage());
                            enemy.setDying(true);
                            Sound.BOOM.play();
                            deaths++;
                            shot3.die();
                        }
                    }
                }

                int y = shot.getY();
                int y2 = shot2.getY();
                int y3 = shot3.getY();
                y -= 4;
                y2 -= 4;
                y3 -= 4;                

                if (y < 0) {
                    shot.die();
                } else {
                    shot.setY(y);
                }
                if (y2 < 0) {
                    shot2.die();
                } else {
                    shot2.setY(y2);
                }
                if (y3 < 0) {
                    shot3.die();
                } else {
                    shot3.setY(y3);
                }
            }
        } else {
            if (shot.isVisible()) {

                int shotX = shot.getX();
                int shotY = shot.getY();

                for (Enemy enemy: enemies) {

                    int enemyX = enemy.getX();
                    int enemyY = enemy.getY();

                    if (enemy.isVisible() && shot.isVisible()) {
                        if (shotX >= (enemyX) && shotX <= (enemyX + ENEMY_WIDTH) && shotY >= (enemyY) && shotY <= (enemyY + ENEMY_HEIGHT)){
                            ImageIcon ii = new ImageIcon(explImg);
                            enemy.setImage(ii.getImage());
                            enemy.setDying(true);
                            Sound.BOOM.play();
                            deaths++;
                            shot.die();
                        }
                    }
                }

                int y = shot.getY();
                y -= 4;

                if (y < 0) {
                    shot.die();
                } else {
                    shot.setY(y);
                }
            }
        }
        // enemies
        //enemy movement
        for (Enemy enemy: enemies) {

            int x = enemy.getX();

            if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {

                direction = -1;
                Iterator i1 = enemies.iterator();

                while (i1.hasNext()) {

                    Enemy a2 = (Enemy) i1.next();
                    a2.setY(a2.getY() + GO_DOWN);
                }
            }

            if (x <= BORDER_LEFT && direction != 1) {

                direction = 1;

                Iterator i2 = enemies.iterator();

                while (i2.hasNext()) {

                    Enemy a = (Enemy) i2.next();
                    a.setY(a.getY() + GO_DOWN);
                }
            }
        }
        //if enemies reach ground level
        Iterator it = enemies.iterator();

        while (it.hasNext()) {
            
            Enemy enemy = (Enemy) it.next();
            
            if (enemy.isVisible()) {

                int y = enemy.getY();

                if (y > GROUND - ENEMY_HEIGHT) {
                    ingame = false;
                    message = "Invasion!";
                }

                enemy.act(direction);
            }
        }

        // bombs
        Random generator = new Random();

        for (Enemy enemy: enemies) {

            int shot = generator.nextInt(15);
            Enemy.Bomb b = enemy.getBomb();

            if (shot == CHANCE && enemy.isVisible() && b.isDestroyed()) {

                b.setDestroyed(false);
                b.setX(enemy.getX());
                b.setY(enemy.getY());
            }

            int bombX = b.getX();
            int bombY = b.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !b.isDestroyed()) {
                //collision detection between bomb and player
                if (bombX >= (playerX) && bombX <= (playerX + PLAYER_WIDTH) && bombY >= (playerY) && bombY <= (playerY + PLAYER_HEIGHT)) {
                    if (hasShield == false){
                        Sound.HURT.play();
                        pDeaths--;
                        lives.pop();
                    }
                    else{
                        Sound.SHIELDHURT.play();
                        shield.pop();
                    }
                    b.setDestroyed(true);
                }
                if (shield.empty() && hasShield == true){
                    hasShield = false;
                }
                if (pDeaths <= 0){ //checks if player is dead; Could be changed to lives.empty
                    ImageIcon ii = new ImageIcon(explImg);
                    player.setImage(ii.getImage());
                    player.setDying(true);
                }
            }
            //movement of bomb
            if (!b.isDestroyed()) {
                
                b.setY(b.getY() + 1);
                
                //check if bomb touches ground
                if (b.getY() >= GROUND - BOMB_HEIGHT) {
                    b.setDestroyed(true);
                }
            }
        }
        
        //powerUps
        
        for (Enemy enemy: enemies){
            if (!enemy.isVisible()){ //check if an enemy is dead
                PowerUp p = enemy.getPower();
                int powerX = p.getX();
                int powerXr = p.getX() + POWER_WIDTH;
                int powerY = p.getY() + POWER_HEIGHT;
                int playerX = player.getX();
                int playerY = player.getY();

                if (player.isVisible() && !p.isTaken()) {
                    //collision detection between powerUo and player
                    if (powerX >= (playerX) && powerX <= (playerX + PLAYER_WIDTH) && powerY >= (playerY) && powerY <= (playerY + PLAYER_HEIGHT) || powerXr >= (playerX) && powerXr <= (playerX + PLAYER_WIDTH) && powerY >= (playerY) && powerY <= (playerY + PLAYER_HEIGHT)) {
                        //if powerUp type is equal to 1, then give health
                        if (p.getType() == 1){
                            Sound.GET_HEALTH.play();
                            if (pDeaths <= 2 && pDeaths != 0){ //to make sure that the player does not overheal/ get more than 3 health
                                pDeaths++;
                                Life life = new Life(lives.peek().getX() + 13, LIFE_INIT_Y);
                                lives.push(life);
                              
                            }
                        }
                        //give player shield
                        else if (p.getType() == 2){
                            Sound.GET_SHIELD.play();
                            if (hasShield == false){
                                hasShield = true;
                                for (int i = 0; i < 3; i++) {
                                    Shield s = new Shield(SHIELD_INIT_X + 13 * i, SHIELD_INIT_Y);
                                    shield.push(s);
                                }
                            }else { //if shield is still active, regenerate shield
                                shield.removeAllElements();
                                for (int i = 0; i < 3; i++) {
                                    Shield s = new Shield(SHIELD_INIT_X + 13 * i, SHIELD_INIT_Y);
                                    shield.push(s);
                                }
                            }
                        }
                        //give gun PowerUp
                        else if (p.getType() == 3){
                            Sound.POWERUP.play();
                            hasGuns = true;
                            powCount = 5;
                        }
                        p.setTaken(true);
                    }
                    
                }
                //movement of powerUp
                if (!p.isTaken()){
                    p.setY(p.getY() + 1);

                    if (p.getY() >= GROUND - POWER_HEIGHT){//if powerUp touches ground, then despawn
                        p.setTaken(true);
                    } 
                }
            }
        }
        
    }

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (ingame) {

            repaint();
            animationCycle();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
            
            beforeTime = System.currentTimeMillis();
        }

        gameOver();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();
            int x2 = x + 13;
            int x3 = x - 13;

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {
                
                if (ingame) {
                    //if has gun PowerUp
                    if (hasGuns == true){
                        if (!shot.isVisible() || !shot2.isVisible() || !shot3.isVisible()) {
                            Sound.SHOT.play();
                            shot = new Shot(x, y);
                            shot2 = new Shot(x2, y);
                            shot3 = new Shot(x3, y);
                            powCount--;
                        }
                    }else{
                        if (!shot.isVisible()) {
                            Sound.SHOT.play();
                            shot = new Shot(x, y);
                        }
                    }
                    //deactivates gun PowerUp
                    if (powCount == 0){
                        hasGuns = false;
                    }
                }
            }
        }
    }
}
