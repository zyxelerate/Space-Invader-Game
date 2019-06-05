package spacegame;

import javax.swing.ImageIcon;

public class Life extends Entity {
    private final String lifeImg = "/life.png";

    public Life() {
    }

    public Life(int x, int y) {
        initLife(x, y);
    }

    private void initLife(int x, int y) {
        ImageIcon ii = new ImageIcon(getClass().getResource(lifeImg));
        setImage(ii.getImage());
        setX(x);
        setY(y);    
    }
}
