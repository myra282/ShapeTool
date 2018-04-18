package shape.control;

import shape.model.IShape;
import shape.model.Rectangle;
import shape.model.RegularPolygon;

public class CommandChange implements ICommand {
	
	private IShape shape;
	private double d1;
	private double d2;
	private double old1;
	private double old2;

	public CommandChange(IShape shape, double d1, double d2) {
		this.shape = shape;
		this.d1 = d1;
		this.d2 = d2;
		if (shape instanceof Rectangle) {
			this.old1 = ((Rectangle) shape).getWidth();
			this.old2 = ((Rectangle) shape).getHeight();
		}
		else if (shape instanceof RegularPolygon) {
			this.old1 = ((RegularPolygon) shape).getNbEdges();
			this.old2 = ((RegularPolygon) shape).getEdgeWidth();
		}
	}

	@Override
	public void execute() {
		if (shape instanceof Rectangle) {
			((Rectangle) shape).setWidth(d1);
			((Rectangle) shape).setHeight(d2);
		}
		else if (shape instanceof RegularPolygon) {
			((RegularPolygon) shape).setNbEdges((int) d1);
			((RegularPolygon) shape).setEdgeWidth(d2);
		}
	}

	@Override
	public void unexecute() {
		if (shape instanceof Rectangle) {
			((Rectangle) shape).setWidth(old1);
			((Rectangle) shape).setHeight(old2);
		}
		else if (shape instanceof RegularPolygon) {
			((RegularPolygon) shape).setNbEdges((int) old1);
			((RegularPolygon) shape).setEdgeWidth(old2);
		}
	}

}
