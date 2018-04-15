package GraphicLibrary;

import shape.model.IShapeSimple;
import shape.model.Point;

public interface IApplication {
	
	public static final double BOARD_WIDTH = 1000;
	public static final double BOARD_HEIGHT = 800;
	public static final double BAR_MIN_WIDTH = 70;
	public static final double BAR_MAX_WIDTH = 140;
	
	//public void draw(IShape s);
	
	public void addTool(IShapeSimple s);
	
	public boolean inTrash(Point p);
	
	public void clear();
	
	public void begin();

}
