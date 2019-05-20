package ru.nsu.g.apleshkov.tron.player.tail;

import ru.nsu.g.apleshkov.tron.field.Field;
import ru.nsu.g.apleshkov.tron.field.Point;

import java.util.LinkedList;
import java.util.List;

public abstract class Tail
{
	int id;
	LinkedList<Point> tail;
	Field field;

	Tail(Field field, int id)
	{
		this.field = field;
		this.id = id;
	}

	public boolean isEmpty() { return tail.isEmpty(); }

	public boolean contains(Point point) { return tail.contains(point); }

	public abstract void add(Point point);

	public void clear()
	{
		for (var point : tail)
		{
			field.mark(point, field.getDefault());
		}

		tail.clear();
	}

	public Point rollback(Point point)
	{
		Point removed;
		do
		{
			removed = tail.removeLast();
			field.mark(removed, field.getDefault());
		} while (!point.equals(removed));

		return tail.peekLast();
	}
	public Point peekLast() { return tail.peekLast(); }

	public List<Point> getList() { return tail; }
}
