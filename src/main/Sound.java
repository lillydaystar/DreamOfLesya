package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.net.URL;

public class Sound {

    private Clip clip;
    private URL anthem_url;
    private URL upa_url;

    public Sound() {
        anthem_url = getClass().getResource("./audio/Ukrainian_national_anthem.wav");
        upa_url = getClass().getResource("./audio/Oy_u_luzi_chervona_kalyna.wav");
    }

    public void setMusic(Music music) {
        try {
            /*switch (music) {
                case Background_UPA: {*/
                    AudioInputStream ais = AudioSystem.getAudioInputStream(upa_url);
                    this.clip = AudioSystem.getClip();
                    this.clip.open(ais);
                    /*break;
                }
                case Background_Anthem: {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(anthem_url);
                    this.clip = AudioSystem.getClip();
                    this.clip.open(ais);
                    break;
                }
            }*/
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error while loading music",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void stop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void loop() {
        clip.stop();
    }
}