package shape.actions;

import shape.control.Mediator;
import shape.model.IShapeSimple;
import shape.model.Point;

public class CommandMove implements ICommand {
	
	private IShapeSimple shape;
	private Point newPos;
	private Point oldPos;
	private boolean tool;

	public CommandMove(IShapeSimple shape, Point newPos, boolean tool) {
		this.shape = shape;
		this.newPos = newPos;
		this.oldPos = shape.getPosition();
		this.tool = tool;
	}

	@Override
	public void execute() {
		shape.setPosition(newPos);
    	if (!tool && !Mediator.getInstance().isInBoard(shape)) {
    		shape.setPosition(oldPos);
    	}
    	else if (tool && !Mediator.getInstance().isInToolbar(shape)) {
    		shape.setPosition(oldPos);
    	}
	}

	@Override
	public void unexecute() {
		shape.setPosition(oldPos);
	}

}
