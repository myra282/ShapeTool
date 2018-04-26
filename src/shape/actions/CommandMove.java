package shape.actions;

import shape.control.Mediator;
import shape.model.IShapeSimple;
import shape.model.Point;

/**
 * 
 * @author Mary Pascal & Marc Saint-Jean-Clergeau <br><br>
 *
 * This class defines a "move" operation on a given shape
 * @see ICommand
 * @see IShapeSimple
 * 
 */
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
    	if (!tool && !Mediator.getInstance().isInBoard(shape) ||
    		tool && !Mediator.getInstance().isInToolbar(shape)) {
    		unexecute();
    		Mediator.getInstance().displayMessage("Your shape wouldn't be in board...", true);
    	}
	}

	@Override
	public void unexecute() {
		shape.setPosition(oldPos);
	}

}
