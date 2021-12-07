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

import com.fmudanyali.Main;
import com.fmudanyali.Screen;
import com.fmudanyali.characters.Player;

import static org.libsdl.api.event.SdlEvents.*;
import static com.fmudanyali.Render.*;
import static org.libsdl.api.keycode.SDL_Keycode.*;
import static org.libsdl.api.render.SdlRender.*;

public class Game extends Scene {
    public static boolean exit = false;
    public static boolean escPressed = false;

    public Player player = new Player();

    public Game() throws Exception{
        Screen.makeBackground("scene1/tile.bmp");
    }

    @Override
    public void loop(){
        while(SDL_PollEvent(Main.e) != 0){
            switch(Main.e.type){
                case SDL_QUIT:
                    Main.exit = true;
                    break;
                case SDL_KEYDOWN:
                    switch(Main.e.key.keysym.sym){
                        case SDLK_ESCAPE:
                            if(!escPressed){
                                Main.scenes.push(new PauseMenu());
                                escPressed = true;
                            }
                            break;
                    }
                    break;
                case SDL_KEYUP:
                    switch(Main.e.key.keysym.sym){
                        case SDLK_ESCAPE:
                            escPressed = false;
                            break;
                    }
                    break;
            }
        }

        player.movement();
        Screen.scroll();

        SDL_RenderClear(renderer);
        SDL_RenderCopy(renderer, Screen.wallpaper, null, null);
        SDL_RenderCopy(renderer, Screen.background, Screen.canvas, Screen.canvasPos);
        SDL_RenderCopy(renderer, player.texture, null, player.position);
        SDL_RenderPresent(renderer);
    }
}