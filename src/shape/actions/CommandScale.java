package shape.actions;

import shape.model.IShapeSimple;

public class CommandScale implements ICommand {
	
	private IShapeSimple shape;
	private double newScale;

	public CommandScale(IShapeSimple shape, double scale) {
		this.shape = shape;
		this.newScale = scale;
	}

	@Override
	public void execute() {
		shape.scale(newScale);
	}

	@Override
	public void unexecute() {
		shape.scale(1/newScale);
	}

}
