package ru.nsu.g.apleshkov.tron.field;

public final class Point
{
	private final int y,
			x;

	public Point(int y, int x)
	{
		this.y = y;
		this.x = x;
	}

	public int getY() { return y; }

	public int getX() { return x; }

	public Point translate(int dy, int dx)
	{
		return new Point(y + dy, x + dx);
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof Point)) return false;
		Point point = (Point)other;
		return x == point.x & y == point.y;
	}

	public boolean equals(int y, int x)
	{
		return this.x == x & this.y == y;
	}
}
