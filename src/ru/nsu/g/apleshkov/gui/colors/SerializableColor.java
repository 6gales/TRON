package ru.nsu.g.apleshkov.gui.colors;

import javafx.scene.paint.Color;

import java.io.Serializable;

public class SerializableColor implements Serializable
{
	private int id;
	private double r,
		g,
		b,
		o;

	public void setId(int id) { this.id = id; }

	public void setColor(Color color)
	{
		r = color.getRed();
		g = color.getGreen();
		b = color.getBlue();
		o = color.getOpacity();
	}

	public SerializableColor(Color color)
	{
		setColor(color);
	}

	public SerializableColor(int id, Color color)
	{
		this.id = id;
		setColor(color);
	}

	public Color getColor() { return new Color(r, g, b, o); }

	public int getId() { return id; }
}