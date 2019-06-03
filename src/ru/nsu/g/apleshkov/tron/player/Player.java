package ru.nsu.g.apleshkov.tron.player;

import ru.nsu.g.apleshkov.tron.PlayerData;
import ru.nsu.g.apleshkov.tron.field.Point;
import ru.nsu.g.apleshkov.tron.field.Field;
import ru.nsu.g.apleshkov.tron.field.Vector;
import ru.nsu.g.apleshkov.tron.player.tail.GrowingTail;
import ru.nsu.g.apleshkov.tron.player.tail.NonGrowingTail;
import ru.nsu.g.apleshkov.tron.player.tail.Tail;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class Player
{
	private String name;
	private int lives;

	protected Field field;
	private boolean alive;

	private Point startPosition;

	private Vector motion,
			initialMotion;

	protected Point head;
	protected int id,
		speed = 1,
		ySize,
		xSize;

	private Direction order;
	protected Tail tail;

	private Accident accident = null;

	public Player(String name, int id, Field field, int lives)
	{
		init(name, id, field, lives);
		tail = new GrowingTail(field, id);
	}

	public Player(String name, int id, Field field, int lives, int tailLen)
	{
		init(name, id, field, lives);
		tail = new NonGrowingTail(field, id, tailLen);
	}

	private void init(String name, int id, Field field, int lives)
	{
		this.name = name;
		this.field = field;
		this.id = id;
		order = Direction.STRAIGHT;
		alive = true;
		this.lives = lives;
	}

	private void reset()
	{
		accident = null;
		lives--;
		alive = true;
		tail.clear();
		motion = initialMotion;
		head = startPosition;
		tail.add(head);
	}

	public void setPosition(Point startPosition, boolean increase)
	{
		this.startPosition = startPosition;
		initialMotion = new Vector(
				startPosition.translate(0, increase ? -speed : speed),
				startPosition,
				increase);

		head = startPosition;
		motion = initialMotion;

		tail.add(head);

		ySize = field.getHeight();
		xSize = field.getWidth();
	}

	public void move()
	{
		if (!alive && lives == 0)
			return;

		if (!alive)
			reset();

		if (!tail.isEmpty())
		{
			motion = changeDirection(motion, getAndRestore());
		}

		applyMotion();
	}

	protected Vector changeDirection(Vector motion, Direction order)
	{
		boolean isY = motion.yChanged();
		boolean increased = motion.isIncreased();

		int y = 0, x = 0;

		switch (order)
		{
			case STRAIGHT:
				if (isY)
					y += (increased ? speed : -speed);
				else x += (increased ? speed : -speed);
				break;

			case LEFT:
				if (isY)
					x += (increased ? speed : -speed);
				else y += (increased ? -speed : speed);
				break;

			case RIGHT:
				if (isY)
					x += (increased ? -speed : speed);
				else y += (increased ? speed : -speed);
		}

		increased = x >= 0 && y >= 0;

		y = (motion.getEnd().getY() + y + ySize) % ySize;
		x = (motion.getEnd().getX() + x + xSize) % xSize;

		return motion.extendVector(new Point(y, x), increased);
	}

	protected boolean checkPath(Vector path, Predicate<Point> check)
	{
		if (path.yChanged())
		{
			for (int i = path.getStart().getY(),
			     dy = (path.isIncreased() ? 1 : -1);
			     i != path.getEnd().getY();
			     i = (i + dy + ySize) % ySize)
			{
				if (!check.test(new Point(i, path.getStart().getX())))
					return false;
			}
		}
		else
		{
			for (int i = path.getStart().getX(),
			     dx = (path.isIncreased() ? 1 : -1);
			     i != path.getEnd().getX();
			     i = (i + dx + xSize) % xSize)
			{
				if (!check.test(new Point(path.getStart().getY(), i)))
					return false;
			}
		}
		return true;
	}

	public void applyMotion()
	{
		tail.rollback(head);

		if (checkPath(motion, this::checkPoint))
		{
			head = motion.getEnd();

			if (!checkPoint(head))
				head = tail.peekLast();
		}
		else head = tail.peekLast();
	}

	private boolean checkPoint(Point point)
	{
		if (field.isSafe(field.getIndex(point), id))
		{
			tail.add(point);
		}
		else
		{
			if (field.isCurrent(point))
				accident = new Accident(point, field.getIndex(point), id);

			alive = false;
		}
		return alive;
	}

	private Direction getAndRestore()
	{
		Direction last = order;
		order = Direction.STRAIGHT;
		return last;
	}

	public void rollback(Point point)
	{
		head = tail.rollback(point);
	}

	public boolean isAlive() { return alive; }

	public int getId() { return id; }

	public int livesLeft() { return lives; }

	public int getDistance(Point point)
	{
		Point last = motion.getStart();

		int distance;
		if (motion.yChanged())
			distance = (motion.isIncreased() ? point.getY() - last.getY() : last.getY() - point.getY());
		else distance = (motion.isIncreased() ? point.getX() - last.getX() : last.getX() - point.getX());

		if (distance < 0)
		{
			distance = (motion.yChanged() ? ySize : xSize) + distance;
		}
		return distance;
	}

	public String getName() { return name; }

	public void addOrder(Direction newOrder) { order = newOrder; }

	public Accident getAccident() { return accident; }

	public int getSpeed() { return speed; }

	public void setAlive(boolean alive)
	{
		this.alive = alive;
		accident = null;
	}

	public boolean hasAccident() { return accident != null; }

	public Vector getMotion() { return motion; }

	public Point getHead() { return head; }

	public List<Point> getPoints() { return tail.getList(); }

	public void clear() { tail.clear(); }

	public PlayerData getData() { return new PlayerData(name, lives, id, head, new LinkedList<>(tail.getList())); }
}