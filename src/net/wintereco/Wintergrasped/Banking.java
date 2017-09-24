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

	static Plugin M = Bukkit.getPluginManager().getPlugin("WinterEco");
	static Configuration conf = M.getConfig();
	
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
			
			Loan LNM = new Loan(LID, Bukkit.getPlayer(UUID.fromString(Owner)), Ammount, PayAmmount, PayRemaining, Type, ServerTime, conf.getInt("ForgivnessPoints"), "");
			
			
			
			if (conf.getBoolean("TakeOfflinePayments")) {
			
			
			if (NextPay <= ServerTime) {
				Player OWN = Bukkit.getPlayer(UUID.fromString(Owner));
				
				//Does player have enough for payment?
				if (eco.has(OWN, PayAmmount)) {
					
					//Withdraw payment
					eco.withdrawPlayer(OWN, PayAmmount);
					PayRemaining--;
					conf.set("LoanInfo."+LID+".PayRemaining", PayRemaining);
					OWN.sendMessage(ChatColor.GREEN+" Loan Payment of $"+PayAmmount+" deducted for loan ID: "+LID);
					NextPay = NextPay+conf.getInt("PaymentTerm");
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
			
		}else {
			
			
			
			
			if (NextPay <= ServerTime) {
				Player OWN = Bukkit.getPlayer(UUID.fromString(Owner));
				
				
				if (OWN.isOnline()) {
				//Does player have enough for payment?
				if (eco.has(OWN, PayAmmount)) {
					
					//Withdraw payment
					eco.withdrawPlayer(OWN, PayAmmount);
					PayRemaining--;
					conf.set("LoanInfo."+LID+".PayRemaining", PayRemaining);
					OWN.sendMessage(ChatColor.GREEN+" Loan Payment of $"+PayAmmount+" deducted for loan ID: "+LID);
					NextPay = NextPay+conf.getInt("PaymentTerm");
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
			}else {
				
			}
			}
			
			
			
		}
		}
		
	}
	
	public boolean payBill(Main NM, Economy eco, String LID) {
		
		String Owner = conf.getString("LoanInfo."+LID+".Owner");
		int LoanAmmount = conf.getInt("LoanInfo."+LID+".Amount");
		int PayAmmount = conf.getInt("LoanInfo."+LID+".PayAmount");
		int Forgivness = conf.getInt("LoanInfo."+LID+".Forgivness");
		int PayRemaining = conf.getInt("LoanInfo."+LID+".PayRemaining");
		long NextPay = conf.getLong("LoanInfo."+LID+".NextPay");
		long ServerTime = conf.getLong("ServerTime");
		int Type = conf.getInt("LoanInfo."+LID+".Type");
		Player p = Bukkit.getPlayer(UUID.fromString(Owner));
		
		
		if (eco.has(Bukkit.getPlayer(UUID.fromString(Owner)), PayAmmount)) {
			eco.withdrawPlayer(Bukkit.getPlayer(UUID.fromString(Owner)), PayAmmount);
			p.sendMessage(conf.getString("Tag")+ChatColor.GREEN+" You made a payment of $"+PayAmmount+" towards loan ID: "+LID);
			PayRemaining--;
			NextPay = NextPay+conf.getInt("PaymentTerm");
			Forgivness = Forgivness+conf.getInt("ForgivnessPoints");
			conf.set("LoanInfo."+LID+".Forgivness", Forgivness);
			conf.set("LoanInfo."+LID+".NextPay", NextPay);
			conf.set("LoanInfo."+LID+".PayRemaining", PayRemaining);
			addPayments(NM, eco, Owner, false, true);
			return true;
		}else {
			p.sendMessage(conf.getString("Tag")+ChatColor.RED+" Insufficent funds.");
			return false;
		}
		
		
		
	}
	
	
	public void addPayments(Main NM, Economy eco, String Player, boolean late, boolean early) {
		
		if (late) {
			int LatePayments = conf.getInt("PlayerData."+Player+".LatePayments");
			LatePayments++;
			conf.set("PlayerData."+Player+".LatePayments", LatePayments);
			M.saveConfig();
		}else {
			if (early) {
				int Payments = conf.getInt("PlayerData."+Player+".Payments");
				Payments = Payments+2;
				conf.set("PlayerData."+Player+".Payments", Payments);
				M.saveConfig();
			}else {
			int Payments = conf.getInt("PlayerData."+Player+".Payments");
			Payments++;
			conf.set("PlayerData."+Player+".Payments", Payments);
			M.saveConfig();
			}
		}
		
	}
}
