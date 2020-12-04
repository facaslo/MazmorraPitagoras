package gameLogic;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;


public abstract class BaseGame extends Game{
	public static LabelStyle labelStyle;
	public static TextButtonStyle textButtonStyle;
	private static BaseGame game;
	
	// Referencia a este objeto
	public BaseGame(){
		game = this;
	}
	
	public void create() 
    {        
		// Labelstyle 
		//labelStyle = new LabelStyle();		
		//labelStyle.font = new BitmapFont();
		// Importación de fuentes
		//FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("assets/OpenSans.ttf"));
		// Parámetros de fuente
		//FreeTypeFontParameter fontParameters = new FreeTypeFontParameter();
		/* fontParameters.size = 30;
		fontParameters.color = Color.WHITE;
		fontParameters.borderWidth = 2;
		fontParameters.borderColor = Color.BLACK;
		fontParameters.borderStraight = true;
		fontParameters.minFilter = TextureFilter.Linear;
		fontParameters.magFilter = TextureFilter.Linear; */
		// Se crea la fuente
		//BitmapFont customFont = fontGenerator.generateFont(fontParameters);
		//labelStyle.font = customFont;
		// Preparar para multiples clases/niveles/actores. Input discreto
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor( im );
        // Se configuran los botones de texto
        /* textButtonStyle = new TextButtonStyle();
        Texture buttonTex = new Texture( Gdx.files.internal("assets/button.png") );
        NinePatch buttonPatch = new NinePatch(buttonTex, 24,24,24,24);
        textButtonStyle.up = new NinePatchDrawable( buttonPatch );
        textButtonStyle.font = customFont;
        textButtonStyle.fontColor = Color.GRAY;
        */      
    }	
	
	
	// Para cambiar la pantalla que se está mostrando 
	public static void setActiveScreen(BaseScreen s)
	{
		game.setScreen(s);
	}
}