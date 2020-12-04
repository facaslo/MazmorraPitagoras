package gameLogic;

import com.badlogic.gdx.graphics.Camera;

public class MathMaze extends BaseGame {
	public void create(){
		super.create();
		setActiveScreen( new LevelScreen() );			
	}	
	
	
}
