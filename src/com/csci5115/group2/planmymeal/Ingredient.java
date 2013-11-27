package com.csci5115.group2.planmymeal;

public class Ingredient {

	private long id;
	private String name;
	private long amount;
	private String unit;
	
	
	public Ingredient(){
		this.name = "Error: Name not filled.";
		this.amount = (long) -1d;
		this.unit = null;
	}
	
	public Ingredient(String name, long amount, String unit){
		this.name = name;
		this.amount = amount;
		this.unit = unit;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
	
	
	
	
}
