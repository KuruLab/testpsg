package com.mygdx.game.psg.old_engine;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.psg.screens.PlayScreen;


public class Gesture implements InputProcessor {

    public static Vector2 point0 = new Vector2(),point1 = new Vector2();
    public static boolean pressed0, pressed1, zoom;
    private float distance;

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        zoom = false;
        distance = 0;
        if(pointer == 0){
            point0.set(screenX,screenY);
            pressed0 = true;
        }

        if(pointer == 1){
            point1.set(screenX,screenY);
            pressed1 = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        zoom = false;
        if(pointer == 0){
            pressed0 = false;
        }

        if(pointer == 1){
            pressed1 = false;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(zoom) {
            if (pointer == 0) {
                    point0.set(screenX, screenY);
                    PlayScreen.zoomFinal = PlayScreen.zoomFinal + 0.0025f * (distance - point0.dst(point1));
                }
            }

            if (pointer == 1) {
                    point1.set(screenX, screenY);
                    PlayScreen.zoomFinal = PlayScreen.zoomFinal + 0.0025f * (distance - point0.dst(point1));
            }

            if(pressed0 && pressed1){zoom=true;}

            distance = point0.dst(point1);

            if (PlayScreen.zoomFinal > 2f / PlayScreen.zoomInit) {
                PlayScreen.zoomFinal = 2f / PlayScreen.zoomInit;
            }
            if (PlayScreen.zoomFinal < 0.25f / PlayScreen.zoomInit) {
                PlayScreen.zoomFinal = 0.25f / PlayScreen.zoomInit;
            }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if(PlayScreen.restartCount == 0) {

            PlayScreen.zoomFinal = PlayScreen.zoomFinal + 0.25f * amount;

            if (PlayScreen.zoomFinal > 2f / PlayScreen.zoomInit) {
                PlayScreen.zoomFinal = 2f / PlayScreen.zoomInit;
            }
            if (PlayScreen.zoomFinal < 0.25f / PlayScreen.zoomInit) {
                PlayScreen.zoomFinal = 0.25f / PlayScreen.zoomInit;
            }
        }
        return false;
    }
}
