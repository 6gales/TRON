package ru.nsu.g.apleshkov.gui.menu;

import javafx.stage.Stage;
import ru.nsu.g.apleshkov.client.Client;
import ru.nsu.g.apleshkov.server.Server;

import java.io.IOException;

public class HostMenu extends SettingsMenu
{
	private int port;

	HostMenu(int port)
	{
		this.port = port;
		Server server = new Server(port);
		server.start();
		System.out.println("Server started");
	}

	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			Client client = new Client("localhost", port);
			gameService = client;
			gameSettings = gameService.getSettings();

			new Thread(client).start();

			displayIPaddr();
			displaySettings();
			displayPlayersRow();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		displayTitle();

		setBackground();
		setAndShow(primaryStage, 400, 600, "Menu", e -> System.exit(0));
	}
}
