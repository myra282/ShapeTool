package shape.graphicapplication;

import shape.model.IShape;

public interface IApplication {
	
	public static final double BOARD_WIDTH = 1000;
	public static final double BOARD_HEIGHT = 800;
	public static final double BAR_MIN_WIDTH = 70;
	public static final double BAR_MAX_WIDTH = 140;
	
	public static final double ROUNDED_VALUE = 45;
	
	public void addTool(IShape s);
	
	public void draw(IShape s);
	
	public void clear();
	
	public void begin();

	public void addEvents();

}
