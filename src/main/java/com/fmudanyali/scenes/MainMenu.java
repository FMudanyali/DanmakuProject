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

public class MainMenu extends Scene {
    private SDL_Surface tempSurface;
    private SDL_Texture startTexture, exitTexture, background;
    private SDL_Rect startPos = new SDL_Rect();
    private SDL_Rect exitPos = new SDL_Rect();
    private SDL_Rect buttonSize = new SDL_Rect();
    private boolean enterPressed = false;
    private boolean wPressed = false;
    private boolean sPressed = false;
    private int selection = 0;
    
    public MainMenu() throws Exception{
        Mix_PauseMusic();
        buttonSize.x = buttonSize.y = 0;
        buttonSize.w = startPos.w = exitPos.w = 150;
        buttonSize.h = startPos.h = exitPos.h = 100;
        startPos.x = (Screen.WIDTH - Screen.CANV_W)/2 + 10;
        startPos.y = (Screen.HEIGHT - Screen.CANV_H)/2 + 10;
        exitPos.x = (Screen.WIDTH - Screen.CANV_W)/2 + 10;
        exitPos.y = (Screen.HEIGHT - Screen.CANV_H)/2 + 120;

        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("menu.bmp"));
        background = SDL_CreateTextureFromSurface(renderer, tempSurface);
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("startsel.bmp"));
        startTexture = SDL_CreateTextureFromSurface(renderer, tempSurface);
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("exit.bmp"));
        exitTexture = SDL_CreateTextureFromSurface(renderer, tempSurface);
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
                        case SDLK_RETURN:
                            if(!enterPressed){
                                confirm();
                                enterPressed = true;
                            }
                            break;
                        case SDLK_w:
                            if(!wPressed){
                                selection = Math.min(selection - 1, 1);
                                wPressed = true;
                                updateButtons();
                            }
                            break;
                        case SDLK_s:
                            if(!sPressed){
                                selection = Math.min(selection + 1, 1);
                                sPressed = true;
                                updateButtons();
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

        SDL_RenderClear(renderer);
        SDL_RenderCopy(renderer, Screen.wallpaper, null, null);
        SDL_RenderCopy(renderer, background, Screen.canvas, Screen.canvasPos);
        SDL_RenderCopy(renderer, startTexture, buttonSize, startPos);
        SDL_RenderCopy(renderer, exitTexture, buttonSize, exitPos);
        SDL_RenderPresent(renderer);
    }

    private void updateButtons() throws Exception{
        switch(selection){
            case 0:
                tempSurface = SDL_LoadBMP(FileLoader.getFilePath("startsel.bmp"));
                startTexture = SDL_CreateTextureFromSurface(renderer, tempSurface);
                tempSurface = SDL_LoadBMP(FileLoader.getFilePath("exit.bmp"));
                exitTexture = SDL_CreateTextureFromSurface(renderer, tempSurface);
                break;
            case 1:
                tempSurface = SDL_LoadBMP(FileLoader.getFilePath("start.bmp"));
                startTexture = SDL_CreateTextureFromSurface(renderer, tempSurface);
                tempSurface = SDL_LoadBMP(FileLoader.getFilePath("exitsel.bmp"));
                exitTexture = SDL_CreateTextureFromSurface(renderer, tempSurface);
                break;
        }
    }

    private void confirm() throws Exception{
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
