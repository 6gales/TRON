package ru.nsu.g.apleshkov.tron.player.tail;

import ru.nsu.g.apleshkov.tron.field.Field;
import ru.nsu.g.apleshkov.tron.field.Point;

import java.util.LinkedList;

public class GrowingTail extends Tail
{
	public GrowingTail(Field field, int id)
	{
		super(field, id);
		tail = new LinkedList<>();
	}

	@Override
	public void add(Point point)
	{
		tail.add(point);
		field.mark(point, id);
	}
}
