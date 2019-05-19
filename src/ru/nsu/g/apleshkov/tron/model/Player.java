package ru.nsu.g.apleshkov.tron.model;

import ru.nsu.g.apleshkov.tron.model.field.Point;
import ru.nsu.g.apleshkov.tron.model.field.Field;
import ru.nsu.g.apleshkov.tron.model.field.Vector;
import ru.nsu.g.apleshkov.tron.model.tail.GrowingTail;
import ru.nsu.g.apleshkov.tron.model.tail.NonGrowingTail;
import ru.nsu.g.apleshkov.tron.model.tail.Tail;

import java.util.List;

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

	protected Direction order;
	protected Tail tail;

	private Accident accident = null;

	public Player(String name, Point startPosition, int id, Field field, int lives)
	{
		init(name, startPosition, id, field, lives);
		tail = new GrowingTail(field, id);
		tail.add(head);
	}

	public Player(String name, Point startPosition, int id, Field field, int lives, int tailLen)
	{
		init(name, startPosition, id, field, lives);
		tail = new NonGrowingTail(field, id, tailLen);
		tail.add(head);
	}

	private void init(String name, Point startPosition, int id, Field field, int lives)
	{
		this.name = name;
		this.field = field;
		this.id = id;
		order = Direction.STRAIGHT;
		alive = true;
		this.lives = lives;
		ySize = field.getHeight();
		xSize = field.getWidth();
		setPosition(startPosition);
	}

	protected void reset()
	{
		accident = null;
		lives--;
		alive = true;
		tail.clear();
		motion = initialMotion;
		head = startPosition;
		tail.add(head);
	}

	public void setPosition(Point startPosition)
	{
		this.startPosition = startPosition;
		initialMotion = new Vector(
				startPosition,
				startPosition.translate(0, speed * (id % 2 != 0 ? 1 : -1)),
				id % 2 != 0);

		head = startPosition;
		motion = initialMotion;
	}

	public void move()
	{
		if (!alive && lives == 0)
			return;

		if (!alive)
			reset();

		if (!tail.isEmpty())
		{
			boolean isY = motion.yChanged();
			boolean increased = motion.isIncreased();

			int y = 0, x = 0;

			switch (getAndRestore())//this?
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

			y = (head.getY() + y + ySize) % ySize;
			x = (head.getX() + x + xSize) % xSize;

			motion = motion.extendVector(new Point(y, x), increased);
		}

		applyMotion();
	}

	public void applyMotion()
	{
		tail.rollback(head);

		if (motion.yChanged())
		{
			for (int i = head.getY(),
			     dy = (motion.isIncreased() ? 1 : -1);
			     i != motion.getEnd().getY();
			     i = (i + dy + ySize) % ySize)
			{
				if (!checkPoint(new Point(i, head.getX())))
					break;
			}
		}
		else
		{
			for (int i = head.getX(),
			     dx = (motion.isIncreased() ? 1 : -1);
			     i != motion.getEnd().getX();
			     i = (i + dx + xSize) % xSize)
			{
				if (!checkPoint(new Point(head.getY(), i)))
					break;
			}
		}

		head = motion.getEnd();
		if (checkPoint(head))
			tail.add(head);
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

	protected boolean isAlive() { return alive; }

	int getId() { return id; }

	int livesLeft() { return lives; }

	int getDistance(Point point)
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

//		boolean increased = true,
//				isY = point.getY() != last.getY();
//		int distance = (isY ? point.getY() - last.getY() : point.getX() - last.getX());
//
//		if (distance < 0)
//		{
//			increased = false;
//			distance *= -1;
//		}
//
//		int additive = (increased ? -1 : 1);
//
//		if (!tail.contains(point.translate(isY ? additive : 0, isY ? 0 : additive)))
//		{
//			increased = !increased;
//			distance = (isY ? ySize : xSize) - distance;
//		}

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
}