package com.quickbite.connector2;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Paha on 5/30/2016.
 */
public class SoundManager {
    public static float gameSoundVolume = 1f;
    public static float gameMusicVolume = 0.7f;

    public static void playSound(String name){
        SoundManager.playSound(name, gameSoundVolume);
    }

    private static Music currMusic;

    public static void playSound(String name, float volume){
        Sound sound = Game.easyAssetManager.get(name, Sound.class);
        sound.play(volume);
    }

    public static void stopSound(String name){

    }

    public static void playMusic(String name){
        Music music = Game.easyAssetManager.get(name, Music.class);
        music.setVolume(gameMusicVolume);
        music.setLooping(true);
        music.play();
        currMusic = music;
    }

    public static void stopMusic(String name){

    }

    public static Music getCurrentMusic(){
        return currMusic;
    }

}
