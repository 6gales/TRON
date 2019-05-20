package ru.nsu.g.apleshkov.tron.field;

public final class Vector
{
	private final Point start,
		end;
	private final boolean yChanged,
		increased;

	public Vector(Point start, Point end, boolean increased)
	{
		this.start = start;
		this.end = end;
		yChanged = start.getY() != end.getY();
		this.increased = increased;
	}

	public boolean yChanged() { return yChanged; }

	public boolean isIncreased() { return increased; }

	public boolean opposite(Vector other)
	{
		return yChanged == other.yChanged && increased != other.increased;
	}

	public Vector extendVector(Point newEnd, boolean increased)
	{
		return new Vector(end, newEnd, increased);
	}

	public Point getStart() { return start; }

	public Point getEnd() { return end; }
}
