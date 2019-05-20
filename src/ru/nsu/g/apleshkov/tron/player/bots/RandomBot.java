package ru.nsu.g.apleshkov.tron.player.bots;

import ru.nsu.g.apleshkov.tron.field.Field;
import ru.nsu.g.apleshkov.tron.field.Point;
import ru.nsu.g.apleshkov.tron.player.Direction;

import java.util.Random;

public class RandomBot extends Bot
{
	public RandomBot(Point point, int id, Field field, int lives)
	{
		super("Random Bot", point, id, field, lives);
	}

	public RandomBot(Point point, int id, Field field, int lives, int tailLen)
	{
		super("Random Bot", point, id, field, lives, tailLen);
	}

	@Override
	public void run()
	{
		Random random = new Random();

		while (isAlive())
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e) { break; }

			switch (random.nextInt(3))
			{
				case 0:
					addOrder(Direction.STRAIGHT);
					break;
				case 1:
					addOrder(Direction.LEFT);
					break;
				default:
					addOrder(Direction.RIGHT);
			}
		}
	}
}
