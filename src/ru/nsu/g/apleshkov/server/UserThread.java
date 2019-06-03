package ru.nsu.g.apleshkov.server;

import ru.nsu.g.apleshkov.gui.menu.SerializableGameSettings;
import ru.nsu.g.apleshkov.gui.menu.settings.SerializableSettings;
import ru.nsu.g.apleshkov.message.Message;
import ru.nsu.g.apleshkov.message.MessageType;
import ru.nsu.g.apleshkov.tron.PlayerOrder;
import ru.nsu.g.apleshkov.tron.Tron;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class UserThread extends Thread
{
	private Socket socket;
	private Server server;
	private Tron tron;
	private ObjectOutputStream output;

	public UserThread(Socket socket, Server server, Tron tron)
	{
		this.socket = socket;
		this.server = server;
		this.tron = tron;

		try
		{
			output = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		sendMessage(new Message(MessageType.GAME_SETTINGS, server.getSettings()));
		sendMessage(new Message(MessageType.BOTS, (Serializable)tron.getBotList()));
	}

	@Override
	public void run()
	{
		try
		{
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

			while (!socket.isClosed())
			{
				try
				{
					Message in = (Message)input.readObject();

					switch (in.getType())
					{
						case GAME_SETTINGS:
							System.out.println("Got game settings");
							server.changeSettings((SerializableGameSettings)in.getData());
							break;

						case PLAYER_SETTINGS:
							SerializableSettings settings = (SerializableSettings)in.getData();
							int id = tron.addPlayer(settings.getName(), settings.isPlayer());
							settings.getColor().setId(id);
							System.out.println("Got player settings, ret id is " + id);
							server.addColor(settings.getColor());
							sendMessage(new Message(MessageType.PLAYER_SETTINGS, id));
							break;

						case APPLY:
							server.apply();
							break;

						case PLAYER_ORDER:
							tron.addOrder((PlayerOrder)in.getData());
							break;
					}

				}
				catch (Exception e) { e.printStackTrace(); }
			}

			socket.close();

		}
		catch (IOException ex)
		{
			System.out.println("Error in UserThread: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	void sendMessage(Message message)
	{
		try
		{
//			output.reset();
//			output.writeUnshared(message);
			output.writeObject(message);
		}
		catch (IOException e) { e.printStackTrace(); }
	}

	Runnable getUpdateThread(Lock lock, Condition update)
	{
		return () ->
		{
			while (true)
			{
				lock.lock();
				try
				{
					update.await();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				lock.unlock();

				sendMessage(new Message(MessageType.PLAYER_DATA, (Serializable)tron.getPlayersData()));
			}
		};
	}
}