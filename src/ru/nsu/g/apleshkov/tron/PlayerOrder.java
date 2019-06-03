package ru.nsu.g.apleshkov.tron;

import ru.nsu.g.apleshkov.tron.player.Direction;

import java.io.Serializable;

public class PlayerOrder implements Serializable
{
	private int id;
	private Direction order;

	public PlayerOrder(int id, Direction order)
	{
		this.id = id;
		this.order = order;
	}

	public int getId() { return id; }

	Direction getOrder() { return order; }
}