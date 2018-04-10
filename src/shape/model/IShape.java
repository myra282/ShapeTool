package shape.model;

import GraphicLibrary.Color;

public interface IShape {
	
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

}
