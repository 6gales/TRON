package ru.nsu.g.apleshkov.gui;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.nsu.g.apleshkov.tron.Tron;
import ru.nsu.g.apleshkov.tron.player.Direction;

public class Controller implements EventHandler<KeyEvent>
{
	private Tron tron;
	private int id;
	private KeyCode leftCode,
					rightCode,
					pauseCode;

	Controller(Tron tron, int id, KeyCode left, KeyCode right, KeyCode pauseCode)
	{
		this.tron = tron;
		this.id = id;
		leftCode = left;
		rightCode = right;
		this.pauseCode = pauseCode;
	}

	@Override
	public void handle(KeyEvent event)
	{
		KeyCode kc = event.getCode();
		if (kc == leftCode)
			tron.addOrder(id, Direction.LEFT);
		else if (kc == rightCode)
			tron.addOrder(id, Direction.RIGHT);
		else if (kc == pauseCode)
			tron.pause();
	}
}