package shape.model;

import java.util.Iterator;
import java.util.Vector;

/**
 * 
 * @author Mary Pascal & Marc Saint-Jean-Clergeau <br><br>
 *
 * This class is a special ShapeMemento for the ShapeComposite class
 * @see ShapeMemento
 * @see ShapeComposite
 */
public class ShapeMementoComposite extends ShapeMemento {
	
	private Vector<ShapeMemento> mementos;
	
	public ShapeMementoComposite(double d1, double d2, Point position, double rotation, Point rotationCenter, 
								 Color color, boolean rounded, Vector<ShapeMemento> mementos) {
		super(d1, d2, position, rotation, rotationCenter, color, rounded);
		this.mementos = mementos;
	}
	
	public Iterator<ShapeMemento> iterator() {
		return mementos.iterator();
	}

}
