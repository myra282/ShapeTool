package GraphicLibrary;

import Shape.IShape;
import Shape.Point;

public interface IShapeUI {
	
	public void move(Point p);
	
	public void rotate(double angle);
	
	public void notifyObservers();
	
	public void addObserver(IShape o);
	
	public void rmObserver(IShape o);

}
