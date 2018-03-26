package GraphicLibrary;

import Shape.IShape;

public interface IShapeUI {
	
	public void draw(IShape s);
	
	public void addTool(IShape s);
	
	public void clear();
	
	public void begin();

}
