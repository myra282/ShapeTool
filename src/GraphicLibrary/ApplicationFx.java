package GraphicLibrary;

import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import shape.model.IShapeSimple;
import shape.model.Point;
import shape.model.RegularPolygon;
import shape.model.ShapeComposite;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
				instance = this;
			}
		}
	}
	
	public static ApplicationFx getInstance() {
		return instance;
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
		return p;
	}
	
	private Point pointFromBoard(Point pos) {
		Bounds b = board.getBoundsInLocal();
		Point p = new Point(pos.getX() - b.getMinX(), pos.getY() - b.getMinY());
		return p;
	}
	
	private boolean inTrash(double x, double y) {
		Bounds b = btnTrash.localToScene(btnTrash.getBoundsInLocal());
		if ((x > b.getMinX() && x < b.getMinX() + btnTrash.getWidth())
		&& (y > b.getMinY() && y < b.getMinY() + btnTrash.getHeight())) {
			return true;
		}
		else {
			return false;
		}
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
		for (Iterator<IShapeSimple> i = s.getShapes().iterator(); i.hasNext();) {
		    IShapeSimple item = i.next();
		    if (item instanceof Rectangle) {
		    	draw((shape.model.Rectangle) item, pane);
		    }
		    else if (item instanceof RegularPolygon) {
		    	draw((RegularPolygon) item, pane);
		    }
		    if (item instanceof ShapeComposite) {
		    	draw((ShapeComposite) item, pane);
		    }
		}
	}
	
	public void draw(shape.model.Rectangle s) {
		draw(s,board);
	}
	
	public void draw(RegularPolygon s) {
		draw(s,board);
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
		else {
			sh = draw((RegularPolygon) s, pane);
		}
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
				if (inBoard(dropEvent.getSceneX(), dropEvent.getSceneY())) {
					Point p = pointToBoard(dropEvent.getSceneX(), dropEvent.getSceneY());
					Controller.getInstance().dragNDrop(s, p);
				}
				sh.setFill(c);
				dropEvent.consume();
			}
		});
	}
	
//	private void dragNDrop(IShapeSimple s, Pane pane) {
//		Shape sh;
//		if(s instanceof shape.model.Rectangle) sh = draw((shape.model.Rectangle) s, pane);
//		else  sh = draw((RegularPolygon) s, pane); 
//		sh.setOnD(new EventHandler<DragEvent>() {
//		    @Override 
//		    public void handle(DragEvent event) {
//		    	System.out.println("drag: ");
//		        sh.setFill(Color.rgb(0, 200, 0));
//	            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
//		        event.consume();
//		    }
//		});
//
//		sh.setOnDragDropped(new EventHandler<DragEvent>() {
//			@Override
//		    public void handle(DragEvent event) {
//		        Dragboard db = event.getDragboard();
//		        boolean success = false;
//		        if (db.hasString()) {
//		            System.out.println("Dropped: " + db.getString());
//		            success = true;
//		            if (inBoard(event.getSceneX(), event.getSceneY())) {
//						Point p = pointToBoard(event.getSceneX(), event.getSceneY());
//						System.out.println(p.getX()+","+p.getY());
//						Controller.getInstance().dragNDrop(s, p);
//		            }
//		        }
//		        event.setDropCompleted(success);
//		        event.consume();
//		    }
//		});
//		
//			
//	}
	
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
	}
	
	public void addEvents() {
		// for selection
		board.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent draggedEvent) {
				for (Node sh : board.getChildren()) {
					if (sh instanceof Shape) {
							// Drag and Move
							sh.setOnMouseDragged(new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent dragEvent) {
									if (dragEvent.getEventType() == MouseEvent.MOUSE_DRAGGED && dragEvent.isPrimaryButtonDown()) {
										sh.setOnMouseReleased(new EventHandler<MouseEvent>() {
											@Override
											public void handle(MouseEvent dropEvent) {
												if (inTrash(dropEvent.getSceneX(), dropEvent.getSceneY())) {
													//Controller.getInstance().erase(s);
												}
												else if (inBoard(dropEvent.getSceneX(), dropEvent.getSceneY())) {
													Point p = pointToBoard(dropEvent.getSceneX(), dropEvent.getSceneY());
													sh.setTranslateX(p.getX());
													sh.setTranslateY(p.getY());
												}
												dropEvent.consume();
												draggedEvent.consume();
											}
										});
									}
									dragEvent.consume();
								}
							});
						}
						// Context Menu
						if (!draggedEvent.isConsumed()) {
							ContextMenu contextMenu = new ContextMenu();
					        MenuItem groupOption = new MenuItem("Group");
					        groupOption.setOnAction(new EventHandler<ActionEvent>() {
					            @Override
					            public void handle(ActionEvent event) {
					                Controller.getInstance().group();
					                event.consume();
					            }
					        });
					        // Add MenuItem to ContextMenu
					        contextMenu.getItems().addAll(groupOption);
					        sh.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
					            @Override
					            public void handle(ContextMenuEvent event) {
				            		contextMenu.show(sh, event.getScreenX(), event.getScreenY());
				            		event.consume();
					            }
					        });
					        draggedEvent.consume();
						}
					}
				//
				if (!draggedEvent.isConsumed()) {
					System.out.println("ca select ?");
					if (draggedEvent.getEventType() == MouseEvent.DRAG_DETECTED && draggedEvent.isPrimaryButtonDown()) {
						System.out.println("ca select !");
						double dragX = draggedEvent.getSceneX();
						double dragY = draggedEvent.getSceneY();
						board.setOnMouseReleased(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent dropEvent) {
								double dropX = dropEvent.getSceneX();
								double dropY = dropEvent.getSceneY();
								if (inBoard(dragX, dragY)
								&& inBoard(dropX, dropY)) {
									Controller.getInstance().select(pointToBoard(draggedEvent.getSceneX(), draggedEvent.getSceneY()), 
																    pointToBoard(dropEvent.getSceneX(), dropEvent.getSceneY()));
								}
								dropEvent.consume();
							}
						});
					}
					draggedEvent.consume();
				}
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
		
		//addEvents();
		
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
