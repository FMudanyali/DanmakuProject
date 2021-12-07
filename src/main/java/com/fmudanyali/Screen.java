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

import org.libsdl.api.rect.SDL_Rect;
import org.libsdl.api.render.SDL_Texture;
import org.libsdl.api.surface.SDL_Surface;

import static org.libsdl.api.surface.SdlSurface.*;
import static org.libsdl.api.render.SdlRender.*;

public class Screen {
    public static final int WIDTH = 960;
    public static final int HEIGHT = 540;
    public static final int CANV_W = 360;
    public static final int CANV_H = 480;

    public static SDL_Rect canvas = new SDL_Rect();
    public static SDL_Rect canvasPos = new SDL_Rect();
    public static SDL_Texture tile, background, wallpaper;
    public static SDL_Surface tempSurface;
    
    public static void init() throws Exception{
        canvas.x = canvas.y = 0;
        canvas.h = canvasPos.h = CANV_H;
        canvas.w = canvasPos.w = CANV_W;
        // Center canvas to the screen
        canvasPos.x = (WIDTH - CANV_W)/2;
        canvasPos.y = (HEIGHT - CANV_H)/2;
        // Create background wallpaper
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("wallpaper.bmp"));
        // Create texture from the surface
        wallpaper = SDL_CreateTextureFromSurface(Render.renderer, tempSurface);
        // Destroy the surface
        tempSurface = null;
    }

    public static void makeBackground(String filename) throws Exception{
        // Load tile
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath(filename));
        tile = SDL_CreateTextureFromSurface(Render.renderer, tempSurface);
        // Clear surface
        tempSurface = null;
        // Create background from tiles
        background = Render.createBackgroundFromTexture(Render.renderer, tile, 12, 30);
    }

    public static void scroll(){
        canvas.y = Math.floorMod(canvas.y - (int)(Time.deltaTime * 0.1), Render.bgh - canvas.h);
    }
}
