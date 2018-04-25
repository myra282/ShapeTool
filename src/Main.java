import javafx.application.Application;
import shape.control.Mediator;
import shape.graphicapplication.ApplicationFx;

public class Main {

	public static void main(String[] args) {
		Mediator mediator = Mediator.getInstance();
		/*Thread t1 = new Thread() {
            @Override
            public void run() {
            	Application.launch(ApplicationFx.class);
            }
        };
        t1.start();
        while (!t1.isAlive() || ApplicationFx.getInstance() == null) {
        	// Wait for initialisation
        }*/
		mediator.setIApplication(ApplicationFx.getInstance());
		System.out.println("coucou");
		mediator.begin();
	}

}
