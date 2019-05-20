package ru.nsu.g.apleshkov.gui;

import javafx.scene.canvas.GraphicsContext;
import ru.nsu.g.apleshkov.tron.Tron;
import ru.nsu.g.apleshkov.tron.field.Field;
import ru.nsu.g.apleshkov.tron.player.Player;

import java.util.Map;
import java.util.Set;

public class GameLoop implements Runnable
{

	private Tron tron;
	private GraphicsContext context;
	private ColorMap colorMap;

	GameLoop(GraphicsContext context, Tron tron, ColorMap colorMap)
	{
		this.context = context;
		this.tron = tron;
		this.colorMap = colorMap;
	}

	@Override
	public void run()
	{
		tron.start();

		do
		{
			paintField(tron.getField());
//			paintField(tron.playerSet());
			try
			{
				Thread.sleep(25L);
			}
			catch (InterruptedException e) { break; }

		} while (tron.iterate());

	}

	private void paintField(Field field)
	{
		context.setFill(colorMap.get(0));
		context.fillRect(0, 0, 1400, 700);

		int[][] pf = field.getField();

		for (int i = 0; i < field.getHeight(); i++)
		{
			for (int j = 0; j < field.getWidth(); j++)
			{
				if (pf[i][j] != field.getDefault())
				{
					context.setFill(colorMap.get(pf[i][j]));
					context.fillRect(j * 10, i * 10, 10, 10);
				}
			}
		}
	}

	private void paintField(Set<Map.Entry<Integer, Player>> players)
	{
		context.setFill(colorMap.get(0));
		context.fillRect(0, 0, 1400, 700);

		players.forEach((entry) -> {
			context.setFill(colorMap.get(entry.getKey()));
			entry.getValue()
					.getPoints()
					.forEach(point ->
							context.fillRect(point.getX() * 10, point.getY() * 10, 10, 10));
		});

	}
}
