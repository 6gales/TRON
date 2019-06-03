package ru.nsu.g.apleshkov.extension;

import ru.nsu.g.apleshkov.gui.colors.SerializableColor;
import ru.nsu.g.apleshkov.gui.menu.settings.SerializableSettings;
import ru.nsu.g.apleshkov.tron.PlayerData;
import ru.nsu.g.apleshkov.tron.PlayerOrder;

import java.util.List;

public interface GameService
{
	int addPlayer(SerializableSettings settings) throws Exception;

	void start();

	void update();

	boolean iterate();

	void pause();

	void addOrder(PlayerOrder order);

	boolean isPaused();

	List<String> getBotList();

	List<SerializableColor> getColors();

	List<PlayerData> getPlayersData();

	GameSettings getSettings();

	void applySettings(GameSettings settings);

	void apply();
}