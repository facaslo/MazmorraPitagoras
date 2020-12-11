package gameActors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.Input.Keys;
import java.util.ArrayList;


public class Carta extends BaseActor{
	private static ArrayList<Carta> cartasClicked;
	private boolean enMano;		
	private boolean clicked;	
	private String nombre;	
	
	public Carta(float x, float y, Stage s , boolean mano , String name) {
		super(x, y, s);
		enMano = mano;		
		clicked = false;	
		nombre = name;
		cartasClicked = new ArrayList<Carta>();
		
		loadTexture(name);		
		this.setBoundaryRectangle();
		
		/*----------------------------------------------------------------------------------------------- */
		// Eventos del mouse con las cartas
		this.addListener(new ClickListener(Buttons.LEFT) {  			
			// Evento de entrada del mouse 
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				/* Vector3 tmpCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				gameCamera.unproject(tmpCoords); */
								
                super.enter(event, x , y, pointer, fromActor);
                
                seleccionar(0.15f);             
            }	
			// Evento salida del mouse
			@Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				/* Vector3 tmpCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				gameCamera.unproject(tmpCoords); */
				
                super.exit(event, x, y, pointer, toActor);
                
                if (pointer == -1 && !clicked) {
                	removerAnimacion();                   
                }
            }
			// Evento click
			@Override
			public void clicked(InputEvent event, float x, float y) {
				click();
							
			}
			
        });
		/*----------------------------------------------------------------------------------------------- */
		
		// Eventos para las tarjetas de la mano
		if(enMano) {
			// Eventos de arrastre 
			this.addListener(new DragListener() {
				@Override
			    public void drag(InputEvent event, float x, float y, int pointer) {
			    	removerAnimacion();			    	
			    	moveBy(x - getWidth() / 2, y - getHeight() / 2);				    	
			    }
			    
				@Override
			    public void dragStop(InputEvent event, float x, float y, int pointer) {
			    	removerAnimacion();
			    	click();
			    }
			});
			
			// Para cambiar el color de la carta pulsando botón derecho
			this.addListener(new ClickListener(Buttons.RIGHT)
			{
			    @Override
			    public void clicked(InputEvent event, float x, float y)
			    {
			    	if (nombre.contains("monstersNegative/MonsterNegative")) {
			    		removeAnimation();
			    		String nuevoNombre = nombre.replaceAll("monstersNegative/MonsterNegative", 
			    				"monstersPositive/MonsterPositive");
			    		nombre = nuevoNombre;
			    		System.out.println("Cambiar textura");
			    		loadTexture(nuevoNombre);
			    	}
			    	
			    	else if (nombre.contains("monstersPositive/MonsterPositive")) {
			    		removeAnimation();
			    		String nuevoNombre = nombre.replaceAll("monstersPositive/MonsterPositive", 
			    				"monstersNegative/MonsterNegative");
			    		nombre = nuevoNombre;
			    		System.out.println("Cambiar textura");
			    		loadTexture(nuevoNombre);
			    	}
			    		
			    }
			});
		}	
		/*----------------------------------------------------------------------------------------------- */
	}	
	
	public void setEnMano(boolean valor) {
		enMano = valor;
	}	
	
	public String getNombreCarta() {
		return nombre;
	}
	
	public static ArrayList<Carta> getCartasClicked(){
		return cartasClicked;
	}
	
	public static void cleanCartasClicked(){
		cartasClicked.clear();
	}
	
	
	
	// Animación para cuando el mouse entre en la carta
	public void seleccionar(float numero) {		
		Action crecer = Actions.scaleBy(numero, numero, 0.3f);
		Action decrecer = Actions.scaleBy(-numero, -numero, 0.3f);
		Action parpadeo = Actions.forever(Actions.sequence(crecer, decrecer));
		this.addAction(parpadeo);			
			
	}
	
	// Animación para cuando el mouse salga de la carta
	public void removerAnimacion() {
		this.clearActions();
		this.setScale(1);
	}
	
	// Animación para cuando se hace click sobre una carta
	public void click() {		
		if (!clicked && cartasClicked.size() <= 1) {
			removerAnimacion();
			seleccionar(0.3f);
			clicked = true;			
			cartasClicked.add(this);
		}
		else if (clicked) {
			removerAnimacion();
			this.setScale(1);
			clicked = false;	
			cartasClicked.remove(this);
		}		
	}

	@Override
	public void act(float dt){
		super.act(dt);		
	}
}
