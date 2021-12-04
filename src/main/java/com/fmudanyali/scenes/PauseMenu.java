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
import com.fmudanyali.Keyboard;

import org.libsdl.api.render.*;
import org.libsdl.api.surface.SDL_Surface;

import static com.fmudanyali.Render.*;
import static org.libsdl.api.render.SdlRender.*;
import static org.libsdl.api.surface.SdlSurface.*;
import static org.libsdl.api.scancode.SDL_Scancode.*;
import static org.libsdl.api.event.SdlEvents.*;
import static org.libsdl.api.keycode.SDL_Keycode.*;

public class PauseMenu extends Scene {
    public static SDL_Surface textureSurface =
        SDL_CreateRGBSurface(0, WIDTH, HEIGHT, 32, 0, 0, 0, 0);
    public static SDL_Texture menuTexture;
    public static boolean escPressed = false;

    public PauseMenu(){
        SDL_FillRect(textureSurface, null, 0);
        menuTexture = SDL_CreateTextureFromSurface(renderer, textureSurface);
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
                                Main.scenes.pop();
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

        SDL_RenderClear(renderer);
        SDL_RenderCopy(renderer, menuTexture, null, null);
        SDL_RenderPresent(renderer);
    }
}
