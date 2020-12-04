package gameActors;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class Puerta extends BaseActor{
	private boolean abierta;
	private String nombrePuerta;
	
	
	public Puerta(float x, float y, Stage S) {
		super(x,y,S);
		abierta = false;
		
		
		loadTexture("assets/DoorClosed.png");		
	}
	
	
	public void act(float dt){
		super.act( dt );		
		applyPhysics(dt);			
	}	
	
	public void abrirPuerta() {
		abierta = true;
		removeAnimation();	
	}
	
	public void setNombrePuerta(String nombre) {
		nombrePuerta = nombre;		
	}
	
	public String getNombrePuerta() {
		return nombrePuerta;
	}
	
}
