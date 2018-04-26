package shape.model;

/**
 * 
 * @author Mary Pascal & Marc Saint-Jean-Clergeau <br><br>
 *
 * This defines an interface for a very basic shape
 * 
 */
public interface IShape {
	
	public Point getPosition();
	
	public double getRotation();
	
	public Point getRotationCenter();
	
	public Color getColor();
	
}
