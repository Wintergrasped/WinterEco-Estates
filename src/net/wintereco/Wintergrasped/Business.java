package net.wintereco.Wintergrasped;

import org.bukkit.entity.Player;

public class Business {

	public Player P;
	public String BName;
	public String BProp;
	public String ubi;
	
	
	public Business (Player owner, String Name, String Property, String UBI) {
		
		P = owner;
		BName = Name;
		BProp = Property;
		ubi = UBI;
		
		
	}
	
	
	public Player getOwner() {
		return P;
	}
	
	public String getName() {
		return BName;
	}
	
	public String getProperty() {
		return BProp;
	}
	
	public String getUBI() {
		return ubi;
	}
	
	
	
	
	public void setOwner(Player N) {
		P = N;
	}
	
	public void setName(String N) {
		BName = N;
	}
	
	public void setProperty(String N) {
		BProp = N;
	}
	
}
