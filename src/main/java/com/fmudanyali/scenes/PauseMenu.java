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
 * <h3>Pause Menu Scene</h3>
 * This class is pushed above the game scene and
 * it pauses the game as the name implies. It gives
 * and option to either resume the game, which pops itself
 * from the scenes stack, or quit the game which pops itself
 * AND the game.
 * @author Furkan Mudanyali
 * @version 0.9.0
 * @since 2021-12-04
 */
public class PauseMenu extends Scene {
    // Variables
    private SDL_Surface tempSurface;
    private SDL_Texture cont, contDark, quit, quitDark, yes, yesDark, no, noDark, sure;
    private SDL_Rect contPos = new SDL_Rect();
    private SDL_Rect exitPos = new SDL_Rect();
    private SDL_Rect surePos = new SDL_Rect();
    private SDL_Rect yesPos = new SDL_Rect();
    private SDL_Rect noPos = new SDL_Rect();
    private SDL_Rect buttonSize = new SDL_Rect();
    private SDL_Rect confirmSize = new SDL_Rect();
    private SDL_Rect sureSize = new SDL_Rect();
    private boolean escPressed = false;
    private boolean enterPressed = false;
    private boolean exitPressed = false;
    private boolean wPressed = false;
    private boolean sPressed = false;
    private int selection = 0;
    private Pointer menuSound = Mix_LoadWAV(FileLoader.getFilePath("sfx/81-sfx-menu.wav"));
    private Pointer confirmSound = Mix_LoadWAV(FileLoader.getFilePath("sfx/81-sfx-bullet.wav"));

    // Load textures and set their sizes.
    public PauseMenu(){
        buttonSize.x = buttonSize.y = 0;
        sureSize.x = sureSize.y = 0;
        confirmSize.x = confirmSize.y = 0;
        buttonSize.w = contPos.w = exitPos.w = 260;
        buttonSize.h = contPos.h = exitPos.h = 134;
        sureSize.w = surePos.w = 190;
        sureSize.h = surePos.h = 31;
        confirmSize.w = yesPos.w = noPos.w = 120;
        confirmSize.h = yesPos.h = noPos.h = 61;

        contPos.x = (Screen.WIDTH - Screen.CANV_W)/2 + 360/2 - 260/2;
        contPos.y = (Screen.HEIGHT - Screen.CANV_H)/2 + 30;
        exitPos.x = contPos.x;
        exitPos.y = (Screen.HEIGHT - Screen.CANV_H)/2 + 200;
        surePos.x = (Screen.WIDTH - Screen.CANV_W)/2 + 360/2 - 190/2;
        surePos.y = (Screen.HEIGHT - Screen.CANV_H)/2 + 350;
        yesPos.x = contPos.x;
        noPos.x = yesPos.x + 20 + 120;
        yesPos.y = (Screen.HEIGHT - Screen.CANV_H)/2 + 390;
        noPos.y = (Screen.HEIGHT - Screen.CANV_H)/2 + 390;

        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("menus/continue.bmp"));
        cont = SDL_CreateTextureFromSurface(renderer, tempSurface);
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("menus/continuedarken.bmp"));
        contDark = SDL_CreateTextureFromSurface(renderer, tempSurface);
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("menus/quit.bmp"));
        quit = SDL_CreateTextureFromSurface(renderer, tempSurface);
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("menus/quitdarken.bmp"));
        quitDark = SDL_CreateTextureFromSurface(renderer, tempSurface);
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("menus/yes.bmp"));
        yes = SDL_CreateTextureFromSurface(renderer, tempSurface);
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("menus/yesdarken.bmp"));
        yesDark = SDL_CreateTextureFromSurface(renderer, tempSurface);
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("menus/no.bmp"));
        no = SDL_CreateTextureFromSurface(renderer, tempSurface);
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("menus/nodarken.bmp"));
        noDark = SDL_CreateTextureFromSurface(renderer, tempSurface);
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("menus/areyousure.bmp"));
        sure = SDL_CreateTextureFromSurface(renderer, tempSurface);
    }

    @Override
    public void loop(){
        // Event handling (Keyboard, window buttons etc)
        while(SDL_PollEvent(Main.e) != 0){
            switch(Main.e.type){
                case SDL_QUIT:
                    Main.exit = true;
                    break;
                case SDL_KEYDOWN:
                    switch(Main.e.key.keysym.sym){
                        case SDLK_ESCAPE:
                            if(!escPressed){
                                Main.scenes.pop();
                                Mix_VolumeMusic(128);
                                escPressed = true;
                            }
                            break;
                        case SDLK_RETURN:
                            if(!enterPressed){
                                Mix_PlayChannel( -1, confirmSound, 0 );
                                confirm();
                                enterPressed = true;
                            }
                            break;
                        case SDLK_w:
                            if(!wPressed){
                                if(exitPressed) selection = Math.max(Math.min(selection - 1, 3), 2);
                                else selection = Math.max(Math.min(selection - 1, 1), 0);
                                Mix_PlayChannel( -1, menuSound, 0 );
                                wPressed = true;
                            }
                            break;
                        case SDLK_s:
                            if(!sPressed){
                                if(exitPressed) selection = Math.max(Math.min(selection + 1, 3), 2);
                                else selection = Math.min(selection + 1, 1);
                                Mix_PlayChannel( -1, menuSound, 0 );
                                sPressed = true;
                            }
                            break;
                        case SDLK_a:
                            if(!wPressed && exitPressed){
                                selection = Math.max(Math.min(selection - 1, 3), 2);
                                Mix_PlayChannel( -1, menuSound, 0 );
                                wPressed = true;
                            }
                            break;
                        case SDLK_d:
                            if(!sPressed && exitPressed){
                                selection = Math.max(Math.min(selection + 1, 3), 2);
                                Mix_PlayChannel( -1, menuSound, 0 );
                                sPressed = true;
                            }
                    }
                    break;
                case SDL_KEYUP:
                    switch(Main.e.key.keysym.sym){
                        case SDLK_ESCAPE:
                            escPressed = false;
                            break;
                        case SDLK_RETURN:
                            enterPressed = false;
                            break;
                        case SDLK_w:
                        case SDLK_a:
                            wPressed = false;
                            break;
                        case SDLK_s:
                        case SDLK_d:
                            sPressed = false;
                            break;
                    }
                    break;
            }
        }
        // Clear renderer and copy background
        SDL_RenderClear(renderer);
        SDL_RenderCopy(renderer, Screen.wallpaper, null, null);
        SDL_RenderCopy(renderer, null, Screen.canvas, Screen.canvasPos);
        // Copy button textures depending on the selection
        switch(selection){
            case 0:
                SDL_RenderCopy(renderer, cont, buttonSize, contPos);
                SDL_RenderCopy(renderer, quitDark, buttonSize, exitPos);
                break;
            case 1:
                SDL_RenderCopy(renderer, contDark, buttonSize, contPos);
                SDL_RenderCopy(renderer, quit, buttonSize, exitPos);
                break;
            case 2:
                SDL_RenderCopy(renderer, contDark, buttonSize, contPos);
                SDL_RenderCopy(renderer, quitDark, buttonSize, exitPos);
                SDL_RenderCopy(renderer, sure, sureSize, surePos);
                SDL_RenderCopy(renderer, yes, confirmSize, yesPos);
                SDL_RenderCopy(renderer, noDark, confirmSize, noPos);
                break;
            case 3:
                SDL_RenderCopy(renderer, contDark, buttonSize, contPos);
                SDL_RenderCopy(renderer, quitDark, buttonSize, exitPos);
                SDL_RenderCopy(renderer, sure, sureSize, surePos);
                SDL_RenderCopy(renderer, yesDark, confirmSize, yesPos);
                SDL_RenderCopy(renderer, no, confirmSize, noPos);
                break;
        }
        SDL_RenderPresent(renderer);
    }
    /**
     * Does stuff depending on the selection.
     */
    private void confirm(){
        switch(selection){
            case 0:
                Main.scenes.pop();
                Mix_VolumeMusic(128);
                Game.escPressed = false;
                break;
            case 1:
                exitPressed = true;
                selection = 2;
                break;
            case 2:
                Main.scenes.pop();
                Main.scenes.pop();
                Main.scenes.pop();
                Main.scenes.push(new MainMenu());
                break;
            case 3:
                exitPressed = false;
                selection = 1;
                break;
        }
    }
}
