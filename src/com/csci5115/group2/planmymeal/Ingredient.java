package com.csci5115.group2.planmymeal;

public class Ingredient {

	private long id;
	private String name;
	private double amount;
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
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
	
	
	
	
}
