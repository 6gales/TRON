package ru.nsu.g.apleshkov.tron.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.paint.*;
import ru.nsu.g.apleshkov.tron.model.Model;

public class Graphics extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage)
	{
		StackPane root = new StackPane();
		Canvas canvas = new Canvas(1400, 700);
		GraphicsContext context = canvas.getGraphicsContext2D();

		Model model = new Model();
		model.addPlayer("Player 1", 1);
		model.addPlayer("Player 2", 2);
//		model.addBot("RandomBot", 2);

		ColorMap colorMap = new ColorMap(model.getField());
		colorMap.add(1, Color.BLUE);
		colorMap.add(2, Color.RED);

		canvas.setFocusTraversable(true);
		canvas.addEventHandler(KeyEvent.KEY_PRESSED, new Controller(model, 1, KeyCode.LEFT, KeyCode.RIGHT));
		canvas.addEventHandler(KeyEvent.KEY_PRESSED, new Controller(model, 2, KeyCode.A, KeyCode.D));
		root.getChildren().add(canvas);

		Scene scene = new Scene(root);

		primaryStage.setResizable(false);
		primaryStage.setTitle("TRON");
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		primaryStage.setScene(scene);
		primaryStage.show();

		new Thread(new GameLoop(context, model, colorMap)).start();
	}

//		while ()

//
//	@Override
//	public void start(Stage primaryStage)
//	{
//		primaryStage.setTitle("TRON Menu");
//		Group root = new Group();
//		Canvas canvas = new Canvas(300, 250);
//		GraphicsContext gc = canvas.getGraphicsContext2D();
//		drawShapes(gc);
//		root.getChildren().add(canvas);
//		primaryStage.setScene(new Scene(root));
//		primaryStage.show();
//	}
//
//	private void drawShapes(GraphicsContext gc) {
//		gc.setFill(Color.GREEN);
//		gc.setStroke(Color.BLUE);
//		gc.setLineWidth(5);
//		gc.strokeLine(40, 10, 10, 40);
//		gc.fillOval(10, 60, 30, 30);
//		gc.strokeOval(60, 60, 30, 30);
//		gc.fillRoundRect(110, 60, 30, 30, 10, 10);
//		gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
//		gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
//		gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
//		gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
//		gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
//		gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
//		gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
//		gc.fillPolygon(new double[]{10, 40, 10, 40},
//				new double[]{210, 210, 240, 240}, 4);
//		gc.strokePolygon(new double[]{60, 90, 60, 90},
//				new double[]{210, 210, 240, 240}, 4);
//		gc.strokePolyline(new double[]{110, 140, 110, 140},
//				new double[]{210, 210, 240, 240}, 4);
//	}
}
