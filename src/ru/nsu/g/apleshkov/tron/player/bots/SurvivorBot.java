package ru.nsu.g.apleshkov.tron.player.bots;

import ru.nsu.g.apleshkov.tron.field.Field;
import ru.nsu.g.apleshkov.tron.field.Point;

public class SurvivorBot extends Bot
{
	public SurvivorBot(String name, Point point, int id, Field field, int lives)
	{
		super(name, point, id, field, lives);
	}

	public SurvivorBot(String name, Point point, int id, Field field, int lives, int tailLen)
	{
		super(name, point, id, field, lives, tailLen);
	}

	@Override
	public void run()
	{
		while (isAlive())
		{
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e) { break; }

//					addOrder(Direction.STRAIGHT);
//					break;
//				case 1:
//					addOrder(Direction.LEFT);
//					break;
//				default:
//					addOrder(Direction.RIGHT);
		}
	}
}
