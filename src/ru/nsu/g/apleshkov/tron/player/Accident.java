package ru.nsu.g.apleshkov.tron.player;

import ru.nsu.g.apleshkov.tron.field.Point;

public class Accident
{
	private Point point;
	private int killerId,
		deadId;

	Accident(Point point, int killerId, int deadId)
	{
		this.point = point;
		this.killerId = killerId;
		this.deadId = deadId;
	}

	public Point getPoint()
	{
		return point;
	}

	public int getKillerId()
	{
		return killerId;
	}

	public int getDeadId()
	{
		return deadId;
	}
}
