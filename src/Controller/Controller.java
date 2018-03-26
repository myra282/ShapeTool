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
		//Application.launch(ShapeUIFx.class);
		//Thread t1 = new Thread(() -> Application.launch(ShapeUIFx.class));
		//while (ShapeUIFx.getInstance() == null) {
			//Non
		//}
		Thread t1 = new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(ShapeUIFx.class);
            }
        };
        t1.start();
        while (!t1.isAlive() || ShapeUIFx.getInstance() == null) {
        	//System.out.println("a");
        }
        begin();
	}
	
	public void begin() {
		System.out.println("meh");
		s = ShapeUIFx.getInstance();
		Rect rect = new Rect(new Point(0, 0), 30, 20);
		rect.setColor(new Dye(200,30,30));
		RegPoly poly = new RegPoly(new Point(0, 30), 5, 20);
		poly.setColor(new Dye(30,30,200));
		System.out.println(s);
		s.addTool(rect);
		s.addTool(poly);
		System.out.println("meh2");
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
