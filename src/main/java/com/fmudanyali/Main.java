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

public class Main {
    public static SDL_Event e = new SDL_Event();
    public static boolean exit = false;
    public static Stack<Scene> scenes = new Stack<>();
    public static void main(String[] args) throws Exception{
        if (RestartJVM.restartJVM()) {
            return;
        }

        Audio.Mix_OpenAudio(44100, Audio.MIX_DEFAULT_FORMAT, 2, 2048);
        scenes.push(new MainMenu());

        while(!scenes.empty() && !exit){
            Time.Tick();
            Keyboard.getKeyboardState();
            scenes.peek().loop();
        }
        exit = true;
        System.exit(0);
    }
}