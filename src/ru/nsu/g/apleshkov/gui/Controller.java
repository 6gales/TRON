package ru.nsu.g.apleshkov.gui;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.nsu.g.apleshkov.tron.PlayerOrder;
import ru.nsu.g.apleshkov.extension.GameService;
import ru.nsu.g.apleshkov.tron.player.Direction;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class Controller implements EventHandler<KeyEvent>
{
	private Map<KeyCode, Runnable> orderMap;

	public Controller(GameService gameService, int id, KeyCode left, KeyCode right, KeyCode pause)
	{
		PlayerOrder leftOrder = new PlayerOrder(id, Direction.LEFT),
			rightOrder = new PlayerOrder(id, Direction.RIGHT);

		orderMap = new TreeMap<>();
		orderMap.put(left, () -> gameService.addOrder(leftOrder));
		orderMap.put(right, () -> gameService.addOrder(rightOrder));
		orderMap.put(pause, gameService::pause);
	}

	@Override
	public void handle(KeyEvent event)
	{
		Optional.ofNullable(orderMap.get(event.getCode()))
		        .ifPresent(Runnable::run);
	}
}