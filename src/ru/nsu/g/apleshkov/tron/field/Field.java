package ru.nsu.g.apleshkov.tron.field;

public class Field
{
	private boolean safeTail;

	private int width,
		height,
		currentTimeStep;

	private int[][] field,
		timeStep;

	private final int defaultValue = 0,
		wall = 11;

	public Field() {}

	public void setSize(int height, int width)
	{
		this.height = height;
		this.width = width;

		field = new int[height][width];
		timeStep = new int[height][width];

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				field[i][j] = defaultValue;
				timeStep[i][j] = 0; //default timestep
			}
		}
	}

	public void setWidth(int width) { this.width = width; }

	public int getWidth() { return width; }

	public void setHeight(int height) { this.height = height; }

	public int getHeight() { return height; }

	public int[][] getField() { return field; }

	public int getDefault() { return defaultValue; }

	public int getWall() { return wall; }

	public void boxMap()
	{

		for (int i = 0; i < width; i++)
		{
			field[0][i] = wall;
			field[height - 1][i] = wall;
		}

		for (int i = 0; i < height; i++)
		{
			field[i][0] = wall;
			field[i][width - 1] = wall;
		}
	}

	public void tunnelMap(boolean isY)
	{
		int yFirstThird = height / 3,
			ySecondThird = 2 * yFirstThird,
			xFirstThird = width / 3,
			xSecondThird = 2 * xFirstThird;

		if (isY)
		{
			for (int i = yFirstThird; i < ySecondThird; i++)
			{
				field[i][xFirstThird] = wall;
				field[i][xSecondThird] = wall;
			}
		}
		else
		{
			for (int i = xFirstThird; i < xSecondThird; i++)
			{
				field[yFirstThird][i] = wall;
				field[ySecondThird][i] = wall;
			}
		}
	}

	public void theWall(boolean isY)
	{
		int yFirstThird = height / 3,
			ySecondThird = 2 * yFirstThird,
			xFirstThird = width / 3,
			xSecondThird = 2 * xFirstThird;

		if (isY)
		{
			for (int i = yFirstThird; i < ySecondThird; i++)
			{
				field[i][width / 2] = wall;
			}
		}
		else
		{
			for (int i = xFirstThird; i < xSecondThird; i++)
			{
				field[height / 2][i] = wall;
			}
		}
	}

	public boolean isSafe(int index, int tailId)
	{
		if (index == defaultValue)
		{
			return true;
		}
		else if (index == tailId)
		{
			return safeTail;
		}
		return false;
//		else if (index > )
	}

	public void setSafeTail(boolean safeTail) { this.safeTail = safeTail; }

	public int getCurrentTimeStep() { return currentTimeStep; }

	public void nextTimeStep() { currentTimeStep++; }

	public void mark(Point point, int id)
	{
		field[point.getY()][point.getX()] = id;
		timeStep[point.getY()][point.getX()] = currentTimeStep;
	}

	public boolean isCurrent(Point point) { return timeStep[point.getY()][point.getX()] == currentTimeStep; }

	public int getIndex(Point point) { return field[point.getY()][point.getX()]; }
}