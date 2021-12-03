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

import static org.libsdl.api.keyboard.SdlKeyboard.*;
import com.sun.jna.ptr.ByteByReference;

public class Keyboard {
    public static ByteByReference keyboard = new ByteByReference();

    public static void getKeyboardState(){
        keyboard = SDL_GetKeyboardState(null);
    }

    public static boolean getKeyState(int key){
        return keyboard.getPointer().getByte(key) == 1;
    }
}