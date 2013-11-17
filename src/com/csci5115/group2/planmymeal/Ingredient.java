package com.csci5115.group2.planmymeal;

public class Ingredient {

	private String name;
	private Double amount;
	private String unit;
	
	
	public Ingredient(){
		this.name = "Error: Name not filled.";
		this.amount = -1d;
		this.unit = null;
	}
	
	public Ingredient(String name, Double amount, String unit){
		this.name = name;
		this.amount = amount;
		this.unit = unit;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
	
	
	
	
}
