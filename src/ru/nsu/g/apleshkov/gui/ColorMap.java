package ru.nsu.g.apleshkov.gui;

import javafx.scene.paint.Color;
import ru.nsu.g.apleshkov.tron.field.Field;

import java.util.Map;
import java.util.TreeMap;

class ColorMap
{
	private Map<Integer, Color> colorMap;

	ColorMap(Field field)
	{
		colorMap = new TreeMap<>();

		colorMap.put(field.getDefault(), new Color(0.089, 0.019, 0.30, 1));
		colorMap.put(field.getWall(), Color.WHITE);
	}

	void add(int id, Color color) { colorMap.put(id, color); }

	Color get(int id) { return colorMap.get(id); }
}