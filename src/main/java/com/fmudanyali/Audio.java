package com.fmudanyali;

import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import java.util.LinkedList;
import java.util.Queue;

import java.io.File;
import java.io.IOException;

public class Audio {
    private static Queue<String> audioQueue = new LinkedList<String>();
    private static Clip clip;

    public static void init() throws Exception{
        clip = AudioSystem.getClip();
        Thread thread = new Thread(){
            public void run(){
                String current = new String();
                while(!Main.exit){
                    if(audioQueue.isEmpty()){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(audioQueue.size() == 1){
                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            clip.loop(0);
                        }
                        if(current != audioQueue.peek()){
                            current = audioQueue.peek();
                            try {
                                addAudio(current);
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(clip.getMicrosecondLength() == clip.getMicrosecondPosition() && audioQueue.size() > 1){
                            audioQueue.remove();
                        }
                    }
                }
            }
        };
        thread.start();
    }

    public static void playAudio(String file) throws Exception{
        File f = new File(FileLoader.getFilePath(file));
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
        clip.open(audioIn);
        clip.start();
    }

    private static void addAudio(String file) throws Exception{
        File f = new File(FileLoader.getFilePath(file));
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
        clip.close();
        clip.open(audioIn);
    }

    public static void queueAudio(String file){
        audioQueue.add(file);
    }
}