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

import org.libsdl.api.render.SDL_Renderer;
import org.libsdl.api.render.SDL_Texture;
import org.libsdl.api.rect.SDL_Rect;
import org.libsdl.api.video.SDL_Window;

import static org.libsdl.api.render.SdlRender.*;
import static org.libsdl.api.pixels.SDL_PixelFormatEnum.*;
import static org.libsdl.api.video.SDL_WindowFlags.*;
import static org.libsdl.api.video.SdlVideo.*;
import static com.fmudanyali.Screen.*;
import static org.libsdl.api.hints.SdlHintsConst.*;
import static org.libsdl.api.hints.SdlHints.*;
import static org.libsdl.api.render.SdlRender.SDL_RenderSetLogicalSize;

public class Render {
    public static SDL_Window window;
    public static SDL_Renderer renderer;
    public static int bgw, bgh;

    public static void init(){
        window = SDL_CreateWindow("DanmakuProject SDL",
            SDL_WINDOWPOS_CENTERED(), SDL_WINDOWPOS_CENTERED(),
            WIDTH, HEIGHT, SDL_WINDOW_SHOWN | SDL_WINDOW_RESIZABLE | SDL_WINDOW_OPENGL);

        SDL_SetHint(SDL_HINT_RENDER_DRIVER, "opengl");
        SDL_SetHint(SDL_HINT_RENDER_SCALE_QUALITY, "2");

        renderer = 
            SDL_CreateRenderer(Render.window, -1, SDL_RENDERER_ACCELERATED | SDL_RENDERER_PRESENTVSYNC);

        SDL_RenderSetLogicalSize(renderer, Screen.WIDTH, Screen.HEIGHT);
    }
    public static SDL_Texture createBackgroundFromTexture(
        SDL_Renderer renderer, SDL_Texture texture, int cols, int rows
    ){
        // Set minimum row and col to 1
        if (rows < 1) rows = 1;
        if (cols < 1) cols = 1;
        // Create int pointers for Query Texture
        IntByReference txwptr = new IntByReference();
        IntByReference txhptr = new IntByReference();
        // Query the texture for dimensions
        SDL_QueryTexture(texture, null, null, txwptr, txhptr);
        // Store them in ints
        int txw = txwptr.getValue();
        int txh = txhptr.getValue();
        // Remove the pointers
        txwptr = txhptr = null;
        // Calculate background resolution
        bgw = txw * cols;
        bgh = txh * rows;
        // Create background texture
        SDL_Texture background = SDL_CreateTexture(
            renderer, SDL_PIXELFORMAT_RGBA8888, SDL_TEXTUREACCESS_TARGET, bgw, bgh);
        // Set render target to background
        SDL_SetRenderTarget(renderer, background);
        // Create position rectangle for the texture
        SDL_Rect txPos = new SDL_Rect();
        txPos.x = txPos.y = 0;
        txPos.w = txw;
        txPos.h = txh;
        // Loop for rows and columns
        for(int i = 0; i < rows; ++i){
            for(int j = 0; j < cols; ++j){
                // Copy texture to the position
                SDL_RenderCopy(renderer, texture, null, txPos);
                // Increment x position
                txPos.x += txw;
            }
            // Increment y position and clear x position
            txPos.x = 0;
            txPos.y += txh;
        }
        // Set render target back to window
        SDL_SetRenderTarget(renderer, null);
        // Return the background.
        return background;
    }
}
