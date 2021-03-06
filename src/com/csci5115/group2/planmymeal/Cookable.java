package com.csci5115.group2.planmymeal;

public class Cookable {
	protected long id;
	protected String name;
	protected double time;
	protected String description;
	protected String type;
	protected boolean belongsToUser;
	protected boolean belongsToCommunity;
	public Cookable() {
		
	}
	
	public Cookable(String name, double time, String description) {
		this.name = name;
		this.time = time;
		this.description = description;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public int getTypeIconResource() {
		if (this.type == "Meal") {
			return R.string.icon_meal;
		} else if (this.type == "Recipe") {
			return R.string.icon_recipe;
		}
		return -1;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	
	public boolean isBelongsToUser()
	{
		return belongsToUser;
	}

	public void setBelongsToUser(boolean belongsToUser)
	{
		this.belongsToUser = belongsToUser;
	}

	public boolean isBelongsToCommunity()
	{
		return belongsToCommunity;
	}

	public void setBelongsToCommunity(boolean belongsToCommunity)
	{
		this.belongsToCommunity = belongsToCommunity;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	// TODO test these
	public int getMinutes() {
		return (int) time / 60;
	}
	public int getSeconds() {
		return (int) time % 60;
	}
	public String getReadableTime() {
		return String.format("%02d:%02d", this.getMinutes(), this.getSeconds());
	}

}
