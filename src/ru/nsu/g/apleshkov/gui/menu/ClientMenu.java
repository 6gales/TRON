package ru.nsu.g.apleshkov.gui.menu;

import javafx.stage.Stage;
import ru.nsu.g.apleshkov.client.Client;

import java.io.IOException;

public class ClientMenu extends SettingsMenu
{
	private String hostname;
	private int port;

	ClientMenu(String hostname, int port)
	{
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public void start(Stage primaryStage)
	{
		displayTitle();
		try
		{
			Client client = new Client(hostname, port);
			gameService = client;
			gameSettings = null;

			new Thread(client).start();

			displayPlayersRow();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		setBackground();
		setAndShow(primaryStage, 400, 600, "Menu", e -> System.exit(0));
	}
}