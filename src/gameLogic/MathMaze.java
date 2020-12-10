package gameLogic;

import com.badlogic.gdx.graphics.Camera;

public class MathMaze extends BaseGame {
	private static LevelScreen pantallaNivel;
	
	
	
	public void create(){		
		super.create();			
		pantallaNivel = new LevelScreen();
		setActiveScreen( pantallaNivel );			
	}	
	
	
	public static LevelScreen getLevelScreen() {		
		return pantallaNivel;
	}
	
	
	
	
}
