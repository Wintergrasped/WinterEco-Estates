package net.wintereco.Wintergrasped;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class City {

	
	public Player M;
	public Player[] Ctz;
	public double Tax;
	public Location CC;
	
	
	public City(Player Mayor, Player[] Citizens, double tax, Location CityCenter) {
		
		M = Mayor;
		Ctz = Citizens;
		Tax = tax;
		CC = CityCenter;
		
	}
	
}
