package Controller;

import java.util.ListIterator;
import java.util.Vector;

import GraphicLibrary.Dye;
import GraphicLibrary.ShapeUIFx;
import Shape.IShape;
import Shape.Point;
import Shape.Rect;
import Shape.RegPoly;
import Shape.ShapeComposite;
import javafx.application.Application;

public class Controller {
	
	private Vector<IShape> tools;
	private Vector<IShape> shapes;
	private Vector<IShape> selected;
	
	private ShapeUIFx view;
	private static Controller controller = new Controller();
	
	private Controller() {
		tools = new Vector<IShape>();
		shapes = new Vector<IShape>();
		selected = new Vector<IShape>();
		Thread t1 = new Thread() {
            @Override
            public void run() {
            	Application.launch(ShapeUIFx.class);
            }
        };
        t1.start();
        while (!t1.isAlive() || ShapeUIFx.getInstance() == null) {
        	// Wait for initialisation
        }
        begin();
	}
	
	public static Controller getInstance() {
		return controller;
	}
	
	public void begin() {
		view = ShapeUIFx.getInstance();
		Rect rect = new Rect(new Point(0, 0), 30, 20);
		rect.setColor(new Dye(200, 30, 30));
		RegPoly poly = new RegPoly(new Point(0, 30), 5, 20);
		poly.setColor(new Dye(30, 30, 200));
		addTool(rect);
		addTool(poly);
	}
	
	public void dragNDrop(IShape s, Point p) {
		IShape s2 = s.clone();
		s2.setPosition(p);
		addShape(s2);
		redraw();
	}
	
	public void erase(IShape s) {
		rmShape(s);
		redraw();
	}
	
	public void eraseAll() {
		for (ListIterator<IShape> i = shapeIterator(); i.hasNext();) {
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
		for (ListIterator<IShape> i = shapeIterator(); i.hasNext();) {
		    IShape item = i.next();
		    if (item.contained(new Point(minx, miny), new Point(maxx, maxy))) {
		    	selected.add(item);
		    	System.out.println(item+" : "+item.getClass());
		    }
		}
	}
	
	public void group() {
		System.out.println(selected.size());
		if (selected.size() > 1) {
			ShapeComposite group = new ShapeComposite();
			for (ListIterator<IShape> i = selected.listIterator(); i.hasNext();) {
			    IShape item = i.next();
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
		for (ListIterator<IShape> i = s.getShapes().listIterator(); i.hasNext();) {
		    IShape item = i.next();
		    addShape(item);
		    i.remove();
		}
	}
	
	public void addTool(IShape s) {
		tools.add(s);
		view.addTool(s);
		view.dragNDrop(s);
	}
	
	public void redraw() {
		view.clear();
		for (ListIterator<IShape> i = shapeIterator(); i.hasNext();) {
		    IShape item = i.next();
		    view.draw(item);
		}
		view.activate();
	}
	
	public ListIterator<IShape> shapeIterator() {
		return shapes.listIterator();
	}
	
	public void addShape(IShape s) {
		shapes.add(s);
	}

	public void rmShape(IShape s) {
		shapes.remove(s);
	}

}
