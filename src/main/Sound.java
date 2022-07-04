package main;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sound {

    private static File anthem = new File("audio/Ukrainian_national_anthem.wav");
    private static File upa = new File("audio/Oy_u_luzi_chervona_kalyna.wav");

    public static Clip getClip(Music music) {
        File file;
        switch (music) {
            case Background_Anthem:
                file = Sound.anthem;
                break;
            case Background_UPA:
                file = Sound.upa;
                break;
            default:
                file = null;
        }
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}