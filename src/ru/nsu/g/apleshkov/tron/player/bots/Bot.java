package ru.nsu.g.apleshkov.tron.player.bots;

import ru.nsu.g.apleshkov.tron.field.Field;
import ru.nsu.g.apleshkov.tron.field.Point;
import ru.nsu.g.apleshkov.tron.player.Player;

public abstract class Bot extends Player
{
	Bot(String name, int id, Field field, int lives)
	{
		super(name, id, field, lives);
	}

	Bot(String name, int id, Field field, int lives, int tailLen)
	{
		super(name, id, field, lives, tailLen);
	}
}
