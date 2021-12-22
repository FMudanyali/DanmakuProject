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
import com.fmudanyali.bullets.PlayerBullet;

import static com.fmudanyali.Audio.*;
import static org.libsdl.api.event.SdlEvents.*;
import static com.fmudanyali.Render.*;
import static org.libsdl.api.keycode.SDL_Keycode.*;
import static org.libsdl.api.render.SdlRender.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game extends Scene {
    public static boolean escPressed = false;
    int kek = 0;
    private Player player = new Player();
    public static List<PlayerBullet> playerBullets = new ArrayList<>();
    private static Thread thread;
    private static Runnable runnable = new Runnable() {
        public void run(){
            try {
                Mix_PlayMusic(Mix_LoadMUS(FileLoader.getFilePath("80-search-intro.wav")), 1);
                Thread.sleep(getMusicLengthInMilliseconds(FileLoader.getFilePath("80-search-intro.wav")));
                Mix_PlayMusic(Mix_LoadMUS(FileLoader.getFilePath("80-search-loop.wav")), -1);
                Thread.currentThread().interrupt();
                return;
            } catch(Exception e){
                // Will throw sleep interrupted, which is what we want.
            }
        }
    };

    public Game(){
        Screen.makeBackground("shmap.bmp");
        Mix_VolumeMusic(128);
        try {
            if(thread.getState() != Thread.State.TERMINATED){
                thread.interrupt();
            }
        } catch (Exception e){
            //
        }
        thread = new Thread(runnable);
        thread.start();
        
    }

    @Override
    public void loop(){
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
        Screen.scroll(player);

        SDL_RenderClear(renderer);
        SDL_RenderCopy(renderer, Screen.wallpaper, null, null);
        SDL_RenderCopy(renderer, Screen.background, Screen.canvas, Screen.canvasPos);
        SDL_RenderCopy(renderer, player.texture, player.shipFrame, player.position);
        SDL_RenderCopy(renderer, player.propeller, player.propellerFrame, player.propellerPos);
        SDL_RenderCopy(renderer, player.shooter, player.shooterFrame, player.shooterPos);

        for(Iterator<PlayerBullet> bulletIterator = playerBullets.iterator(); bulletIterator.hasNext();){
            PlayerBullet b = bulletIterator.next();
            b.fly();
            if(b.position.y < 30){
                bulletIterator.remove();
            } else {
                SDL_RenderCopy(renderer, PlayerBullet.texture, null, b.position);
            }
        }

        SDL_RenderPresent(renderer);
        player.shiftFrame();
    }
}