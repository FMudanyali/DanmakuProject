package com.fmudanyali;


import java.util.Stack;
import org.libsdl.api.render.SDL_Renderer;

import static org.libsdl.api.Sdl.*;
import static org.libsdl.api.render.SdlRender.*;

public class Renderer {
    public static Stack<SDL_Renderer> renderStack = new Stack<>();

    public static enum GameState{
        MAIN_MENU,
        SETTINGS,
        GAME
    }

    public static void back(){
        SDL_DestroyRenderer(renderStack.peek());
        renderStack.pop();

        if(renderStack.empty()){
            SDL_Quit();
        }
    }

    public static void initialize(){
        renderStack.push(
            SDL_CreateRenderer(Game.window, -1, SDL_RENDERER_ACCELERATED | SDL_RENDERER_PRESENTVSYNC)
        );

    }
}
