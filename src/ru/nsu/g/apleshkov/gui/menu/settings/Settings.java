package ru.nsu.g.apleshkov.gui.menu.settings;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import ru.nsu.g.apleshkov.extension.GameService;
import ru.nsu.g.apleshkov.gui.Controller;
import ru.nsu.g.apleshkov.gui.colors.SerializableColor;

public abstract class Settings
{
	private int row;

	private boolean isPlayer;

	ComboBox<Color> colors;

	Button remove;

	private static ObservableList<Color> colorsToChoose = FXCollections.observableArrayList(
			Color.BLUE,
			Color.RED,
			Color.DEEPPINK,
			Color.DARKORCHID,
			Color.GREEN,
			Color.DARKORANGE,
			Color.GOLD,
			Color.DARKGRAY,
			Color.ROYALBLUE,
			Color.BLANCHEDALMOND,
			Color.LIGHTBLUE);

	private static int nextColor = 0;

	private void initColors()
	{
		colors.setCellFactory(lv -> new ListCell<>()
		{
			@Override
			protected void updateItem(Color item, boolean empty)
			{
				super.updateItem(item, empty);

				if (empty || item == null)
				{
					setBackground(Background.EMPTY);
				}
				else setBackground(new Background(new BackgroundFill(item,
				                                                     CornerRadii.EMPTY,
				                                                     Insets.EMPTY)));
				StackPane arrowButton = (StackPane)colors.lookup(".arrow-button");
				if (arrowButton != null)
					arrowButton.setBackground(getBackground());
			}
		});
		colors.setButtonCell(colors.getCellFactory().call(null));

		colors.setValue(colorsToChoose.get(nextColor));
		nextColor = (nextColor + 1) % colorsToChoose.size();
	}

	public void setRemove(EventHandler<ActionEvent> rm) { remove.setOnAction(rm); }

	public int getRow() { return row; }

	public void shift() { row--; }

	public Settings(int row, boolean isPlayer)
	{
		this.row = row;
		this.isPlayer = isPlayer;
		colors = new ComboBox<>(colorsToChoose);
		colors.setPrefSize(100, 20);
		colors.setMaxSize(100, 20);
		initColors();

		remove = new Button();
		remove.setPrefSize(30, 20);
	}

	public abstract String getName();

	public abstract Controller getController(GameService tron, int id);

	public boolean isPlayer() { return isPlayer; }

	public Color getColor() { return colors.getValue(); }

	public SerializableSettings getSerializableVersion()
	{
		return new SerializableSettings(getName(), isPlayer, new SerializableColor(getColor()));
	}
}