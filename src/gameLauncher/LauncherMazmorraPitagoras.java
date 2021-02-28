package gameLauncher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gameLogic.MazmorraPitagoras;

public class LauncherMazmorraPitagoras{
	public static void main (String[] args){
	Game myGame = new MazmorraPitagoras();
	LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	config.width= 800;
	config.height= 600;
	config.title = "La mazmorra de pitágoras";
	
	LwjglApplication launcher =	new LwjglApplication( myGame, config ); 
	}
}