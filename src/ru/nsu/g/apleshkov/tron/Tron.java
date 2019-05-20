package ru.nsu.g.apleshkov.tron;

import ru.nsu.g.apleshkov.tron.field.Point;
import ru.nsu.g.apleshkov.tron.player.Player;
import ru.nsu.g.apleshkov.tron.player.Accident;
import ru.nsu.g.apleshkov.tron.player.bots.Bot;
import ru.nsu.g.apleshkov.tron.field.Field;
import ru.nsu.g.apleshkov.tron.player.Direction;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tron
{
	private final int maxPlayers = 4;
	private int tailLen,
		lives,
		height,
		width;

	private Properties botList;

	private boolean
		pause,
		constTailLen,
		safeTail,
		arcade,
		botsLoaded;

	private Map<Integer, Player> players;
	private Map<Integer, Callable<Player>> playerCreators;

	private List<String> listedBots;
	private Queue<Integer> deleteQueue;

	private Field field;

	private Lock lock;
	private Condition unpaused;

	public Tron()
	{
		field = new Field();
		playerCreators = new TreeMap<>();
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
			botList.load(in);
			botsLoaded = true;

			var bots = botList.entrySet();
			for (var entry : bots)
			{
				listedBots.add((String)entry.getKey());
			}
		}
		catch (IOException e)
		{
			botsLoaded = false;
		}
	}

	private void init()
	{
		playerCreators.clear();
		players.clear();

		pause = false;
		constTailLen = true;
		safeTail = false;
		arcade = false;

		tailLen = 160;
		lives = 3;
		height = 70;
		width = 140;
	}

	public void addPlayer(String name, int id)
	{
		playerCreators.put(id, () ->
		{
			Point p = new Point(height / (playerCreators.size() > 2 ? 3 : 2) * (id > 2 ? 2 : 1),
			                    width / 6 * (id % 2 == 0 ? 5 : 1));
			return (!constTailLen
					? new Player(name, p, id, field, lives)
					: new Player(name, p, id, field, lives, tailLen));
		});
	}

	public void addBot(String name, int id)
	{
		if (!botsLoaded)
			return;

		playerCreators.put(id, () ->
		{
			Point p = new Point(height / (playerCreators.size() > 2 ? 3 : 2) * (id > 2 ? 2 : 1),
								width / 6 * (id % 2 == 0 ? 5 : 1));
			Class<?> classDef = Class.forName(botList.getProperty(name));
			Bot bot = (Bot)(constTailLen
					? classDef.getConstructor(Point.class, int.class, Field.class, int.class)
							.newInstance(p, id, field, lives)
					: classDef.getConstructor(Point.class, int.class, Field.class, int.class, int.class)
							.newInstance(p, id, field, lives, tailLen));

			new Thread(bot).start();
			return bot;
		});
	}

	public void start()
	{
		field.setSize(height, width);
		field.setSafeTail(safeTail);

		for (var entry : playerCreators.entrySet())
		{
			try
			{
				players.put(entry.getKey(), entry.getValue().call());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
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
//			killer.rollback(point);
			killer.setAlive(false);
			if (killer.livesLeft() == 0)
				deleteQueue.add(killer.getId());
			return;
		}//???

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
			unpaused.signal();
			lock.unlock();
		}
	}

	public void addOrder(int id, Direction order) { Optional.ofNullable(players.get(id)).ifPresent(p -> p.addOrder(order)); }

	public List<String> getBotList() { if (botsLoaded) return listedBots; return null; }

	public void setArcade(boolean arcade) { this.arcade = arcade; }

	public void setConstTailLen(boolean constTailLen) { this.constTailLen = constTailLen; }

	public void setSafeTail(boolean safeTail) { this.safeTail = safeTail; }

	public boolean isArcade() { return arcade; }

	public boolean isSafeTail() { return safeTail; }

	public boolean isConstTailLen() { return constTailLen; }

	public int getMaxPlayers() { return maxPlayers; }

	public int getTailLen() { return tailLen; }

	public void setTailLen(int tailLen) { this.tailLen = tailLen; }

	public int getLives() { return lives; }

	public void setLives(int lives) { this.lives = lives; }

	public boolean isBotsLoaded() { return botsLoaded; }

	public int getHeight() { return height; }

	public int getWidth() { return width; }

	public void setWidth(int width) { this.width = width; }

	public void setHeight(int height) { this.height = height; }

	public Field getField() { return field; }

	public void reset() { init(); }

	public Set<Map.Entry<Integer, Player>> playerSet() { return players.entrySet(); }
}