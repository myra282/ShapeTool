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

public class Controller {
	
	private Vector<IShape> observers;
	
	private ShapeUIFx s;
	
	public Controller() {
		begin();
		//new Thread(() -> Application.launch(ShapeUIFx.class)).start();
		//while (ShapeUIFx.getInstance() == null);
		System.out.println("meh");
		s = ShapeUIFx.getInstance();
		Rect rect = new Rect(new Point(0, 0), 30, 20);
		rect.setColor(new Dye(200,30,30));
		RegPoly poly = new RegPoly(new Point(0, 30), 5, 20);
		poly.setColor(new Dye(30,30,200));
		s.addTool(rect);
		s.addTool(poly);
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
	
	public void begin() {
		Application.launch(ShapeUIFx.class);
	}

}
