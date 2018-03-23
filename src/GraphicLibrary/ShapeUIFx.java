package GraphicLibrary;

import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import Shape.RegularPolygon;
import Shape.IShape;
import Shape.Point;
import Shape.Rect;
import Shape.ShapeComposite;

import java.util.Iterator;
import java.util.Vector;

public class ShapeUIFx implements IShapeUI {
	
	private Vector<IShape> observers;
	
	private Shape shape;

	public ShapeUIFx(RegularPolygon p) {
		double points[] = new double[p.getNbEdges() * 2];
		double radius = p.getEdgeWidth() / (2 * Math.sin(180/p.getNbEdges()));
		double angle = 0;
		double inc = 360 / p.getNbEdges();
		for (int i = 0 ; i < p.getNbEdges() ; ++i) {
			points[2 * i] = p.getRotationCenter().getX() + (radius * Math.cos(angle));
			points[2 * i + 1] = p.getRotationCenter().getY() + (radius * Math.sin(angle));
			angle += inc;
		}
		shape = new Polygon(points);
	}
	
	public ShapeUIFx(Rect r) {
		shape = new Rectangle(r.getPosition().getX(), r.getPosition().getY(), r.getWidth(), r.getHeight());
	}
	
	public ShapeUIFx(ShapeComposite s) {
		for (Iterator<IShape> i = observers.iterator(); i.hasNext();) {
		    IShape item = i.next();
		    
		}
	}

	@Override
	public void move(Point p) {
		shape.setTranslateX(p.getX());
		shape.setTranslateY(p.getY());
	}

	@Override
	public void rotate(double angle) {
		shape.getTransforms().add(new Rotate(angle,0,0));
	}
	
	@Override
	public void notifyObservers() {
		for (Iterator<IShape> i = observers.iterator(); i.hasNext();) {
		    IShape item = i.next();
		    item.update();
		}
	}

	@Override
	public void addObserver(IShape o) {
		observers.add(o);
	}

	@Override
	public void rmObserver(IShape o) {
		observers.remove(o);
	}

}
