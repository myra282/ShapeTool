package GraphicLibrary;

import shape.model.IShapeSimple;
import shape.model.Point;
import shape.model.Rectangle;
import shape.model.RegularPolygon;
import shape.model.ShapeComposite;

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

	public void draw(Rectangle item);

	public void draw(RegularPolygon item);

	public void draw(ShapeComposite item);

	public void addEvents();

}
