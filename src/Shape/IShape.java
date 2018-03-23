package Shape;

import GraphicLibrary.Color;

public interface IShape extends Cloneable {
	
	public IShape clone();
	
	public Point getPosition();
	
	public void setPosition(Point p);
	
	public double getRotation();	
	
	public void setRotation(double angle);
	
	public Color getColor();

	public void setColor(Color color);

	public void setRotationCenter(Point p);
	
	public Point getRotationCenter();
	
	public void update();

}
