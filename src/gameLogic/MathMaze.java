package gameLogic;

public class MathMaze extends BaseGame {
	private static LevelScreen pantallaNivel;	
	
	@Override
	public void create(){		
		super.create();			
		pantallaNivel = new LevelScreen();
		setActiveScreen( pantallaNivel );			
	}	
	
	
	public static LevelScreen getLevelScreen() {		
		return pantallaNivel;
	}
	
	
	
	
}
