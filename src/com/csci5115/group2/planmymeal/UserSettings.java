package com.csci5115.group2.planmymeal;

public class UserSettings
{
	private long id;
	private double reminderTime;
	private int reminderSound;
	private int startSound;
	private int numOvens;
	private int numMicrowaves;
	private int numBurners;
	
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public double getReminderTime()
	{
		return reminderTime;
	}
	public void setReminderTime(double d)
	{
		this.reminderTime = d;
	}
	public int getReminderSound()
	{
		return reminderSound;
	}
	public void setReminderSound(int reminderSound)
	{
		this.reminderSound = reminderSound;
	}
	public int getStartSound()
	{
		return startSound;
	}
	public void setStartSound(int startSound)
	{
		this.startSound = startSound;
	}
	public int getNumOvens()
	{
		return numOvens;
	}
	public void setNumOvens(int numOvens)
	{
		this.numOvens = numOvens;
	}
	public int getNumMicrowaves()
	{
		return numMicrowaves;
	}
	public void setNumMicrowaves(int numMicrowaves)
	{
		this.numMicrowaves = numMicrowaves;
	}
	public int getNumBurners()
	{
		return numBurners;
	}
	public void setNumBurners(int numBurners)
	{
		this.numBurners = numBurners;
	}
	
	

}
