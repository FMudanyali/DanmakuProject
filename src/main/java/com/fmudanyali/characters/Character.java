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

import org.libsdl.api.rect.SDL_Rect;
import org.libsdl.api.render.SDL_Texture;
import org.libsdl.api.surface.SDL_Surface;

public class Character {
    public SDL_Rect position = new SDL_Rect();
    public SDL_Texture texture = new SDL_Texture();
    public SDL_Surface tempSurface = new SDL_Surface();
}
