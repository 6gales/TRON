package ru.nsu.g.apleshkov.extension;

import javafx.scene.paint.Color;
import ru.nsu.g.apleshkov.gui.colors.SerializableColor;
import ru.nsu.g.apleshkov.gui.menu.settings.SerializableSettings;
import ru.nsu.g.apleshkov.tron.Tron;

import java.util.LinkedList;
import java.util.List;

public class ExtendedTron extends Tron implements GameSettings, GameService
{
	private int scale;
	private List<SerializableColor> colors;

	public ExtendedTron()
	{
		scale = 10;
		colors = new LinkedList<>();
		colors.add(new SerializableColor(getField().getDefault(), new Color(0.089, 0.019, 0.30, 1)));
		colors.add(new SerializableColor(getField().getWall(), Color.WHITE));
	}

	@Override
	public int addPlayer(SerializableSettings settings) throws Exception
	{
		int id = addPlayer(settings.getName(), settings.isPlayer());
		settings.getColor().setId(id);
		colors.add(settings.getColor());
		return id;
	}

	@Override
	public void update()
	{
		try
		{
			Thread.sleep(25L);//40 fps
		}
		catch (InterruptedException e) { e.printStackTrace(); }
	}

	@Override
	public List<SerializableColor> getColors() { return colors; }

	@Override
	public GameSettings getSettings() { return this; }

	@Override
	public void applySettings(GameSettings settings) { }

	@Override
	public void setScale(int scale) { if (scale > 0) this.scale = scale; }

	@Override
	public int getScale() { return scale; }

	@Override
	public int getDefault() { return getField().getDefault(); }

	@Override
	public int getWall() { return getField().getWall();	}

	@Override
	public void apply() { }
}