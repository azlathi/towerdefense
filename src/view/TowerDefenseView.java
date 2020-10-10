package view;

import java.io.File;
import java.io.IOException;
import java.util.*;

import constants.Constants;
import constants.TowerType;
import controller.TowerDefenseController;
import exceptions.InvalidPlacementException;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import messages.*;
import models.Enemy;
import models.SpriteAnimation;

import javax.sound.sampled.*;

public class TowerDefenseView extends Application implements Observer {

	//Towers
	private String tower1 = Constants.TOWER1;
	private String tower2 = Constants.TOWER2;
	private String tower3 = Constants.TOWER3;
	private String tower4 = Constants.TOWER4;
	private String tower5 = Constants.TOWER5;
	private String tower6 = Constants.TOWER6;

	// images
	private String MACHINEGUN = Constants.MACHINEGUN;
	private String BASIC = Constants.BASIC;
	private String ICE = Constants.ICE;
	private String SNIPER = Constants.SNIPER;
	private String PITFALL = Constants.PITFALL;
	private String TANK = Constants.TANK;
	private String grass = Constants.GROUND;
	private String path = Constants.PATH;
	private String base = Constants.BASE;
	private String enemyBase = Constants.ENEMYBASE;
	private String cancel = Constants.CANCEL;
	// using in layout
	private Stage mainStage = new Stage();
	private Scene gameScene;
	private Scene menuScene;
	private BorderPane menuPane = new BorderPane();
	private BorderPane mainPane = new BorderPane();
	private BorderPane titlePane = new BorderPane();
	private GridPane stageSelect = new GridPane();
	private GridPane boardPane = new GridPane();
	private GridPane ctrlPane = new GridPane();
	private GridPane btnPane = new GridPane();
	private GridPane moneyPane = new GridPane();
	private GridPane towerPane = new GridPane();
	private Button startButton = new Button("START");
	private Button resumeButton = new Button("PAUSE");
	private Button speedButton = new Button("X2\nSPEED");
	private Button sellButton = new Button("SELL");
	private Button reTryButton = new Button("Try Again");
	private Button cancelButton = new Button("Cancel");
	private Button continueButton = new Button("Main Menu");
	private HBox moneyCoin = new HBox();
	private Label moneyLabel = new Label(": 000000");
	private HBox healthHeart = new HBox();
	private Label healthLabel = new Label(" : 100%");
	private Text pauseLabel = new Text();
	private HBox T1 = new HBox();
	private HBox T2 = new HBox();
	private HBox T3 = new HBox();
	private HBox T4 = new HBox();
	private HBox T5 = new HBox();
	private HBox T6 = new HBox();
	private HBox Stage1 = new HBox();
	private HBox Stage2 = new HBox();
	private HBox Stage3 = new HBox();
	private HBox Stage4 = new HBox();
	private HBox title = new HBox();
	private MenuBar menuBar = new MenuBar();
	private Menu menu = new Menu("Setting");
	private MenuItem menuItem = new MenuItem("Main Menu");
	private Rectangle pauseShade = new Rectangle();
	private Group root = new Group();
	private Group enemyGroup = new Group();
	private Group bullets = new Group();
	// using in handeler
	public TowerDefenseController controller = new TowerDefenseController(this);
	private HBox currBox = null;
	private String currImage = null;
	private String curOption = "";
	private String currTowerName = null;
	private int CURRENCY;
	private int HEALTH;
	private int currRow;
	private int currCol;
	private double enemyBaseRow;
	private double enemyBaseCol;
	private double baseRow;
	private double baseCol;
	// using in update
	private LinkedList<Enemy> list;
	private HashMap<Integer, HBox> imageMap;
	private Circle rangeCircle = new Circle(); //this follows mouse when placing tower
	private Circle showRange = new Circle(); //this shows up when clicking on a tower
	private int enemyCount = 0;
	// using in audio
	private Long currentFrame;
	private Clip clip;
	private AudioInputStream ais;

	private boolean firstGame;

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof AddTowerMessage) {
			addTower((AddTowerMessage) arg);
		} else if (arg instanceof EnemyMoveMessage) {
			moveEnemy((EnemyMoveMessage) arg);
		} else if (arg instanceof SellTowerMessage) {
			sellTower((SellTowerMessage) arg);
		} else if (arg instanceof DamageBaseMessage) {
			damageBase((DamageBaseMessage) arg);
		} else if (arg instanceof KilledEnemyMessage) {
			CURRENCY += ((KilledEnemyMessage) arg).getBounty();
			Platform.runLater(() -> {
				moneyLabel.setText(" : " + CURRENCY);
				HBox dyingEnemy = new HBox();
				dyingEnemy.setLayoutX(((KilledEnemyMessage) arg).getCol() - 25);
				dyingEnemy.setLayoutY(((KilledEnemyMessage) arg).getRow() - 25);
				mainPane.getChildren().add(dyingEnemy);
				ImageView explode = new ImageView(Constants.ENEMYDIE);
				explode.setViewport(new Rectangle2D(0, 0, 64, 64));
				final Animation animation = new SpriteAnimation(
						explode, Duration.millis(250), 11, 11, 0, 0, 64, 64);
				animation.setCycleCount(1);
				animation.play();
				dyingEnemy.getChildren().add(explode);
				Thread waitAnimation = new Thread(() -> {
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Platform.runLater(() -> {
						mainPane.getChildren().remove(dyingEnemy);
					});
				});
				waitAnimation.start();
				
				enemyCount = enemyCount + 1;
				if(enemyCount == controller.getEnemySize()) {
					enemyGroup.getChildren().clear();
					controller.setPaused(true);
					pauseShade.setVisible(true);
					pauseLabel.setText("Next Level");
					pauseLabel.setVisible(true);
					showWinStage();
					enemyCount = 0;
				}
			});
		} else if (arg instanceof RemoveTowerMessage) {
			Platform.runLater(() -> {
				RemoveTowerMessage message = (RemoveTowerMessage) arg;
				Node temp = null;
				for (Node n : boardPane.getChildren()) {
					if (GridPane.getColumnIndex(n) == message.getCol()
							&& GridPane.getRowIndex(n) == message.getRow()) {
						temp = n;
						break;
					}
				}
				boardPane.getChildren().remove(temp);
				boardPane.add(getHBoxFromImage(path, 75), message.getCol(), message.getRow());
			});
		} else if (arg instanceof ShootBulletMessage) {
			shootBullet((ShootBulletMessage) arg);
		}else if (arg instanceof RestartMessage) {
			Platform.runLater(() -> {
				enemyGroup.getChildren().clear();
				bullets.getChildren().clear();
			});
		}
	}

	private void damageBase(DamageBaseMessage arg) {
		HEALTH -= arg.getDamage();
		Platform.runLater(() -> {
			HBox damageBase = new HBox();
			damageBase.setLayoutX(arg.getCol() * 75 + 14);
			damageBase.setLayoutY(arg.getRow() * 75 + 55);
			mainPane.getChildren().add(damageBase);
			ImageView slash = new ImageView(Constants.SLASH);
			slash.setViewport(new Rectangle2D(0, 0, 64, 64));
			final Animation animation = new SpriteAnimation(
					slash, Duration.millis(500), 10, 5, 0, 0, 64, 64);
			animation.setCycleCount(1);
			animation.play();
			damageBase.getChildren().add(slash);
			Thread waitAnimation = new Thread(() -> {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Platform.runLater(() -> damageBase.getChildren().remove(slash));
			});
			waitAnimation.start();
			healthLabel.setText(" : " + HEALTH + "%");
			if(HEALTH<=0) {
				controller.setPaused(true);
				pauseShade.setVisible(true);
				pauseLabel.setText("You Lose");
				pauseLabel.setVisible(true);
				showLoseStage();
			}
		});
	}

	private void showLoseStage() {
		GridPane buttonPane = new GridPane();
		buttonPane.add(continueButton,0,0);
		buttonPane.add(cancelButton, 1, 0);
		buttonPane.setPadding(new Insets(10, 50, 20, 50));
		buttonPane.setHgap(60);
		BorderPane pane = new BorderPane();
		Label loseLabel= new Label("You Lose!");
		loseLabel.setFont(new Font("Impact", 30));
		loseLabel.setTextFill(Color.WHITE);
		pane.setCenter(loseLabel);
		pane.setBottom(buttonPane);
		pane.setBackground(new Background(new BackgroundFill(Color.grayRgb(40),null,null)));
		Stage loseStage = new Stage();
		Scene Lostscene = new Scene(pane, 350, 150);
		loseStage.setTitle("");
		loseStage.setResizable(false);
		loseStage.setScene(Lostscene);
		loseStage.initModality(Modality.APPLICATION_MODAL);
		loseStage.show();
	}

	private void showWinStage() {
		GridPane buttonPane = new GridPane();
		buttonPane.add(continueButton,0,0);
		buttonPane.add(cancelButton, 1, 0);
		buttonPane.setPadding(new Insets(10, 20, 20, 50));
		buttonPane.setHgap(30);
		BorderPane pane = new BorderPane();
		Label winLabel= new Label("You Win!");
		winLabel.setFont(new Font("Impact", 60));
		winLabel.setTextFill(Color.WHITE);
		pane.setCenter(winLabel);
		pane.setBottom(buttonPane);
		pane.setBackground(new Background(new BackgroundFill(Color.grayRgb(40),null,null)));
		Stage winStage = new Stage();
		Scene winScene = new Scene(pane, 300, 150);
		winStage.setTitle("");
		winStage.setResizable(false);
		winStage.setScene(winScene);
		winStage.initModality(Modality.APPLICATION_MODAL);
		winStage.show();
	}

	private void sellTower(SellTowerMessage arg) {
		CURRENCY = controller.getCurrency();
		moneyLabel.setText(" : " + CURRENCY);
		sellButton.setText("SELL");
		sellButton.setBackground(new Background(new BackgroundFill(Color.LIMEGREEN,null,null)));
		Node temp = null;
		for (Node n : boardPane.getChildren()) {
			if (GridPane.getColumnIndex(n) == arg.getCol()
					&& GridPane.getRowIndex(n) == arg.getRow()) {
				temp = n;
				break;
			}
		}
		boardPane.getChildren().remove(temp);
		if (arg.getType().equals(tower5)) {
			boardPane.add(getHBoxFromImage(path, 75), arg.getCol(), arg.getRow());
		} else {
			boardPane.add(getHBoxFromImage(grass, 75), arg.getCol(), arg.getRow());
		}
		curOption = "";
	}

	private void addTower(AddTowerMessage arg) {
		currRow = arg.getRow();
		currCol = arg.getCol();
		currImage = arg.getTower().getType(); //in tower type
		CURRENCY = controller.getCurrency();
		HBox hBox = new HBox();
		hBox.getChildren().add(makeAnimation(currImage, 75));
		hBox.setOnMouseEntered((event -> {
			if (curOption.equals("")) {
				HBox h = (HBox) event.getSource();
				int row = GridPane.getRowIndex(h);
				int col = GridPane.getColumnIndex(h);
				showRange.setCenterX(col * 75 + 37.5);
				showRange.setCenterY(row * 75 + 70);
				showRange.setRadius(TowerType.valueOf(controller.getPosition(row, col).getTower().getType()).getFiringRange());
				showRange.setVisible(true);
				showRange.setDisable(false);
			}
		}));
		hBox.setOnMouseExited((event -> {
			showRange.setVisible(false);
			showRange.setDisable(true);
		}));
		boardPane.add(hBox, currCol, currRow);
		moneyLabel.setText(" : " + CURRENCY);
		currImage = null;
		curOption = "";
	}

	//shoots the bullet when a tower shoots
	private void shootBullet(ShootBulletMessage arg) {
		Platform.runLater(() -> {
			if (arg.getToCol() != -1 && arg.getToRow() != -1) {
				double row1 = arg.getRow() * 75 + 75;
				double row2 = arg.getToRow();
				double col1 = arg.getCol() * 75 + 37.5;
				double col2 = arg.getToCol();
				double rotateAngle = Math.toDegrees(Math.atan((col2 - col1)/(row1 - row2)));
				if (row1 < row2) {
					rotateAngle += 180;
				}
				if (arg.getType().equals(tower1) || arg.getType().equals(tower2)) {
					HBox bullet = getHBoxFromImage(Constants.BASICBULLET,15,33);
					bullet.setMouseTransparent(true);
					bullet.setRotate(rotateAngle);
					bullets.getChildren().add(bullet);
					PathTransition pathTransition = new PathTransition();
					Path path = new Path();
					path.getElements().add(new MoveTo(col1, row1));
					path.getElements().addAll(new LineTo(col2, row2));
					pathTransition.setNode(bullet);
					pathTransition.setDuration(Duration.millis(100));
					pathTransition.setPath(path);
					Thread waitForTransition = new Thread(() -> {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Platform.runLater(() -> {
							bullets.getChildren().remove(bullet);
						});
					});
					pathTransition.play();
					waitForTransition.start();
				} else if (arg.getType().equals(tower3)) {
					HBox bullet = getHBoxFromImage(Constants.ICEBULLET,30,30);
					bullet.setMouseTransparent(true);
					bullet.setRotate(rotateAngle);
					bullets.getChildren().add(bullet);
					PathTransition pathTransition = new PathTransition();
					Path path = new Path();
					path.getElements().add(new MoveTo(col1, row1));
					path.getElements().addAll(new LineTo(col2, row2));
					pathTransition.setNode(bullet);
					pathTransition.setDuration(Duration.millis(250));
					pathTransition.setPath(path);
					Thread waitForTransition = new Thread(() -> {
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Platform.runLater(() -> {
							bullets.getChildren().remove(bullet);
						});
					});
					pathTransition.play();
					waitForTransition.start();
				} else if (arg.getType().equals(tower4)) {
					HBox bullet = getHBoxFromImage(Constants.SNIPERBULLET,15,50);
					bullet.setMouseTransparent(true);
					bullet.setRotate(rotateAngle);
					bullets.getChildren().add(bullet);
					PathTransition pathTransition = new PathTransition();
					Path path = new Path();
					path.getElements().add(new MoveTo(col1, row1));
					path.getElements().addAll(new LineTo(col2, row2));
					pathTransition.setNode(bullet);
					pathTransition.setDuration(Duration.millis(150));
					pathTransition.setPath(path);
					Thread waitForTransition = new Thread(() -> {
						try {
							Thread.sleep(150);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Platform.runLater(() -> {
							bullets.getChildren().remove(bullet);
						});
					});
					pathTransition.play();
					waitForTransition.start();
				} else if (arg.getType().equals(tower6)) {
					HBox bullet = getHBoxFromImage(Constants.TANKBULLET,20,50);
					bullet.setMouseTransparent(true);
					bullet.setRotate(rotateAngle);
					bullets.getChildren().add(bullet);
					PathTransition pathTransition = new PathTransition();
					Path path = new Path();
					path.getElements().add(new MoveTo(col1, row1));
					path.getElements().addAll(new LineTo(col2, row2));
					pathTransition.setNode(bullet);
					pathTransition.setDuration(Duration.millis(150));
					pathTransition.setPath(path);
					Thread waitForTransition = new Thread(() -> {
						try {
							Thread.sleep(150);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Platform.runLater(() -> {
							bullets.getChildren().remove(bullet);
						});
					});
					pathTransition.play();
					waitForTransition.start();
				}
			}
		});
	}

	private void moveEnemy(EnemyMoveMessage arg) {
		Platform.runLater(() -> {
			Map<Integer, Enemy> list = arg.getList();
			if(list.size()!=0) {
				Map<Integer, Enemy> tempMap = new TreeMap<>(list);
				enemyGroup.getChildren().clear();
				for (Enemy enemy : tempMap.values()) {
					if (enemy.isSpawn()) {
						PathTransition pathTransition = new PathTransition();
						Path path = new Path();
						path.getElements().add(new MoveTo(enemy.getFromCol(),
								enemy.getFromRow()));
						path.getElements().add(
								new LineTo(enemy.getToCol(), enemy.getToRow()));
						pathTransition.setDuration(Duration.millis(20));
						pathTransition.setPath(path);
						int id = enemy.getID();
						HBox hBox = getHBoxFromImage(enemy.getImage(), 75);
						if (!imageMap.containsKey(id)) {
							imageMap.put(id, hBox);
						}
						pathTransition.setNode(imageMap.get(id));
						pathTransition.play();
						enemyGroup.setMouseTransparent(true);
						enemyGroup.getChildren().add(imageMap.get(id));
					}
				}
			}else {
				enemyGroup.getChildren().clear();
				bullets.getChildren().clear();
				controller.setPaused(true);
				pauseShade.setVisible(true);
				pauseLabel.setText("Next Level");
				pauseLabel.setVisible(true);
				showWinStage();
			}
		});
	}

	/**
	 * Handlers below.
	 **/

	private void addHandlers() {
		EventHandler<MouseEvent> boardHandeler = new BoardHandeler();
		EventHandler<MouseEvent> boardMouse = new BoardMouse(); //this is for the range indicator when placing a tower
		boardPane.setOnMouseMoved(boardMouse);
		boardPane.setOnMouseClicked(boardHandeler);

		EventHandler<MouseEvent> startHandeler = new StartHandeler();
		startButton.setOnMouseClicked(startHandeler);

		resumeButton.setOnMouseClicked((event -> {
			if (startButton.getText().equals("RESTART")) {
				if (resumeButton.getText().equals("PAUSE")) {
					resumeButton.setText("RESUME");
					resumeButton.setFont(Font.font("Impact", 16));
					resumeButton.setBackground(new Background(new BackgroundFill(Color.rgb(100, 255, 100),null,null)));
					pauseShade.setVisible(true);
					pauseLabel.toFront();
					pauseLabel.setText("PAUSED");
					pauseLabel.setVisible(true);
					controller.setPaused(true);
				} else if (resumeButton.getText().equals("RESUME")) {
					resumeButton.setText("PAUSE");
					resumeButton.setFont(Font.font("Impact", 18));
					resumeButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 100, 100),null,null)));
					pauseShade.setVisible(false);
					pauseLabel.setVisible(false);
					controller.runGame();
				}
			}
		}));
		resumeButton.setOnMouseEntered((event -> {
			if (startButton.getText().equals("RESTART")) {
				if (resumeButton.getText().equals("PAUSE")) {
					resumeButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 100, 100),null,null)));
				} else if (resumeButton.getText().equals("RESUME")) {
					resumeButton.setBackground(new Background(new BackgroundFill(Color.rgb(100, 255, 100),null,null)));
				}
			}
		}));
		resumeButton.setOnMouseExited(((event -> {
			if (startButton.getText().equals("RESTART")) {
				if (resumeButton.getText().equals("PAUSE")) {
					resumeButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 0, 0),null,null)));
				} else if (resumeButton.getText().equals("RESUME")) {
					resumeButton.setBackground(new Background(new BackgroundFill(Color.rgb(0, 255, 0),null,null)));
				}
			}
		})));


		speedButton.setOnMouseClicked((event -> {
			if (startButton.getText().equals("RESTART")) {
				if (speedButton.getText().equals("X2\nSPEED")) {
					speedButton.setText("X1\nSPEED");
					controller.setTickRate(35);
				} else if (speedButton.getText().equals("X1\nSPEED")) {
					speedButton.setText("X2\nSPEED");
					controller.setTickRate(70);
				}
			}
		}));
		speedButton.setOnMouseEntered((event -> {
			if (startButton.getText().equals("RESTART")) {
				speedButton.setBackground(new Background(new BackgroundFill(Color.rgb(100, 100, 255),null,null)));
			}
		}));
		speedButton.setOnMouseExited((event -> {
			if (startButton.getText().equals("RESTART")) {
				speedButton.setBackground(new Background(new BackgroundFill(Color.rgb(50, 50, 255),null,null)));
			}
		}));

		startButton.setOnMouseEntered((event -> {
			if (startButton.getText().equals("RESTART")) {
				startButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 100, 100),null,null)));
			} else {
				startButton.setBackground(new Background(new BackgroundFill(Color.rgb(100, 255, 100),null,null)));
			}
		}));
		startButton.setOnMouseExited((event -> {
			if (startButton.getText().equals("RESTART")) {
				startButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 0, 0),null,null)));
			} else {
				startButton.setBackground(new Background(new BackgroundFill(Color.rgb(0, 255, 0),null,null)));
			}
		}));

		EventHandler<MouseEvent> sellHandeler = new SellHandeler();
		sellButton.setOnMouseClicked(sellHandeler);
		sellButton.setOnMouseEntered(event -> {
			if (sellButton.getText().equals("SELL")) {
				sellButton.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,null,null)));
			} else {
				sellButton.setBackground(new Background(new BackgroundFill(Color.rgb(255,100,100),null,null)));
			}
		});
		sellButton.setOnMouseExited(event -> {
			if (sellButton.getText().equals("SELL")) {
				sellButton.setBackground(new Background(new BackgroundFill(Color.LIMEGREEN,null,null)));
			} else {
				sellButton.setBackground(new Background(new BackgroundFill(Color.rgb(255,0,0),null,null)));
			}
		});

		EventHandler<MouseEvent> towerHandeler = new TowerHandeler();
		T1.setOnMouseClicked(towerHandeler);
		T2.setOnMouseClicked(towerHandeler);
		T3.setOnMouseClicked(towerHandeler);
		T4.setOnMouseClicked(towerHandeler);
		T5.setOnMouseClicked(towerHandeler);
		T6.setOnMouseClicked(towerHandeler);

		EventHandler<MouseEvent> reTryHandeler = new ReTryHandeler();
		reTryButton.setOnMouseClicked(reTryHandeler);
		reTryButton.setBackground(new Background(new BackgroundFill(Color.rgb(0,255,0),null,null)));
		reTryButton.setMinWidth(100);
		reTryButton.setMinHeight(50);
		reTryButton.setFont(new Font("Impact",15));
		reTryButton.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
		reTryButton.setOnMouseEntered(event -> {
			reTryButton.setBackground(new Background(new BackgroundFill(Color.rgb(100, 255, 100),null,null)));
		});
		reTryButton.setOnMouseExited(event -> {
			reTryButton.setBackground(new Background(new BackgroundFill(Color.rgb(0, 255, 0),null,null)));
		});
		
		EventHandler<MouseEvent> continueHandeler = new ContinueHandeler();
		continueButton.setOnMouseClicked(continueHandeler);
		continueButton.setBackground(new Background(new BackgroundFill(Color.rgb(0,255,0),null,null)));
		continueButton.setMinWidth(100);
		continueButton.setMinHeight(50);
		continueButton.setFont(new Font("Impact",15));
		continueButton.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
		continueButton.setOnMouseEntered(event -> {
			continueButton.setBackground(new Background(new BackgroundFill(Color.rgb(100, 255, 100),null,null)));
		});
		continueButton.setOnMouseExited(event -> {
			continueButton.setBackground(new Background(new BackgroundFill(Color.rgb(0, 255, 0),null,null)));
		});
		
		EventHandler<MouseEvent> cancelHandeler = new CancelHandeler();
		cancelButton.setOnMouseClicked(cancelHandeler);
		cancelButton.setMinWidth(100);
		cancelButton.setMinHeight(50);
		cancelButton.setBackground(new Background(new BackgroundFill(Color.rgb(255,0,0),null,null)));
		cancelButton.setFont(new Font("Impact",15));
		cancelButton.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
		cancelButton.setOnMouseEntered(event -> {
			cancelButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 100, 100),null,null)));
		});
		cancelButton.setOnMouseExited(event -> {
			cancelButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 0, 0),null,null)));
		});
	}

	private class BoardMouse implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			rangeCircle.setCenterX(event.getSceneX());
			rangeCircle.setCenterY(event.getSceneY());
		}
	}

	private class BoardHandeler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent args) {
			HBox h = (HBox) ((ImageView) args.getTarget()).getParent();
			int row = GridPane.getRowIndex(h);
			int col = GridPane.getColumnIndex(h);;
			if (curOption.equals("Build") && currTowerName != null) {
				try {
					rangeCircle.setVisible(false);
					rangeCircle.setDisable(true);
					ImageView iv = makeAnimation(currTowerName, 120);
					if (currTowerName.equals(tower1)) {
						T1.getChildren().clear();
						iv.setImage(new Image(MACHINEGUN));
						T1.getChildren().add(iv);
					} else if (currTowerName.equals(tower2)) {
						T2.getChildren().clear();
						iv.setImage(new Image(BASIC));
						T2.getChildren().add(iv);
					} else if (currTowerName.equals(tower3)) {
						T3.getChildren().clear();
						iv.setImage(new Image(ICE));
						T3.getChildren().add(iv);
					} else if (currTowerName.equals(tower4)) {
						T4.getChildren().clear();
						iv.setImage(new Image(SNIPER));
						T4.getChildren().add(iv);
					} else if (currTowerName.equals(tower5)) {
						T5.getChildren().clear();
						T5.getChildren().add(iv);
					} else if (currTowerName.equals(tower6)) {
						T6.getChildren().clear();
						iv.setImage(new Image(TANK));
						T6.getChildren().add(iv);
					}
					controller.placeTower(row, col, currTowerName);
				} catch (InvalidPlacementException e) {
					e.printStackTrace();
				} finally {
					curOption = "";
				}
			}
			else if (curOption.equals("Sell")) {
				controller.sellTower(row, col);
			}
		}
	}

	private HBox getHBoxFromImage(String image, int size) {
		HBox hBox = new HBox();
		hBox.setMinSize(size, size);
		hBox.setMaxSize(size, size);
		ImageView iv = new ImageView(new Image(image));
		iv.setFitWidth(size);
		iv.setFitHeight(size);
		hBox.getChildren().add(iv);
		return hBox;
	}

	private HBox getHBoxFromImage(String image, int width, int height) {
		HBox hBox = new HBox();
		hBox.setMinSize(width, height);
		hBox.setMaxSize(width, height);
		ImageView iv = new ImageView(new Image(image));
		iv.setFitWidth(width);
		iv.setFitHeight(height);
		hBox.getChildren().add(iv);
		return hBox;
	}
	// TODO need associate with model.
	private class StartHandeler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent args) {
			if (startButton.getText().equals("START")) {
				controller.setEnemyStartPoint(enemyBaseRow, enemyBaseCol);
				pauseShade.setVisible(false);
				pauseLabel.setText("PAUSED");
				pauseLabel.setX(150);
				pauseLabel.setVisible(false);
				controller.runGame();
				startButton.setText("RESTART");
				startButton.setFont(new Font("Impact", 15));
				startButton.setBackground(new Background(new BackgroundFill(Color.rgb(255,100,100),null,null)));
			} else if (startButton.getText().equals("RESTART")) {
				startButton.setText("START");
				startButton.setFont(new Font("Impact", 19));
				startButton.setBackground(new Background(new BackgroundFill(Color.rgb(100,255,100),null,null)));
				pauseShade.setVisible(true);
				pauseLabel.setText("GAME RESET");
				pauseLabel.setX(60);
				pauseLabel.setVisible(true);
				Platform.runLater(() -> {
					controller.restartLevel();
					boardPane.getChildren().clear();
					btnPane.getChildren().clear();
					initialBoardPane();
					initialBtnPane();
				});
			}
			CURRENCY = controller.getCurrency();
			HEALTH = controller.getHealth();
			moneyLabel.setText(" : " + CURRENCY);
			healthLabel.setText(" : " + HEALTH + "%");
			imageMap = new HashMap<>();

		}
	}

	private void setBoardByLevel() {
		// add road and grass
		// add base and enemyBase

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				HBox hBox;
				if (controller.getPosition(row, col).isBase()) {
					baseRow = (double) row * 75 + 37.5 + 30;
					baseCol = (double) col * 75 + 37.5;
					hBox = getHBoxFromImage(base, 75);
				} else if (controller.getPosition(row, col)
						.equals(controller.getEnemyPath().get(0))) {
					enemyBaseRow = (double) row * 75 + 37.5 + 30;
					enemyBaseCol = (double) col * 75 + 37.5;
					hBox = getHBoxFromImage(enemyBase, 75);
				} else if (controller.getEnemyPath()
						.contains(controller.getPosition(row, col))) {
					hBox = getHBoxFromImage(path, 75);
				} else {
					int blocked = 5;
					if (constants.Stage.valueOf(controller.getStage()).getBlockedChance() != 0) {
						blocked = new Random().nextInt(constants.Stage.valueOf(controller.getStage()).getBlockedChance());
					}
					if (blocked == 0) {
						hBox = getHBoxFromImage(Constants.BLOCKED, 75);
						controller.getPosition(row, col).setBlocked();
					} else {
						hBox = getHBoxFromImage(grass, 75);
					}
				}
				boardPane.add(hBox, col, row);
			}
		}
	}

	private class SellHandeler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent args) {
			if (startButton.getText().equals("RESTART")) {
				if (sellButton.getText().equals("SELL")) {
					curOption = "Sell";
					sellButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 100, 100), null, null)));
					sellButton.setText("CANCEL");
					sellButton.setFont(new Font("Impact", 15));
				} else if (sellButton.getText().equals("CANCEL")) {
					curOption = "";
					sellButton.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200), null, null)));
					sellButton.setText("SELL");
					sellButton.setFont(new Font("Impact", 20));
				}
			}
		}
	}

	private class TowerHandeler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent args) {
			if (startButton.getText().equals("RESTART")
					&& resumeButton.getText().equals("PAUSE")) {
				currBox = (HBox) args.getSource();
				if (curOption.equals("")) {
					if (currBox.equals(T1)) {
						currTowerName = tower1;
					} else if (currBox.equals(T2)) {
						currTowerName = tower2;
					} else if (currBox.equals(T3)) {
						currTowerName = tower3;
					} else if (currBox.equals(T4)) {
						currTowerName = tower4;
					} else if (currBox.equals(T5)) {
						currTowerName = tower5;
					} else if (currBox.equals(T6)) {
						currTowerName = tower6;
					}
					ImageView iv = new ImageView(new Image(cancel));
					iv.setFitHeight(120);
					iv.setFitHeight(120);
					currBox.getChildren().clear();
					currBox.getChildren().add(iv);
					rangeCircle.setVisible(true);
					rangeCircle.setDisable(false);
					rangeCircle.setCenterY(args.getSceneY());
					rangeCircle.setCenterX(args.getSceneX());
					rangeCircle.setRadius(TowerType.valueOf(currTowerName).getFiringRange());
					curOption = "Build";
				} else if (curOption.equals("Build")) {
					String img = "";
					boolean valid = true;
					if (currTowerName.equals(tower1) && currBox.equals(T1)) img = Constants.TOWER1;
					else if(currTowerName.equals(tower2) && currBox.equals(T2)) img = Constants.TOWER2;
					else if (currTowerName.equals(tower3) && currBox.equals(T3)) img = Constants.TOWER3;
					else if (currTowerName.equals(tower4) && currBox.equals(T4)) img = Constants.TOWER4;
					else if (currTowerName.equals(tower5) && currBox.equals(T5)) img = Constants.TOWER5;
					else if (currTowerName.equals(tower6) && currBox.equals(T6)) img = Constants.TOWER6;
					else valid = false;
					if (valid) {
						currBox.getChildren().clear();
						ImageView tempIv = makeAnimation(img, 120);
						currBox.getChildren().add(tempIv);
						rangeCircle.setVisible(false);
						rangeCircle.setDisable(true);
						curOption = "";
					}
				}
			}
		}
	}

	private class StageSelectHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			boolean valid = true;
			String stage = "";
			if(event.getX() >= Stage1.getLayoutX() && event.getX() <= (Stage1.getLayoutX() + Stage1.getWidth())) {
				if(event.getY() >= Stage1.getLayoutY() && event.getY() <= (Stage1.getLayoutY() + Stage1.getHeight())){
					stage = "STAGE1";
				} else if (event.getY() >= Stage3.getLayoutY() && event.getY() <= (Stage3.getLayoutY() + Stage3.getHeight())){
					stage = "STAGE3";
				}
			} else if(event.getX() >= Stage2.getLayoutX() && event.getX() <= (Stage2.getLayoutX() + Stage2.getWidth())) {
				if(event.getY() >= Stage2.getLayoutY() && event.getY() <= (Stage2.getLayoutY() + Stage2.getHeight())){
					stage = "STAGE2";
				} else if (event.getY() >= Stage4.getLayoutY() && event.getY() <= (Stage4.getLayoutY() + Stage4.getHeight())){
					stage = "STAGE4";
				}
			} else {
				valid = false;
			}
			if (valid && firstGame) {
				controller.initialize(stage, controller.getObserver());
				initialMainPane();
				addHandlers();
				stopSong();
				playSong(Constants.GAMEMUSIC);
				mainPane.getChildren().add(enemyGroup);
				mainPane.getChildren().add(bullets);
				mainStage.setScene(gameScene);
				firstGame = false;
			} else {
				controller.restartLevel(stage);
				stopSong();
				playSong(Constants.GAMEMUSIC);
				mainPane.getChildren().add(enemyGroup);
				mainPane.getChildren().add(bullets);
				initialBoardPane();
				initialBtnPane();
				mainStage.setScene(gameScene);
			}
		}

	}
	private class ReTryHandeler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			Stage s = (Stage) ((Button)event.getSource()).getScene().getWindow();
			s.close();
		}
	}
	//TODO
	private class ContinueHandeler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			Stage s = (Stage) ((Button)event.getSource()).getScene().getWindow();
			s.close();
			initialBoardPane();
			if (speedButton.getText().equals("X1\nSPEED")) {
				speedButton.setText("X2\nSPEED");
				controller.setTickRate(70);
			}

			Platform.runLater(() -> {
				stopSong();
				playSong(Constants.MENUMUSIC);
				mainStage.setScene(menuScene);
				mainPane.getChildren().remove(enemyGroup);
				mainPane.getChildren().remove(bullets);
				boardPane.getChildren().clear();
				btnPane.getChildren().clear();
				moneyLabel.setText(" : 000000");
			});
		}
	}
	private class CancelHandeler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			Stage s = (Stage) ((Button)event.getSource()).getScene().getWindow();
			s.close();
		}
	}
	/**
	 * Layout below.
	 **/

	@Override
	public void start(Stage stage) throws Exception {
		initialMenuPane();
		playSong(Constants.MENUMUSIC);
		firstGame = true;
		mainStage.setTitle("Tower Defense");
		root.getChildren().add(mainPane);
		menuScene = new Scene(menuPane, Constants.SCREEN_WIDTH,
				Constants.SCREEN_HEIGHT + 29, Color.BLUE);
		gameScene = new Scene(root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT + 29,
				Color.BLUE);
		mainStage.setScene(menuScene);
		mainStage.setResizable(true);
		mainStage.show();

	}

	private void initialMenuPane() {
		menuPane.setBackground(new Background(new BackgroundImage(new Image(Constants.BACKGROUND), null, null, null, null)));
		Stage1 = getHBoxFromImage(Constants.STAGE1, 200);
		Tooltip tool1 = new Tooltip("Click to Select Stage 1");
		Tooltip.install(Stage1, tool1);
		Stage2 = getHBoxFromImage(Constants.STAGE2, 200);
		Tooltip tool2 = new Tooltip("Click to Select Stage 2");
		Tooltip.install(Stage2, tool2);
		Stage3 = getHBoxFromImage(Constants.STAGE3, 200);
		Tooltip tool3 = new Tooltip("Click to Select Stage 3");
		Tooltip.install(Stage3, tool3);
		Stage4 = getHBoxFromImage(Constants.STAGE4, 200);
		Tooltip tool4 = new Tooltip("Click to Select Stage 4");
		Tooltip.install(Stage4, tool4);
		stageSelect.add(Stage1, 0, 0);
		stageSelect.add(Stage2, 1, 0);
		stageSelect.add(Stage3, 0, 1);
		stageSelect.add(Stage4, 1, 1);
		stageSelect.setHgap(15);
		stageSelect.setVgap(15);
		stageSelect.setAlignment(Pos.CENTER);
		title = getHBoxFromImage(Constants.TITLE, 300, 150);
		titlePane.setCenter(title);
		menuPane.setTop(titlePane);

		menuPane.setCenter(stageSelect);

		menuPane.setMinHeight(Constants.SCREEN_HEIGHT);
		menuPane.setMinWidth(Constants.SCREEN_WIDTH);
		menuPane.setMaxHeight(Constants.SCREEN_HEIGHT);
		menuPane.setMaxWidth(Constants.SCREEN_WIDTH);

		EventHandler<MouseEvent>  stageSelectHandler = new StageSelectHandler();
		menuPane.getCenter().setOnMouseClicked(stageSelectHandler);
	}

	private void initialMainPane() {
		initialBoardPane();
		initialCtrlPane();
		mainPane.setLeft(boardPane);
		mainPane.setRight(ctrlPane);
		layoutMenuBar();
		mainPane.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));

		rangeCircle.setFill(Color.BLACK); //this is the range indicator when placing
		rangeCircle.setOpacity(.2);
		rangeCircle.setDisable(true);
		rangeCircle.setVisible(false);
		rangeCircle.setMouseTransparent(true);

		showRange.setFill(Color.BLACK); //this is the range indicator when clicking on a pre existing one
		showRange.setOpacity(.2);
		showRange.setDisable(true);
		showRange.setVisible(false);
		showRange.setMouseTransparent(true);

		mainPane.getChildren().add(rangeCircle);
		mainPane.getChildren().add(showRange);

		pauseShade.setX(0);
		pauseShade.setY(29);
		pauseShade.setVisible(false);
		pauseShade.setFill(Color.BLACK);
		pauseShade.setHeight(Constants.SCREEN_HEIGHT);
		pauseShade.setWidth(Constants.SCREEN_WIDTH-300);
		pauseShade.setOpacity(.5);
		mainPane.getChildren().addAll(pauseShade);

		pauseLabel.setText("PAUSED");
		pauseLabel.setVisible(false);
		pauseLabel.setFont(Font.font("Impact", 100));
		pauseLabel.setFill(Color.WHITE);
		pauseLabel.setX(150);
		pauseLabel.setY(Constants.SCREEN_HEIGHT/2 + 60);
		mainPane.getChildren().add(pauseLabel);

	}

	private void initialBoardPane() {
		setBoardByLevel();
		boardPane.setBackground(new Background(new BackgroundFill(Color.GREY,null,null)));
		boardPane.setMinSize(580, 600);
		boardPane.setMaxSize(600, 600);
		boardPane.setPadding(new Insets(0, 0, 0, 0));
	}

	// Contains all buttons and towers.
	private void initialCtrlPane() {
		initialBtnPane();
		initialMnyPane();
		initialTowerPane();

		ctrlPane.add(btnPane, 0, 0);
		ctrlPane.add(moneyPane, 0, 1);
		ctrlPane.add(towerPane, 0, 2);

		ctrlPane.setBackground(new Background(new BackgroundFill(
				Color.LIGHTGREY,null,null)));
		ctrlPane.setMinSize(310, 600);
		ctrlPane.setMaxSize(310, 600);
		ctrlPane.setPadding(new Insets(10, 10, 10, 10));
	}

	private void initialBtnPane() {
		pauseShade.setVisible(false);
		pauseLabel.setVisible(false);
		startButton.setText("START");
		startButton.setMinSize(80, 80);
		startButton.setMaxSize(80, 80);
		startButton.setTextFill(Color.BLACK);
		startButton.setFont(Font.font("Impact", 19));
		startButton.setBackground(new Background(new BackgroundFill(Color.rgb(0, 255, 0),null,null)));
		startButton.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
		startButton.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));

		resumeButton.setText("PAUSE");
		resumeButton.setMinSize(80, 80);
		resumeButton.setMaxSize(80, 80);
		resumeButton.setTextFill(Color.BLACK);
		resumeButton.setFont(Font.font("Impact", 18));
		resumeButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 0, 0),null,null)));
		resumeButton.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));

		speedButton.setText("X2\nSPEED");
		speedButton.setMinSize(80, 80);
		speedButton.setMaxSize(80, 80);
		speedButton.setFont(Font.font("Impact", 18));
		speedButton.setTextAlignment(TextAlignment.CENTER);
		speedButton.setBackground(new Background(new BackgroundFill(Color.rgb(50, 50, 255),null,null)));
		speedButton.setTextFill(Color.BLACK);
		speedButton.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));

		btnPane.add(startButton, 1, 1);
		btnPane.add(resumeButton, 2, 1);
		btnPane.add(speedButton, 3, 1);

		btnPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY,null,null)));
		btnPane.setHgap(10);
		btnPane.setPadding(new Insets(10, 10, 10, 0));
	}

	private void initialMnyPane() {
		healthLabel.setMaxSize(85, 40);
		healthLabel.setMinSize(85, 40);
		healthLabel.setFont(Font.font("Impact", 18));
		healthLabel.setTextFill(Color.BLACK);

		ImageView heart = new ImageView(Constants.HEART);
		heart.setViewport(new Rectangle2D(0, 0, 50, 50));
		final Animation animationHeart = new SpriteAnimation(heart, Duration.millis(500),
				2, 2, 0, 0, 50, 50);
		animationHeart.setCycleCount(Animation.INDEFINITE);
		animationHeart.play();
		healthHeart.getChildren().add(heart);
		GridPane healthPane = new GridPane();
		healthPane.add(healthHeart, 0, 0);
		healthPane.add(healthLabel, 1, 0);

		moneyLabel.setMinSize(85, 40);
		moneyLabel.setMaxSize(85, 40);
		moneyLabel.setFont(Font.font("Impact", 18));
		moneyLabel.setTextFill(Color.BLACK);
		ImageView coin = new ImageView(Constants.COIN);
		coin.setViewport(new Rectangle2D(0, 0, 32, 32));
		final Animation animationCoin = new SpriteAnimation(
				coin, Duration.millis(750), 8, 8, 0, 0, 32, 32);
		animationCoin.setCycleCount(Animation.INDEFINITE);
		animationCoin.play();
		moneyCoin.getChildren().add(coin);
		GridPane moneyCoinLabel = new GridPane();
		moneyCoinLabel.add(moneyCoin, 0, 0);
		moneyCoinLabel.add(moneyLabel, 1, 0);
		moneyCoinLabel.setPadding(new Insets(5,0,-5,0));

		sellButton.setMinSize(70, 40);
		sellButton.setMaxSize(70, 40);
		sellButton.setTextFill(Color.BLACK);
		sellButton.setFont(Font.font("Impact", 20));
		sellButton.setBackground(new Background(new BackgroundFill(Color.LIMEGREEN,null,null)));
		sellButton.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));

		GridPane leftPane = new GridPane();
		leftPane.add(healthPane, 0, 0);
		leftPane.add(moneyCoinLabel, 1, 0);
		leftPane.setHgap(-30);
		HBox sellLabel = new HBox();
		sellLabel.getChildren().add(sellButton);

		moneyPane.add(leftPane, 0, 0);
		moneyPane.add(sellLabel, 1, 0);
		moneyPane.setHgap(-5);

		moneyPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY,null,null)));
		moneyPane.setPadding(new Insets(10, 5, 10, -3));
	}

	private void initialTowerPane() {
		Tooltip tt1 = new Tooltip(TowerType.valueOf(Constants.TOWER1).getDescr());
		Tooltip.install(T1, tt1);
		T1.getChildren().add(makeAnimation(Constants.TOWER1, 120));

		Tooltip tt2 = new Tooltip(TowerType.valueOf(Constants.TOWER2).getDescr());
		Tooltip.install(T2, tt2);
		T2.getChildren().add(makeAnimation(Constants.TOWER2, 120));

		T3.getChildren().add(makeAnimation(Constants.TOWER3, 120));
		Tooltip tt3 = new Tooltip(TowerType.valueOf(Constants.TOWER3).getDescr());
		Tooltip.install(T3, tt3);

		Tooltip tt4 = new Tooltip(TowerType.valueOf(Constants.TOWER4).getDescr());
		Tooltip.install(T4, tt4);
		T4.getChildren().add(makeAnimation(Constants.TOWER4, 120));

		T5.getChildren().add(makeAnimation(Constants.TOWER5, 120));
		Tooltip tt5 = new Tooltip(TowerType.valueOf(Constants.TOWER5).getDescr());
		Tooltip.install(T5, tt5);

		Tooltip tt6 = new Tooltip(TowerType.valueOf(Constants.TOWER6).getDescr());
		Tooltip.install(T6, tt6);
		T6.getChildren().add(makeAnimation(Constants.TOWER6, 120));

		towerPane.add(T1, 0, 0);
		towerPane.add(T2, 1, 0);
		towerPane.add(T3, 0, 1);
		towerPane.add(T4, 1, 1);
		towerPane.add(T5, 0, 2);
		towerPane.add(T6, 1, 2);

		towerPane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
		towerPane.setMinSize(280, 410);
		towerPane.setVgap(15);
		towerPane.setHgap(20);
		towerPane.setPadding(new Insets(10, 0, 0, 10));
	}

	private ImageView makeAnimation(String constant, int size) {
		ImageView ptIV = new ImageView(TowerType.valueOf(constant).getImage());
		ptIV.setViewport(new Rectangle2D(TowerType.valueOf(constant).getOffsetX(),
				TowerType.valueOf(constant).getOffsetY(),
				TowerType.valueOf(constant).getWidth(),
				TowerType.valueOf(constant).getHeight()));

		final Animation animation = new SpriteAnimation(
				ptIV,
				Duration.millis(1000),
				TowerType.valueOf(constant).getCount(),
				TowerType.valueOf(constant).getColumns(),
				TowerType.valueOf(constant).getOffsetX(),
				TowerType.valueOf(constant).getOffsetY(),
				TowerType.valueOf(constant).getWidth(),
				TowerType.valueOf(constant).getHeight()
		);
		animation.setCycleCount(Animation.INDEFINITE);
		animation.play();
		ptIV.setFitHeight(size);
		ptIV.setFitWidth(size);
		return ptIV;
	}	

	// TODO Menu. If necessary.
	private void layoutMenuBar() {
		menu.getItems().add(menuItem);
		menuBar.getMenus().add(menu);
		mainPane.setTop(menuBar);
		menuItem.setOnAction((event) -> {
			stopSong();
			playSong(Constants.MENUMUSIC);
			mainStage.setScene(menuScene);
			mainPane.getChildren().remove(enemyGroup);
			mainPane.getChildren().remove(bullets);
			boardPane.getChildren().clear();
			btnPane.getChildren().clear();
			moneyLabel.setText(" : 000000");

		});
	}

	private void playSong(String filePath){
		try {
			ais = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	private void stopSong(){
		currentFrame = 0L;
		clip.stop();
		clip.close();
	}
}
