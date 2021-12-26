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

/**
 * <h3>Keyboard Class</h3>
 * 
 * A class to simplify keyboard functions.
 * 
 * @author Furkan Mudanyali
 * @version 1.0.0
 * @since 2021-12-01
 */
public class Keyboard {
    // Basically array pointer
    public static ByteByReference keyboard = new ByteByReference();

    public static void getKeyboardState(){
        keyboard = SDL_GetKeyboardState(null);
    }
    /**
     * Checks if given index of the keyboard array is true or not.
     * 
     * @param key Index of the key in the keyboard array.
     * @return true if key is pressed, false if not.
     */
    public static boolean getKeyState(int key){
        return keyboard.getPointer().getByte(key) == 1;
    }
}