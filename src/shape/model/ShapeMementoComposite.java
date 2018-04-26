package shape.model;

import java.util.Iterator;
import java.util.Vector;

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
