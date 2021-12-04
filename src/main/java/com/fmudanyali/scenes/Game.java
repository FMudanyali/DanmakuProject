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
import com.fmudanyali.Keyboard;
import com.fmudanyali.Main;
import com.fmudanyali.Render;
import com.fmudanyali.Time;
import com.sun.jna.ptr.IntByReference;

import org.libsdl.api.render.*;
import org.libsdl.api.surface.SDL_Surface;
import org.libsdl.api.rect.SDL_Rect;

import static org.libsdl.api.render.SdlRender.*;
import static org.libsdl.api.surface.SdlSurface.*;
import static org.libsdl.api.video.SdlVideo.*;
import static org.libsdl.api.Sdl.*;
import static org.libsdl.api.scancode.SDL_Scancode.*;
import static org.libsdl.api.event.SdlEvents.*;
import static com.fmudanyali.Render.*;
import static org.libsdl.api.keycode.SDL_Keycode.*;

public class Game extends Scene {
    public static SDL_Texture texture, background, wallpaper, player;
    public static SDL_Surface textureSurface = new SDL_Surface();

    public static SDL_Rect viewport = new SDL_Rect();
    public static SDL_Rect canvas = new SDL_Rect();
    public static SDL_Rect playerPos = new SDL_Rect();

    public static boolean exit = false;
    public static int speed;
    public static boolean escPressed = false;

    public static IntByReference bgwptr = new IntByReference();
    public static IntByReference bghptr = new IntByReference();

    public Game() throws Exception{
        // Create surface to load BMP into
        textureSurface = SDL_LoadBMP(FileLoader.getFilePath("image.bmp"));
        // Create texture from the surface
        wallpaper = SDL_CreateTextureFromSurface(renderer, textureSurface);
        // Destroy the surface
        textureSurface = null;
        textureSurface = SDL_LoadBMP(FileLoader.getFilePath("tile.bmp"));
        texture = SDL_CreateTextureFromSurface(renderer, textureSurface);
        textureSurface = null;
        textureSurface = SDL_LoadBMP(FileLoader.getFilePath("person.bmp"));
        player = SDL_CreateTextureFromSurface(renderer, textureSurface);
        textureSurface = null;
        // Create a background double the width of the texture, used for scrolling animation
        background = Render.createBackgroundFromTexture(renderer, texture, 12, 30);
        SDL_QueryTexture(background, null, null, bgwptr, bghptr);

        // Dimensions of the viewport, its x and y is used for positioning
        viewport.x = viewport.y = 0;
        viewport.w = canvas.w = 384;
        viewport.h = canvas.h = 480;
        canvas.x = (WIDTH - canvas.w)/2;
        canvas.y = (HEIGHT - canvas.h)/2;
        playerPos.x = playerPos.y = viewport.w - 16;
        playerPos.w = playerPos.h = 32;
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

        if(Keyboard.getKeyState(SDL_SCANCODE_LSHIFT)){
            speed = 1;
        }else{
            speed = 2;
        }

        if(Keyboard.getKeyState(SDL_SCANCODE_A) | Keyboard.getKeyState(SDL_SCANCODE_LEFT)){
            playerPos.x = Math.max(playerPos.x - (int)(speed * Time.deltaTime * 0.1), canvas.x);
        }
        if(Keyboard.getKeyState(SDL_SCANCODE_D) | Keyboard.getKeyState(SDL_SCANCODE_RIGHT)){
            playerPos.x = Math.min(playerPos.x + (int)(speed * Time.deltaTime * 0.1), canvas.x + canvas.w - 32);
        }
        if(Keyboard.getKeyState(SDL_SCANCODE_W) | Keyboard.getKeyState(SDL_SCANCODE_UP)){
            playerPos.y = Math.max(playerPos.y - (int)(speed * Time.deltaTime * 0.1), canvas.y);
        }
        if(Keyboard.getKeyState(SDL_SCANCODE_S) | Keyboard.getKeyState(SDL_SCANCODE_DOWN)){
            playerPos.y = Math.min(playerPos.y + (int)(speed * Time.deltaTime * 0.1), canvas.y + canvas.h - 32);
        }

        viewport.y = Math.floorMod(viewport.y - (int)(Time.deltaTime * 0.1), bghptr.getValue() - canvas.h);

        SDL_RenderClear(renderer);
        SDL_RenderCopy(renderer, wallpaper, null, null);
        SDL_RenderCopy(renderer, background, viewport, canvas);
        SDL_RenderCopy(renderer, player, null, playerPos);
        SDL_RenderPresent(renderer);
    }
    
    public static void quit(){
        SDL_DestroyTexture(texture);
        SDL_DestroyRenderer(renderer);
        SDL_DestroyWindow(Render.window);
        SDL_Quit();
    }
}