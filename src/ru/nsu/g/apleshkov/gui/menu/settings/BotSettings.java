package ru.nsu.g.apleshkov.gui.menu.settings;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import ru.nsu.g.apleshkov.extension.GameService;
import ru.nsu.g.apleshkov.gui.Controller;

import java.util.List;

public class BotSettings extends Settings
{
	private ComboBox<String> names;


	public BotSettings(GridPane root, int row, List<String> bots)
	{
		super(row, false);

		names = new ComboBox<>(FXCollections.observableList(bots));
		names.getSelectionModel().select(0);
		names.setPrefSize(100, 20);
		names.setMaxSize(100, 20);

		root.add(names, 0, row);
		root.add(colors, 1, row);
		root.add(remove, 5, row);
	}

	@Override
	public String getName()
	{
		return names.getValue();
	}

	@Override
	public Controller getController(GameService tron, int id) { return null; }
}
