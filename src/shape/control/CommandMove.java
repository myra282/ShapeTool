package shape.control;

import shape.model.IShape;
import shape.model.Point;

public class CommandMove implements ICommand {
	
	private IShape shape;
	private Point newPos;
	private Point oldPos;

	public CommandMove(IShape shape, Point newPos) {
		this.shape = shape;
		this.newPos = newPos;
		this.oldPos = shape.getPosition();
		//this.oldPos = new Point(shape.getPosition().getX(), shape.getPosition().getY());
	}

	@Override
	public void execute() {
		shape.setPosition(newPos);
    	if (!Controller.getInstance().isInBoard(shape)) { //if shape exceeds board bounds, rollback
    		shape.setPosition(oldPos);
    	}
	}

	@Override
	public void unexecute() {
		shape.setPosition(oldPos);
	}

}
