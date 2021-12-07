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

package com.fmudanyali.characters;

import com.fmudanyali.FileLoader;
import com.fmudanyali.Render;
import com.fmudanyali.Screen;
import com.fmudanyali.Keyboard;
import com.fmudanyali.Time;

import static org.libsdl.api.surface.SdlSurface.*;
import static org.libsdl.api.render.SdlRender.*;
import static org.libsdl.api.scancode.SDL_Scancode.*;

public class Player extends Character {
    public int lives;
    public int speed;

    public Player() throws Exception{
        lives = 3;
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("player.bmp"));
        texture = SDL_CreateTextureFromSurface(Render.renderer, tempSurface);
        tempSurface = null;
        position.x = Screen.canvasPos.x + Screen.canvas.w/2 - 16;
        position.y = Screen.canvasPos.y + (int)(Screen.canvas.h/1.25) - 16;
        position.w = position.h = 32;
    }

    public void movement(){
        if(Keyboard.getKeyState(SDL_SCANCODE_LSHIFT)){
            speed = 1;
        }else{
            speed = 2;
        }

        if(Keyboard.getKeyState(SDL_SCANCODE_A) | Keyboard.getKeyState(SDL_SCANCODE_LEFT)){
            position.x = Math.max(position.x - (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.x);
        }
        if(Keyboard.getKeyState(SDL_SCANCODE_D) | Keyboard.getKeyState(SDL_SCANCODE_RIGHT)){
            position.x = Math.min(position.x + (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.x + Screen.canvas.w - 32);
        }
        if(Keyboard.getKeyState(SDL_SCANCODE_W) | Keyboard.getKeyState(SDL_SCANCODE_UP)){
            position.y = Math.max(position.y - (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.y);
        }
        if(Keyboard.getKeyState(SDL_SCANCODE_S) | Keyboard.getKeyState(SDL_SCANCODE_DOWN)){
            position.y = Math.min(position.y + (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.y + Screen.canvas.h - 32);
        }
    }
}
