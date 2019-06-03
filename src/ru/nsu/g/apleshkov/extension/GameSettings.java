package ru.nsu.g.apleshkov.extension;

public interface GameSettings
{
	void setWidth(int width);

	void setHeight(int height);

	void setArcade(boolean arcade);

	void setConstTail(boolean isConstTail);

	void setTailLen(int tailLen);

	void setSafeTail(boolean safeTail);

	void setLives(int lives);

	void setScale(int scale);

	int getWidth();

	int getHeight();

	boolean isArcade();

	boolean isSafeTail();

	boolean isConstTail();

	int getTailLen();

	int getLives();

	int getScale();

	int getDefault();

	int getWall();
}