package shape.control;

import java.util.Vector;

public class ActionJournal {
	
	private Vector<ICommand> undoList;
	private Vector<ICommand> redoList;
	
	public ActionJournal() {
		undoList = new Vector<ICommand>();
		redoList = new Vector<ICommand>();
	}
	
	public void add(ICommand c) {
		undoList.add(c);
	}
	
	public boolean canUndo() {
		return (undoList.size() > 0);
	}
	
	public boolean canRedo() {
		return (redoList.size() > 0);
	}
	
	public void undo() {
		ICommand c = undoList.lastElement();
		undoList.remove(undoList.size()-1);
		c.unexecute();
		redoList.add(c);
	}
	
	public void redo() {
		ICommand c = redoList.lastElement();
		redoList.remove(redoList.size()-1);
		c.execute();
		undoList.add(c);
	}

}
