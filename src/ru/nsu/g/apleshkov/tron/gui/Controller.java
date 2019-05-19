package ru.nsu.g.apleshkov.tron.gui;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.nsu.g.apleshkov.tron.model.Direction;
import ru.nsu.g.apleshkov.tron.model.Model;

public class Controller implements EventHandler<KeyEvent>
{
	private Model model;
	private int id;
	private KeyCode leftCode,
					rightCode;

	Controller(Model model, int id, KeyCode left, KeyCode right)
	{
		this.model = model;
		this.id = id;
		leftCode = left;
		rightCode = right;
	}

	@Override
	public void handle(KeyEvent event)
	{
		KeyCode kc = event.getCode();
		if (kc == leftCode)
			model.addOrder(id, Direction.LEFT);
		else if (kc == rightCode)
			model.addOrder(id, Direction.RIGHT);
		else if (kc == KeyCode.ESCAPE)
			model.pause();
	}
}