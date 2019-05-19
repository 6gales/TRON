package ru.nsu.g.apleshkov.tron.model.tail;

import ru.nsu.g.apleshkov.tron.model.field.Point;
import ru.nsu.g.apleshkov.tron.model.field.Field;

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
