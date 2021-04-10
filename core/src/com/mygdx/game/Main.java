package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;

public class Main extends ApplicationAdapter implements InputProcessor{
    
        // AVAILABLE RESOLUTIONS FOR ZOOMING (all 16:9)
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
        
        // We have a world layer and the ui layer on top
        World world;
        UI ui;
        
	@Override
	public void create () {
            
            world = new World(50,50);
            ui = new UI();
            
            // basically this just says, that the Main class is handling the Inoput
                // We outsourced that to the Objects themself (except of scroll)
            Gdx.input.setInputProcessor(this);
        }

        @Override
        public void resize(int width, int height) {
            world.resize(width,height); // Method is being called when the user is resizing the window
            super.resize(width, height);        // adapts the Viewport resolution to new window size
        }

        
	@Override
	public void render () {
            Gdx.gl.glClearColor(0f/255f, 0f/255f, 0f/255f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            
            // Rendering the world first
            world.render();
            // Rendering the UI later (on top)
            ui.render();

	}
	
        // Called when the programm is being closed
            // Basically it is removing all the graphics from memory
	@Override
	public void dispose () {
            world.getStage().dispose();
            ui.getUIbatch().dispose();
            ui.getUIrenderer().dispose();
            ui.getFont().dispose();
            for(MapElement me : ui.mapElements){
                me.getT().dispose();
            }
            for(int i = 0; i < world.getGrid().length; i++){
                for(int j = 0; j < world.getGrid()[0].length; j++){
                    world.getGrid()[i][j].getT().dispose();
                }
            }
	}

        // NOT USED - HANDLED IN RENDER ////////////////////////////////////////
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
