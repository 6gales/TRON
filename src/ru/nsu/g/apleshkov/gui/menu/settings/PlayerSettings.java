package ru.nsu.g.apleshkov.gui.menu.settings;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import ru.nsu.g.apleshkov.extension.GameService;
import ru.nsu.g.apleshkov.gui.Controller;

import java.util.ArrayList;

public class PlayerSettings extends Settings
{
	private static KeyCode[][] defaultKeys =
			{{ KeyCode.LEFT, KeyCode.RIGHT, KeyCode.ESCAPE},
			{ KeyCode.A, KeyCode.D, KeyCode.Q },
			{ KeyCode.J, KeyCode.L, KeyCode.O }};
	private static int nextKeySet = 0;

	private final int leftKey = 0,
			rightKey = 1,
			pauseKey = 2;

	private TextField name;

	private ArrayList<KeyCode> keys;

	private void keyCodeTextField(TextField textField, int keyType)
	{
		keys.add(defaultKeys[nextKeySet][keyType]);
		textField.setMaxSize(60, 20);
		textField.setText(keys.get(keyType).getName());
		textField.textProperty().addListener(
				(ObservableValue<? extends String> observable, String oldValue, String newValue) -> {});
		textField.setOnKeyPressed(event ->
		{
			keys.set(keyType, event.getCode());
			System.out.println(event.getCode().getName());
			textField.setText(event.getCode().getName());
		});
	}

	public PlayerSettings(GridPane root, int row, int playerNum)
	{
		super(row, true);

		name = new TextField("Player " + playerNum);
		name.setPrefSize(100, 20);
		name.setMaxSize(100, 20);


		keys = new ArrayList<>();

		TextField left = new TextField(),
				right = new TextField(),
				pause = new TextField();

		keyCodeTextField(left, leftKey);
		keyCodeTextField(right, rightKey);
		keyCodeTextField(pause, pauseKey);
		nextKeySet = (nextKeySet + 1) % defaultKeys.length;

		root.add(name, 0, row);
		root.add(colors, 1, row);
		root.add(left, 2, row);
		root.add(right, 3, row);
		root.add(pause, 4, row);
		root.add(remove, 5, row);
	}

	@Override
	public String getName() { return name.getText(); }

	@Override
	public Controller getController(GameService tron, int id)
	{
		return new Controller(tron, id, keys.get(leftKey), keys.get(rightKey), keys.get(pauseKey));
	}
}