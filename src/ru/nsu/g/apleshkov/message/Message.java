package ru.nsu.g.apleshkov.message;

import java.io.Serializable;

public class Message implements Serializable
{
	private MessageType type;

	private Serializable data;

	public Message(MessageType type, Serializable data)
	{
		this.type = type;
		this.data = data;
	}

	public MessageType getType() { return type; }

	public Serializable getData() { return data; }
}
