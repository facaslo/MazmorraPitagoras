package gameActors;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class Jugador extends BaseActor{
		
	public Jugador(float x, float y , Stage s) {
		super(x,y,s);
		
		
		String[] filenames = {"assets/character_sprites/Knight-Standard/Knight_Idle_1.png", 
				"assets/character_sprites/Knight-Standard/Knight_Idle_2.png",
				"assets/character_sprites/Knight-Standard/Knight_Idle_3.png",
				"assets/character_sprites/Knight-Standard/Knight_Idle_4.png"};
				
		loadAnimationFromFiles(filenames, 0.1f, true);
		
		setAcceleration(5000);
		setMaxSpeed(100);
		setDeceleration(5000);	
		
				
		float[] listaVertices = {getWidth()/4, 0 , 
								getWidth()*3/4,0,
								getWidth()*3/4, getHeight()* 1/3,
								getWidth()/4, getHeight()* 1/3};
		setBoundaryPolygon(listaVertices); 
		
		
	}
	
	public void AnimacionIdle() {
		String[] filenames = {"assets/character_sprites/Knight-Standard/Knight_Idle_1.png", 
				"assets/character_sprites/Knight-Standard/Knight_Idle_2.png",
				"assets/character_sprites/Knight-Standard/Knight_Idle_3.png",
				"assets/character_sprites/Knight-Standard/Knight_Idle_4.png"};
		
		setAnimation(loadAnimationFromFiles(filenames, 0.1f, true));
	}
	
	public void AnimacionCorrerDerecha() {
		String[] filenames2 = {"assets/character_sprites/Knight-Standard/Knight_Walk_1.png", 
				"assets/character_sprites/Knight-Standard/Knight_Walk_2.png",
				"assets/character_sprites/Knight-Standard/Knight_Walk_3.png",
				"assets/character_sprites/Knight-Standard/Knight_Walk_4.png"};
		
		setAnimation(loadAnimationFromFiles(filenames2, 0.1f, true));
	}
	
	public void AnimacionCorrerArriba() {
		setAnimation(loadAnimationFromSheet("assets/character_sprites/Knight-Standard/Knight_walk_backwards.png"
				,1,8,0.1f,true));
	}
	
	public void AnimacionCorrerAbajo() {
		setAnimation(loadAnimationFromSheet("assets/character_sprites/Knight-Standard/Knight_walk_front.png"
				,1,8,0.1f,true));
	}
	
	public void AnimacionCorrerIzquierda() {
		setAnimation(loadAnimationFromSheet("assets/character_sprites/Knight-Standard/Knight_Walk_left.png"
				,1,4,0.1f,true));
	}	
	
	@Override
	public void act(float dt){
		super.act( dt );
		
		// Para verificar el input del usuario
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			accelerateAtAngle(0);
			AnimacionCorrerDerecha();
		}
			
		else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			accelerateAtAngle(180);
			AnimacionCorrerIzquierda();
		}
		
		else if (Gdx.input.isKeyPressed(Keys.UP)) {
			accelerateAtAngle(90);
			AnimacionCorrerArriba();			
		}
		
		else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			accelerateAtAngle(270);
			AnimacionCorrerAbajo();
		}
				
		applyPhysics(dt);	
		
		getBoundaryPolygon();
		
		if (getSpeed() == 0)
			AnimacionIdle() ;	
		
		
		boundToWorld();	
		
		
		alignCamera();
	}
	
}
