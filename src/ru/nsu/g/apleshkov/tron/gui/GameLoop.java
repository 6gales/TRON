package ru.nsu.g.apleshkov.tron.gui;

import javafx.scene.canvas.GraphicsContext;
import ru.nsu.g.apleshkov.tron.model.Model;
import ru.nsu.g.apleshkov.tron.model.Player;
import ru.nsu.g.apleshkov.tron.model.field.Field;

import java.util.Map;
import java.util.Set;

public class GameLoop implements Runnable
{

	private Model model;
	private GraphicsContext context;
	private ColorMap colorMap;

	GameLoop(GraphicsContext context, Model model, ColorMap colorMap)
	{
		this.context = context;
		this.model = model;
		this.colorMap = colorMap;
	}

	@Override
	public void run()
	{
		model.start();

		do
		{
			paintField(model.getField());
//			paintField(model.playerSet());
			try
			{
				Thread.sleep(25L);
			}
			catch (InterruptedException e) { break; }

		} while (model.iterate());

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
