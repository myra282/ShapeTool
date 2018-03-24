package Controller;

import java.util.Iterator;
import java.util.Vector;

import GraphicLibrary.ShapeUIFx;
import Shape.IShape;

public class Controller {
	
	private Vector<IShape> observers;
	
	private ShapeUIFx s;
	
	public Controller() {
		s = ShapeUIFx.getInstance();
	}
	
	public void notifyObservers() {
		for (Iterator<IShape> i = observers.iterator(); i.hasNext();) {
		    IShape item = i.next();
		    item.update();
		}
	}

	public void addObserver(IShape o) {
		observers.add(o);
	}

	public void rmObserver(IShape o) {
		observers.remove(o);
	}
	
	public void begin() {
		s.begin();
	}

}
