package com.quickbite.connector2;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Paha on 5/30/2016.
 */
public class SoundManager {
    public static float gameSoundVolume = 1f;
    public static float gameMusicVolume = 0.7f;

    private static Music currMusic;
    private static boolean musicOn = true, soundsOn = true;

    public static void playSound(String name){
        SoundManager.playSound(name, gameSoundVolume);
    }

    public static void playSound(String name, float volume){
        if(!soundsOn) return;

        Sound sound = Game.easyAssetManager.get(name, Sound.class);
        long id = sound.play(volume);
        System.out.println(id+"");
    }

    public static void stopSound(String name){

    }

    public static void playMusic(){
        if(!musicOn) return;

        if(Game.easyAssetManager != null) {
            if (currMusic == null) {
                Music music = Game.easyAssetManager.get("gameMusicTrack", Music.class);
                if (music == null) return;
                music.setVolume(gameMusicVolume);
                music.setLooping(true);
                music.play();
                currMusic = music;
            } else if (!currMusic.isPlaying()) {
                currMusic.setVolume(gameMusicVolume);
                currMusic.setLooping(true);
                currMusic.play();
            }
        }
    }

    /**
     * Sets if the music should be play or not.
     * @param playing True if the music should play, false otherwise
     */
    public static void setMusicOn(boolean playing){
        musicOn = playing;

        if(!musicOn && currMusic.isPlaying())
            currMusic.pause();
        else if(musicOn)
            playMusic();
    }

    /**
     * Sets if the sound effects should play or not.
     * @param on True if the sounds should play, false otherwise.
     */
    public static void setSoundsOn(boolean on){
        soundsOn = on;
    }

    public static void pauseMusic(){
        if(currMusic!=null && currMusic.isPlaying())
            currMusic.pause();
    }

    public static boolean isMusicOn(){
        return musicOn;
    }

    public static boolean isSoundsOn(){
        return soundsOn;
    }

    public static void dispose() {
        //We need to unload this because it won't play if we open the app again after using Gdx.app.exit()
        Game.easyAssetManager.unload("gameMusicTrack");
        currMusic.stop();
        currMusic.dispose();
        currMusic = null;
    }
}
