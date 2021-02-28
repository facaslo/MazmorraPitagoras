package gameLogic;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;


public abstract class BaseGame extends Game{
	public static LabelStyle labelStyle;
	public static LabelStyle labelStyleLlaves;
	public static LabelStyle labelStyleMainMenu;
	public static TextButtonStyle textButtonStyle;
	public static TextButtonStyle textButtonStyleMain;
	private static BaseGame game;
	
	// Referencia a este objeto
	public BaseGame(){
		game = this;
	}
	
	public void create() 
    {        
		// Labelstyle 
		labelStyle = new LabelStyle();		
		labelStyle.font = new BitmapFont();
		
		labelStyleLlaves = new LabelStyle();
		labelStyleLlaves.font = new BitmapFont();
		
								
		
		// Importación de fuentes
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("assets/Fonts/manaspc.ttf"));
		// Parámetros de fuente
		FreeTypeFontParameter fontParameters = new FreeTypeFontParameter();
		fontParameters.size = 18;
		fontParameters.color = new Color(65/255f,37/255f,102/255f,1);	
		fontParameters.minFilter = TextureFilter.Linear;
		fontParameters.magFilter = TextureFilter.Linear; 
		
		FreeTypeFontParameter fontParametersLlaves = new FreeTypeFontParameter();
		fontParametersLlaves.size = 12;
		fontParametersLlaves.color = new Color(1f,1f,1f,1);	
		fontParametersLlaves.borderWidth = 1;
		fontParametersLlaves.borderStraight = true;
		fontParametersLlaves.borderColor = new Color(0f,0f,0f,1);
		fontParametersLlaves.minFilter = TextureFilter.Linear;
		fontParametersLlaves.magFilter = TextureFilter.Linear; 
		
		
		
		// Se crea la fuente
		BitmapFont customFont = fontGenerator.generateFont(fontParameters);
		customFont.setUseIntegerPositions(false);
		
		BitmapFont customFontLlaves = fontGenerator.generateFont(fontParametersLlaves);
		customFontLlaves.setUseIntegerPositions(false);
		
		labelStyle.font = customFont;	
		labelStyleLlaves.font = customFontLlaves;
			
		// Preparar para multiples clases/niveles/actores. Input discreto
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor( im );
        
        // Se configuran los botones de texto
        textButtonStyle = new TextButtonStyle();        
        Texture buttonTex = new Texture( Gdx.files.internal("assets/fondoPregunta.png") );
        NinePatch buttonPatch = new NinePatch(buttonTex, 1,1,1,1);
        textButtonStyle.up = new NinePatchDrawable( buttonPatch );
        textButtonStyle.font = customFont;
        textButtonStyle.fontColor = new Color(57/255f,60/255f,125/255f,1);     
        
        textButtonStyleMain = new TextButtonStyle();  
        Texture buttonTexMain = new Texture( Gdx.files.internal("assets/Botones/fondoMenuPrincipal.png") );
        NinePatch buttonPatchMain = new NinePatch(buttonTexMain, 1,1,1,1);
        textButtonStyleMain.up = new NinePatchDrawable( buttonPatchMain );
        textButtonStyleMain.font = customFont;
             
        
        
    }	
	
	
	// Para cambiar la pantalla que se está mostrando 
	public static void setActiveScreen(BaseScreen s)
	{
		game.setScreen(s);		
	}
	
	
	
}