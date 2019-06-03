package ru.nsu.g.apleshkov.tron.player.bots;

import ru.nsu.g.apleshkov.tron.field.Field;
import ru.nsu.g.apleshkov.tron.field.Point;
import ru.nsu.g.apleshkov.tron.field.Vector;
import ru.nsu.g.apleshkov.tron.player.Direction;

import java.util.Random;

public class SurvivorBot extends Bot
{
	protected int safeDistance = 10;
	protected int checkTurn = 8;
	private int turn;

	public SurvivorBot(int id, Field field, int lives)
	{
		super("SurvivorBot", id, field, lives);
		Random rand = new Random();
		turn = rand.nextInt(checkTurn);
	}

	public SurvivorBot(int id, Field field, int lives, int tailLen)
	{
		super("SurvivorBot", id, field, lives, tailLen);
		Random rand = new Random();
		turn = rand.nextInt(checkTurn);
	}

	private boolean isSafe(Point point)
	{
		return field.isSafe(field.getIndex(point), id);
	}

	private int getSafeLevel(int safeDistance, Direction direction)
	{
		tail.rollback(head);
		Vector nextMotion = getMotion();
		for (int i = 0; i < safeDistance; i++)
		{
			nextMotion = changeDirection(nextMotion, direction);
			if (!checkPath(nextMotion, this::isSafe))
			{
				tail.add(head);
				return i;
			}
			direction = Direction.STRAIGHT;
		}
		tail.add(head);
		return safeDistance;
	}

	@Override
	public void move()
	{
		if (isAlive() && turn == 0)
		{

			Direction nextDirection = Direction.STRAIGHT;
			int maxSafeLevel = 0;

			for (int i = 0; i < Direction.values().length; i++)
			{
				int safeLevel = getSafeLevel(safeDistance, Direction.values()[i]);
				if (safeLevel < safeDistance)
				{
					if (safeLevel > maxSafeLevel)
					{
						nextDirection = Direction.values()[i];
						maxSafeLevel = safeLevel;
					}
				}
				else
				{
					nextDirection = Direction.values()[i];
					break;
				}
			}
			addOrder(nextDirection);
		}

		turn = (turn + 1) % checkTurn;

		super.move();
	}

}
