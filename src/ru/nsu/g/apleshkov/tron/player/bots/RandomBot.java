package ru.nsu.g.apleshkov.tron.player.bots;

import ru.nsu.g.apleshkov.tron.field.Field;
import ru.nsu.g.apleshkov.tron.field.Point;
import ru.nsu.g.apleshkov.tron.player.Direction;

import java.util.Random;

public class RandomBot extends Bot
{
	public RandomBot(int id, Field field, int lives)
	{
		super("Random Bot", id, field, lives);
	}

	public RandomBot(int id, Field field, int lives, int tailLen)
	{
		super("Random Bot", id, field, lives, tailLen);
	}

	private Random random = new Random();

	@Override
	public void move()
	{
		switch (random.nextInt(40))
		{
			case 0:
				addOrder(Direction.LEFT);
				break;
			case 1:
				addOrder(Direction.RIGHT);
				break;
			default:
				addOrder(Direction.STRAIGHT);
		}
		super.move();
	}
}
