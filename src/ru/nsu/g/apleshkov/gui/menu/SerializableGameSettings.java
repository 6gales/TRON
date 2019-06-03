package ru.nsu.g.apleshkov.gui.menu;

import ru.nsu.g.apleshkov.extension.GameSettings;
import ru.nsu.g.apleshkov.tron.Tron;

import java.io.Serializable;

public class SerializableGameSettings implements GameSettings, Serializable
{
	private int
			defaultValue,
			wall,
			tailLen,
			lives,
			height,
			width,
			scale;

	private boolean
			isConstTail,
			safeTail,
			arcade;

	public SerializableGameSettings(int defaultValue, int wall)
	{
		scale = 10;
		this.defaultValue = defaultValue;
		this.wall = wall;
	}
	
	public void initialize(Tron tron)
	{
		setHeight(tron.getHeight());
		setWidth(tron.getWidth());
		setLives(tron.getLives());
		setTailLen(tron.getTailLen());

		setConstTail(tron.isConstTail());
		setSafeTail(tron.isSafeTail());
		setArcade(tron.isArcade());
	}
	
	public void initTron(Tron tron)
	{
		tron.setHeight(getHeight());
		tron.setWidth(getWidth());
		tron.setLives(getLives());
		tron.setTailLen(getTailLen());

		tron.setConstTail(isConstTail());
		tron.setSafeTail(isSafeTail());
		tron.setArcade(isArcade());
	}
	
	@Override
	public void setWidth(int width) { if (width > 0) this.width = width; }

	@Override
	public void setHeight(int height) { if (height > 0) this.height = height; }

	@Override
	public void setArcade(boolean arcade) { this.arcade = arcade; }

	@Override
	public void setConstTail(boolean isConstTail) { this.isConstTail = isConstTail; }

	@Override
	public void setTailLen(int tailLen) { if (tailLen > 0) this.tailLen = tailLen; }

	@Override
	public void setSafeTail(boolean safeTail) { this.safeTail = safeTail; }

	@Override
	public void setLives(int lives) { this.lives = lives; }

	@Override
	public void setScale(int scale) { this.scale = scale; }

	@Override
	public int getWidth() { return width; }

	@Override
	public int getHeight() { return height; }

	@Override
	public boolean isArcade() { return arcade; }

	@Override
	public boolean isSafeTail() { return safeTail; }

	@Override
	public boolean isConstTail() { return isConstTail; }

	@Override
	public int getTailLen() { return tailLen; }

	@Override
	public int getLives() { return lives; }

	@Override
	public int getScale() { return scale; }

	@Override
	public int getDefault() { return defaultValue; }

	@Override
	public int getWall() { return wall; }
}