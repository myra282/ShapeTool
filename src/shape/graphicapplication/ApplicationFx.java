package shape.graphicapplication;

import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import shape.control.Controller;
import shape.model.IShape;
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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ApplicationFx extends Application implements IApplication {
	
	private BorderPane borderPane;
	private StackPane board;
	private ScrollPane toolbar;
	private Scene scene;
	private ToolBar menu, trash;
	private Button btnSave, btnLoad, btnUndo, btnRedo, btnTrash;
	private Stage pStage;
	
	private IShape shadow;
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
				trash = new ToolBar();
				
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
				btnUndo.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						Controller.getInstance().undo();
						updateUI();
						event.consume();
					}
				});
				
				btnRedo = new Button("Redo");
				ImageView imRedo = new ImageView(ApplicationFx.class.getResource("/"+"redo.png").toString());
				imRedo.setFitWidth(20);
				imRedo.setPreserveRatio(true);
				btnRedo.setGraphic(imRedo);
				btnRedo.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						Controller.getInstance().redo();
						updateUI();
						event.consume();
					}
				});
				
				btnTrash = new Button("");
				ImageView imTrash = new ImageView(ApplicationFx.class.getResource("/"+"trash.png").toString());
				imTrash.setFitWidth(20);
				imTrash.setPreserveRatio(true);
				btnTrash.setGraphic(imTrash);
				btnTrash.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						Controller.getInstance().eraseAll();
						updateUI();
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
	
	private void addMenu(Shape sh, IShape s) {
		// Context Menu
		ContextMenu contextMenu = new ContextMenu();
        MenuItem groupOption = new MenuItem("Group");
        groupOption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller.getInstance().group();
                updateUI();
                event.consume();
            }
        });
        contextMenu.getItems().add(groupOption);
        MenuItem ungroupOption = new MenuItem("Ungroup");
        ungroupOption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	Point p = new Point(sh.getTranslateX()+sh.getLayoutBounds().getWidth()/2,
            						sh.getTranslateY() + sh.getLayoutBounds().getHeight()/2);
            	Controller.getInstance().select(p);
                Controller.getInstance().ungroup();
                updateUI();
                event.consume();
            }
        });
        contextMenu.getItems().add(ungroupOption);
        if (s instanceof shape.model.Rectangle) {
	        MenuItem roundedOption = new MenuItem("Rounded");
	        roundedOption.setOnAction(new EventHandler<ActionEvent>() {
	            @Override public void handle(ActionEvent e) {
	            	Controller.getInstance().roundCorners((shape.model.Rectangle) s);
	            	updateUI();
	            }
	        });
	        contextMenu.getItems().add(roundedOption);
		}
        if (!(s instanceof ShapeComposite)) {
        	String name1, name2;
        	double d1, d2;
        	if (s instanceof shape.model.Rectangle) {
        		name1 = "Width : ";
        		d1 = ((shape.model.Rectangle) s).getWidth();
        		name2 = "Height : ";
        		d2 = ((shape.model.Rectangle) s).getHeight();
        	}
        	else {
        		name1 = "Edges : ";
        		d1 = ((RegularPolygon) s).getNbEdges();
        		name2 = "Width : ";
        		d2 = ((RegularPolygon) s).getEdgeWidth();
        	}
	        MenuItem attrOption = new MenuItem("Attributes");
	        attrOption.setOnAction(new EventHandler<ActionEvent>() {
	            @SuppressWarnings({ "rawtypes", "unchecked" })
				@Override
	            public void handle(ActionEvent event) {
	            	// Text formatter
	            	Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");
	            	UnaryOperator<TextFormatter.Change> filter = c -> {
	            	    String text = c.getControlNewText();
	            	    if (validEditingState.matcher(text).matches()) {
	            	        return c ;
	            	    } else {
	            	        return null ;
	            	    }
	            	};
	            	StringConverter<Double> converter = new StringConverter<Double>() {
	            	    @Override
	            	    public Double fromString(String s) {
	            	        if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
	            	            return 0.0 ;
	            	        } else {
	            	            return Double.valueOf(s);
	            	        }
	            	    }
	            	    @Override
	            	    public String toString(Double d) {
	            	        return d.toString();
	            	    }
	            	};
	            	TextFormatter<Double> textFormatterx = new TextFormatter<>(converter, s.getPosition().getX(), filter);
	            	TextFormatter<Double> textFormattery = new TextFormatter<>(converter, s.getPosition().getY(), filter);
	            	TextFormatter<Double> textFormatter1 = new TextFormatter<>(converter, d1, filter);
	            	TextFormatter<Double> textFormatter2 = new TextFormatter<>(converter, d2, filter);
	            	TextFormatter<Double> textFormatterr = new TextFormatter<>(converter, s.getRotation(), filter);
	            	TextFormatter<Double> textFormattercx = new TextFormatter<>(converter, s.getRotationCenter().getX(), filter);
	            	TextFormatter<Double> textFormattercy = new TextFormatter<>(converter, s.getRotationCenter().getY(), filter);
	            	TextFormatter<Double> textFormatterop = new TextFormatter<>(converter, s.getColor().getAlpha(), filter);
	            	// Dialog
	            	Dialog<shape.model.Rectangle> dialog = new Dialog<>();
	        		dialog.setTitle("Attributes Editor");
	        		dialog.setHeaderText("Here you can modify the shape attributes");
	        		Label labelPos = new Label("Position : ");
	        		Label label1 = new Label(name1);
	    			Label label2 = new Label(name2);
	    			Label labelr = new Label("Rotation : ");
	    			Label labelCtr = new Label("Rotation center : ");
	    			Label labelOp = new Label("Opacity : ");
	    			TextField textx = new TextField();
	    			TextField texty = new TextField();
	    			TextField text1 = new TextField();
	    			TextField text2 = new TextField();
	    			TextField textr = new TextField();
	    			TextField textcx = new TextField();
	    			TextField textcy = new TextField();
	    			TextField textop = new TextField();
	    			textx.setTextFormatter(textFormatterx);
	    			texty.setTextFormatter(textFormattery);
	    			text1.setTextFormatter(textFormatter1);
	    			text2.setTextFormatter(textFormatter2);
	    			textr.setTextFormatter(textFormatterr);
	    			textcx.setTextFormatter(textFormattercx);
	    			textcy.setTextFormatter(textFormattercy);
	    			textop.setTextFormatter(textFormatterop);
	    			shape.graphicapplication.Color color = s.getColor().clone();
	    			ColorPicker colorPicker = new ColorPicker(Color.rgb(color.getR(), color.getG(), color.getB()));
	    			 colorPicker.setOnAction(new EventHandler() {
						@Override
						public void handle(Event event) {
							Color c = colorPicker.getValue();
							color.setR((int) (c.getRed() * 255));
							color.setG((int) (c.getGreen() * 255));
							color.setB((int) (c.getBlue() * 255));
						}
	    			 });
	    			GridPane grid = new GridPane();
	    			grid.add(labelPos, 1, 1);
	    			grid.add(textx, 2, 1);
	    			grid.add(texty, 3, 1);
	    			grid.add(label1, 1, 2);
	    			grid.add(text1, 2, 2);
	    			grid.add(label2, 1, 3);
	    			grid.add(text2, 2, 3);
	    			grid.add(labelr, 1, 4);
	    			grid.add(textr, 2, 4);
	    			grid.add(labelCtr, 1, 5);
	    			grid.add(textcx, 2, 5);
	    			grid.add(textcy, 3, 5);
	    			grid.add(labelOp, 1, 6);
	    			grid.add(textop, 2, 6);
	    			grid.add(colorPicker, 2, 7);
	    			dialog.getDialogPane().setContent(grid);
	    			ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
	    			dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
	        		dialog.setResultConverter(new Callback<ButtonType,shape.model.Rectangle>() {
						@Override
						public shape.model.Rectangle call(ButtonType param) {
							if (param == buttonTypeOk) {
								shape.model.Rectangle res = new shape.model.Rectangle(new Point(Double.parseDouble(textx.getText()), 
								Double.parseDouble(texty.getText())), Double.parseDouble(text1.getText()), Double.parseDouble(text2.getText()));
								res.setRotation(Double.parseDouble(textr.getText()));
								res.setRotationCenter(new Point(Double.parseDouble(textcx.getText()), Double.parseDouble(textcy.getText())));
								color.setAlpha(Double.parseDouble(textop.getText()));
								res.setColor(color);
								return res;
							}
							return null;
						}
	        		});
	        		Optional<shape.model.Rectangle> result = dialog.showAndWait();
	        		if (result.isPresent()) {
	        			Controller.getInstance().changeAttributes(s, result.get());
	        		}
	                updateUI();
	                event.consume();
	            }
	        });
	        contextMenu.getItems().add(attrOption);
        }
        // Add MenuItem to ContextMenu
        sh.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
        		contextMenu.show(sh, event.getScreenX(), event.getScreenY());
        		event.consume();
            }
        });
	}
	
	private Shape draw(shape.model.Rectangle r, Pane pane) {
		Rectangle sh = new Rectangle(r.getWidth(), r.getHeight());
		sh.setFill(Color.rgb(r.getColor().getR(), r.getColor().getG(), r.getColor().getB(), r.getColor().getAlpha()));
		if (r.getRounded()) {
			sh.setArcWidth(ROUNDED_VALUE);
			sh.setArcHeight(ROUNDED_VALUE);
		}
		sh.setTranslateX(r.getPosition().getX());
		sh.setTranslateY(r.getPosition().getY());
		sh.getTransforms().add(new Rotate(r.getRotation(), r.getRotationCenter().getX(), r.getRotationCenter().getY()));
		pane.getChildren().add(sh);
		return sh;
	}
	
	private Shape draw(RegularPolygon r, Pane pane) {
		Polygon sh = new Polygon(r.computePoints());
		sh.setFill(Color.rgb(r.getColor().getR(), r.getColor().getG(), r.getColor().getB(), r.getColor().getAlpha()));
		sh.setTranslateX(r.getPosition().getX());
		sh.setTranslateY(r.getPosition().getY());
		sh.getTransforms().add(new Rotate(r.getRotation(), r.getRotationCenter().getX() + r.getRadius(), r.getRotationCenter().getY() + r.getRadius()));
		pane.getChildren().add(sh);
		return sh;
	}
	
	private void draw(ShapeComposite s, Pane pane) {
		for (Iterator<IShape> i = s.iterator(); i.hasNext();) {
		    IShape item = i.next();
		    if (item instanceof shape.model.Rectangle) {
		    	Shape sh = draw((shape.model.Rectangle) item, pane);
		    	addMenu(sh, s);
		    }
		    else if (item instanceof RegularPolygon) {
		    	Shape sh = draw((RegularPolygon) item, pane);
		    	addMenu(sh, s);
		    }
		    else if (item instanceof ShapeComposite) {
		    	draw((ShapeComposite) item, pane);
		    }
		}
	}
	
	public void draw(IShape s) {
		if (s instanceof shape.model.Rectangle) {
			addMenu(draw((shape.model.Rectangle) s, board), s);
	    }
	    else if (s instanceof RegularPolygon) {
	    	addMenu(draw((RegularPolygon) s, board), s);
	    }
	    else if (s instanceof ShapeComposite) {
	    	draw((ShapeComposite) s, board);
	    }
	}
	
	@Override
	public void clear() {
		board.getChildren().clear();
		((StackPane) toolbar.getContent()).getChildren().clear();
		for (ListIterator<Node> i = borderPane.getChildren().listIterator(); i.hasNext();) {
		    Node item = i.next();
		    if (item instanceof Shape) {
				i.remove();
			}
		}
	}
	
	private void updateUI() {
		btnUndo.setDisable(!Controller.getInstance().canUndo());
		btnRedo.setDisable(!Controller.getInstance().canRedo());
	}
	
	private void selectionZone(Point p1, Point p2) {
		double minx, miny, maxx, maxy;
		if (p1.getX() < p2.getX()) {
			minx = p1.getX();
			maxx = p2.getX();
		}
		else {
			minx = p2.getX();
			maxx = p1.getX();
		}
		if (p1.getY() < p2.getY()) {
			miny = p1.getY();
			maxy = p2.getY();
		}
		else {
			miny = p2.getY();
			maxy = p1.getY();
		}
		Rectangle selectionZone = new Rectangle(minx, miny, maxx - minx, maxy - miny);
		selectionZone.getStrokeDashArray().add(5.0);
		selectionZone.setTranslateX(minx);
		selectionZone.setTranslateY(miny);
		selectionZone.setStroke(Color.DARKGREY);
		selectionZone.setFill(Color.TRANSPARENT);
		board.getChildren().add(selectionZone);
	}
	
	@Override
	public void addTool(IShape s) {
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
	
	
	public void addEvents() {
		addBoardEvents();
		addToolbarEvents();
	}
	
	private void addBoardEvents() {
		board.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().compareTo(MouseButton.PRIMARY) == 0) {
					Point tmp = pointToBoard(event.getSceneX(), event.getSceneY());
					if (eventPoint == null) {
						eventPoint = new Point(tmp.getX(), tmp.getY());
						if (shadow == null) {
							IShape s = Controller.getInstance().getShapeFromPoint(eventPoint);
							if (s != null) {
								shadow = s.clone();
								shape.graphicapplication.Color c = shadow.getColor().clone();
								c.setAlpha(0.1);
								shadow.setColor(c);
								gap = new Point(eventPoint.getX() - s.getPosition().getX(), 
												eventPoint.getY() - s.getPosition().getY());
							}
						}
					}
					Controller.getInstance().redraw();
					if (shadow != null) {
						shadow.setPosition(new Point(event.getSceneX() - gap.getX(), event.getSceneY() - gap.getY()));
						if (shadow instanceof shape.model.Rectangle) {
					    	draw((shape.model.Rectangle) shadow, borderPane).setMouseTransparent(true);
					    }
					    else if (shadow instanceof RegularPolygon) {
					    	draw((RegularPolygon) shadow, borderPane).setMouseTransparent(true);
					    }
					    else if (shadow instanceof ShapeComposite) {
					    	draw((ShapeComposite) shadow, borderPane);
					    }
					}
					else if (shadow == null) {
						selectionZone(eventPoint, tmp);
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
				updateUI();
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
				if (event.getButton().compareTo(MouseButton.PRIMARY) == 0) {
					Point tmp = pointToToolbar(event.getSceneX(), event.getSceneY());
					if (eventPoint == null) {
						eventPoint = new Point(tmp.getX(), tmp.getY());
						if (shadow == null) {
							IShape s = Controller.getInstance().getToolFromPoint(eventPoint);
							if (s != null) {
								shadow = s.clone();
								shape.graphicapplication.Color c = shadow.getColor().clone();
								c.setAlpha(0.1);
								shadow.setColor(c);
								gap = new Point(eventPoint.getX() - s.getPosition().getX(), 
												eventPoint.getY() - s.getPosition().getY());
							}
						}
					}
					if (shadow != null) {
						Controller.getInstance().redraw();
						shadow.setPosition(new Point(event.getSceneX() - gap.getX(), event.getSceneY() - gap.getY()));
						if (shadow instanceof shape.model.Rectangle) {
					    	draw((shape.model.Rectangle) shadow, borderPane).setMouseTransparent(true);
					    }
					    else if (shadow instanceof RegularPolygon) {
					    	draw((RegularPolygon) shadow, borderPane).setMouseTransparent(true);
					    }
					    else if (shadow instanceof ShapeComposite) {
					    	draw((ShapeComposite) shadow, borderPane);
					    }
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
				updateUI();
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
		updateUI();
		
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
