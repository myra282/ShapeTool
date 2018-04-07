package GraphicLibrary;

import java.util.ListIterator;
import java.util.Vector;

import Shape.IShape;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ObservableRectFx extends Rectangle implements IObservableShape {
	
	private Vector<IShape> observers;
	
	public ObservableRectFx(double x, double y, double width, double height) {
		super(x, y, width, height);
		setTranslateX(x);
		setTranslateY(y);
		observers = new Vector<IShape>();
	}
	
	@Override
	public void addObserver(IShape s) {
		observers.add(s);
	}
	
	@Override
	public boolean detachObserver(IShape s) {
		return observers.remove(s);
	}

	@Override
	public void notifyObserver() {
		for (ListIterator<IShape> i = observers.listIterator(); i.hasNext();) {
		    i.next().update(this);;
		}
	}

}
