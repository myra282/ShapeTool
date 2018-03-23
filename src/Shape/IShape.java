package Shape;

import GraphicLibrary.Point;

public interface IShape extends Cloneable {
	
	public IShape clone();
	
	public void setPosition(Point p);
	
	public void setRotation(double angle);
	
	public void update();

}
