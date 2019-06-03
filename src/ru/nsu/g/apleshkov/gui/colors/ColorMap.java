package ru.nsu.g.apleshkov.gui.colors;

import javafx.scene.paint.Color;
import ru.nsu.g.apleshkov.tron.field.Field;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ColorMap
{
	private Map<Integer, Color> colorMap;

	public ColorMap() { colorMap = new TreeMap<>(); }

	public ColorMap(Field field)
	{
		colorMap = new TreeMap<>();

		colorMap.put(field.getDefault(), new Color(0.089, 0.019, 0.30, 1));
		colorMap.put(field.getWall(), Color.WHITE);
	}

	public ColorMap(List<SerializableColor> colors)
	{
		colorMap = new TreeMap<>();
		add(colors);
	}

	public void add(int id, Color color) { colorMap.put(id, color); }

	public void add(List<SerializableColor> colors)
	{
		for (var color : colors)
		{
			colorMap.put(color.getId(), color.getColor());
		}
	}

	public Color get(int id) { return colorMap.get(id); }
}