
package spacegame;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class SpaceGame extends JFrame implements Constants{
    
    public SpaceGame(){
        initUI();
    }
    
    public void initUI(){
        add(new Board());
        setTitle("Space Invaders Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            SpaceGame ex = new SpaceGame();
            ex.setVisible(true);
        });
    }
    
}
