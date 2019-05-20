package ru.nsu.g.apleshkov.tron.player.tail;

import ru.nsu.g.apleshkov.tron.field.Field;
import ru.nsu.g.apleshkov.tron.field.Point;

import java.util.LinkedList;

public class NonGrowingTail extends Tail
{
	private int len;

	public NonGrowingTail(Field field, int id, int len)
	{
		super(field, id);
		this.len = len;
		tail = new LinkedList<>();
	}

	@Override
	public void add(Point point)
	{
		if (tail.size() == len)
		{
			field.mark(tail.remove(0), field.getDefault());
		}

		tail.add(point);
		field.mark(point, id);
	}
}