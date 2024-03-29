package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Main;
//
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                config.width = 1600;
                config.height = 900;

				config.title = "AmusementPark";

                // turns on fullscreen:
                //config.fullscreen = true;
                
		new LwjglApplication(new Main(), config);
	}
}
