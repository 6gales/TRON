package ru.nsu.g.apleshkov.gui.menu.settings;

import ru.nsu.g.apleshkov.gui.colors.SerializableColor;

import java.io.Serializable;

public class SerializableSettings implements Serializable
{
	private String name;
	private boolean isPlayer;
	private SerializableColor color;

	SerializableSettings(String name, boolean isPlayer, SerializableColor color)
	{
		this.name = name;
		this.isPlayer = isPlayer;
		this.color = color;
	}

	public String getName() { return name; }

	public boolean isPlayer() {	return isPlayer; }

	public SerializableColor getColor() { return color; }
}