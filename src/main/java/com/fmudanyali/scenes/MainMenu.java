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

import com.fmudanyali.Main;
import com.fmudanyali.Screen;
import com.sun.jna.Pointer;
import com.fmudanyali.FileLoader;

import org.libsdl.api.rect.SDL_Rect;
import org.libsdl.api.render.*;
import org.libsdl.api.surface.SDL_Surface;

import static com.fmudanyali.Render.*;
import static com.fmudanyali.Audio.*;
import static org.libsdl.api.render.SdlRender.*;
import static org.libsdl.api.surface.SdlSurface.*;
import static org.libsdl.api.event.SdlEvents.*;
import static org.libsdl.api.keycode.SDL_Keycode.*;

/**
 * <h3>Main Menu Scene</h3>
 * This scene gives 2 options, start option
 * pushes new game scene to the scenes stack,
 * quit option pops itself, closing the application.
 * 
 * @author Furkan Mudanyali
 * @version 0.9.0
 * @since 2021-12-04
 */
public class MainMenu extends Scene {
    private SDL_Surface tempSurface;
    private SDL_Texture start, startDark, quit, quitDark;
    private SDL_Rect startPos = new SDL_Rect();
    private SDL_Rect exitPos = new SDL_Rect();
    private SDL_Rect buttonSize = new SDL_Rect();
    private boolean enterPressed = false;
    private boolean wPressed = false;
    private boolean sPressed = false;
    private int selection = 0;
    private Pointer menuSound = Mix_LoadWAV(FileLoader.getFilePath("sfx/81-sfx-menu.wav"));
    private Pointer confirmSound = Mix_LoadWAV(FileLoader.getFilePath("sfx/81-sfx-bullet.wav"));
    
    public MainMenu(){
        Mix_PauseMusic();
        buttonSize.x = buttonSize.y = 0;
        buttonSize.w = startPos.w = exitPos.w = 260;
        buttonSize.h = startPos.h = exitPos.h = 134;
        startPos.x = (Screen.WIDTH - Screen.CANV_W)/2 + 360/2 - 260/2;
        startPos.y = (Screen.HEIGHT - Screen.CANV_H)/2 + 30;
        exitPos.x = startPos.x;
        exitPos.y = (Screen.HEIGHT - Screen.CANV_H)/2 + 200;

        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("menus/start.bmp"));
        start = SDL_CreateTextureFromSurface(renderer, tempSurface);
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("menus/startdarken.bmp"));
        startDark = SDL_CreateTextureFromSurface(renderer, tempSurface);
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("menus/quit.bmp"));
        quit = SDL_CreateTextureFromSurface(renderer, tempSurface);
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("menus/quitdarken.bmp"));
        quitDark = SDL_CreateTextureFromSurface(renderer, tempSurface);
    }

    @Override
    public void loop(){
        // Event handling (keyboard, window buttons etc)
        while(SDL_PollEvent(Main.e) != 0){
            switch(Main.e.type){
                case SDL_QUIT:
                    Main.exit = true;
                    break;
                case SDL_KEYDOWN:
                    switch(Main.e.key.keysym.sym){
                        case SDLK_RETURN:
                            if(!enterPressed){
                                Mix_PlayChannel( -1, confirmSound, 0 );
                                confirm();
                                enterPressed = true;
                            }
                            break;
                        case SDLK_w:
                            if(!wPressed){
                                selection = Math.min(selection - 1, 1);
                                Mix_PlayChannel( -1, menuSound, 0 );
                                wPressed = true;
                            }
                            break;
                        case SDLK_s:
                            if(!sPressed){
                                selection = Math.min(selection + 1, 1);
                                Mix_PlayChannel( -1, menuSound, 0 );
                                sPressed = true;
                            }
                            break;
                    }
                    break;
                case SDL_KEYUP:
                    switch(Main.e.key.keysym.sym){
                        case SDLK_RETURN:
                            enterPressed = false;
                            break;
                        case SDLK_w:
                            wPressed = false;
                            break;
                        case SDLK_s:
                            sPressed = false;
                            break;
                    }
                    break;
            }
        }
        // Clear renderer
        SDL_RenderClear(renderer);
        // Copy background
        SDL_RenderCopy(renderer, Screen.wallpaper, null, null);
        SDL_RenderCopy(renderer, null, Screen.canvas, Screen.canvasPos);
        // Copy button textures depending on the selection
        switch(selection){
            case 0:
                SDL_RenderCopy(renderer, start, buttonSize, startPos);
                SDL_RenderCopy(renderer, quitDark, buttonSize, exitPos);
                break;
            case 1:
                SDL_RenderCopy(renderer, startDark, buttonSize, startPos);
                SDL_RenderCopy(renderer, quit, buttonSize, exitPos);
                break;
        }
        // Present renderer
        SDL_RenderPresent(renderer);
    }

    /**
     * Does stuff depending on the selection.
     */
    private void confirm(){
        switch(selection){
            case 0:
                Main.scenes.push(new Game());
                break;
            case 1:
                Main.scenes.pop();
                break;
        }
    }
}
