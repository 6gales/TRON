package ru.nsu.g.apleshkov.tron.exception;

public class BotsNotFoundedException extends Exception
{
	public BotsNotFoundedException() { super("bots not founded"); }
	public BotsNotFoundedException(String message) { super(message); }
}
