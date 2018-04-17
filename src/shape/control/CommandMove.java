package shape.control;

import shape.model.IShapeSimple;
import shape.model.Point;

public class CommandMove implements ICommand {
	
	private IShapeSimple shape;
	private Point newPos;
	private Point oldPos;

	public CommandMove(IShapeSimple shape, Point newPos) {
		this.shape = shape;
		this.newPos = newPos;
		this.oldPos = shape.getPosition();
	}

	@Override
	public void execute() {
		shape.setPosition(newPos);
	}

	@Override
	public void unexecute() {
		shape.setPosition(oldPos);
	}

}
