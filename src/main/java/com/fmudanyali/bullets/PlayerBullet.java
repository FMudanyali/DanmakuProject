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

package com.fmudanyali.bullets;

import com.fmudanyali.FileLoader;
import com.fmudanyali.Render;
import com.fmudanyali.Time;

import org.libsdl.api.rect.SDL_Rect;
import org.libsdl.api.render.SDL_Texture;
import org.libsdl.api.surface.SDL_Surface;

import static org.libsdl.api.surface.SdlSurface.*;
import static org.libsdl.api.render.SdlRender.*;

public class PlayerBullet {
    public static SDL_Surface tempSurface = SDL_LoadBMP(FileLoader.getFilePath("player/bullet.bmp"));
    public static SDL_Texture texture = SDL_CreateTextureFromSurface(Render.renderer, tempSurface);
    public SDL_Rect position = new SDL_Rect();
    public int width = 5;
    public int height = 12;

    public PlayerBullet(SDL_Rect playerPos, int xOffset, int yOffset){
        position.w = width;
        position.h = height;
        position.x = Math.min(Math.max(playerPos.x + xOffset, 300), 628 + 27);
        position.y = playerPos.y - 6 - 3 - yOffset;
    }

    public void fly(){
        position.y = position.y - (int)(Time.deltaTime * 0.5);
    }
}