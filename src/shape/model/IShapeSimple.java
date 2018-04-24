package shape.model;

public interface IShapeSimple extends Cloneable, IShape {
	
	public IShapeSimple clone();
	
	public ShapeMemento createMemento();
	
	public void restoreMemento(ShapeMemento mem);
	
	public void setPosition(Point p);
	
	public double getWidth();
	
	public double getHeight();	
	
	public void setRotation(double angle);

	public void setColor(Color color);

	public void setRotationCenter(Point p);
	
	public void scale(double ratio);
	
	public IShapeSimple getParent();
	
	public boolean isInside(Point min, Point max);

	public boolean contains(Point p);

	public boolean getRounded();

}
