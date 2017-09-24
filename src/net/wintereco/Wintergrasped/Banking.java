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
	
	
	public void AutoPay(Main NM, Economy eco) {
		
		
		
		List<String> STR = conf.getStringList("LoanList");	
		for (String LID : STR) {
			String Owner = conf.getString("LoanInfo."+LID+".Owner");
			int Ammount = conf.getInt("LoanInfo."+LID+".Amount");
			int PayAmmount = conf.getInt("LoanInfo."+LID+".PayAmount");
			int PayRemaining = conf.getInt("LoanInfo."+LID+".PayRemaining");
			long NextPay = conf.getLong("LoanInfo."+LID+".NextPay");
			long ServerTime = conf.getLong("ServerTime");
			int Type = conf.getInt("LoanInfo."+LID+".Type");
			
			Loan LNM = new Loan(LID, Bukkit.getPlayer(UUID.fromString(Owner)), Ammount, PayAmmount, PayRemaining, Type, ServerTime, "");
			
			if (NextPay >= ServerTime) {
				Player OWN = Bukkit.getPlayer(UUID.fromString(Owner));
				
				//Does player have enough for payment?
				if (eco.has(OWN, PayAmmount)) {
					
					//Withdraw payment
					eco.withdrawPlayer(OWN, PayAmmount);
					PayRemaining--;
					conf.set("LoanInfo."+LID+".PayRemaining", PayRemaining);
					OWN.sendMessage(ChatColor.GREEN+" Loan Payment of $"+PayAmmount+" deducted for loan ID: "+LID);
					NextPay = NextPay+720;
					conf.set("LoanInfo."+LID+".NextPay", NextPay);
					
					//Add Payment to player credit history
					int Payments = conf.getInt("PlayerData."+OWN.getUniqueId()+".Payments");
					Payments++;
					conf.set("PlayerData."+OWN.getUniqueId()+".Payments", Payments);
					M.saveConfig();
				}else {
					
					//Add Payment to player credit history
					int LatePayments = conf.getInt("PlayerData."+OWN.getUniqueId()+".LatePayments");
					LatePayments++;
					conf.set("PlayerData."+OWN.getUniqueId()+".LatePayments", LatePayments);
					M.saveConfig();
					
					//Calculate Ammount needed to pay off loan
					int SELL = LNM.getPaymentAmount()*LNM.getPaymentsRemaining();
					
					
					if (LNM.involvesPropety()) {
					//Evict the player
					NM.evictPlayer(OWN, LNM.getProperty(), SELL);
					
					LNM.setPaymentsRemaining(0);
					conf.set("LoanInfo."+LID+".PayRemaining", 0);
					}else {
						LatePayments = +conf.getInt("LoanInfo."+LID+".PayRemaining"); 
						conf.set("PlayerData."+OWN.getUniqueId()+".LatePayments", LatePayments);
						M.saveConfig();
					}
				}
			}
			
		}
		
	}
	
}
