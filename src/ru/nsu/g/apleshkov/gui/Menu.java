package ru.nsu.g.apleshkov.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.nsu.g.apleshkov.tron.Tron;

import java.util.ArrayList;
import java.util.List;

public class Menu extends Application
{

	public static void main(String[] args)
	{
		launch(args);
	}

	private ObservableList<Color> colorsToChoose;

	private void setColorsToChoose()
	{
		colorsToChoose = FXCollections.observableArrayList(
				Color.BLUE,
				Color.RED,
				Color.DARKGRAY,
				Color.LIGHTGRAY,
				Color.LIGHTBLUE);
	}
	private ArrayList<TextField> names;

	List<ArrayList<Control>> playerLines;

	@Override
	public void start(Stage primaryStage)
	{
		StackPane root = new StackPane();

		names = new ArrayList<>();

		setColorsToChoose();

		Tron tron = new Tron();
		int playerNum = 1;
		Button addPlayer = new Button("Add player");
		addPlayer.setOnAction(
				(event) ->
				{
					names.add(new TextField("Player " + playerNum));
					Button color;
				});


		//root.getChildren().add(
	}
}
