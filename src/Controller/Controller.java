package Controller;

import java.util.ListIterator;
import java.util.Vector;

import GraphicLibrary.Color;
import GraphicLibrary.ApplicationFx;
import javafx.application.Application;
import shape.model.IShapeSimple;
import shape.model.Point;
import shape.model.Rectangle;
import shape.model.RegularPolygon;
import shape.model.ShapeComposite;

public class Controller {
	
	private Vector<IShapeSimple> tools;
	private Vector<IShapeSimple> shapes;
	private Vector<IShapeSimple> selected;
	
	private ApplicationFx view;
	private static Controller controller = new Controller();
	
	private Controller() {
		tools = new Vector<IShapeSimple>();
		shapes = new Vector<IShapeSimple>();
		selected = new Vector<IShapeSimple>();
		Thread t1 = new Thread() {
            @Override
            public void run() {
            	Application.launch(ApplicationFx.class);
            }
        };
        t1.start();
        while (!t1.isAlive() || ApplicationFx.getInstance() == null) {
        	// Wait for initialisation
        }
        begin();
	}
	
	public static Controller getInstance() {
		return controller;
	}
	
	public void begin() {
		view = ApplicationFx.getInstance();
		shape.model.Rectangle rect = new Rectangle(new Point(0, 0), 30, 20);
		rect.setColor(new Color(200, 30, 30));
		RegularPolygon poly = new RegularPolygon(new Point(0, 30), 5, 20);
		poly.setColor(new Color(30, 30, 200));
		addTool(rect);
		addTool(poly);
	}
	
	public void dragNDrop(IShapeSimple s, Point p) {
		IShapeSimple s2 = s.clone();
		s2.setPosition(p);
		addShape(s2);
		redraw();
	}
	
	public void erase(IShapeSimple s) {
		rmShape(s);
		redraw();
	}
	
	public void eraseAll() {
		for (ListIterator<IShapeSimple> i = shapeIterator(); i.hasNext();) {
			i.next();
		    i.remove();
		}
		redraw();
	}
	
	public void select(Point p1, Point p2) {
		selected.removeAllElements();
		System.out.println("Bamboozled");
		double minx, miny, maxx, maxy;
		if (p1.getX() < p2.getX()) {
			minx = p1.getX();
			maxx = p2.getX();
		}
		else {
			minx = p2.getX();
			maxx = p1.getX();
		}
		if (p1.getY() < p2.getY()) {
			miny = p1.getY();
			maxy = p2.getY();
		}
		else {
			miny = p2.getY();
			maxy = p1.getY();
		}
		for (ListIterator<IShapeSimple> i = shapeIterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
		    if (item.isInside(new Point(minx, miny), new Point(maxx, maxy))) {
		    	selected.add(item);
		    	System.out.println(item+" : "+item.getClass());
		    }
		}
	}
	
	public void group() {
		System.out.println(selected.size());
		if (selected.size() > 1) {
			ShapeComposite group = new ShapeComposite();
			for (ListIterator<IShapeSimple> i = selected.listIterator(); i.hasNext();) {
			    IShapeSimple item = i.next();
			    rmShape(item);
			    group.add(item);
			}
			addShape(group);
			System.out.println("Grouped (Yay !)");
			selected.removeAllElements();
		}
	}
	
	public void ungroup(ShapeComposite s) {
		rmShape(s);
		for (ListIterator<IShapeSimple> i = s.getShapes().listIterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
		    addShape(item);
		    i.remove();
		}
	}
	
	public void addTool(IShapeSimple s) {
		tools.add(s);
		view.addTool(s);
		view.dragNDrop(s);
	}
	
	public void redraw() {
		view.clear();
		for (ListIterator<IShapeSimple> i = shapeIterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
		    if (item instanceof Rectangle) {
		    	view.draw((Rectangle) item);
		    }
		    else if (item instanceof RegularPolygon) {
		    	view.draw((RegularPolygon) item);
		    }
		    if (item instanceof ShapeComposite) {
		    	view.draw((ShapeComposite) item);
		    }
		}
		view.addEvents();
	}
	
	public ListIterator<IShapeSimple> shapeIterator() {
		return shapes.listIterator();
	}
	
	public void addShape(IShapeSimple s) {
		shapes.add(s);
	}

	public void rmShape(IShapeSimple s) {
		shapes.remove(s);
	}

}
