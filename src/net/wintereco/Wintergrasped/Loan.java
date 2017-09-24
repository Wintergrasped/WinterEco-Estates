package net.wintereco.Wintergrasped;

import org.bukkit.entity.Player;

public class Loan {

	public Main m;
	public Player p;
	public int amt;
	public int pamt;
	public int pmtl;
	
	public Loan(Main M, Player P, int Ammount, int paymentAmmount, int paymentsLeft) {
		
		m = M;
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

}
