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
import com.fmudanyali.bullets.PlayerBullet;
import com.fmudanyali.scenes.Game;

import org.libsdl.api.rect.SDL_Rect;
import org.libsdl.api.render.SDL_Texture;

import static org.libsdl.api.surface.SdlSurface.*;
import static org.libsdl.api.render.SdlRender.*;
import static org.libsdl.api.scancode.SDL_Scancode.*;

/**
 * <h3>Player Class</h3>
 * 
 * This class extends on the character class and has all the
 * bells and whistles such as handling movement, shooting bullets,
 * animated sprites etc.
 * 
 * @author Furkan Mudanyali
 * @version 0.9.0
 * @since 2021-12-08
 */
public class Player extends Character {
    // Variables
    public int lives;
    public double speed;
    public SDL_Texture propeller;
    public SDL_Texture shooter;
    public int frame = 0;
    public int roll = 0;
    public int cooldown = 0;
    // New position rectangles for additional parts
    public SDL_Rect propellerPos = new SDL_Rect();
    public SDL_Rect shooterPos = new SDL_Rect();
    // These rectangles are used for shifting 
    // sprite animation.
    public SDL_Rect shipFrame = new SDL_Rect();
    public SDL_Rect propellerFrame = new SDL_Rect();
    public SDL_Rect shooterFrame = new SDL_Rect();
    
    // Load the textures and set the positions.
    public Player(){
        lives = 3;
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("player/ship.bmp"));
        texture = SDL_CreateTextureFromSurface(Render.renderer, tempSurface);
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("player/propeller.bmp"));
        propeller = SDL_CreateTextureFromSurface(Render.renderer, tempSurface);
        tempSurface = SDL_LoadBMP(FileLoader.getFilePath("player/shooter.bmp"));
        shooter = SDL_CreateTextureFromSurface(Render.renderer, tempSurface);
        tempSurface = null;

        position.x = Screen.canvasPos.x + Screen.canvas.w/2 - 16;
        position.y = Screen.canvasPos.y + (int)(Screen.canvas.h/1.25) - 16;
        position.w = position.h = shipFrame.w = shipFrame.h = 32;

        propellerPos.x = Screen.canvasPos.x + Screen.canvas.w/2 - 5;
        propellerPos.y = Screen.canvasPos.y + (int)(Screen.canvas.h/1.25) - 3 + 16;
        propellerPos.w = propellerFrame.w = 10;
        propellerPos.h = propellerFrame.h = 8;
        propellerFrame.x = propellerFrame.y = 0;

        shooterPos.x = Screen.canvasPos.x + Screen.canvas.w/2 - 4;
        shooterPos.y = Screen.canvasPos.y + (int)(Screen.canvas.h/1.25) - 3 - 16;
        shooterPos.w = shooterFrame.w = 8;
        shooterPos.h = shooterFrame.h = 3;
        shooterFrame.x = shooterFrame.y = 0;
    }

    /**
     * Changes the frames depending on frame and roll
     * TODO: This whole area needs work
     */
    public void shiftFrame(){
        // Shooter
        switch(frame){
            case 0:
            case 200:
            case 400:
                shooterFrame.x = shooterFrame.y = 0;
                break;
            case 25:
            case 225:
            case 425:
                shooterFrame.x = 8;
                break;
            case 50:
            case 250:
            case 450:
                shooterFrame.x = 0;
                shooterFrame.y = 3;
                break;
            case 75:
            case 275:
            case 475:
                shooterFrame.x = 8;
                break;
            case 100:
            case 300:
            case 500:
                shooterFrame.x = 0;
                shooterFrame.y = 6;
                break;
            case 125:
            case 325:
            case 525:
                shooterFrame.x = 8;
                break;
            case 150:
            case 350:
            case 550:
                shooterFrame.x = 0;
                shooterFrame.y = 9;
                break;
            case 175:
            case 375:
            case 575:
                shooterFrame.x = 8;
                break;
        }
        // Propeller
        switch(frame){
            case 0:
            case 150:
            case 300:
            case 450:
                propellerFrame.x = propellerFrame.y = 0;
                break;
            case 25:
            case 175:
            case 325:
            case 475:
                propellerFrame.x = 10;
                break;
            case 50:
            case 200:
            case 350:
            case 500:
                propellerFrame.x = 0;
                propellerFrame.y = 8;
                break;
            case 75:
            case 225:
            case 375:
            case 525:
                propellerFrame.x = 10;
                break;
            case 100:
            case 250:
            case 400:
            case 550:
                propellerFrame.x = 0;
                propellerFrame.y = 16;
                break;
            case 125:
            case 275:
            case 425:
            case 575:
                propellerFrame.x = 10;
                break;
        }
        // Not a switch as DeltaTime does not provide
        // linear increase.
        if(roll > 0){
            shipFrame.x = shipFrame.y = 0;
        } if(roll > 15){
            shipFrame.x = 32;
        } if(roll > 30){
            shipFrame.x = 0;
            shipFrame.y = 32;
        } if(roll > 45){
            shipFrame.x = 32;
        }
        // Frame is kept in 600 instead of 60
        // to be accurate with the varying framerate
        // animations.
        frame = (frame+10)%600;
    }

    /**
     * Changes positions depending on the keyboard state.
     */
    public void movement(){
        // If shift is pressed, slow down the player.
        if(Keyboard.getKeyState(SDL_SCANCODE_LSHIFT)){
            speed = 1.5;
        }else{
            speed = 2;
        }
        // Bunch of mathematical mumbo jumbo.
        if(Keyboard.getKeyState(SDL_SCANCODE_A) | Keyboard.getKeyState(SDL_SCANCODE_LEFT)){
            position.x = Math.max(position.x - (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.x - 5);
            shooterPos.x = Math.max(shooterPos.x - (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.x + 16 - 4 - 5);
            propellerPos.x = Math.max(propellerPos.x - (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.x + 16 - 5 - 5);
            roll = Math.floorMod(roll + (int)(speed * Time.deltaTime * 0.1),60);
        }
        if(Keyboard.getKeyState(SDL_SCANCODE_D) | Keyboard.getKeyState(SDL_SCANCODE_RIGHT)){
            position.x = Math.min(position.x + (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.x + Screen.canvas.w - 27);
            shooterPos.x = Math.min(shooterPos.x + (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.x + Screen.canvas.w - 16 - 4 + 5);
            propellerPos.x = Math.min(propellerPos.x + (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.x + Screen.canvas.w - 16 - 5 + 5);
            roll = Math.floorMod(roll - (int)(speed * Time.deltaTime * 0.1),60);
        }
        if(Keyboard.getKeyState(SDL_SCANCODE_W) | Keyboard.getKeyState(SDL_SCANCODE_UP)){
            position.y = Math.max(position.y - (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.y + 6);
            shooterPos.y = Math.max(shooterPos.y - (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.y + 3);
            propellerPos.y = Math.max(propellerPos.y - (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.y + 32 + 3);
        }
        if(Keyboard.getKeyState(SDL_SCANCODE_S) | Keyboard.getKeyState(SDL_SCANCODE_DOWN)){
            position.y = Math.min(position.y + (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.y + Screen.canvas.h - 32 - 8 + 3);
            shooterPos.y = Math.min(shooterPos.y + (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.y + Screen.canvas.h - 32 - 8);
            propellerPos.y = Math.min(propellerPos.y + (int)(speed * Time.deltaTime * 0.1), Screen.canvasPos.y + Screen.canvas.h - 8);
        }
        if(Keyboard.getKeyState(SDL_SCANCODE_SPACE) && cooldown == 0 && !Keyboard.getKeyState(SDL_SCANCODE_LSHIFT)){
            Game.playerBullets.add(new PlayerBullet(position, -2, 0));
            Game.playerBullets.add(new PlayerBullet(position, 4, 0));
            Game.playerBullets.add(new PlayerBullet(position, 10, 0));
            Game.playerBullets.add(new PlayerBullet(position, 17, 0));
            Game.playerBullets.add(new PlayerBullet(position, 23, 0));
            Game.playerBullets.add(new PlayerBullet(position, 29, 0));
            cooldown = 1;
        }
        if(Keyboard.getKeyState(SDL_SCANCODE_SPACE) && cooldown == 0 && Keyboard.getKeyState(SDL_SCANCODE_LSHIFT)){
            Game.playerBullets.add(new PlayerBullet(position, 2, 9));
            Game.playerBullets.add(new PlayerBullet(position, 6, 6));
            Game.playerBullets.add(new PlayerBullet(position, 10, 0));
            Game.playerBullets.add(new PlayerBullet(position, 17, 0));
            Game.playerBullets.add(new PlayerBullet(position, 21, 6));
            Game.playerBullets.add(new PlayerBullet(position, 25, 9));
            // Cooldown is used to prevent overshooting.
            cooldown = 3;
        }
        cooldown = Math.max(cooldown -1, 0);
    }
}
