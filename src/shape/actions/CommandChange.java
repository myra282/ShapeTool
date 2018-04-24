package shape.actions;

import shape.model.IShape;
import shape.model.Point;
import shape.model.Rectangle;
import shape.model.RegularPolygon;

public class CommandChange implements ICommand {
	
	private IShape shape;
	private Rectangle info;
	private Rectangle old;

	public CommandChange(IShape shape, Rectangle info) {
		this.shape = shape;
		this.info = info;
		double d1 = 0;
		double d2 = 0;
		if (shape instanceof Rectangle) {
			d1 = ((Rectangle) shape).getWidth();
			d2 = ((Rectangle) shape).getHeight();
		}
		else if (shape instanceof RegularPolygon) {
			d1 = ((RegularPolygon) shape).getNbEdges();
			d2 = ((RegularPolygon) shape).getEdgeWidth();
		}
		this.old = new Rectangle(new Point(shape.getPosition().getX(), shape.getPosition().getY()), d1, d2);
		this.old.setRotation(shape.getRotation());
		this.old.setRotationCenter(new Point(shape.getRotationCenter().getX(), shape.getRotationCenter().getY()));
		this.old.setColor(shape.getColor().clone());
	}

	@Override
	public void execute() {
		shape.setPosition(new Point(info.getPosition().getX(), info.getPosition().getY()));
		if (shape instanceof Rectangle) {
			((Rectangle) shape).setWidth(info.getWidth());
			((Rectangle) shape).setHeight(info.getHeight());
		}
		else if (shape instanceof RegularPolygon) {
			((RegularPolygon) shape).setNbEdges((int) info.getWidth());
			((RegularPolygon) shape).setEdgeWidth(info.getHeight());
		}
		shape.setRotation(info.getRotation());
		shape.setRotationCenter(new Point(info.getRotationCenter().getX(), info.getRotationCenter().getY()));
		shape.setColor(info.getColor());
	}

	@Override
	public void unexecute() {
		shape.setPosition(new Point(old.getPosition().getX(), old.getPosition().getY()));
		if (shape instanceof Rectangle) {
			((Rectangle) shape).setWidth(old.getWidth());
			((Rectangle) shape).setHeight(old.getHeight());
		}
		else if (shape instanceof RegularPolygon) {
			((RegularPolygon) shape).setNbEdges((int) old.getWidth());
			((RegularPolygon) shape).setEdgeWidth(old.getHeight());
		}
		shape.setRotation(old.getRotation());
		shape.setRotationCenter(new Point(old.getRotationCenter().getX(), old.getRotationCenter().getY()));
		shape.setColor(old.getColor());
	}

}
