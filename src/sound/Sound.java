package sound;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class Sound {

    private static File anthem = new File("audio/Ukrainian_national_anthem.wav");
    private static File upa = new File("audio/Oy_u_luzi_chervona_kalyna.wav");
    private static File bestiary = new File("audio/shum_eurovision.wav");
    private static File cossack_death = new File("audio/cossack_death.wav");
    private static File enemy_death = new File("audio/enemy_death.wav");
    private static File sabre_sound = new File("audio/sabre_sound.wav");
    private static File knife_sound = new File("audio/throw_knife_sound.wav");
    private static File bonus_throw = new File("audio/bonus_throw.wav");
    private static File victory_sound = new File("audio/victory.wav");
    private static File viy_opens_eyes = new File("audio/viy_opens_eyes.wav");
    private static File bonus_pick = new File("audio/bonus_pick.wav");
    private static File coin_pick = new File("audio/coin_pick.wav");

    public static Clip getClip(Music music) {
        File file;
        switch (music) {
            case Background_Anthem:
                file = Sound.anthem;
                break;
            case Background_UPA:
                file = Sound.upa;
                break;
            case Bestiary_Background:
                file = Sound.bestiary;
                break;
            case Cossack_Death:
                file = Sound.cossack_death;
                break;
            case Enemy_Death:
                file = Sound.enemy_death;
                break;
            case Sabre_Sound:
                file = Sound.sabre_sound;
                break;
            case Knife_Sound:
                file = Sound.knife_sound;
                break;
            case Bonus_Throw:
                file = Sound.bonus_throw;
                break;
            case Victory_Sound:
                file = Sound.victory_sound;
                break;
            case Viy_Opens_Eyes:
                file = Sound.viy_opens_eyes;
                break;
            case Take_Bonus:
                file = Sound.bonus_pick;
                break;
            case Coin_Pick:
                file = Sound.coin_pick;
                break;
            default:
                file = null;
        }
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            return clip;
        } catch (Exception ignored) { }
        return null;
    }

    public static void setVolume(Clip clip, float level) {
        if (level < 0 || level > 1)
            throw new IllegalArgumentException("Sound level has to be in range 0..1!");
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        if (volume != null) {
            volume.setValue(20f * (float) Math.log10(level));
        }
    }
}