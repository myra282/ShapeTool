package shape.control;

import shape.model.Rectangle;

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
