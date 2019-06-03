package ru.nsu.g.apleshkov.gui.menu;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ru.nsu.g.apleshkov.extension.GameSettings;
import ru.nsu.g.apleshkov.gui.GameLoop;
import ru.nsu.g.apleshkov.gui.colors.ColorMap;
import ru.nsu.g.apleshkov.gui.menu.settings.BotSettings;
import ru.nsu.g.apleshkov.gui.menu.settings.PlayerSettings;
import ru.nsu.g.apleshkov.gui.menu.settings.Settings;
import ru.nsu.g.apleshkov.extension.GameService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class SettingsMenu extends Menu
{
	int row = 1,
		firstPlayerRow;


	GameService gameService;
	GameSettings gameSettings;
	List<Settings> settingsList;

	GridPane gridRoot;

	SettingsMenu()
	{
		root = new GridPane();
		gridRoot = (GridPane) root;
		gridRoot.setHgap(10);
		gridRoot.setVgap(10);
		gridRoot.setPadding(new Insets(0, 10, 0, 10));
	}

	void initAddButton(Button addButton, Callable<Settings> creator)
	{
		addButton.setPrefSize(100, 20);
		addButton.setOnAction(event ->
		                      {
			                      try
			                      {
				                      Settings settings = creator.call();
				                      settingsList.add(settings);
				                      settings.setRemove((e) ->
				                                         {
					                                         int settingsRow = settings.getRow();
					                                         gridRoot.getChildren().removeIf(node -> GridPane.getRowIndex(node) == settingsRow);
					                                         gridRoot.getChildren().forEach(node ->
					                                                                        {
						                                                                        int row = GridPane.getRowIndex(node);
						                                                                        if (row > settingsRow)
							                                                                        GridPane.setRowIndex(node, row - 1);
					                                                                        });
					                                         settingsList.remove(settingsRow - firstPlayerRow);

					                                         for (int i = settingsRow - firstPlayerRow; i < settingsList.size(); i++)
					                                         {
						                                         settingsList.get(i).shift();
					                                         }
					                                         row--;
				                                         });
				                      row++;
			                      }
			                      catch (Exception e) { e.printStackTrace(); }
		                      });
	}

	void displayTitle()
	{
		Label title = new Label("TRON: Light cycles");
		title.setTextFill(Color.WHITE);
		title.setFont(Font.font(Font.getFontNames().get(0), FontWeight.EXTRA_LIGHT, 25));
		gridRoot.add(title, 1, 0, 3, 1);
	}

	void displayIPaddr()
	{
		addWhiteLabel("Your host is:", (c) -> gridRoot.add(c, 0, row));
		try
		{
			addWhiteLabel(InetAddress.getLocalHost().getHostAddress(), c -> gridRoot.add(c, 1, row));
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		row++;
	}

	void displaySettings()
	{


		TextField heightSetter = new TextField(String.valueOf(gameSettings.getHeight())),
				widthSetter = new TextField(String.valueOf(gameSettings.getWidth())),
				lives = new TextField(String.valueOf(gameSettings.getLives())),
				tailLen = new TextField(String.valueOf(gameSettings.getTailLen())),
				scale = new TextField(String.valueOf(gameSettings.getScale()));

		ToggleButton isConstTail = new ToggleButton("Const tail"),
				isSafeTail = new ToggleButton("Safe tail"),
				isArcade = new ToggleButton("Arcade");

		addWhiteLabel("Height:", (c) -> gridRoot.add(c, 0, row));
		setNumericTextField(heightSetter, gameSettings::setHeight, (c) -> gridRoot.add(c, 1, row));
		setToggleButton(isConstTail, gameSettings.isConstTail(), gameSettings::setConstTail, (c) -> gridRoot.add(c, 2, row));
		addWhiteLabel("Lives:", (c) -> gridRoot.add(c, 3, row));
		setNumericTextField(lives, gameSettings::setLives, (c) -> gridRoot.add(c, 4, row));
		row++;

		addWhiteLabel("Width:", (c) -> gridRoot.add(c, 0, row));
		setNumericTextField(widthSetter, gameSettings::setWidth, (c) -> gridRoot.add(c, 1, row));
		setToggleButton(isSafeTail, gameSettings.isSafeTail(), gameSettings::setSafeTail, (c) -> gridRoot.add(c, 2, row));
		addWhiteLabel("Tail len:", (c) -> gridRoot.add(c, 3, row));
		setNumericTextField(tailLen, gameSettings::setTailLen, (c) -> gridRoot.add(c, 4, row));
		row++;

		setToggleButton(isArcade, gameSettings.isArcade(), gameSettings::setArcade, (c) -> gridRoot.add(c, 2, row));
		addWhiteLabel("Scale:", (c) -> gridRoot.add(c, 3, row));
		setNumericTextField(scale, gameSettings::setScale, (c) -> gridRoot.add(c, 4, row));
		row++;
	}

	void displayPlayersRow()
	{
		settingsList = new LinkedList<>();

		List<String> bots = gameService.getBotList();
		Button addPlayer = new Button("Add player"),
				addBot = new Button("Add bot");

		gridRoot.add(addPlayer, 0, row);
		gridRoot.add(addBot, 1, row);

		Button apply = new Button("Apply");
		apply.setOnAction(e -> apply());

		gridRoot.add(apply, 5, row);
		row++;

		addWhiteLabel("Name:", (c) -> gridRoot.add(c, 0, row));
		addWhiteLabel("Color:", (c) -> gridRoot.add(c, 1, row));
		addWhiteLabel("Left:", (c) -> gridRoot.add(c, 2, row));
		addWhiteLabel("Right:", (c) -> gridRoot.add(c, 3, row));
		addWhiteLabel("Pause:", (c) -> gridRoot.add(c, 4, row));
		addWhiteLabel("Remove:", (c) -> gridRoot.add(c, 5, row));
		row++;

		firstPlayerRow = row;

		initAddButton(addPlayer, () -> new PlayerSettings(gridRoot, row, settingsList.size()));
		initAddButton(addBot, () -> new BotSettings(gridRoot, row, bots));
	}

	private void apply()
	{
		StackPane stackPane = new StackPane();
		Stage gameStage = new Stage();

		gameService.applySettings(gameSettings);

		Canvas canvas = new Canvas();

		for (var settings : settingsList)
		{
			try
			{
				int id = gameService.addPlayer(settings.getSerializableVersion());

				if (settings.isPlayer())
					canvas.addEventHandler(KeyEvent.KEY_PRESSED, settings.getController(gameService, id));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		gameService.apply();
		gameSettings = gameService.getSettings();

		canvas.setWidth(gameSettings.getWidth() * gameSettings.getScale());
		canvas.setHeight(gameSettings.getHeight() * gameSettings.getScale());
		GraphicsContext context = canvas.getGraphicsContext2D();

		canvas.setFocusTraversable(true);
		stackPane.getChildren().add(canvas);


		Scene scene = new Scene(stackPane);

		gameStage.setResizable(false);
		gameStage.setTitle("TRON");

		gameStage.setOnCloseRequest(e -> System.exit(0));
		gameStage.setScene(scene);
		gameStage.show();

		new Thread(new GameLoop(context, gameService, gameSettings, new ColorMap(gameService.getColors()))).start();
	}
}