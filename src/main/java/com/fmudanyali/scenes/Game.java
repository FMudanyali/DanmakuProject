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
import com.fmudanyali.Main;
import com.fmudanyali.Screen;
import com.fmudanyali.characters.Player;
import com.fmudanyali.bullets.PlayerBullet;

import static com.fmudanyali.Audio.*;
import static org.libsdl.api.event.SdlEvents.*;
import static com.fmudanyali.Render.*;
import static org.libsdl.api.keycode.SDL_Keycode.*;
import static org.libsdl.api.render.SdlRender.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <h3>Game Class</h3>
 * 
 * This class extends on the Scene class and is mainly
 * the core of the game.
 * 
 * @author Furkan Mudanyali
 * @version 0.1.0
 * @since 2021-12-04
 */
public class Game extends Scene {
    // Variables
    public static boolean escPressed = false;
    int kek = 0;
    private Player player = new Player();
    // Keeping track of the shots fired
    public static List<PlayerBullet> playerBullets = new ArrayList<>();
    // Thread for bg music
    private static Thread thread;
    // Create a runnable for the thread
    private static Runnable runnable = new Runnable() {
        public void run(){
            try {
                Mix_PlayMusic(Mix_LoadMUS(FileLoader.getFilePath("80-search-intro.wav")), 1);
                // Sleeping for the duration of the intro instead of checking if it stopped on a 
                // loop saves a loooot of clock cycles.
                Thread.sleep(getMusicLengthInMilliseconds(FileLoader.getFilePath("80-search-intro.wav")));
                Mix_PlayMusic(Mix_LoadMUS(FileLoader.getFilePath("80-search-loop.wav")), -1);
                // Kill self because the last music will loop forever
                Thread.currentThread().interrupt();
                return;
            } catch(Exception e){
                // Will throw sleep interrupted, which is exactly what we want.
            }
        }
    };

    public Game(){
        // Create the looping background
        Screen.makeBackground("shmap.bmp");
        // Set music volume to max
        Mix_VolumeMusic(128);
        // Try reading thread state, will throw exception for
        // the first time as the thread will not be initialized
        // at that time, which is perfectly expected.
        try {
            // Interrupt the thread if its running, this is needed
            // to keep only one thread alive at a time because if
            // the game is immediately quitted and a new game started,
            // the music will not get confused.
            if(thread.getState() != Thread.State.TERMINATED){
                thread.interrupt();
            }
        } catch (Exception e){
            // Do nothing as I expect this behavior.
        }
        // Overwrite the the TERMINATED (or not initialized)
        // thread with a new thread with our runnable.
        thread = new Thread(runnable);
        // Start the thread.
        thread.start();
        
    }

    @Override
    public void loop(){
        // Handle events first
        while(SDL_PollEvent(Main.e) != 0){
            switch(Main.e.type){
                // If close button on the window is pressed.
                case SDL_QUIT:
                    Main.exit = true;
                    break;
                // If a key is pressed
                case SDL_KEYDOWN:
                    switch(Main.e.key.keysym.sym){
                        // If ESC is pressed, push pause menu to the scenes stack
                        case SDLK_ESCAPE:
                            // Needed to execute it only once per conventional key press
                            if(!escPressed){
                                Main.scenes.push(new PauseMenu());
                                Mix_VolumeMusic(30);
                                escPressed = true;
                            }
                            break;
                    }
                    break;
                // If a key is released
                case SDL_KEYUP:
                    switch(Main.e.key.keysym.sym){
                        case SDLK_ESCAPE:
                            escPressed = false;
                            break;
                    }
                    break;
            }
        }

        // Do player movement thing and scroll the screen
        player.movement();
        Screen.scroll(player);
        // Copying stuff to the renderer
        SDL_RenderClear(renderer);
        SDL_RenderCopy(renderer, Screen.wallpaper, null, null);
        SDL_RenderCopy(renderer, Screen.background, Screen.canvas, Screen.canvasPos);
        SDL_RenderCopy(renderer, player.texture, player.shipFrame, player.position);
        SDL_RenderCopy(renderer, player.propeller, player.propellerFrame, player.propellerPos);
        SDL_RenderCopy(renderer, player.shooter, player.shooterFrame, player.shooterPos);
        // Iterate over the bullets on the screen
        for(Iterator<PlayerBullet> bulletIterator = playerBullets.iterator(); bulletIterator.hasNext();){
            PlayerBullet b = bulletIterator.next();
            // Move the bullet
            b.fly();
            // If the bullet is out of the screen, destroy it, else copy it to the renderer.
            if(b.position.y < 30){
                bulletIterator.remove();
            } else {
                SDL_RenderCopy(renderer, PlayerBullet.texture, null, b.position);
            }
        }
        // Present the renderer to the window.
        SDL_RenderPresent(renderer);
        // Advance player sprite animation
        player.shiftFrame();
    }
}