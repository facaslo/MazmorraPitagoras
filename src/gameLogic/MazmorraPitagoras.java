package gameLogic;

public class MazmorraPitagoras extends BaseGame {
	private static LevelScreen pantallaNivel;	
	private static MainMenuScreen menu;
	
	@Override
	public void create(){		
		super.create();		
		menu = new MainMenuScreen();
		pantallaNivel = new LevelScreen();
		setActiveScreen( menu );			
	}	
	
	
	public static LevelScreen getLevelScreen() {		
		return pantallaNivel;
	}
	
	
	
	
}
