package net.wintereco.Wintergrasped;

import org.bukkit.entity.Player;

public class Loan {

	public Player p;
	public int amt;
	public int pamt;
	public int pmtl;
	
	public Loan(Player P, int Ammount, int paymentAmmount, int paymentsLeft) {
		
		p = P;
		amt= Ammount;
		pamt = paymentAmmount;
		pmtl = paymentsLeft;
		
	}
	
	public Player getOwner() {
		return p;
	}
	
	public int getAmount() {
		return amt;
	}
	
	public int getPaymentAmount() {
		return pamt;
	}
	
	public int getPaymentsRemaining() {
		return pmtl;
	}
	
	public void setOwner(Player no) {
		p = no;
	}
	
	public void setAmount(int i) {
		amt = i;
	}
	
	public void setPaymentAmount(int i) {
		pamt = i;
	}
	
	public void setPaymentsRemaining(int i) {
		pmtl = i;
	}

}
