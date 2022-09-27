package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.npcs.Cleaner;
import com.mygdx.game.npcs.Repairman;

public class UI {
    
    public static int selectedBuilding = -1;
    
    /**
     * Buildings on the left
     *      UI saves whole mapElements which can then be "copied"
     *      in order to be placed on the map
     * 
     * easy changing of the different parts of the buildings list
     * can be made extended easily just by adding more MapElements to the array
     */
    public static MapElement[] mapElements;
    
    /**
     * Shape renderer for simple shapes
     */
    private ShapeRenderer UIrenderer;
    /**
     * Font for drawing text
     */
    private BitmapFont font;
    
    /**
     * User Interface resolution
     */
    public static int UI_WIDTH = 1920;
    public static int UI_HEIGHT = 1080;
    
    
    /**
     * Stage for UI
     *      handling Viewport
     *               Camera
     *               Actors (not used in UI)
     */
    public Stage stage = new Stage(new FitViewport(UI_WIDTH,UI_HEIGHT, new OrthographicCamera()));

    public UI() {
        UIrenderer = new ShapeRenderer();
        UIrenderer.setProjectionMatrix(stage.getCamera().combined);
        
        // generating the font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 21;
        font = generator.generateFont(parameter);
        generator.dispose();
        font.getData().setScale(2f);
        font.setColor(Color.BLACK);

        // Creating all the MapElements which can be built and will be part of the UI
        mapElements = new MapElement[13];
        mapElements[0] = MapElement.getNewMapElementById(0);
        mapElements[1] = MapElement.getNewMapElementById(1);
        mapElements[2] = MapElement.getNewMapElementById(2);
        mapElements[3] = MapElement.getNewMapElementById(10);
        mapElements[4] = MapElement.getNewMapElementById(11);
        mapElements[5] = MapElement.getNewMapElementById(12);
        mapElements[6] = MapElement.getNewMapElementById(20);
        mapElements[7] = MapElement.getNewMapElementById(21);
        mapElements[8] = MapElement.getNewMapElementById(22);
        mapElements[9] = MapElement.getNewMapElementById(23);
        mapElements[10] = MapElement.getNewMapElementById(30);
        mapElements[11] = MapElement.getNewMapElementById(32);
        mapElements[12] = MapElement.getNewMapElementById(-1);
    }
    
    public void render(int repairmanCounter, int cleanerCounter){
        stage.getCamera().update();
        UIrenderer.setProjectionMatrix(stage.getCamera().combined);
        stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
        
        // RENDERING Text Boxes ////////////////////////////////////////////////
        stage.getBatch().begin();
        getFont().setColor(Color.CLEAR);
        getFont().getData().setScale(2f);
        /**
         * First checking how big the different boxes have to be (drawing color is CLEAR)
         */
        float moneyboxWidth = getFont().draw(stage.getBatch(), Player.money+" $", 0, 0).width;
        getFont().getData().setScale(1f);
        float costperminboxwidth = getFont().draw(stage.getBatch(), "-"+(int)Player.costpermin+"$ /min", 0, 0).width;
        getFont().getData().setScale(2f);
        float moodboxwidth = getFont().draw(stage.getBatch(), "Mood: "+(int)Player.mood, 0, 0).width;
        float moodboxheight = getFont().draw(stage.getBatch(), "Mood: "+(int)Player.mood, 0, 0).height;
        float visitorsboxWidth = getFont().draw(stage.getBatch(), "Visitors: "+(int)Player.visitors, 0, 0).width;
        stage.getBatch().end();
        
        /**
         * Then finally drawing the Boxes with the previously calculated values
         */
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        getUIrenderer().begin(ShapeRenderer.ShapeType.Filled);
        getUIrenderer().setColor(0,0,0,0.8f);
        
        //  drawing Rect with             xPos            yPos            width      height
        getUIrenderer().rect(UI_WIDTH-moneyboxWidth-52, UI_HEIGHT-100, moneyboxWidth+4, 70);
        getUIrenderer().rect(UI_WIDTH-costperminboxwidth-52, UI_HEIGHT-130, costperminboxwidth+4, 30);
        getUIrenderer().rect(UI_WIDTH-moodboxwidth-52, 50, moodboxwidth+4, 50);
        getUIrenderer().rect(UI_WIDTH-visitorsboxWidth-52, 100, visitorsboxWidth+4, 50);
        getUIrenderer().end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        stage.getBatch().begin();
        /**
         * Drawing the Text onto the boxes
         */
        // Current Player Money
        getFont().setColor(Color.FOREST);
        getFont().getData().setScale(2f);
        getFont().draw(stage.getBatch(), Player.money+" $", UI_WIDTH-moneyboxWidth-50, UI_HEIGHT-55);
        
        // Current Loss of Money per Minute due to OperatingCost etc
        getFont().setColor(Color.FIREBRICK);
        getFont().getData().setScale(1f);
        getFont().draw(stage.getBatch(), "-"+(int)Player.costpermin+"$ /min", UI_WIDTH-costperminboxwidth-50, UI_HEIGHT-110);
        
        // Current General Mood of the Park
        getFont().setColor(Color.SKY);
        getFont().getData().setScale(2f);
        getFont().draw(stage.getBatch(), "Mood: "+(int)Player.mood, UI_WIDTH-49-moodboxwidth, 55+moodboxheight);
        
        // Current Amount of Guests
        getFont().setColor(Color.GRAY);
        getFont().getData().setScale(2f);
        getFont().draw(stage.getBatch(), "Visitors: "+(int)Player.visitors, UI_WIDTH-49-visitorsboxWidth, 105+moodboxheight);
        
        stage.getBatch().end();
        ////////////////////////////////////////////////////////////////////////
        
        
        // RENDERING BUILDINGS UI //////////////////////////////////////////////
        getFont().getData().setScale(2f);
        getUIrenderer().setColor(Color.BLACK);
        getUIrenderer().begin(ShapeRenderer.ShapeType.Filled);
        
        // Drawing One big Rect where all the Icons will be displayed on
        getUIrenderer().rect(18, UI_HEIGHT - 82 - (mapElements.length-1)*72-8, 76, mapElements.length*74);
        
        // Drawing More stuff for the currently selected Building
        if(selectedBuilding != -1){
            getUIrenderer().setColor(Color.RED);
            getUIrenderer().rect(20, UI_HEIGHT - 80 - selectedBuilding*72-8, 72, 72);
            
            getUIrenderer().end();
            stage.getBatch().begin();
            getFont().setColor(Color.CLEAR);
            // Drawing the Name of the selected Building and the Price
            // first calculating width of both
            float width = getFont().draw(stage.getBatch(), mapElements[selectedBuilding].getName(), 0, 0).width;
            float priceWidth = getFont().draw(stage.getBatch(), mapElements[selectedBuilding].getBuildingCost()+" $", 0, 0).width;
            stage.getBatch().end();
            
            // Drawing the Rect
            
            // Gdx.gl.glEnable ... needs to be enabled in order to draw stuff transparent
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            getUIrenderer().begin(ShapeRenderer.ShapeType.Filled);
            getUIrenderer().setColor(0,0,0, 0.5f);
            // Rendering the Rectangles underlying the text
            getUIrenderer().rect(92, UI_HEIGHT - 80 - selectedBuilding*72-8, width+7, 72);
            getUIrenderer().rect(90+width+5-priceWidth, UI_HEIGHT - 80 - (selectedBuilding+1)*72-8, priceWidth+4, 72);
            getUIrenderer().end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            getUIrenderer().setColor(Color.WHITE);
            
            stage.getBatch().begin();
            getFont().setColor(Color.WHITE);
            // Rendering the Text on top of the new rectangles
            getFont().draw(stage.getBatch(), mapElements[selectedBuilding].getName(), 96, UI_HEIGHT - 80 - selectedBuilding*72+40, width, 1, false);
            getFont().draw(stage.getBatch(), mapElements[selectedBuilding].getBuildingCost()+" $", 92+width+5-priceWidth, UI_HEIGHT - 80 - (selectedBuilding+1)*72+40, priceWidth, 1, false);
            stage.getBatch().end();
            
            
            
        }
        if(getUIrenderer().isDrawing()){
            getUIrenderer().end();
        }
        
        
        // SPAWN BUTTON FOR CLEANER AND REPAIRMAN
        getUIrenderer().begin(ShapeRenderer.ShapeType.Filled);
        getUIrenderer().setColor(Color.BLACK);
        getUIrenderer().rect(96+200, 964, 144, 48);
        getUIrenderer().rect(96+200, 1012, 48, 48);
        getUIrenderer().rect(192+200, 1012, 48, 48);
        
        getUIrenderer().rect(96+200, 856, 144, 48);
        getUIrenderer().rect(96+200, 904, 48, 48);
        getUIrenderer().rect(192+200, 904, 48, 48);
        
        
        getUIrenderer().setColor(Color.WHITE);
        getUIrenderer().rect(144+200-2, 964, 48+4, 96);
        getUIrenderer().rect(144+200-2, 856, 48+4, 96);
        
        getUIrenderer().end();
        
        stage.getBatch().begin();


        getFont().setColor(Color.BLACK);
        getFont().draw(stage.getBatch(), ""+cleanerCounter, 352, 947);
        getFont().draw(stage.getBatch(), ""+repairmanCounter, 352, 1055);
        
        getFont().setColor(Color.WHITE);
        getFont().draw(stage.getBatch(), "R", 304, 1055);
        getFont().draw(stage.getBatch(), "T", 400, 1055);
        
        getFont().draw(stage.getBatch(), "F", 104+200, 947);
        getFont().draw(stage.getBatch(), "G", 200+200, 947);
        
        getFont().setColor(Color.FIREBRICK);
        getFont().draw(stage.getBatch(), "-", 104+200, 1007);
        getFont().draw(stage.getBatch(), "-", 104+200, 899);
        
        getFont().setColor(Color.FOREST);
        getFont().draw(stage.getBatch(), "+", 200+200, 899);
        getFont().draw(stage.getBatch(), "+", 200+200, 1007);
        
        stage.getBatch().draw(Repairman.t, 144+200-2, 964, 48+4, 48+4);
        stage.getBatch().draw(Cleaner.t, 144+200-2, 856, 48+4, 48+4);
        
        
        // Rendering all the Icons on top of the big Rect
        for(int i = 0; i < mapElements.length; i++){
            mapElements[i].renderTo(0, stage.getBatch(), 24, UI_HEIGHT - 80 - i*72-4, 64,64);
        }
        
        stage.getBatch().end(); 
        ////////////////////////////////////////////////////////////////////////
        
        // Checking Input
        // If Key Up or Down we change the selected Building accordingly
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            scroll(0, -1);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            scroll(0, 1);
        }
        
        
    }
    
    public void resize(int width, int height){
        stage.getViewport().update(width, height);
    }
    
    
    
    // Handling the scrolling through the buildings with Scroll Wheel
    public void scroll(float amountX, float amountY){
        if(amountY < 0){
            if(selectedBuilding > 0){
                selectedBuilding--;
            }
        }
        else{
            if(selectedBuilding < mapElements.length-1){
                selectedBuilding++;
            }
        }
    }
    
    /**
     * GETTER + SETTER
     */

    /**
     * @return the UIrenderer
     */
    public ShapeRenderer getUIrenderer() {
        return UIrenderer;
    }

    /**
     * @param UIrenderer the UIrenderer to set
     */
    public void setUIrenderer(ShapeRenderer UIrenderer) {
        this.UIrenderer = UIrenderer;
    }

    /**
     * @return the font
     */
    public BitmapFont getFont() {
        return font;
    }

    /**
     * @param font the font to set
     */
    public void setFont(BitmapFont font) {
        this.font = font;
    }
    
}
