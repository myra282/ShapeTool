package Controller;

import java.util.ListIterator;
import java.util.Vector;

import GraphicLibrary.Dye;
import GraphicLibrary.ShapeUIFx;
import Shape.IShape;
import Shape.Point;
import Shape.Rect;
import Shape.RegPoly;
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
	
	public void erase(IShape s) {
		rmObserver(s);
		redraw();
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
