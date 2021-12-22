/*
 *   Copyright (c) 2021 Furkan Mudanyali

 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.

 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.

 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.fmudanyali;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

import org.libsdl.api.rwops.SDL_RWops;

import static org.libsdl.api.rwops.SdlRWops.SDL_RWFromFile;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * <h3>Audio Class</h3>
 * 
 * This class is a native wrapper for SDL2_Mixer,
 * it also contains some additional methods
 * 
 * @author Furkan Mudanyali
 * @version 0.1.0
 * @since 2021-12-20
 */
public class Audio {
    // Using SDL2_Mixer for native functions
    static {
        // TODO: Better way to load SDL2_Mixer on Windows
        if(System.getProperty("os.name").contains("Win")){
            try {
                if(System.getenv("ProgramFiles(x86)") != null){
                    System.load(FileLoader.getFilePath("libraries/w64/libmodplug-1.dll"));
                    System.load(FileLoader.getFilePath("libraries/w64/libogg-0.dll"));
                    System.load(FileLoader.getFilePath("libraries/w64/libvorbis-0.dll"));
                    System.load(FileLoader.getFilePath("libraries/w64/libvorbisfile-3.dll"));
                    System.load(FileLoader.getFilePath("libraries/w64/libopus-0.dll"));
                    System.load(FileLoader.getFilePath("libraries/w64/libopusfile-0.dll"));
                    System.load(FileLoader.getFilePath("libraries/w64/libFLAC-8.dll"));
                    System.load(FileLoader.getFilePath("libraries/w64/libmpg123-0.dll"));
                    System.load(FileLoader.getFilePath("libraries/w64/SDL2.dll"));
                } else{
                    System.load(FileLoader.getFilePath("libraries/w32/libmodplug-1.dll"));
                    System.load(FileLoader.getFilePath("libraries/w32/libogg-0.dll"));
                    System.load(FileLoader.getFilePath("libraries/w32/libvorbis-0.dll"));
                    System.load(FileLoader.getFilePath("libraries/w32/libvorbisfile-3.dll"));
                    System.load(FileLoader.getFilePath("libraries/w32/libopus-0.dll"));
                    System.load(FileLoader.getFilePath("libraries/w32/libopusfile-0.dll"));
                    System.load(FileLoader.getFilePath("libraries/w32/libFLAC-8.dll"));
                    System.load(FileLoader.getFilePath("libraries/w32/libmpg123-0.dll"));
                    System.load(FileLoader.getFilePath("libraries/w32/SDL2.dll"));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        Native.register("SDL2_mixer");
    }

    // Constants from header
    public static final int AUDIO_U8 = 0x0008;
    public static final int AUDIO_S8 = 0x8008;
    public static final int AUDIO_U16LSB = 0x0010;
    public static final int AUDIO_S16LSB = 0x8010;
    public static final int AUDIO_U16MSB = 0x1010;
    public static final int AUDIO_S16MSB = 0x9010;
    public static final int AUDIO_U16 = AUDIO_U16LSB;
    public static final int AUDIO_S16 = AUDIO_S16LSB;
    public static final int AUDIO_S32LSB = 0x8020;
    public static final int AUDIO_S32MSB = 0x9020;
    public static final int AUDIO_S32 = AUDIO_S32LSB;
    public static final int AUDIO_F32LSB = 0x8120;
    public static final int AUDIO_F32MSB = 0x9120;
    public static final int AUDIO_F32 = AUDIO_F32LSB;

    public static final int MIX_DEFAULT_FREQUENCY = 44100;
    public static final int MIX_DEFAULT_FORMAT = AUDIO_S16;
    public static final int MIX_DEFAULT_CHANNELS = 2;
    public static final int MIX_MAX_VOLUME = 128;
    public static final int MIX_CHANNEL_POST = -2;

    // Native functions (Not all of them)
    public static native Pointer Mix_Linked_Version();
    public static native int Mix_Init(int flags);
    public static native void Mix_Quit();
    public static native int Mix_OpenAudio(int frequency, int format, int channels, int chunksize);
    public static native void Mix_CloseAudio();
    public static native int Mix_QuerySpec(int frequency, int format, int channels);

    public static native int Mix_GetNumChunkDecoders();
    public static native String Mix_GetChunkDecoder();
    public static native Pointer Mix_LoadWAV_RW(SDL_RWops src, int freesrc);
    public static native Pointer Mix_LoadMUS(String file);
    public static native int Mix_VolumeMusic(int volume);

    public static native int Mix_PlayChannelTimed(int channel, Pointer chunk, int loops, int ticks);
    public static native int Mix_PlayMusic(Pointer music, int loops);
    public static native int Mix_PlayingMusic();
    public static native int Mix_PausedMusic();
    public static native void Mix_ResumeMusic();
    public static native void Mix_PauseMusic();
    public static native void Mix_HaltMusic();
    public static native void Mix_FreeChunk(Pointer chunk);
    public static native void Mix_FreeMusic(Pointer music);

    // Defines from header
    public static Pointer Mix_LoadWAV(String file){
        return Mix_LoadWAV_RW(SDL_RWFromFile(file, "rb"), 1);
    }
    public static int Mix_PlayChannel(int channel, Pointer chunk, int loops){
        return Mix_PlayChannelTimed(channel, chunk, loops, -1);
    }
    // Methods

    /**
     * Credits to mdma from stackoverflow
     * https://stackoverflow.com/a/3009973
     * 
     * @param filepath WAV file path
     * @return length in milliseconds
     */
    public static long getMusicLengthInMilliseconds(String filepath){
        try {
            File file = new File(filepath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            return (long)Math.floor(1000*(frames+0.0) / format.getFrameRate()); 
        } catch (Exception e) {
            return -1;
        }
    }
}