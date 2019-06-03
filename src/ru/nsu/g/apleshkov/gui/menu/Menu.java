package ru.nsu.g.apleshkov.gui.menu;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Menu extends Application
{
	final int standartWidth = 100,
					standartHeight = 20;

	Pane root;

	void addWhiteLabel(String text, Consumer<Control> placement)
	{
		Label label = new Label(text);
		label.setTextFill(Color.WHITE);
//		root.add(label, column, row);
		placement.accept(label);
	}

	void setNumericTextField(TextField textField, Consumer<Integer> onChange, Consumer<Control> placement)
	{
		setTextField(textField,
					(ObservableValue<? extends String> observable, String oldValue, String newValue) ->
					{
						if (!newValue.matches("\\d*"))
						{
							textField.setText(newValue.replaceAll("[^\\d]", ""));
						}
					},
					onChange, Integer::parseInt, placement);
	}

	<T> void setTextField(TextField textField,
							ChangeListener<? super String> listener,
							Consumer<T> onChange,
							Function<String, T> translator,
							Consumer<Control> placement)
	{
		textField.textProperty().addListener(listener);

		textField.setPrefSize(standartWidth, standartHeight);
		textField.setMaxSize(standartWidth, standartHeight);
		textField.setOnAction(actionEvent ->
		{
			if (!textField.getText().isEmpty())
				onChange.accept(translator.apply(textField.getText()));
		});

		placement.accept(textField);
	}

	void setToggleButton(ToggleButton toggleButton, boolean initial, Consumer<Boolean> onChange,
	                     Consumer<Control> placement)
	{
		toggleButton.setSelected(initial);
		toggleButton.setPrefSize(standartWidth, standartHeight);
		toggleButton.setOnAction(event -> onChange.accept(toggleButton.isSelected()));

		placement.accept(toggleButton);
	}

	void setBackground()
	{
		root.setBackground(new Background(new BackgroundFill(new Color(0.089, 0.019, 0.30, 1),
		                                                     CornerRadii.EMPTY,
		                                                     Insets.EMPTY)));
	}

	void setAndShow(Stage stage, int height, int width, String title, EventHandler<WindowEvent> onClose)
	{
		stage.setHeight(height);
		stage.setWidth(width);
		Scene scene = new Scene(root);

		stage.setTitle(title);
		stage.setOnCloseRequest(onClose);
		stage.setScene(scene);
		stage.show();
	}
}