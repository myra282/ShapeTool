package Controller;

import java.util.Iterator;
import java.util.Vector;

import GraphicLibrary.Dye;
import GraphicLibrary.ShapeUIFx;
import Shape.IShape;
import Shape.Point;
import Shape.Rect;
import Shape.RegPoly;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Controller {
	
	private Vector<IShape> observers;
	private Vector<IShape> tools;
	
	private ShapeUIFx view;
	
	public Controller() {
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
	
	public void begin() {
		view = ShapeUIFx.getInstance();
		Rect rect = new Rect(new Point(0, 0), 30, 20);
		rect.setColor(new Dye(200,30,30));
		RegPoly poly = new RegPoly(new Point(0, 30), 5, 20);
		poly.setColor(new Dye(30,30,200));
		addTool(rect);
		addTool(poly);
		dragNDrop(poly);
	}
	
	public void draw(IShape s) {
		observers.add(s);
		view.draw(s);
	}
	
	public void dragNDrop(IShape s) {
		view.dragNDrop(s);
	}
	
	public void erase(IShape s) {
		observers.remove(s);
		redraw();
	}
	
	public void addTool(IShape s) {
		tools.add(s);
		view.addTool(s);
	}
	
	public void redraw() {
		view.clear();
		for (Iterator<IShape> i = observers.iterator(); i.hasNext();) {
		    IShape item = i.next();
		    draw(item);
		}
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

}
