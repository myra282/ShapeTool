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
	
	private Vector<IShape> observers;
	private Vector<IShape> tools;
	
	private ShapeUIFx view;
	private static Controller controller = new Controller();
	
	private Controller() {
		observers = new Vector<IShape>();
		tools = new Vector<IShape>();
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
		rect.setColor(new Dye(200,30,30));
		RegPoly poly = new RegPoly(new Point(0, 30), 5, 20);
		poly.setColor(new Dye(30,30,200));
		addTool(rect);
		addTool(poly);
	}
	
	public void draw(IShape s) {
		view.draw(s);
	}
	
	public void dragNDrop(IShape s, Point p) {
		IShape s2 = s.clone();
		s2.setPosition(p);
		addObserver(s2);
		redraw();
	}
	
	public void dragNMove(IShape s, Point p) {
		s.setPosition(p);
		redraw();
	}
	
	public void erase(IShape s) {
		rmObserver(s);
		redraw();
	}
	
	public void eraseAll() {
		for (ListIterator<IShape> i = iterator(); i.hasNext();) {
		    IShape item = i.next();
		    i.remove();
		}
		redraw();
	}
	
	public void group(Point p1, Point p2) {
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
		System.out.println("group from ("+minx+", "+miny+") to ("+maxx+", "+maxy+")");
		ShapeComposite group = new ShapeComposite();
		Vector<IShape> toGroup = new Vector<IShape>();
		for (ListIterator<IShape> i = iterator(); i.hasNext();) {
		    IShape item = i.next();
		    Point p = item.getPosition();
		    //System.out.println("p = "+p);
		    System.out.println("p = "+p.getX()+", "+p.getY());
		    /*if ((p.getX() > minx && p.getY() > miny) 
		    && (p.getX() + item.getWidth() < maxx && p.getY() + item.getHeight() < maxy)) {*/
		    if (item.contained(new Point(minx, miny), new Point(maxx, maxy))) {
		    	toGroup.add(item);
		    }
		}
		if (toGroup.size() > 1) {
			System.out.println("grouped "+toGroup.size()+" shapes");
			for (ListIterator<IShape> i = toGroup.listIterator(); i.hasNext();) {
			    IShape item = i.next();
			    rmObserver(item);
			    group.add(item);
			}
			addObserver(group);
		}
	}
	
	public void ungroup(ShapeComposite s) {
		rmObserver(s);
		for (ListIterator<IShape> i = s.getShapes().listIterator(); i.hasNext();) {
		    IShape item = i.next();
		    addObserver(item);
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
		for (ListIterator<IShape> i = iterator(); i.hasNext();) {
		    IShape item = i.next();
		    draw(item);
		}
	}
	
	public ListIterator<IShape> iterator() {
		return observers.listIterator();
	}
	
	public void notifyObservers() {
		for (ListIterator<IShape> i = iterator(); i.hasNext();) {
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

}
