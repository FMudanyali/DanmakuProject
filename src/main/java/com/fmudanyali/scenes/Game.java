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

package com.fmudanyali.scenes;

import com.fmudanyali.FileLoader;
import com.fmudanyali.Main;
import com.fmudanyali.Screen;
import com.fmudanyali.characters.Player;

import static com.fmudanyali.Audio.*;
import static org.libsdl.api.event.SdlEvents.*;
import static com.fmudanyali.Render.*;
import static org.libsdl.api.keycode.SDL_Keycode.*;
import static org.libsdl.api.render.SdlRender.*;
//import static org.libsdl.api.error.SdlError.SDL_GetError;

public class Game extends Scene {
    public static boolean escPressed = false;
    int kek = 0;
    private Player player = new Player();

    public Game() throws Exception{
        Screen.makeBackground("scene1/tile.bmp");
        Mix_OpenAudio(44100, MIX_DEFAULT_FORMAT, 2, 2048);
        Mix_VolumeMusic(128);
        Thread thread = new Thread(){
            public void run(){
                try {
                    Mix_PlayMusic(Mix_LoadMUS(FileLoader.getFilePath("80-search-intro.wav")), 1);
                    while(Mix_PlayingMusic() == 1);
                    Mix_PlayMusic(Mix_LoadMUS(FileLoader.getFilePath("80-search-loop.wav")), -1);
                    Thread.currentThread().interrupt();
                    return;
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    @Override
    public void loop() throws Exception{
        while(SDL_PollEvent(Main.e) != 0){
            switch(Main.e.type){
                case SDL_QUIT:
                    Main.exit = true;
                    break;
                case SDL_KEYDOWN:
                    switch(Main.e.key.keysym.sym){
                        case SDLK_ESCAPE:
                            if(!escPressed){
                                Main.scenes.push(new PauseMenu());
                                Mix_VolumeMusic(30);
                                escPressed = true;
                            }
                            break;
                    }
                    break;
                case SDL_KEYUP:
                    switch(Main.e.key.keysym.sym){
                        case SDLK_ESCAPE:
                            escPressed = false;
                            break;
                    }
                    break;
            }
        }

        player.movement();
        Screen.scroll();

        SDL_RenderClear(renderer);
        SDL_RenderCopy(renderer, Screen.wallpaper, null, null);
        SDL_RenderCopy(renderer, Screen.background, Screen.canvas, Screen.canvasPos);
        SDL_RenderCopy(renderer, player.texture, null, player.position);
        SDL_RenderPresent(renderer);
    }
}