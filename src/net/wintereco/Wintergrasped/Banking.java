package net.wintereco.Wintergrasped;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.milkbowl.vault.economy.Economy;

public class Banking {

	Plugin M = Bukkit.getPluginManager().getPlugin("WinterEco");
	Configuration conf = M.getConfig();
	
	public Loan LN;
	
	public void AutoPay(Economy eco) {
		
		
		
		List<String> STR = conf.getStringList("LoanList");	
		for (String LID : STR) {
			String Owner = conf.getString("LoanInfo."+LID+".Owner");
			int Ammount = conf.getInt("LoanInfo."+LID+".Amount");
			int PayAmmount = conf.getInt("LoanInfo."+LID+".PayAmount");
			int PayRemaining = conf.getInt("LoanInfo."+LID+".PayRemaining");
			long NextPay = conf.getLong("LoanInfo."+LID+".NextPay");
			long ServerTime = conf.getLong("ServerTime");
			int Type = conf.getInt("LoanInfo."+LID+".Type");
			
			if (NextPay >= ServerTime) {
				Player OWN = Bukkit.getPlayer(UUID.fromString(Owner));
				if (eco.has(OWN, PayAmmount)) {
					eco.withdrawPlayer(OWN, PayAmmount);
					PayRemaining--;
					conf.set("LoanInfo."+LID+".PayRemaining", PayRemaining);
					OWN.sendMessage(ChatColor.GREEN+" Loan Payment of $"+PayAmmount+" deducted for loan ID: "+LID);
					NextPay = NextPay+720;
					conf.set("LoanInfo."+LID+".NextPay", NextPay);
					
				}
			}
			
		}
		
	}
	
}
