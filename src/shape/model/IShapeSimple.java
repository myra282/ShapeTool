package shape.model;

import java.io.Serializable;

/**
 * 
 * @author Mary Pascal & Marc Saint-Jean-Clergeau <br><br>
 *
 * This defines an interface for a simple shape
 * @see IShape
 * 
 */
public interface IShapeSimple extends Cloneable, IShape, Serializable {
	
	public IShapeSimple clone();
	
	public ShapeMemento createMemento();
	
	public void restoreMemento(ShapeMemento mem);
	
	public void setPosition(Point p);
	
	public IShapeSimple getParent();
	
	public double getWidth();
	
	public double getHeight();	
	
	public boolean getRounded();

	public void setRotation(double angle);

	public void setColor(Color color);

	public void setRotationCenter(Point p);
	
	public void scale(double ratio);
	
	public boolean isInside(Point min, Point max);

	public boolean contains(Point p);
}
