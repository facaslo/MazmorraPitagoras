package gameLogic;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import gameActors.BaseActor;
import gameActors.TiledActor;
import gameActors.Carta;
import gameActors.Cofre;
import gameActors.Remolino;

import com.badlogic.gdx.math.MathUtils;

public class Minijuego extends BaseScreen{
	
	String nombreTablero;
	private TiledActor tma;
	Label numeroNivel;
	
	/*Elementos de la ui */
	MapObject rectanguloBotonLimpiarCartas;
	MapObject rectanguloBotonReinicio;
	MapObject rectanguloLabelNivel;
	/*Listas de rectangulos obtenidos del tiled map */
	ArrayList<MapObject> listaCajasPositivas;
	ArrayList<MapObject> listaCajasNegativas;
	MapObject objetoIncognita;
	Cofre incognita;
	ArrayList<MapObject> listaCeros;
	MapObject zonaIzquierda;
	MapObject zonaDerecha;
	/*Elementos de las cartas arrastrables*/
	boolean incognitaZonaIzquierda;
	BaseActor hitboxZonaIzquierda;
	BaseActor hitboxZonaDerecha;
	/*Las cartas del tablero*/
	public static ArrayList<BaseActor> BaseActors;	
	int elementosZona;
	/*Las cartas de la mano*/
	ArrayList<Carta> cartaManoSuma;
	ArrayList<MapObject> hitboxesCartasManoSuma;
	ArrayList<MapObject> CartasSeleccionadas;
	BaseActor cartaSeleccionadaIzquierda;
	BaseActor cartaSeleccionadaDerecha;	
	
	
	boolean victoria;
	Cofre cofre;
	
	
	
	public Minijuego(String letraTab , int noTablero, int tabTotales, boolean izquierda) {
		super(letraTab, noTablero, tabTotales, izquierda);			
	}
	
	
	@Override
	public void initialize() {	
		nombreTablero = String.format("assets/tablero%s%d.tmx", letraTablero, numeroTablero);
		tma = new TiledActor(nombreTablero, mainStage , false);
		
		Camera mainCamera = tma.getStage().getCamera();
		mainCamera.viewportWidth = 208;
		mainCamera.viewportHeight = 160;
		mainCamera.position.x= 104;
		mainCamera.position.y= 80;	
				
				
		// Hitboxes de cartas positivas y negativas
		listaCajasPositivas = tma.getRectangleListContain("numeropositivo");
		listaCajasNegativas = tma.getRectangleListContain("numeronegativo");	
		objetoIncognita = tma.getRectangleList("incognita").get(0);
		listaCeros = tma.getRectangleListContain("cero");
		zonaIzquierda = tma.getRectangleList("zonaIzquierda").get(0);
		zonaDerecha = tma.getRectangleList("zonaDerecha").get(0);
		
		hitboxesCartasManoSuma = tma.getRectangleListContain("cartaManoSuma");
		cartaManoSuma = new ArrayList<Carta>();
		
		CartasSeleccionadas = tma.getRectangleListContain("cartaElegida");
		
		
		BaseActors = new ArrayList<BaseActor>();		
		
		
		
		// Crear las zonas del tablero
				hitboxZonaIzquierda = new BaseActor((float)zonaIzquierda.getProperties().get("x"), 
						(float)zonaIzquierda.getProperties().get("y"), mainStage);
				hitboxZonaIzquierda.setWidth((float)zonaIzquierda.getProperties().get("width"));
				hitboxZonaIzquierda.setHeight((float)zonaIzquierda.getProperties().get("height"));
				hitboxZonaIzquierda.setBoundaryRectangle();
				
				hitboxZonaDerecha = new BaseActor((float)zonaDerecha.getProperties().get("x"), 
						(float)zonaDerecha.getProperties().get("y"), mainStage);
				hitboxZonaDerecha.setWidth((float)zonaDerecha.getProperties().get("width"));
				hitboxZonaDerecha.setHeight((float)zonaDerecha.getProperties().get("height"));
				hitboxZonaDerecha.setBoundaryRectangle();
		
				
		// Se colocan las incognitas		
		incognita = new Cofre((float)objetoIncognita.getProperties().get("x"),
				(float)objetoIncognita.getProperties().get("y"),					
				mainStage ) ;		
		BaseActors.add(incognita);
		
		
		
		//Cargar sprites de las cartas positivas
		for (int i=0; i<listaCajasPositivas.size(); i++) {	
			int random = MathUtils.random(1,44);			
			Carta nuevaCarta = new Carta((float)listaCajasPositivas.get(i).getProperties().get("x"),
					(float)listaCajasPositivas.get(i).getProperties().get("y"),					
					mainStage, false, false,
					"assets/Boxes/monstersPositive/MonsterPositive"+ Integer.toString(random) + ".png");
			cartaManoSuma.add(nuevaCarta);
			BaseActors.add(nuevaCarta);
		}		
		
		//Cargar sprites de las cartas negativas
		for (int i=0; i<listaCajasNegativas.size(); i++) {
			int random = MathUtils.random(1,44);
			Carta nuevaCarta = new Carta((float)listaCajasNegativas.get(i).getProperties().get("x"),
					(float)listaCajasNegativas.get(i).getProperties().get("y"),					
					mainStage, false, false,
					"assets/Boxes/monstersNegative/MonsterNegative"+ Integer.toString(random) + ".png");
			cartaManoSuma.add(nuevaCarta);
			BaseActors.add(nuevaCarta);
		}	
		
		//Cargar los sprites del cero
		for (int i=0; i<listaCeros.size(); i++) {			
			Remolino remolino = new Remolino((float)listaCeros.get(i).getProperties().get("x"),
					(float)listaCeros.get(i).getProperties().get("y"),					
					mainStage);
			BaseActors.add(remolino);
					
		}	
		
		// Crear los contenedores de las cartas seleccionadas
		cartaSeleccionadaIzquierda = new BaseActor((float)CartasSeleccionadas.get(0).getProperties().get("x"),
					(float)CartasSeleccionadas.get(0).getProperties().get("y"),					
					mainStage); 
		cartaSeleccionadaIzquierda.setWidth((float)CartasSeleccionadas.get(0).getProperties().get("width"));
		cartaSeleccionadaIzquierda.setHeight((float)CartasSeleccionadas.get(0).getProperties().get("height"));
		cartaSeleccionadaIzquierda.setBoundaryRectangle();
		
		cartaSeleccionadaDerecha = new BaseActor((float)CartasSeleccionadas.get(1).getProperties().get("x"),
				(float)CartasSeleccionadas.get(1).getProperties().get("y"),					
				mainStage); 
		cartaSeleccionadaDerecha.setWidth((float)CartasSeleccionadas.get(1).getProperties().get("width"));
		cartaSeleccionadaDerecha.setHeight((float)CartasSeleccionadas.get(1).getProperties().get("height"));
		cartaSeleccionadaDerecha.setBoundaryRectangle();
		
		
		// Cargar las cartas en mano de suma
		for (int i=0; i<(listaCajasPositivas.size()+listaCajasNegativas.size()); i++) {
			if (cartaManoSuma.get(i).getNombreCarta().contains("monstersNegative/MonsterNegative")) {
				String nuevoNombre = cartaManoSuma.get(i).getNombreCarta().replaceAll("monstersNegative/MonsterNegative", 
						"monstersPositive/MonsterPositive");
				Carta nuevaCarta = new Carta((float)hitboxesCartasManoSuma.get(i).getProperties().get("x"), 
						(float)hitboxesCartasManoSuma.get(i).getProperties().get("y"),
						mainStage, false, true, nuevoNombre);				
				
				// Añadir funcionalidad a las cartas de la mano
				nuevaCarta.addListener(new ClickListener(Buttons.LEFT){
					@Override
					public void clicked(InputEvent event, float x, float y) {						
						
						if(!estaOcupadaCasillasCartasElegidas()) {
							Carta cartaIzquierda = new Carta(cartaSeleccionadaIzquierda.getX(), 
									cartaSeleccionadaIzquierda.getY(), mainStage, true,false,nuevaCarta.getNombreCarta());
							cartaIzquierda.setBoundaryRectangle();
							BaseActors.add(cartaIzquierda);
							
							/* Para permitir que la carta izquierda solo se pueda dejar en la zona izquierda
							 * y que la carta derecha en la zona derecha
							 */
							cartaIzquierda.addListener(new DragListener(){
								@Override
							    public void drag(InputEvent event, float x, float y, int pointer) {
									cartaIzquierda.removerAnimacion();			    	
									cartaIzquierda.moveBy(x - cartaIzquierda.getWidth() / 2, y - cartaIzquierda.getHeight() / 2);				    	
							    }
								@Override
							    public void dragStop(InputEvent event, float x, float y, int pointer) {
							    	if(cartaIzquierda.overlaps(hitboxZonaIzquierda)) {
							    		
							    		cartaIzquierda.removerAnimacion();
							    		cartaIzquierda.click();
							    	}
							    	else {
							    		
							    		cartaIzquierda.setPosition(cartaSeleccionadaIzquierda.getX(), 
							    				cartaSeleccionadaIzquierda.getY());
							    		cartaIzquierda.removerAnimacion();
							    		cartaIzquierda.click();
							    	}
							    }	
							});
							
							Carta cartaDerecha = new Carta(cartaSeleccionadaDerecha.getX(), 
									cartaSeleccionadaDerecha.getY(), mainStage, true,false,nuevaCarta.getNombreCarta());
							cartaDerecha.setBoundaryRectangle();
							BaseActors.add(cartaDerecha);
							
							cartaDerecha.addListener(new DragListener(){
								@Override
							    public void drag(InputEvent event, float x, float y, int pointer) {
									cartaDerecha.removerAnimacion();			    	
									cartaDerecha.moveBy(x - cartaDerecha.getWidth() / 2, y - cartaDerecha.getHeight() / 2);				    	
							    }
								@Override
							    public void dragStop(InputEvent event, float x, float y, int pointer) {
									
							    	if(cartaDerecha.overlaps(hitboxZonaDerecha)) {
							    		cartaDerecha.removerAnimacion();
							    		cartaDerecha.click();
							    	}
							    	else {
							    		cartaDerecha.setPosition(cartaSeleccionadaDerecha.getX(), 
							    				cartaSeleccionadaDerecha.getY());
							    		cartaDerecha.removerAnimacion();
							    		cartaDerecha.click();
							    	}
							    }	
							});
						}
					}	
				});			
				
			}
			
			else if (cartaManoSuma.get(i).getNombreCarta().contains("monstersPositive/MonsterPositive")) {
				String nuevoNombre = cartaManoSuma.get(i).getNombreCarta().replaceAll("monstersPositive/MonsterPositive", 
						"monstersNegative/MonsterNegative");
				Carta nuevaCarta = new Carta((float)hitboxesCartasManoSuma.get(i).getProperties().get("x"), 
						(float)hitboxesCartasManoSuma.get(i).getProperties().get("y"),
						mainStage, false, true, nuevoNombre);
				
				// Añadir funcionalidad a las cartas de la mano				
				nuevaCarta.addListener(new ClickListener(Buttons.LEFT){
					@Override
					public void clicked(InputEvent event, float x, float y) {
						
						
						if(!estaOcupadaCasillasCartasElegidas()) {
							Carta cartaIzquierda = new Carta(cartaSeleccionadaIzquierda.getX(), 
									cartaSeleccionadaIzquierda.getY(), mainStage, true,false,nuevaCarta.getNombreCarta());
							cartaIzquierda.setBoundaryRectangle();
							BaseActors.add(cartaIzquierda);
							
							/* Para permitir que la carta izquierda solo se pueda dejar en la zona izquierda
							 * y que la carta derecha en la zona derecha
							 */
							cartaIzquierda.addListener(new DragListener(){
								@Override
							    public void drag(InputEvent event, float x, float y, int pointer) {
									cartaIzquierda.removerAnimacion();			    	
									cartaIzquierda.moveBy(x - cartaIzquierda.getWidth() / 2, y - cartaIzquierda.getHeight() / 2);				    	
							    }
								@Override
							    public void dragStop(InputEvent event, float x, float y, int pointer) {
							    	if(cartaIzquierda.overlaps(hitboxZonaIzquierda)) {							    		
							    		cartaIzquierda.removerAnimacion();
							    		cartaIzquierda.click();
							    	}
							    	else {
							    		System.out.println("Reiniciar posición");
							    		cartaIzquierda.setPosition(cartaSeleccionadaIzquierda.getX(), 
							    				cartaSeleccionadaIzquierda.getY());
							    		cartaIzquierda.removerAnimacion();
							    		cartaIzquierda.click();
							    	}
							    }	
							});
							
							Carta cartaDerecha = new Carta(cartaSeleccionadaDerecha.getX(), 
									cartaSeleccionadaDerecha.getY(), mainStage, true,false,nuevaCarta.getNombreCarta());
							cartaDerecha.setBoundaryRectangle();
							BaseActors.add(cartaDerecha);
							
							cartaDerecha.addListener(new DragListener(){
								@Override
							    public void drag(InputEvent event, float x, float y, int pointer) {
									cartaDerecha.removerAnimacion();			    	
									cartaDerecha.moveBy(x - cartaDerecha.getWidth() / 2, y - cartaDerecha.getHeight() / 2);				    	
							    }
								@Override
							    public void dragStop(InputEvent event, float x, float y, int pointer) {
									
							    	if(cartaDerecha.overlaps(hitboxZonaDerecha)) {
							    		cartaDerecha.removerAnimacion();
							    		cartaDerecha.click();
							    	}
							    	else {
							    		cartaDerecha.setPosition(cartaSeleccionadaDerecha.getX(), 
							    				cartaSeleccionadaDerecha.getY());
							    		cartaDerecha.removerAnimacion();
							    		cartaDerecha.click();
							    	}
							    }	
							});
						}
					}	
				});						
			}
		}
		
		
		// Elementos de la UI
		// Label de nivel
				String cadena = String.format("Nivel %d  de  %d", numeroTablero , tablerosTotales);
				numeroNivel = new Label(cadena, BaseGame.labelStyle);
				// 11 font
				rectanguloLabelNivel = tma.getRectangleList("levelLabel").get(0);
					 
				//numeroNivel.setPosition(0,0);
				numeroNivel.setFontScale(0.37f);		
				numeroNivel.setPosition((float)rectanguloLabelNivel.getProperties().get("x"),
						(float)rectanguloLabelNivel.getProperties().get("y")-numeroNivel.getHeight()*numeroNivel.getFontScaleX());
				uiStage.addActor(numeroNivel);
		//Botón para limpiar cartas
		ButtonStyle estiloLimpiarCartas = new ButtonStyle();
		Texture texturaLimpiarCartas = new Texture(Gdx.files.internal("assets/Botones/botonLimpiarCartas.png"));
		TextureRegion regionLimpiarCartas = new TextureRegion(texturaLimpiarCartas);
		estiloLimpiarCartas.up = new TextureRegionDrawable(regionLimpiarCartas);		
		
		rectanguloBotonLimpiarCartas = tma.getRectangleList("botonLimpiarCartas").get(0);
		Button botonLimpiarCartas = new Button(estiloLimpiarCartas);
		botonLimpiarCartas.setPosition((float)rectanguloBotonLimpiarCartas.getProperties().get("x"), 
				(float)rectanguloBotonLimpiarCartas.getProperties().get("y"));			
		uiStage.addActor(botonLimpiarCartas);
		
		botonLimpiarCartas.addListener(new ClickListener() {
			// Evento de entrada del mouse 
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				/* Vector3 tmpCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				gameCamera.unproject(tmpCoords); */
								
                super.enter(event, x , y, pointer, fromActor);
                botonLimpiarCartas.setColor(Color.GREEN);
                          
            }	
			// Evento salida del mouse
			@Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				/* Vector3 tmpCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				gameCamera.unproject(tmpCoords); */
				
                super.exit(event, x, y, pointer, toActor);
                
                if (pointer == -1) {                	  
                	botonLimpiarCartas.setColor(1,1,1,1);
                }
            }
			// Borrar las cartas seleccionadas cuando no se han usado las dos simultaneamente
			@Override					
			public void clicked(InputEvent event, float x, float y) {
				ArrayList<BaseActor> actores = new ArrayList(BaseActors);
				for(int i=0; i<(actores.size() - 1); i++){
					if (actores.get(i).overlaps(cartaSeleccionadaIzquierda) && 
							actores.get(i+1).overlaps(cartaSeleccionadaDerecha)) {
						BaseActors.remove(actores.get(i));
						BaseActors.remove(actores.get(i+1));
						actores.get(i).remove();	
						actores.get(i+1).remove();
					}
				}					
			}
		});		
		
		ButtonStyle estiloReinicio = new ButtonStyle();
		Texture texturaReinicio = new Texture(Gdx.files.internal("assets/Botones/botonReset.png"));
		TextureRegion regionReinicio = new TextureRegion(texturaReinicio);
		estiloReinicio.up = new TextureRegionDrawable(regionReinicio);		
		
		rectanguloBotonReinicio = tma.getRectangleList("botonReinicio").get(0);
		Button botonReinicio = new Button(estiloReinicio);
		botonReinicio.setPosition((float)rectanguloBotonReinicio.getProperties().get("x"), 
				(float)rectanguloBotonReinicio.getProperties().get("y"));			
		uiStage.addActor(botonReinicio);
		
		botonReinicio.addListener(new ClickListener() {
			// Evento de entrada del mouse 
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
												
                super.enter(event, x , y, pointer, fromActor);
                botonReinicio.setColor(Color.GREEN);
                          
            }	
			// Evento salida del mouse
			@Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
							
                super.exit(event, x, y, pointer, toActor);
                
                if (pointer == -1) {                	  
                	botonReinicio.setColor(1,1,1,1);
                }
            }
			// Borrar las cartas seleccionadas cuando no se han usado las dos simultaneamente
			@Override					
			public void clicked(InputEvent event, float x, float y) {
				resetTablero();										
			}
		});		
		
		
			
	}
	
	
	public void resetTablero() {
		mainStage.clear();
		initialize();
	}
	
	
	

	@Override
	public void update(float dt) {
		Camera mainCamera = tma.getStage().getCamera();		
		mainCamera.viewportWidth = 208;
		mainCamera.viewportHeight = 160;
		mainCamera.position.x= 104;
		mainCamera.position.y= 80;	
		
		// Verificamos si las cartas coinciden
		try {
			String nombreCartaA = Carta.getCartasClicked().get(0).getNombreCarta();
			String nombreCartaB = Carta.getCartasClicked().get(1).getNombreCarta();
			String numeroCartaA = nombreCartaA.replaceAll("[^0-9]", "");
			String numeroCartaB = nombreCartaB.replaceAll("[^0-9]", "");			
			
			if(numeroCartaA.equals(numeroCartaB)) {
				if(
				((nombreCartaA.contains("monstersPositive") && nombreCartaB.contains("monstersNegative")) ||
				(nombreCartaB.contains("monstersPositive") && nombreCartaA.contains("monstersNegative"))) &&
				((Carta.getCartasClicked().get(0).overlaps(hitboxZonaIzquierda) && 
				(Carta.getCartasClicked().get(1).overlaps(hitboxZonaIzquierda))) ||
				(Carta.getCartasClicked().get(0).overlaps(hitboxZonaDerecha) && 
				(Carta.getCartasClicked().get(1).overlaps(hitboxZonaDerecha))))
				) {
				// Reemplazar las dos cartas con un remolino
				Remolino remolinoNuevo = new Remolino(Carta.getCartasClicked().get(0).getX(),
							Carta.getCartasClicked().get(0).getY(), mainStage);
				BaseActors.add(remolinoNuevo);
				Action desaparecer = Actions.sequence(Actions.fadeOut(2f), Actions.removeActor());
				Carta.getCartasClicked().get(0).addAction(desaparecer);
				Carta.getCartasClicked().get(1).addAction(desaparecer);
				
				
				Carta.getCartasClicked().get(0).remove();
				Carta.getCartasClicked().get(1).remove();
				BaseActors.remove(Carta.getCartasClicked().get(0));
				BaseActors.remove(Carta.getCartasClicked().get(1));
				Carta.cleanCartasClicked();				
				}
			}
		}
		catch(Exception e) {
			// System.out.println(Carta.getCartasClicked());
		}
		
		
		elementosZona = 0;
		
		// Veificar de que lado está la incognita a despejar
		for (BaseActor actor: BaseActors) {		
			if (incognitaZonaIzquierda) {
				if (hitboxZonaIzquierda.overlaps(actor)) {				
					elementosZona++;			
				}
			}else {
				if (hitboxZonaDerecha.overlaps(actor)) {				
					elementosZona++;										
				}
			}			
		}		
			
		// Verificar si se puede avanzar de nivel
		
				
		if((elementosZona == 1) && !estaOcupadaCasillasCartasElegidas()) {			
			incognita.setCofreAbierto(true); 
			victoria = true;			
		}
		
		else {
			victoria = false;	
		}
		
		//System.out.println(victoria);
	}
	
	public boolean estaOcupadaCasillasCartasElegidas() {
		boolean ocupado = false;
		Array<Actor> actoresEscenario = mainStage.getActors();
		ArrayList<Carta> cartasEscenario = new ArrayList();
		for (Actor actor : actoresEscenario) {
			if(actor instanceof Carta) {
				cartasEscenario.add((Carta)actor);
			}
		}		
		for( Carta carta: cartasEscenario) {			
			if(cartaSeleccionadaIzquierda.overlaps(carta) || cartaSeleccionadaDerecha.overlaps(carta)) {	
				ocupado = true;
				break;
			}			
		}		
		return ocupado;
	}
	
	public TiledActor getTma() {
		return tma;
	}
	
	@Override
	public boolean scrolled(float arg0, float arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode)
    {               
        
        if ( keycode == Keys.B) {        	
        	MathMaze.setActiveScreen(MathMaze.getLevelScreen());
        }
        
        if ( keycode == Keys.R) {
        	resetTablero();        	
        }        
        
        if ( keycode == Keys.L) {        		        
        	if(numeroTablero < tablerosTotales) {
        		MathMaze.setActiveScreen(new Minijuego("A", numeroTablero+1, tablerosTotales, true));
        	}
        }  
        
        return false;  
    }
	
	
	@Override
	public void resize(int width, int height) {		
		mainStage.getViewport().update(width, height, true);
	}
	
	
	
	
}
