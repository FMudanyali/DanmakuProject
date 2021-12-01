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

import static org.libsdl.api.timer.SdlTimer.*;

public class Time {
    public static int lastTime = 0;
    public static int currentTime = 0;
    public static int deltaTime = 0;

    public static void Tick(){
        lastTime = currentTime;
        currentTime = SDL_GetTicks();
        deltaTime = currentTime - lastTime;
    }
}
