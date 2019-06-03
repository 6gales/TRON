package ru.nsu.g.apleshkov.gui.menu;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.nsu.g.apleshkov.extension.ExtendedTron;
import ru.nsu.g.apleshkov.extension.GameSettings;
import ru.nsu.g.apleshkov.gui.GameLoop;
import ru.nsu.g.apleshkov.gui.colors.ColorMap;

public class OfflineMenu extends SettingsMenu
{
	@Override
	public void start(Stage primaryStage)
	{
		gameService = new ExtendedTron();
		gameSettings = gameService.getSettings();

		displayTitle();
		displaySettings();
		displayPlayersRow();

		setBackground();
		setAndShow(primaryStage, 400, 600, "Menu", e -> System.exit(0));
	}
}