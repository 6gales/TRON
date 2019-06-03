package ru.nsu.g.apleshkov.tron;

import ru.nsu.g.apleshkov.tron.field.Point;

import java.io.Serializable;
import java.util.List;

public class PlayerData implements Serializable
{
	private String name;
	private int lives;
	private int id;
	private Point head;
	private List<Point> tail;

	public PlayerData(String name, int lives, int id, Point head, List<Point> tail)
	{
		this.name = name;
		this.lives = lives;
		this.id = id;
		this.head = head;
		this.tail = tail;
	}

	public String getName() { return name; }

	public int getLives() {	return lives; }

	public int getId() { return id; }

	public Point getHead() { return head; }

	public List<Point> getTail() { return tail; }
}