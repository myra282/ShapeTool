package GraphicLibrary;

import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import shape.model.IShapeSimple;
import shape.model.Point;
import shape.model.RegularPolygon;
import shape.model.ShapeComposite;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.Iterator;
import java.util.ListIterator;

import Controller.Controller;

public class ApplicationFx extends Application implements IApplication {
	
	private BorderPane borderPane;
	private StackPane board;
	private ScrollPane toolbar;
	private Scene scene;
	private ToolBar menu, trash;
	private Button btnSave, btnLoad, btnUndo, btnRedo, btnTrash;
	private Stage pStage;
	
	private IShapeSimple shadow;
	private Point eventPoint, gap;
	
	private static ApplicationFx instance = null;
	
	public ApplicationFx() throws Exception {
		synchronized(ApplicationFx.class) {
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
				ImageView imSave = new ImageView(ApplicationFx.class.getResource("/"+"save.png").toString());
				imSave.setFitWidth(20);
				imSave.setPreserveRatio(true);
				btnSave.setGraphic(imSave);
				
				btnLoad = new Button("Load");
				ImageView imLoad = new ImageView(ApplicationFx.class.getResource("/"+"load.png").toString());
				imLoad.setFitWidth(20);
				imLoad.setPreserveRatio(true);
				btnLoad.setGraphic(imLoad);
				
				btnUndo = new Button("Undo");
				ImageView imUndo = new ImageView(ApplicationFx.class.getResource("/"+"undo.png").toString());
				imUndo.setFitWidth(20);
				imUndo.setPreserveRatio(true);
				btnUndo.setGraphic(imUndo);
				
				btnRedo = new Button("Redo");
				ImageView imRedo = new ImageView(ApplicationFx.class.getResource("/"+"redo.png").toString());
				imRedo.setFitWidth(20);
				imRedo.setPreserveRatio(true);
				btnRedo.setGraphic(imRedo);
				
				btnTrash = new Button("");
				ImageView imTrash = new ImageView(ApplicationFx.class.getResource("/"+"trash.png").toString());
				imTrash.setFitWidth(20);
				imTrash.setPreserveRatio(true);
				btnTrash.setGraphic(imTrash);
				btnTrash.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						Controller.getInstance().eraseAll();
						event.consume();
					}
				});
				
				shadow = null;
				eventPoint = null;
				gap = null;
				instance = this;
			}
		}
	}
	
	public static ApplicationFx getInstance() {
		return instance;
	}
	
	private boolean inBoard(Point p) {
		Bounds b = board.localToScene(board.getBoundsInLocal());
		if ((p.getX() > b.getMinX() && p.getX() < b.getMinX() + board.getWidth())
		&& (p.getY() > b.getMinY() && p.getY() < b.getMinY() + board.getHeight())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private Point pointToBoard(double x, double y) {
		Bounds b = board.localToScene(board.getBoundsInLocal());
		Point p = new Point(x - b.getMinX(), y - b.getMinY());
		return p;
	}
	
	/*private Point pointFromBoard(Point pos) {
		Bounds b = board.getBoundsInLocal();
		Point p = new Point(pos.getX() - b.getMinX(), pos.getY() - b.getMinY());
		return p;
	}*/
	
	private boolean inTrash(Point p) {
		Bounds b = btnTrash.localToScene(btnTrash.getBoundsInLocal());
		if ((p.getX() > b.getMinX() && p.getX() < b.getMinX() + btnTrash.getWidth())
		&& (p.getY() > b.getMinY() && p.getY() < b.getMinY() + btnTrash.getHeight())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean inToolbar(Point p) {
		Bounds b = toolbar.getContent().localToScene(toolbar.getContent().getBoundsInLocal());
		if ((p.getX() > b.getMinX() && p.getX() < b.getMinX() + toolbar.getWidth())
		&& (p.getY() > b.getMinY() && p.getY() < b.getMinY() + toolbar.getHeight())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private Point pointToToolbar(double x, double y) {
		Bounds b = toolbar.getContent().localToScene(toolbar.getContent().getBoundsInLocal());
		Point p = new Point(x - b.getMinX(), y - b.getMinY());
		return p;
	}
	
	private Shape draw(shape.model.Rectangle r, Pane pane) {
		Shape sh = new Rectangle(r.getPosition().getX(), r.getPosition().getY(), r.getWidth(), r.getHeight());
		sh.setFill(Color.rgb(r.getColor().getR(), r.getColor().getG(), r.getColor().getB(), r.getColor().getAlpha()));
		sh.setTranslateX(r.getPosition().getX());
		sh.setTranslateY(r.getPosition().getY());
		pane.getChildren().add(sh);
		return sh;
	}
	
	private Shape draw(RegularPolygon r, Pane pane) {
		Shape sh = new Polygon(r.computePoints());
		sh.setFill(Color.rgb(r.getColor().getR(), r.getColor().getG(), r.getColor().getB(), r.getColor().getAlpha()));
		sh.setTranslateX(r.getPosition().getX());
		sh.setTranslateY(r.getPosition().getY());
		pane.getChildren().add(sh);
		return sh;
	}
	
	private void draw(ShapeComposite s, Pane pane) {
		for (Iterator<IShapeSimple> i = s.iterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
		    if (item instanceof shape.model.Rectangle) {
		    	draw((shape.model.Rectangle) item, pane);
		    }
		    else if (item instanceof RegularPolygon) {
		    	draw((RegularPolygon) item, pane);
		    }
		    else if (item instanceof ShapeComposite) {
		    	draw((ShapeComposite) item, pane);
		    }
		}
	}
	
	private void addMenu(Shape sh) {
		// Context Menu
		ContextMenu contextMenu = new ContextMenu();
        MenuItem groupOption = new MenuItem("Group");
        groupOption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller.getInstance().group();
                event.consume();
            }
        });
        MenuItem ungroupOption = new MenuItem("Ungroup");
        ungroupOption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	Point p = new Point(sh.getTranslateX()+sh.getLayoutBounds().getWidth()/2,
            						sh.getTranslateY() + sh.getLayoutBounds().getHeight()/2);
            	Controller.getInstance().select(p);
                Controller.getInstance().ungroup();
                event.consume();
            }
        });
        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(groupOption);
        contextMenu.getItems().addAll(ungroupOption);
        sh.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
        		contextMenu.show(sh, event.getScreenX(), event.getScreenY());
        		event.consume();
            }
        });
	}
	
	public void draw(shape.model.Rectangle s) {
		Shape sh = draw(s,board);
		addMenu(sh);
	}
	
	public void draw(RegularPolygon s) {
		Shape sh = draw(s,board);
		addMenu(sh);
	}
	
	public void draw(ShapeComposite s) {
		for (ListIterator<IShapeSimple> i = ((ShapeComposite) s).iterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
		    if (item instanceof shape.model.Rectangle) {
		    	draw((shape.model.Rectangle) item);
		    }
		    else if (item instanceof RegularPolygon) {
		    	draw((RegularPolygon) item);
		    }
		    if (item instanceof ShapeComposite) {
		    	draw((ShapeComposite) item);
		    }
		}
	}
	
	@Override
	public void addTool(IShapeSimple s) {
		if (s instanceof shape.model.Rectangle) {
			draw((shape.model.Rectangle) s,(StackPane) toolbar.getContent());
		}
		else if (s instanceof RegularPolygon) {
			draw((RegularPolygon) s,(StackPane) toolbar.getContent());
		}
		else if (s instanceof ShapeComposite) {
			draw((ShapeComposite) s,(StackPane) toolbar.getContent());
		}
	}
	
	private void dragNDrop(IShapeSimple s, Pane pane) {
		Shape sh;
		if (s instanceof shape.model.Rectangle) {
			sh = draw((shape.model.Rectangle) s, pane);
		}
		else if (s instanceof RegularPolygon) {
			sh = draw((RegularPolygon) s, pane);
		}
		else {
			sh = null;
		}
		if (sh != null) {
			Color c = (Color) sh.getFill();
			sh.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent dragEvent) {
					if (dragEvent.getEventType() == MouseEvent.MOUSE_DRAGGED  && dragEvent.isPrimaryButtonDown()) {
						sh.setFill(Color.rgb(0, 200, 0));
					}
					dragEvent.consume();
				}
			});
			sh.setOnMouseReleased(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent dropEvent) {
					Point dropped = new Point(dropEvent.getSceneX(), dropEvent.getSceneY());
					if (inBoard(dropped)) {
						Point p = pointToBoard(dropEvent.getSceneX() - sh.getLayoutBounds().getWidth()/2, 
											   dropEvent.getSceneY() - sh.getLayoutBounds().getHeight()/2);
						Controller.getInstance().dragNDrop(s, p);
					}
					sh.setFill(c);
					dropEvent.consume();
				}
			});
		}
	}
	
	public void dragNDrop(IShapeSimple s) {		
		if(s instanceof RegularPolygon) {
			dragNDrop((RegularPolygon) s,(StackPane) toolbar.getContent());
		}
		else if (s instanceof shape.model.Rectangle) {
			dragNDrop((shape.model.Rectangle) s, (StackPane) toolbar.getContent());
		}
	}
	
	@Override
	public void clear() {
		board.getChildren().clear();
		((StackPane) toolbar.getContent()).getChildren().clear();
	}
	
	public void addEvents() {
		addBoardEvents();
		addToolbarEvents();
	}
	
	private void addBoardEvents() {
		board.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Point tmp = pointToBoard(event.getSceneX(), event.getSceneY());
				if (eventPoint == null) {
					eventPoint = new Point(tmp.getX(), tmp.getY());
					if (shadow == null) {
						IShapeSimple s = Controller.getInstance().getShapeFromPoint(eventPoint);
						if (s != null) {
							shadow = s.clone();
							GraphicLibrary.Color c = shadow.getColor().clone();
							c.setAlpha(0.1);
							shadow.setColor(c);
							gap = new Point(eventPoint.getX() - s.getPosition().getX(), 
											eventPoint.getY() - s.getPosition().getY());
						}
					}
				}
				if (shadow != null && inBoard(new Point(event.getSceneX(), event.getSceneY()))) {
					Controller.getInstance().redraw();
					shadow.setPosition(new Point(tmp.getX() - gap.getX(), tmp.getY() - gap.getY()));
					if (shadow instanceof shape.model.Rectangle) {
				    	draw((shape.model.Rectangle) shadow);
				    }
				    else if (shadow instanceof RegularPolygon) {
				    	draw((RegularPolygon) shadow);
				    }
				    else if (shadow instanceof ShapeComposite) {
				    	draw((ShapeComposite) shadow, board);
				    }
				}
			}
		});
		board.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				boolean mouseKey = event.getButton().compareTo(MouseButton.PRIMARY) == 0;
				Point dropped = new Point(event.getSceneX(),event.getSceneY());
				if (inBoard(dropped)) {
					Point p2 = pointToBoard(event.getSceneX(), event.getSceneY());
					Controller.getInstance().handleMouseEvent(eventPoint, p2, mouseKey);
				}
				else if (inTrash(dropped)) {
					Controller.getInstance().handleTrashEvent(eventPoint, mouseKey);
				}
				else if (inToolbar(dropped)) {
					Point p2 = pointToToolbar(event.getSceneX(), event.getSceneY());
					Controller.getInstance().handleNewToolEvent(eventPoint, p2, mouseKey);
				}
				shadow = null;
				eventPoint = null;
				gap = null;
			}
		});
	}
	
	private void addToolbarEvents() {
		toolbar.getContent().setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Point tmp = pointToToolbar(event.getSceneX(), event.getSceneY());
				if (eventPoint == null) {
					eventPoint = new Point(tmp.getX(), tmp.getY());
					if (shadow == null) {
						IShapeSimple s = Controller.getInstance().getToolFromPoint(eventPoint);
						if (s != null) {
							shadow = s.clone();
							GraphicLibrary.Color c = shadow.getColor().clone();
							c.setAlpha(0.1);
							shadow.setColor(c);
							gap = new Point(eventPoint.getX() - s.getPosition().getX(), 
											eventPoint.getY() - s.getPosition().getY());
						}
					}
				}
				if (shadow != null && inToolbar(new Point(event.getSceneX(), event.getSceneY()))) {
					Controller.getInstance().redraw();
					shadow.setPosition(new Point(tmp.getX() - gap.getX(), tmp.getY() - gap.getY()));
					if (shadow instanceof shape.model.Rectangle) {
				    	addTool((shape.model.Rectangle) shadow);
				    }
				    else if (shadow instanceof RegularPolygon) {
				    	addTool((RegularPolygon) shadow);
				    }
				    else if (shadow instanceof ShapeComposite) {
				    	addTool((ShapeComposite) shadow);
				    }
				}
			}
		});
		toolbar.getContent().setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				boolean mouseKey = event.getButton().compareTo(MouseButton.PRIMARY) == 0;
				Point dropped = new Point(event.getSceneX(),event.getSceneY());
				if (inToolbar(dropped)) {
					Point p2 = pointToToolbar(event.getSceneX(), event.getSceneY());
					Controller.getInstance().handleMouseToolEvent(eventPoint, p2, mouseKey);
				}
				else if (inTrash(dropped)) {
					Controller.getInstance().handleTrashToolEvent(eventPoint, mouseKey);
				}
				else if (inBoard(dropped)) {
					Point p2 = pointToBoard(event.getSceneX(), event.getSceneY());
					Controller.getInstance().handleDragToolEvent(eventPoint, p2, mouseKey);
				}
				shadow = null;
				eventPoint = null;
				gap = null;
			}
		});
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
		((StackPane) toolbar.getContent()).setAlignment(Pos.TOP_LEFT);
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
		
		addEvents();
		
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
		Application.launch(ApplicationFx.class);
	}

		



	

}
