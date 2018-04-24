package shape.actions;

import shape.model.IShapeSimple;
import shape.model.ShapeMemento;

public class CommandChange implements ICommand {
	
	private IShapeSimple shape;
	private ShapeMemento info;
	private ShapeMemento old;

	public CommandChange(IShapeSimple shape, ShapeMemento info) {
		this.shape = shape;
		this.info = info;
		this.old = shape.createMemento();
	}

	@Override
	public void execute() {
		shape.restoreMemento(info);
	}

	@Override
	public void unexecute() {
		shape.restoreMemento(old);
	}

}
