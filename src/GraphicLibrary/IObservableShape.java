package GraphicLibrary;

import java.util.ListIterator;
import java.util.Vector;

import Shape.IShape;

public interface IObservableShape {
	
	public void notifyObserver();
	
	public void addObserver(IShape s);
	
	public boolean detachObserver(IShape s);
	
	public ListIterator<IShape> iterator();
	
	

}
