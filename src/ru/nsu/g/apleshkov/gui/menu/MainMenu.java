package ru.nsu.g.apleshkov.gui.menu;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class MainMenu extends Menu
{
	public static void main(String[] args)
	{
		launch(args);
	}

	private String hostName;
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage)
	{
		int port = 8080;

		this.primaryStage = primaryStage;

		FlowPane borderPane = new FlowPane();
		root = borderPane;

		addWhiteLabel("TRON Main menu", borderPane.getChildren()::add);

		Button offline = new Button("Play offline"),
				host = new Button("Become host"),
				connect = new Button("Connect to");

		offline.setPrefSize(standartWidth, standartHeight);
		offline.setOnAction(event -> createMenu(new OfflineMenu()));
		borderPane.getChildren().add(offline);

		host.setPrefSize(standartWidth, standartHeight);
		host.setOnAction(event -> createMenu(new HostMenu(port)));
		borderPane.getChildren().add(host);

		connect.setPrefSize(standartWidth, standartHeight);
		connect.setOnAction(event -> createMenu(new ClientMenu(hostName, port)));
		borderPane.getChildren().add(connect);

		hostName = "127.0.0.1";

		TextField address = new TextField(hostName);

		setTextField(address,
					(ObservableValue<? extends String> observable, String oldValue, String newValue) ->
					{
						if (!newValue.matches(".\\d*"))
						{
							address.setText(newValue.replaceAll("[^.\\d]", ""));
						}
					},
					s -> hostName = address.getText(),
					s -> s,
					borderPane.getChildren()::add);

		setBackground();
		setAndShow(primaryStage, 350, 350, "Main Menu", e -> System.exit(0));
	}

	private void createMenu(SettingsMenu menu)
	{
		Stage stage = new Stage();
		try
		{
			menu.start(stage);
		}
		catch (Exception e) { e.printStackTrace(); }
		primaryStage.hide();
	}
}
