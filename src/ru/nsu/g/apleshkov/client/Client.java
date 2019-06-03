package ru.nsu.g.apleshkov.client;

import ru.nsu.g.apleshkov.extension.GameService;
import ru.nsu.g.apleshkov.gui.colors.SerializableColor;
import ru.nsu.g.apleshkov.extension.GameSettings;
import ru.nsu.g.apleshkov.gui.menu.settings.SerializableSettings;
import ru.nsu.g.apleshkov.message.Message;
import ru.nsu.g.apleshkov.message.MessageType;
import ru.nsu.g.apleshkov.tron.PlayerData;
import ru.nsu.g.apleshkov.tron.PlayerOrder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements Runnable, GameService
{
	private boolean host;
	private String hostname;
	private int port;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	private Lock lock;
	private Condition updated;
	private int recievedId;

	private boolean pause,
			recievedSettings,
			recievedColors,
			recievedData;

	private List<String> bots;
	private List<SerializableColor> colors;
	private List<PlayerData> playerData;
	private GameSettings settings;

	public Client(String hostname, int port) throws IOException
	{
		this.hostname = hostname;
		this.port = port;
		socket = new Socket(hostname, port);
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());

		lock = new ReentrantLock();
		updated = lock.newCondition();

		try
		{
			Message in = (Message) input.readObject();
			settings = (GameSettings) in.getData();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		recievedSettings = true;
		recievedColors = false;
		recievedData = false;
	}

	@Override
	public void run()
	{
		while (!socket.isClosed())
		{
			System.out.println("runnin n readin");
			try
			{
				Message in = (Message)input.readObject();

				switch (in.getType())
				{
					case GAME_SETTINGS:
						lock.lock();
						settings = (GameSettings)in.getData();
						updated.signal();
						lock.unlock();
						break;

					case PLAYER_SETTINGS:
						System.out.println("recieved back player settings, ret id is ");
						lock.lock();
						recievedId = (Integer)in.getData();
						recievedSettings = true;
						updated.signal();
						lock.unlock();
						break;

					case BOTS:
						bots = (List<String>)in.getData();
						break;

					case COLORS:
						System.out.println("RECIEVED COLORSSSS");
						lock.lock();
						recievedColors = true;
						colors = (List<SerializableColor>)in.getData();
						updated.signal();
						lock.unlock();
						break;

					case PAUSE:
						pause = !pause;
						break;

					case PLAYER_DATA:
						System.out.println("got data: " + recievedData);
						lock.lock();
						recievedData = true;
						playerData = (List<PlayerData>)in.getData();
						updated.signal();
						lock.unlock();
						break;
				}

			}
			catch (Exception ex)
			{
				System.out.println("Error reading from server: " + ex.getMessage());
				ex.printStackTrace();
				break;
			}
		}

	}

	void sendMessage(Message message)
	{
		try
		{
			output.writeObject(message);
		}
		catch (IOException e) { e.printStackTrace(); }
	}

	@Override
	public int addPlayer(SerializableSettings settings) throws Exception
	{
		lock.lock();
		sendMessage(new Message(MessageType.PLAYER_SETTINGS, settings));
		try
		{
			updated.await();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		lock.unlock();
		return recievedId;
	}

	@Override
	public void start() { }

	@Override
	public void update()
	{
//		lock.lock();
//		try
//		{
//			updated.await();
//		}
//		catch (InterruptedException e)
//		{
//			e.printStackTrace();
//		}
//		lock.unlock();
	}

	@Override
	public boolean iterate() { return playerData.size() > 1; }

	@Override
	public void pause() { sendMessage(new Message(MessageType.PAUSE, !pause)); }

	@Override
	public void addOrder(PlayerOrder order) { sendMessage(new Message(MessageType.PLAYER_ORDER, order)); }

	@Override
	public boolean isPaused() { return pause; }

	@Override
	public List<String> getBotList() { return bots; }

	@Override
	public List<SerializableColor> getColors()
	{
		lock.lock();
		try
		{
			while (!recievedColors)
				updated.await();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		lock.unlock();
		return colors;
	}

	@Override
	public List<PlayerData> getPlayersData()
	{
		lock.lock();
		try
		{
			while (!recievedData)
				updated.await();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		recievedData = false;
		lock.unlock();
		return playerData;
	}

	@Override
	public GameSettings getSettings()
	{
		lock.lock();
		try
		{
			while (!recievedSettings)
				updated.await();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		lock.unlock();
		return settings;
	}

	@Override
	public void applySettings(GameSettings settings)
	{
		recievedSettings = false;
		if (settings != null)
			sendMessage(new Message(MessageType.GAME_SETTINGS, (Serializable)settings));
	}

	@Override
	public void apply()
	{
		sendMessage(new Message(MessageType.APPLY, true));
		lock.lock();
		try
		{
			updated.await();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		lock.unlock();
	}
}