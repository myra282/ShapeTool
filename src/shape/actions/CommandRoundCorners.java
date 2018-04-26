package shape.actions;

import shape.model.Rectangle;

/**
 * 
 * @author Mary Pascal & Marc Saint-Jean-Clergeau <br><br>
 *
 * This class defines an operation to round corners of a given Rectangle
 * @see ICommand
 * @see Rectangle
 * 
 */
public class CommandRoundCorners implements ICommand {
	
	private Rectangle shape;

	public CommandRoundCorners(Rectangle shape) {
		this.shape = shape;
	}

	@Override
	public void execute() {
		shape.setRounded(!shape.getRounded());
	}

	@Override
	public void unexecute() {
		shape.setRounded(!shape.getRounded());
	}

}
