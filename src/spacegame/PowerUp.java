package spacegame;

import javax.swing.ImageIcon;

public class PowerUp extends Entity{
    private int type;
    private final String pow1Img = "/pow1.png";
    private final String pow2Img = "/pow2.png";
    private final String pow3Img = "/pow3.png";
    private boolean taken;
    
    public PowerUp(int x, int y){
        initPowerUp(x, y);
        setVisible(false);
        
    }
    
    private void initPowerUp(int x, int y){
        setX(x);
        setY(y);
        setTaken(false);
        type = (int)(Math.random() * 4 + 0);
        
        switch (type) {
            case 1:
                {
                    ImageIcon ii = new ImageIcon(getClass().getResource(pow1Img));
                    setImage(ii.getImage());
                    break;
                }
            case 2:
                {
                    ImageIcon ii = new ImageIcon(getClass().getResource(pow2Img));
                    setImage(ii.getImage());
                    break;
                }
            case 3:
                {
                    ImageIcon ii = new ImageIcon(getClass().getResource(pow3Img));
                    setImage(ii.getImage());
                    break;
                }                
            default:
                break;
        }
    }
    
    public int getType(){
        return this.type;
    }
    
    public void setTaken(boolean taken){
        this.taken = taken;
    }
    
    public boolean isTaken(){
        return taken;
    }
}
