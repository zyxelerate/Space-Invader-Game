package spacegame;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {
    public static final AudioClip SHOT = Applet.newAudioClip(Sound.class.getResource("Shot.wav"));
    public static final AudioClip BOOM = Applet.newAudioClip(Sound.class.getResource("Boom.wav"));
    public static final AudioClip GG = Applet.newAudioClip(Sound.class.getResource("gg.wav"));
    public static final AudioClip HURT = Applet.newAudioClip(Sound.class.getResource("hurt.wav"));
    public static final AudioClip POWERUP = Applet.newAudioClip(Sound.class.getResource("powerUp.wav"));
    public static final AudioClip GET_SHIELD = Applet.newAudioClip(Sound.class.getResource("getShield.wav"));
    public static final AudioClip GET_HEALTH = Applet.newAudioClip(Sound.class.getResource("getHealth.wav"));
    public static final AudioClip SHIELDHURT = Applet.newAudioClip(Sound.class.getResource("shieldHit.wav"));
}
