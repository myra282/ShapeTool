package shape.actions;

import shape.control.Mediator;
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
	}

	@Override
	public void execute() {
		shape.setPosition(newPos);
    	if (!Mediator.getInstance().isInBoard(shape)) { //if shape exceeds board bounds, rollback
    		shape.setPosition(oldPos);
    	}
	}

	@Override
	public void unexecute() {
		shape.setPosition(oldPos);
	}

}
