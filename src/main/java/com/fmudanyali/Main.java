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

import com.fmudanyali.scenes.*;
import java.util.Stack;
import org.libsdl.api.event.events.SDL_Event;

/**
 * <h3>Main Class</h3>
 * This is the starting point of the application.
 * 
 * @author Furkan Mudanyali
 * @version 1.0.0
 * @since 2021-12-01
 */
public class Main {
    public static SDL_Event e = new SDL_Event();
    public static boolean exit = false;
    public static Stack<Scene> scenes = new Stack<>();
    public static void main(String[] args) throws Exception{
        /*
        Restart JVM if on macOS and it hasn't been launched
        with -XStartOnFirstThread arg. Needed for SDL to be
        able to create a window and show it on macOS as macOS
        ignores windows that are not opened by the first thread
        of the application for safety measures. Took me days
        to track the bug.
        */ 
        if (RestartJVM.restartJVM()) {
            return;
        }

        // Open audio device with basic defaults.
        Audio.Mix_OpenAudio(44100, Audio.MIX_DEFAULT_FORMAT, 2, 2048);
        // Push main menu to the scenes stack.
        scenes.push(new MainMenu());

        // Each loop means a frame of the game.
        while(!scenes.empty() && !exit){
            // Calculate delta time, between last frame
            // and this frame.
            Time.Tick();
            Keyboard.getKeyboardState();
            // Call the loop function of the scene on the
            // top of the scenes stack.
            scenes.peek().loop();
        }
        // Exit is set to true manually to make sure the existing threads
        // are stopped, not really necessary as System.exit should close
        // those lingering threads but better be safe than sorry.
        exit = true;
        System.exit(0);
    }
}