package com.csci5115.group2.planmymeal;

public class UserSettings
{
	private long id;
	private long reminderTime;
	private String reminderSound;
	private String startSound;
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
	public long getReminderTime()
	{
		return reminderTime;
	}
	public void setReminderTime(long reminderTime)
	{
		this.reminderTime = reminderTime;
	}
	public String getReminderSound()
	{
		return reminderSound;
	}
	public void setReminderSound(String reminderSound)
	{
		this.reminderSound = reminderSound;
	}
	public String getStartSound()
	{
		return startSound;
	}
	public void setStartSound(String startSound)
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
