package Shape;

import GraphicLibrary.Dye;

public interface IShape extends Cloneable {
	
	public IShape clone();
	
	public IShape getParent();
	
	public void setParent(IShape parent);
	
	//public void notifyParent(Point p);
	
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
	
	public void resize(double ratio);
	
	public boolean contained(Point min, Point max);
	
	public void update();

}
