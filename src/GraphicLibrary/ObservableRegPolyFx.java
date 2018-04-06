package GraphicLibrary;

import java.util.ListIterator;
import java.util.Vector;

import Shape.IShape;
import Shape.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class ObservableRegPolyFx extends Polygon implements IObservableShape {
	
	private Vector<IShape> observers;
	
	public ObservableRegPolyFx(double[] points) {
		super(points);
		observers = new Vector<IShape>();
	}
	
	private static double[] computePoints(Point position, int nbEdges, double edgeWidth) {
		double points[] = new double[nbEdges * 2];
		double angle = 0;
		double inc = 360 / nbEdges;
		double radius = edgeWidth / (2 * Math.sin(Math.toRadians(180/nbEdges)));
		for (int i = 0 ; i < nbEdges ; ++i) {
			points[2 * i] = (radius * Math.cos(Math.toRadians(angle)));
			points[2 * i + 1] = (radius * Math.sin(Math.toRadians(angle)));
			angle += inc;
		}
		return points;
	}
	
	public ObservableRegPolyFx(Point position, int nbEdges, double edgeWidth) {
		super(computePoints(position, nbEdges, edgeWidth));
		setFill(Color.BLACK);
		setTranslateX(position.getX());
		setTranslateY(position.getY());
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
		    i.next().update(this);
		}
	}

}
