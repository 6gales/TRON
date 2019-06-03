package ru.nsu.g.apleshkov.tron.player;

import java.io.Serializable;

public enum Direction implements Serializable
{
	STRAIGHT,
	LEFT,
	RIGHT;

	Direction next()
	{
		switch (this)
		{
			case STRAIGHT:
				return LEFT;
			case LEFT:
				return RIGHT;
			case RIGHT:
				return STRAIGHT;
		}
		return null;
	}
}
