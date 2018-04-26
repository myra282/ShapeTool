package shape.graphicapplication;

import shape.model.IShapeSimple;

/**
 * 
 * @author Mary Pascal & Marc Saint-Jean-Clergeau <br><br>
 *
 * This interface defines a builder for the class Mediator
 * @see Mediator
 * 
 */
public interface IApplication {
	
	public static final double BOARD_WIDTH = 1000;
	public static final double BOARD_HEIGHT = 800;
	public static final double BAR_MIN_WIDTH = 70;
	public static final double BAR_MAX_WIDTH = 140;
	public static final double ROUNDED_VALUE = 30;
	
	public void addTool(IShapeSimple s);
	
	public void draw(IShapeSimple s);
	
	public void clear();
	
	public void displayMessage(String message, boolean warning);

	public void addEvents();
	
}
