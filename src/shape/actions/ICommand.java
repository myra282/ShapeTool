package shape.actions;

/**
 * 
 * @author Mary Pascal & Marc Saint-Jean-Clergeau <br><br>
 *
 * This interface depicts a Command
 * @see ICommand
 * 
 */
public interface ICommand {
	
	public void execute();
	
	public void unexecute();
	
}
