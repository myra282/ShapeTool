package GraphicLibrary;

import shape.model.IShapeSimple;

public interface IApplication {
	
	//public void draw(IShape s);
	
	public void addTool(IShapeSimple s);
	
	public void clear();
	
	public void begin();

}
