package com.csci5115.group2.planmymeal;

public class UserSettings
{
	private long id;
	private int reminderTime;
	private int reminderSound;
	private int startSound;
	private int numOvens;
	private int numMicrowaves;
	private int numBurners;
	
	// Conversion methods - converts indexs of dropdown menus to actual values that we need.
	public double getReminderTimeInSeconds()
	{
		switch(reminderTime)
		{
		case 0:
			return 15;
		case 1:
			return 30;
		case 2:
			return 60;
		default:
			return 30;
		}
	}
	public int getReminderSoundAlarmValue()
	{
		switch(reminderSound)
		{
		case 0:
			return 1;
		case 1:
			return 2;
		case 2: 
			return 4;
		default:
			return 1;
		}
	}
	public int getStartSoundAlarmValue()
	{
		switch(startSound)
		{
		case 0:
			return 1;
		case 1:
			return 2;
		case 2:
			return 4;
		default:
			return 1;
		}
	}
	
	// Getters & Setters
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public int getReminderTime()
	{
		return reminderTime;
	}
	public void setReminderTime(int d)
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
