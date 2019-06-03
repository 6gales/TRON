package ru.nsu.g.apleshkov.tron;

import ru.nsu.g.apleshkov.tron.exception.BotsNotFoundedException;
import ru.nsu.g.apleshkov.tron.exception.OutOfIdException;
import ru.nsu.g.apleshkov.tron.field.Point;
import ru.nsu.g.apleshkov.tron.player.Player;
import ru.nsu.g.apleshkov.tron.player.Accident;
import ru.nsu.g.apleshkov.tron.field.Field;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tron
{
	private int maxPlayers = 100,
		tailLen,
		lives,
		height,
		width;

	private Properties botList;

	private boolean
		pause,
		isConstTail,
		safeTail,
		arcade;

	private Queue<Integer> idPool;
	private Map<Integer, Player> players;

	private List<String> listedBots;
	private Queue<Integer> deleteQueue;

	private Field field;

	private Lock lock;
	private Condition unpaused;

	public Tron()
	{
		field = new Field();
		idPool = new LinkedList<>();
		players = new TreeMap<>();
		deleteQueue = new LinkedList<>();

		lock = new ReentrantLock();
		unpaused = lock.newCondition();

		init();

		InputStream in = this.getClass().getResourceAsStream("BotList.properties");
		listedBots = new LinkedList<>();
		botList = new Properties();
		try
		{
			if (in != null)
				botList.load(in);

			var bots = botList.entrySet();
			for (var entry : bots)
			{
				listedBots.add((String)entry.getKey());
			}
		}
		catch (IOException ignore) {}
	}

	private void init()
	{
		players.clear();

		idPool.clear();
		for (int i = 0; i < maxPlayers; i++)
		{
			idPool.add(i);
		}

		pause = false;
		isConstTail = false;
		safeTail = false;
		arcade = false;

		tailLen = 160;
		lives = 3;
		height = 70;
		width = 140;
	}

	public void reset() { init(); }

	public int addPlayer(String name) throws Exception
	{
		return addPlayer(name, true);
	}

	public int addPlayer(String name, boolean player) throws Exception
	{
		if (idPool.isEmpty())
			throw new OutOfIdException();

		if (!player && listedBots.size() == 0)
			throw new BotsNotFoundedException();

		int id = idPool.poll();

		if (player)
		{
			players.put(id, !isConstTail
					? new Player(name, id, field, lives)
					: new Player(name, id, field, lives, tailLen));
		}
		else
		{
			players.put(id, (Player)(isConstTail
					? Class.forName(botList.getProperty(name))
					       .getConstructor(int.class, Field.class, int.class, int.class)
					       .newInstance(id, field, lives, tailLen)
					: Class.forName(botList.getProperty(name))
					       .getConstructor(int.class, Field.class, int.class)
					       .newInstance(id, field, lives)));
		}

		return id;
	}

	public void remove(int id) { Player p = players.remove(id); if (p != null) idPool.add(id); }

	//GAME PROCESS

	public void start()
	{
		field.setSize(height, width);
		field.setSafeTail(safeTail);

		boolean increase = true;
		int columnSize = players.size() / 2 + (players.size() % 2 == 0 ? 0 : 1),
			parts = height / (columnSize + 1),
			line = parts,
			column = width / 6;

		for (var entry : players.entrySet())
		{
			entry.getValue()
				.setPosition(new Point(line, column * (increase ? 1 : 5)), increase);
			increase = !increase;
			if (increase)
				line += parts;
		}

		lock.lock();
	}
	
	public boolean iterate()
	{
		if (players.size() > 1)
		{
			if (pause)
			{
				try
				{
					unpaused.await();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			Player player;
			for (var entry : players.entrySet())
			{
				player = entry.getValue();
				player.move();

				if (player.hasAccident())
					investigateAccident(player.getAccident());

				if (!player.isAlive() && player.livesLeft() == 0)
					deleteQueue.add(player.getId());
			}

			while (deleteQueue.size() > 0)
			{
				playerLost(deleteQueue.poll());
			}

			field.nextTimeStep();
			return true;
		}

		return false;
	}

	private void playerLost(int id)
	{
		players.get(id).clear();
		players.remove(id);
	}

	private void investigateAccident(Accident accident)
	{
		Player killer = players.get(accident.getKillerId()),
				dead = players.get(accident.getDeadId());

		if (killer == dead)
			return;

		Point point = accident.getPoint();

		if (killer.getMotion().opposite(dead.getMotion()))
		{
			killer.setAlive(false);
			if (killer.livesLeft() == 0)
				deleteQueue.add(killer.getId());
			return;
		}

		int killerDistance = killer.getDistance(point),
				deadDistance = dead.getDistance(point),
				killerSpeed = killer.getSpeed(),
				deadSpeed = dead.getSpeed();

		float killerTime = (float)killerDistance / killerSpeed,
				deadTime = (float)deadDistance / deadSpeed;

		if (killerTime < deadTime)
		{
			if (dead.livesLeft() == 0)
				deleteQueue.add(dead.getId());
			return;
		}

		killer.rollback(point);
		killer.setAlive(false);
		if (killer.livesLeft() == 0)
			deleteQueue.add(killer.getId());

		if (killerTime > deadTime)
		{
			dead.setAlive(true);
			dead.applyMotion();

			if (dead.hasAccident())
				investigateAccident(dead.getAccident());
		}
	}

	public void pause()
	{
		pause = !pause;
		if (!pause)
		{
			lock.lock();
			unpaused.signalAll();
			lock.unlock();
		}
	}

	public void addOrder(PlayerOrder order)
	{
		Optional.ofNullable(players.get(order.getId()))
		        .ifPresent(p -> p.addOrder(order.getOrder()));
	}

	//SETTINGS SETTERS

	public void setWidth(int width) { if (width > 0) this.width = width; }

	public void setHeight(int height) { if (height > 0) this.height = height; }

	public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

	public void setArcade(boolean arcade) { this.arcade = arcade; }

	public void setConstTail(boolean isConstTail) { this.isConstTail = isConstTail; }

	public void setTailLen(int tailLen) { if (tailLen > 0) this.tailLen = tailLen; }

	public void setSafeTail(boolean safeTail) { this.safeTail = safeTail; }

	public void setLives(int lives) { this.lives = lives; }

	//SETTINGS GETTERS

	public int getWidth() { return width; }

	public int getHeight() { return height; }

	public int getMaxPlayers() { return maxPlayers; }

	public boolean isArcade() { return arcade; }

	public boolean isSafeTail() { return safeTail; }

	public boolean isConstTail() { return isConstTail; }

	public int getTailLen() { return tailLen; }

	public int getLives() { return lives; }

	//GAMEDATA GETTERS

	public boolean isPaused() { return pause; }

	public List<String> getBotList() { return listedBots; }

	public Field getField() { return field; }

	public List<PlayerData> getPlayersData()
	{
		List<PlayerData> data = new LinkedList<>();
		for (var entry : players.entrySet())
		{
			data.add(entry.getValue().getData());
		}

		return data;
	}
}