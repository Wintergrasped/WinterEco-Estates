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

	public Plugin M = Bukkit.getPluginManager().getPlugin("WinterEco");
	public Configuration conf = M.getConfig();
	
	public Loan LN;
	
	
	public void AutoPay(Main NM, Economy eco) {
		
		M.saveConfig();
		
		M = NM;
		conf = NM.getConfig();
		
		
		List<String> STR = conf.getStringList("LoanList");	
		for (String LID : STR) {
			String Owner = conf.getString("LoanInfo."+LID+".Player"); //TODO FIX NULL
			int Ammount = conf.getInt("LoanInfo."+LID+".Amount");
			int PayAmmount = conf.getInt("LoanInfo."+LID+".PayAmount");
			int PayRemaining = conf.getInt("LoanInfo."+LID+".PayRemaining");
			long NextPay = conf.getLong("LoanInfo."+LID+".NextPay");
			long ServerTime = conf.getLong("ServerTime");
			int Type = conf.getInt("LoanInfo."+LID+".Type");
			
			if (PayRemaining < 0) {
				return;
			}
			
			Loan LNM = new Loan(LID, Bukkit.getPlayer(UUID.fromString(Owner)), Ammount, PayAmmount, PayRemaining, Type, ServerTime, conf.getInt("ForgivnessPoints"), "");
			
			for (Loan MM : NM.Loans) {
				
				if (MM.getID().equalsIgnoreCase(LID)) {
					LNM = MM;
				}
				
			}
			
			
			M.saveConfig();
			
			if (conf.getBoolean("TakeOfflinePayments")) {
				M.saveConfig();
			
			if (NextPay <= ServerTime) {
				Player OWN = Bukkit.getPlayer(UUID.fromString(Owner));
				
				//Does player have enough for payment?
				if (eco.has(OWN, PayAmmount)) {
					M.saveConfig();
					//Withdraw payment
					eco.withdrawPlayer(OWN, PayAmmount);
					PayRemaining--;
					conf.set("LoanInfo."+LID+".PayRemaining", PayRemaining);
					LNM.setPaymentsRemaining(PayRemaining);
					NM.Loans.remove(LNM);
					NM.Loans.add(LNM);
					OWN.sendMessage(ChatColor.GREEN+" Loan Payment of $"+PayAmmount+" deducted for loan ID: "+LID);
					NextPay = NextPay+conf.getInt("PaymentTerm");
					conf.set("LoanInfo."+LID+".NextPay", NextPay);
					
					//Add Payment to player credit history
					int Payments = conf.getInt("PlayerData."+OWN.getUniqueId()+".Payments");
					Payments++;
					conf.set("PlayerData."+OWN.getUniqueId()+".Payments", Payments);
					M.saveConfig();
				}else {
					M.saveConfig();
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
				if (PayRemaining >= 1) {
				Player OWN = Bukkit.getPlayer(UUID.fromString(Owner));
				
				if (PayRemaining < 0) {
					return;
				}
				
				M.saveConfig();
				if (OWN.isOnline()) {
				//Does player have enough for payment?
				if (eco.has(OWN, PayAmmount)) {
					
					//Withdraw payment
					eco.withdrawPlayer(OWN, PayAmmount);
					PayRemaining--;
					conf.set("LoanInfo."+LID+".PayRemaining", PayRemaining);
					LNM.setPaymentsRemaining(PayRemaining);
					NM.Loans.remove(LNM);
					NM.Loans.add(LNM);
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
		M.saveConfig();
	}
	
	public boolean payBill(Main NM, Economy eco, String LID) {
		M.saveConfig();
		String Owner = conf.getString("LoanInfo."+LID+".Player"); //TODO FIX NULL
		int LoanAmmount = conf.getInt("LoanInfo."+LID+".Amount");
		int PayAmmount = conf.getInt("LoanInfo."+LID+".PayAmount");
		int Forgivness = conf.getInt("LoanInfo."+LID+".Forgivness");
		int PayRemaining = conf.getInt("LoanInfo."+LID+".PayRemaining");
		long NextPay = conf.getLong("LoanInfo."+LID+".NextPay");
		long ServerTime = conf.getLong("ServerTime");
		int Type = conf.getInt("LoanInfo."+LID+".Type");
		Player p = Bukkit.getPlayer(UUID.fromString(Owner));
		
		Loan LNM = new Loan(LID, Bukkit.getPlayer(UUID.fromString(Owner)), LoanAmmount, PayAmmount, PayRemaining, Type, ServerTime, conf.getInt("ForgivnessPoints"), "");
		
		for (Loan MM : NM.Loans) {
			
			if (MM.getID().equalsIgnoreCase(LID)) {
				LNM = MM;
			}
			
		}
		
		
		if (eco.has(Bukkit.getPlayer(UUID.fromString(Owner)), PayAmmount)) {
			M.saveConfig();
			if (PayRemaining >= 1) {
			eco.withdrawPlayer(Bukkit.getPlayer(UUID.fromString(Owner)), PayAmmount);
			p.sendMessage(NM.TAG+ChatColor.GREEN+" You made a payment of $"+PayAmmount+" towards loan ID: "+LID);
			PayRemaining--;
			NextPay = NextPay+conf.getInt("PaymentTerm");
			Forgivness = Forgivness+conf.getInt("ForgivnessPoints");
			conf.set("LoanInfo."+LID+".Forgivness", Forgivness);
			conf.set("LoanInfo."+LID+".NextPay", NextPay);
			conf.set("LoanInfo."+LID+".PayRemaining", PayRemaining);
			LNM.setPaymentsRemaining(PayRemaining);
			NM.Loans.remove(LNM);
			NM.Loans.add(LNM);
			addPayments(NM, eco, Owner, false, true);
			M.saveConfig();
			return true;
			}else {
				p.sendMessage(NM.TAG+ChatColor.GREEN+" Loan Paid Off!");
				return true;
			}
		}else {
			p.sendMessage(NM.TAG+ChatColor.RED+" Insufficent funds.");
			M.saveConfig();
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
