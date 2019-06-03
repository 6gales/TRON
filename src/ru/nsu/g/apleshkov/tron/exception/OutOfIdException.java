package ru.nsu.g.apleshkov.tron.exception;

public class OutOfIdException extends Exception
{
	public OutOfIdException() { super("ID pool is empty"); }
	public OutOfIdException(String message) { super(message); }
}
