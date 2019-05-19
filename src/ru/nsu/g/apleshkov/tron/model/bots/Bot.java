package ru.nsu.g.apleshkov.tron.model.bots;

import ru.nsu.g.apleshkov.tron.model.Player;
import ru.nsu.g.apleshkov.tron.model.field.Field;
import ru.nsu.g.apleshkov.tron.model.field.Point;

public abstract class Bot extends Player implements Runnable
{
	Bot(String name, Point point, int id, Field field, int lives)
	{
		super(name, point, id, field, lives);
	}

	Bot(String name, Point point, int id, Field field, int lives, int tailLen)
	{
		super(name, point, id, field, lives, tailLen);
	}
}
