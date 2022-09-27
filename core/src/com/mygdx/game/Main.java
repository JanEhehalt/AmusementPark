package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.npcs.Cleaner;
import com.mygdx.game.npcs.Repairman;

public class Main extends ApplicationAdapter implements InputProcessor{

        public static int worldWidth = 80;
        public static int worldHeight = 46;
    
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
            1920, 1080,
            3840, 2160,
            7680, 4320
        };
        
        // We have a world layer and the ui layer on top
        World world;
        UI ui;
        
	@Override
	public void create () {
            MapElementData.initCostpermin();
            
            world = new World(worldWidth,worldHeight);
            ui = new UI();

            // basically this just says, that the Main class is handling the Inoput
                // We outsourced that to the Objects themself (except of scroll)
            Gdx.input.setInputProcessor(this);
        }
        
    // Method is being called when the user is resizing the window | Width and Height new Bounds of window
        @Override
        public void resize(int width, int height) {
            world.resize(width,height); 
            ui.resize(width,height);    
            super.resize(width, height);        // adapts the Viewport resolution to new window size
        }

        // THE GAME LOOP, CALLED 60 TIMES PER SEC
	@Override
	public void render () {
            Gdx.gl.glClearColor(0f/255f, 0f/255f, 0f/255f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            
            // UPDATES THE MONEY THE PLAYER HAS WITH MONEY_OFFSET
            Player.updateMoney();
            
            // Rendering the world first
            world.render();

            int cleanerCounter = 0;
            int repairmanCounter = 0;
            for(Actor a : world.getStage().getActors()){
                if(a instanceof Cleaner){
                    cleanerCounter++;
                }
                if(a instanceof Repairman){
                    repairmanCounter++;
                }
            }

            // Rendering the UI later (on top)
            ui.render(repairmanCounter, cleanerCounter);

            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
                Gdx.app.exit();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                boolean fullScreen = Gdx.graphics.isFullscreen();
                Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
                if (fullScreen)
                    //Gdx.graphics.setWindowedMode(currentMode.width, currentMode.height);
                    Gdx.graphics.setWindowedMode(1280, 720);
                else
                    Gdx.graphics.setFullscreenMode(currentMode);
            }
            
	}
	
        // Called when the programm is being closed
            // Basically it is removing all the graphics from memory
	@Override
	public void dispose () {
            world.getStage().dispose();
            ui.stage.getBatch().dispose();
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
        // EXCEPT OF SCROLL (CAN'T BE HANDLED ON THE FLY) //////////////////////
        
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
