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

import com.sun.jna.ptr.IntByReference;

import org.libsdl.api.render.*;
import org.libsdl.api.video.SDL_Window;
import org.libsdl.api.surface.SDL_Surface;
import org.libsdl.api.rect.SDL_Rect;
import org.libsdl.api.event.events.SDL_Event;

import static org.libsdl.api.render.SdlRender.*;
import static org.libsdl.api.surface.SdlSurface.*;
import static org.libsdl.api.video.SDL_WindowFlags.*;
import static org.libsdl.api.video.SdlVideo.*;
import static org.libsdl.api.SDL_SubSystem.*;
import static org.libsdl.api.Sdl.*;
import static org.libsdl.api.scancode.SDL_Scancode.*;
//import static org.libsdl.api.error.SdlError.*;
import static org.libsdl.api.event.SdlEvents.*;

public class Game {
    public static SDL_Window window;
    public static SDL_Renderer renderer;
    public static SDL_Texture texture, background, wallpaper, player;
    public static SDL_Surface textureSurface = new SDL_Surface();

    public static SDL_Rect viewport = new SDL_Rect();
    public static SDL_Rect canvas = new SDL_Rect();
    public static SDL_Rect playerPos = new SDL_Rect();

    public static SDL_Event e = new SDL_Event();
    public static boolean exit = false;

    public static int WIDTH = 960;
    public static int HEIGHT = 540;
    public static int speed;

    public static IntByReference bgwptr = new IntByReference();
    public static IntByReference bghptr = new IntByReference();

    public static void initialize() throws Exception{
        // Initialize SDL
        SDL_Init(SDL_INIT_VIDEO);
        // Create Window
        window = SDL_CreateWindow("SDL Java Test",
            SDL_WINDOWPOS_CENTERED(), SDL_WINDOWPOS_CENTERED(),
            WIDTH, HEIGHT, SDL_WINDOW_SHOWN);
        
        // Create Renderer
        renderer = SDL_CreateRenderer(window, -1,
            SDL_RENDERER_ACCELERATED | SDL_RENDERER_PRESENTVSYNC);

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
        System.out.printf("Background: %dx%d%n", bgwptr.getValue(), bghptr.getValue());

        // Dimensions of the viewport, its x and y is used for positioning
        viewport.x = viewport.y = 0;
        viewport.w = canvas.w = 384;
        viewport.h = canvas.h = 480;
        canvas.x = (WIDTH - canvas.w)/2;
        canvas.y = (HEIGHT - canvas.h)/2;
        playerPos.x = playerPos.y = viewport.w - 16;
        playerPos.w = playerPos.h = 32;
        System.out.printf("BG Res: %dx%d%n", bgwptr.getValue(), bghptr.getValue());
    }

    public static void loop(){
        while(!exit){
            Time.Tick();

            while(SDL_PollEvent(e) != 0){
                switch(e.type){
                    case SDL_QUIT:
                        exit = true;
                        break;
                    case SDL_KEYDOWN:
                    break;
                }
            }

            Keyboard.getKeyboardState();

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

            //System.out.printf("%d,%d%n", viewport.x, viewport.y);
        }
    }
    
    public static void quit(){
        SDL_DestroyTexture(texture);
        SDL_DestroyRenderer(renderer);
        SDL_DestroyWindow(window);
        SDL_Quit();
    }
}