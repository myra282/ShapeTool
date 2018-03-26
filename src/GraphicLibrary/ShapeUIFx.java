package GraphicLibrary;

import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
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
	private ToolBar menu;
	private Button btnSave, btnLoad, btnUndo, btnRedo;
	private Stage pStage;
	
	private static ShapeUIFx instance = null;
	
	/*private ShapeUIFx() {
		borderPane = new BorderPane();
		stage = new Stage();
		board = new StackPane();
		scene = new Scene(board);
		ToolBar menu = new ToolBar();
		HBox statusbar = new HBox();
		borderPane.setTop(menu);
		borderPane.setCenter(board);
		borderPane.setBottom(statusbar);
		board.setPrefSize(250, 200);
	}*/
	
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
				
				instance = this;
			}
		}
	}
	
	public static ShapeUIFx getInstance() {
		return instance;
	}

	private void draw(RegPoly p, Pane pane) {
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
		shape.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				shape.setFill(Color.rgb(0, 200, 0));
			}
		});
	}
	
	private void draw(Rect r, Pane pane) {
		Shape shape = new Rectangle(r.getPosition().getX(), r.getPosition().getY(), r.getWidth(), r.getHeight());
		shape.setFill(Color.rgb(r.getColor().getR(), r.getColor().getG(), r.getColor().getB()));
		shape.setTranslateX(r.getPosition().getX());
		shape.setTranslateY(r.getPosition().getY());
		pane.getChildren().add(shape);
	}
	
	private void draw(ShapeComposite s, Pane pane) {
		for (Iterator<IShape> i = s.getShapes().iterator(); i.hasNext();) {
		    IShape item = i.next();
		    draw(item);
		}
	}
	
	@Override
	public void draw(IShape s) {
		if (s instanceof Rect) {
			draw((Rect) s,board);
		}
		else if (s instanceof RegPoly) {
			draw((RegPoly) s,board);
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
		//pStage.setScene(scene);
	}
	
	/*double w = shape.getLayoutBounds().getWidth();
	double h = shape.getLayoutBounds().getHeight();
	double ratio = w / h;*/

	@Override
	public void start(Stage primaryStage) throws Exception {
		pStage = primaryStage;
		/*borderPane = new BorderPane();
		board = new StackPane();
		toolbar = new ScrollPane();
		toolbar.setContent(new StackPane());
		toolbar.setPannable(true);
		scene = new Scene(borderPane);
		menu = new ToolBar();
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
		btnRedo.setGraphic(imRedo);*/
		
		/*Rect rect = new Rect(new Point(0, 0), 30, 20);
		rect.setColor(new Dye(200,30,30));
		RegPoly poly = new RegPoly(new Point(0, 30), 5, 20);
		poly.setColor(new Dye(30,30,200));
		addTool(rect);
		addTool(poly);*/
		
		HBox statusbar = new HBox();
		board.setStyle("-fx-border-color: black;");
		toolbar.setStyle("-fx-border-color: black;");
		toolbar.setMaxWidth(BAR_MAX_WIDTH);
		toolbar.setMinWidth(BAR_MIN_WIDTH);
		toolbar.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		toolbar.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		toolbar.setFitToWidth(true);
		borderPane.setTop(menu);
		borderPane.setLeft(toolbar);
		borderPane.setCenter(board);
		borderPane.setBottom(statusbar);
		board.setPrefSize(BOARD_WIDTH, BOARD_HEIGHT);
		menu.getItems().addAll(new Separator(), btnSave, btnLoad, new Separator(), btnUndo, btnRedo, new Separator());
		// set app
		primaryStage.setTitle("ShapeOfView");
		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.show();
	}
	
	public Stage getpStage() {
		return pStage;
	}

	@Override
	public void begin() {
		//ShapeUIFx.launch();
		Application.launch(ShapeUIFx.class);
		//launch();
	}

}
