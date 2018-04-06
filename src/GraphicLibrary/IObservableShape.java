package GraphicLibrary;

import Shape.IShape;

public interface IObservableShape {
	
	public void notifyObserver();
	
	public void addObserver(IShape s);
	
	public boolean detachObserver(IShape s);

}
