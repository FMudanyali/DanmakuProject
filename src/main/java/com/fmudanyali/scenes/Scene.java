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

/**
 * <h3>Scene Class</h3>
 * 
 * This class is an abstract for other scenes and makes able to
 * keep all the different kind of scenes on a single stack.
 * 
 * @author Furkan Mudanyali
 * @version 1.0.0
 * @since 2021-12-04
 */
public abstract class Scene {
    /**
     * By design, this function will be
     * executed each game frame.
     */
    public abstract void loop();
}