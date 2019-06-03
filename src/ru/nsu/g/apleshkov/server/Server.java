package ru.nsu.g.apleshkov.server;

import javafx.scene.paint.Color;
import ru.nsu.g.apleshkov.gui.colors.SerializableColor;
import ru.nsu.g.apleshkov.gui.menu.SerializableGameSettings;
import ru.nsu.g.apleshkov.message.Message;
import ru.nsu.g.apleshkov.message.MessageType;
import ru.nsu.g.apleshkov.tron.Tron;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread
{
	private Tron tron;
	private int port;
	private List<SerializableColor> colorMap;
	private List<UserThread> users;
	private int applies;
	private ServerSocket serverSocket;
	private SerializableGameSettings settings;

	public Server(int port)
	{
		this.port = port;
		tron = new Tron();
		users = new LinkedList<>();
		colorMap = new LinkedList<>();
		applies = 0;

		colorMap.add(new SerializableColor(tron.getField().getDefault(), new Color(0.089, 0.019, 0.30, 1)));
		colorMap.add(new SerializableColor(tron.getField().getWall(), Color.WHITE));

		settings = new SerializableGameSettings(tron.getField().getDefault(), tron.getField().getWall());
		settings.initialize(tron);
	}

	public void addColor(SerializableColor color) { colorMap.add(color); }

	public void changeSettings(SerializableGameSettings settings) { this.settings = settings; }

	SerializableGameSettings getSettings() { return settings; }

	public void apply()
	{
		applies++;
		try
		{
			if (applies == users.size() && serverSocket != null)
				serverSocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		try
		{
			serverSocket = new ServerSocket(port);

			System.out.println("Server is listening on port " + port);

			do
			{
				try
				{
					Socket socket = serverSocket.accept();
					System.out.println("New user connected");
					UserThread newUser = new UserThread(socket, this, tron);
					users.add(newUser);
					newUser.start();
				}
				catch (SocketException ignore) {}

			} while (applies != users.size());

			broadcast(new Message(MessageType.COLORS, (Serializable)colorMap));
			broadcast(new Message(MessageType.GAME_SETTINGS, settings));


			settings.initTron(tron);

			Lock lock = new ReentrantLock();
			Condition update = lock.newCondition();

			for (var user : users)
			{
				new Thread(user.getUpdateThread(lock, update)).start();
			}

			tron.start();
			do
			{
				lock.lock();
				update.signalAll();
//				broadcast(new Message(MessageType.PLAYER_DATA, (Serializable)tron.getPlayersData()));
				lock.unlock();
				Thread.sleep(25L);

			} while (tron.iterate());
		}
		catch (Exception ex)
		{
			System.out.println("Error in the server: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void broadcast(Message message)
	{
		for (UserThread user : users)
		{
			user.sendMessage(message);
		}
	}
}