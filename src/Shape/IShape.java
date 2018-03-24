package Shape;

import GraphicLibrary.Dye;

public interface IShape extends Cloneable {
	
	public IShape clone();
	
	public Point getPosition();
	
	public void setPosition(Point p);
	
	public double getWidth();
	
	public double getHeight();
	
	public double getRotation();	
	
	public void setRotation(double angle);
	
	public Dye getColor();

	public void setColor(Dye color);

	public void setRotationCenter(Point p);
	
	public Point getRotationCenter();
	
	public void update();

}
