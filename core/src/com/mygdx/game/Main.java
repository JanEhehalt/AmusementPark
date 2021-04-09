package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main extends ApplicationAdapter implements InputProcessor{
    
        public static int[] RESOLUTIONS={
            160, 90,
            240, 135,
            320, 180,
            480, 270,
            640, 360,
            800, 450,
            1024, 576,
            1152, 648,
            1280, 720,
            1366, 768,
            1600, 900,
            1920, 1080
        };
        
        
	
        
        World world;
        UI ui;
        
	@Override
	public void create () {
            
            world = new World(50,50);
            ui = new UI(world.stage.getViewport());
            
            Gdx.input.setInputProcessor(this);
        }

        @Override
        public void resize(int width, int height) {
            world.resize(width,height);
            super.resize(width, height); //To change body of generated methods, choose Tools | Templates.
        }

        
	@Override
	public void render () {
            Gdx.gl.glClearColor(0f/255f, 0f/255f, 0f/255f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            
            world.render();
            ui.render();

	}
	
        // NOT USED - HANDLED IN RENDER ////////////////////////////////////////
	@Override
	public void dispose () {
	}

        @Override
        public boolean keyDown(int keycode) {
            System.out.println(keycode);
            return true;
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
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }
        ////////////////////////////////////////////////////////////////////////

        // HANDLING MOUSE SCROLL -> ZOOM IN/OUT ////////////////////////////////
        @Override
        public boolean scrolled(float amountX, float amountY) {
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
                world.scroll(amountX, amountY);
            }
            else{ 
                ui.scroll(amountX, amountY);
            }
            return true;
        }
        ////////////////////////////////////////////////////////////////////////
}
