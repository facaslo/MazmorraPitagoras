package gameLauncher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gameLogic.MathMaze;

public class LauncherMathMaze{
	public static void main (String[] args){
	Game myGame = new MathMaze();
	LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	config.width= 800;
	config.height= 600;
	config.title = "La mazmorra de pitágoras";
	
	LwjglApplication launcher =	new LwjglApplication( myGame, config ); 
	}
}