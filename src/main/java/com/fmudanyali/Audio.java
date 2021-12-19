package com.fmudanyali;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

import org.libsdl.api.rwops.SDL_RWops;

import static org.libsdl.api.rwops.SdlRWops.SDL_RWFromFile;

public class Audio {
    // Using SDL2_Mixer for native functions
    static {
        Native.register("SDL2_mixer");
    }

    // Constants
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

    // Native functions
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

    // Defines
    public static Pointer Mix_LoadWAV(String file){
        return Mix_LoadWAV_RW(SDL_RWFromFile(file, "rb"), 1);
    }
    public static int Mix_PlayChannel(int channel, Pointer chunk, int loops){
        return Mix_PlayChannelTimed(channel, chunk, loops, -1);
    }
}