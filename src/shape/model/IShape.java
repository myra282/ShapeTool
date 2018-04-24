package shape.model;

public interface IShape extends Cloneable {
	
public Point getPosition();
	
	public void setPosition(Point p);
	
	public double getWidth();
	
	public double getHeight();
	
	public double getRotation();	
	
	public void setRotation(double angle);
	
	public Color getColor();

	public void setColor(Color color);

	public void setRotationCenter(Point p);
	
	public Point getRotationCenter();
	
	public void scale(double ratio);
	
	public IShape clone();
	
	public IShape getParent();
	
	public boolean isInside(Point min, Point max);

	public boolean contains(Point p);

}
