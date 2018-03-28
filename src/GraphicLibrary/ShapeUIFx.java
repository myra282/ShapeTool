package GraphicLibrary;

import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import Shape.RegPoly;
import Shape.ShapeComposite;
import java.util.Iterator;

import Controller.Controller;
import Shape.IShape;
import Shape.Point;
import Shape.Rect;

public class ShapeUIFx extends Application implements IShapeUI {
	
	private static final double BOARD_WIDTH = 1000;
	private static final double BOARD_HEIGHT = 800;
	private static final double BAR_MIN_WIDTH = 70;
	private static final double BAR_MAX_WIDTH = 140;
	
	private BorderPane borderPane;
	private StackPane board;
	private ScrollPane toolbar;
	private Scene scene;
	private ToolBar menu, trash;
	private Button btnSave, btnLoad, btnUndo, btnRedo, btnTrash;
	private Stage pStage;
	
	private static ShapeUIFx instance = null;
	
	public ShapeUIFx() throws Exception {
		synchronized(ShapeUIFx.class) {
			if (instance != null) {
				throw new UnsupportedOperationException(getClass()+" is a singleton but constructor was called multiple times !");
			}
			else {
				borderPane = new BorderPane();
				board = new StackPane();
				toolbar = new ScrollPane();
				toolbar.setContent(new StackPane());
				toolbar.setPannable(true);
				scene = new Scene(borderPane);
				menu = new ToolBar();
				trash = new ToolBar(); // Pour la poubelle en bas
				
				btnSave = new Button("Save");
				ImageView imSave = new ImageView(ShapeUIFx.class.getResource("/"+"save.png").toString());
				imSave.setFitWidth(20);
				imSave.setPreserveRatio(true);
				btnSave.setGraphic(imSave);
				
				btnLoad = new Button("Load");
				ImageView imLoad = new ImageView(ShapeUIFx.class.getResource("/"+"load.png").toString());
				imLoad.setFitWidth(20);
				imLoad.setPreserveRatio(true);
				btnLoad.setGraphic(imLoad);
				
				btnUndo = new Button("Undo");
				ImageView imUndo = new ImageView(ShapeUIFx.class.getResource("/"+"undo.png").toString());
				imUndo.setFitWidth(20);
				imUndo.setPreserveRatio(true);
				btnUndo.setGraphic(imUndo);
				
				btnRedo = new Button("Redo");
				ImageView imRedo = new ImageView(ShapeUIFx.class.getResource("/"+"redo.png").toString());
				imRedo.setFitWidth(20);
				imRedo.setPreserveRatio(true);
				btnRedo.setGraphic(imRedo);
				
				btnTrash = new Button("");
				ImageView imTrash = new ImageView(ShapeUIFx.class.getResource("/"+"trash.png").toString());
				imTrash.setFitWidth(20);
				imTrash.setPreserveRatio(true);
				btnTrash.setGraphic(imTrash);
				btnTrash.setOnDragDone(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent event) {
						((Shape) event.getGestureSource()).setFill(Color.rgb(0, 200, 0));
						event.acceptTransferModes(TransferMode.COPY_OR_MOVE);

					}				
				});
				
				instance = this;
			}
		}
	}
	
	public static ShapeUIFx getInstance() {
		return instance;
	}

	private Shape draw(RegPoly p, Pane pane) {
		double points[] = new double[p.getNbEdges() * 2];
		double angle = 0;
		double inc = 360 / p.getNbEdges();
		for (int i = 0 ; i < p.getNbEdges() ; ++i) {
			points[2 * i] = p.getRotationCenter().getX() + (p.getRadius() * Math.cos(Math.toRadians(angle)));
			points[2 * i + 1] = p.getRotationCenter().getY() + (p.getRadius() * Math.sin(Math.toRadians(angle)));
			angle += inc;
		}
		Shape shape = new Polygon(points);
		shape.setFill(Color.rgb(p.getColor().getR(), p.getColor().getG(), p.getColor().getB()));
		shape.setTranslateX(p.getPosition().getX());
		shape.setTranslateY(p.getPosition().getY());
		pane.getChildren().add(shape);
		
		return shape;
		
	}
	
	private Shape draw(Rect r, Pane pane) {
		Shape shape = new Rectangle(r.getPosition().getX(), r.getPosition().getY(), r.getWidth(), r.getHeight());
		shape.setFill(Color.rgb(r.getColor().getR(), r.getColor().getG(), r.getColor().getB()));
		shape.setTranslateX(r.getPosition().getX());
		shape.setTranslateY(r.getPosition().getY());
		pane.getChildren().add(shape);
		return shape;
	}
	
	private void draw(ShapeComposite s, Pane pane) {
		for (Iterator<IShape> i = s.getShapes().iterator(); i.hasNext();) {
		    IShape item = i.next();
		    draw(item);
		}
	}
	
	@Override
	public void draw(IShape s) {
		Shape sh;
		if (s instanceof Rect) {
			sh = draw((Rect) s,board);
		}
		else if (s instanceof RegPoly) {
			sh = draw((RegPoly) s,board);

		}
		else if (s instanceof ShapeComposite) {
			draw((ShapeComposite) s,board);
		}
	}
	
	@Override
	public void addTool(IShape s) {
		if (s instanceof Rect) {
			draw((Rect) s,(StackPane) toolbar.getContent());
		}
		else if (s instanceof RegPoly) {
			draw((RegPoly) s,(StackPane) toolbar.getContent());
		}
		else if (s instanceof ShapeComposite) {
			draw((ShapeComposite) s,(StackPane) toolbar.getContent());
		}
	}
	
	private boolean inBoard(double x, double y) {
		Bounds b = board.localToScene(board.getBoundsInLocal());
		if ((x > b.getMinX() && x < b.getMinX() + board.getWidth())
		&& (y > b.getMinY() && y < b.getMinY() + board.getHeight())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private Point pointToBoard(double x, double y) {
		Bounds b = board.localToScene(board.getBoundsInLocal());
		Point p = new Point(x - b.getMinX(), y - b.getMinY());
		System.out.println(p.getX()+" , "+p.getY());
		return p;
	}
	
	private void dragNDrop(RegPoly rp, Pane pane) {
		Shape s = draw(rp,pane);
		Color c = (Color) s.getFill();
		s.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent dragEvent) {
				s.setFill(Color.rgb(0, 200, 0));
				s.setOnMouseReleased(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent dropEvent) {
						if (inBoard(dropEvent.getSceneX(), dropEvent.getSceneY())) {
							Point p = pointToBoard(dropEvent.getSceneX(), dropEvent.getSceneY());
							Controller.getInstance().dragNDrop(rp, p);
							
						}
						s.setFill(c);
					}
				});
				//notify
			}
		});
	}
	
	private void dragNDrop(Rect r, Pane pane) {
		Shape s = draw((Rect) r, pane);
		Color c = (Color) s.getFill();
		s.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent dragEvent) {
				s.setFill(Color.rgb(0, 200, 0));
				s.setOnMouseReleased(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent dropEvent) {
						if (inBoard(dropEvent.getSceneX(), dropEvent.getSceneY())) {
							Point p = pointToBoard(dropEvent.getSceneX(), dropEvent.getSceneY());
							Controller.getInstance().dragNDrop(r, p);
							
						}
						s.setFill(c);
					}
				});
				//notify
			}
		});
	}
	
	public void dragNDrop(IShape s) {		
		if(s instanceof RegPoly) {
			dragNDrop((RegPoly) s,(StackPane) toolbar.getContent());
		}
		else if (s instanceof Rect) {
			dragNDrop((Rect) s, (StackPane) toolbar.getContent());
		}
	}
	
	@Override
	public void clear() {
		board.getChildren().clear();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		pStage = primaryStage;
		
		HBox statusbar = new HBox();
		board.setStyle("-fx-border-color: black;");
		toolbar.setStyle("-fx-border-color: black;");
		toolbar.setMaxWidth(BAR_MAX_WIDTH);
		toolbar.setMinWidth(BAR_MAX_WIDTH);
		toolbar.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		toolbar.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		toolbar.setFitToWidth(true);
		borderPane.setTop(menu);
		borderPane.setLeft(toolbar);
		borderPane.setCenter(board);
		borderPane.setBottom(statusbar);
		borderPane.setBottom(trash);
		board.setPrefSize(BOARD_WIDTH, BOARD_HEIGHT);
		board.resize(BOARD_WIDTH, BOARD_HEIGHT);
		board.setAlignment(Pos.TOP_LEFT);
		trash.getItems().add(btnTrash);
		trash.setMinWidth(BAR_MIN_WIDTH);
		trash.setMaxWidth(BAR_MIN_WIDTH);
		menu.getItems().addAll(new Separator(), btnSave, btnLoad, new Separator(), btnUndo, btnRedo, new Separator());
		
		// set app
		primaryStage.setTitle("ShapeOfView");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public Stage getpStage() {
		return pStage;
	}

	@Override
	public void begin() {
		Application.launch(ShapeUIFx.class);
	}

		



	

}
