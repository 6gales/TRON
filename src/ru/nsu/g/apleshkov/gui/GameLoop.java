package ru.nsu.g.apleshkov.gui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import ru.nsu.g.apleshkov.extension.GameService;
import ru.nsu.g.apleshkov.gui.colors.ColorMap;
import ru.nsu.g.apleshkov.extension.GameSettings;
import ru.nsu.g.apleshkov.tron.PlayerData;
import ru.nsu.g.apleshkov.tron.field.Point;

import java.util.List;

public class GameLoop implements Runnable
{
	private GameService tron;
	private GameSettings settings;
	private GraphicsContext context;
	private ColorMap colorMap;
	private List<Point> walls;

	public GameLoop(GraphicsContext context, GameService tron, GameSettings settings, ColorMap colorMap)
	{
		this.context = context;
		this.tron = tron;
		this.settings = settings;
		this.colorMap = colorMap;
	}

	@Override
	public void run()
	{
		tron.start();
		paintField(tron.getPlayersData());

		do
		{
			tron.update();

			if (tron.isPaused())
			{
				context.setFill(Color.BEIGE);
				context.setFont(new Font("Arial", 30));
				context.fillText("PAUSED",
								settings.getWidth() * settings.getScale() * 3 / 8,
								settings.getHeight() * settings.getScale() * 3 / 8);
			}

			paintField(tron.getPlayersData());


		} while (tron.iterate());



	}

	private void paintField(List<PlayerData> playersData)
	{
		context.setFill(colorMap.get(settings.getDefault()));
		context.fillRect(0, 0, settings.getWidth() * settings.getScale(), settings.getHeight() * settings.getScale());

//		context.setFill(colorMap.get(tron.getWall()));
//		walls.forEach(point -> context.fillRect(point.getX() * scale, point.getY() * scale, scale, scale));

		playersData.forEach((data) ->
		{
			context.setFill(colorMap.get(data.getId()));
			context.fillText(data.getName() + ": " + data.getLives(),
						10,
						10 * data.getId() + 10);
			data.getTail()
					.forEach(point ->
							context.fillRect(point.getX() * settings.getScale(),
											point.getY() * settings.getScale(),
											settings.getScale(),
											settings.getScale()));
		});

	}
}
