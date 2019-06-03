package ru.nsu.g.apleshkov.message;

import java.io.Serializable;

public enum MessageType implements Serializable
{
	GAME_SETTINGS,
	PLAYER_SETTINGS,
	PAUSE,
	APPLY,
	BOTS,
	COLORS,
	WALLS,
	PLAYER_ORDER,
	PLAYER_DATA
}
