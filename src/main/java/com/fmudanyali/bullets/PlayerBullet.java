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

/**
 * <h3>PlayerBullet Class</h3>
 * 
 * This class is for player made projectiles
 * that advance forward in the screen and explode
 * on enemy contact (to be implemented).
 * 
 * @author Furkan Mudanyali
 * @version 0.4.0
 * @since 2021-12-22
 */
public class PlayerBullet {
    // Load the texture
    public static SDL_Surface tempSurface = SDL_LoadBMP(FileLoader.getFilePath("player/bullet.bmp"));
    public static SDL_Texture texture = SDL_CreateTextureFromSurface(Render.renderer, tempSurface);
    public SDL_Rect position = new SDL_Rect();
    public int width = 5;
    public int height = 12;

    // Set the bullet position relative to the player position and given
    // offsets.
    public PlayerBullet(SDL_Rect playerPos, int xOffset, int yOffset){
        position.w = width;
        position.h = height;
        position.x = Math.min(Math.max(playerPos.x + xOffset, 300), 628 + 27);
        position.y = playerPos.y - 6 - 3 - yOffset;
    }

    /**
     * Advances the bullet on the screen.
     */
    public void fly(){
        position.y = position.y - (int)(Time.deltaTime * 0.5);
    }
}