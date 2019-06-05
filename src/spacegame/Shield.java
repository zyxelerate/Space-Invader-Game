package spacegame;

import javax.swing.ImageIcon;

public class Shield extends Entity{
    private final String shieldImg = "/shield.png";
    
    public Shield(){
    
    }
    
    public Shield(int x, int y){
        initShield(x, y);
    }
    
    public void initShield(int x, int y){
        ImageIcon ii = new ImageIcon(getClass().getResource(shieldImg));
        setImage(ii.getImage());
        setX(x);
        setY(y);
    }
}
