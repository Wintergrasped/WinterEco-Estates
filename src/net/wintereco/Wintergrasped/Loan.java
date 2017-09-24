package net.wintereco.Wintergrasped;

import org.bukkit.entity.Player;

public class Loan {

	public Player p;
	public int amt;
	public int pamt;
	public int pmtl;
	public int t;
	public String Prop;
	
	
	public Loan(Player P, int Ammount, int paymentAmmount, int paymentsLeft, int type, String property) {
		
		p = P;
		amt= Ammount;
		pamt = paymentAmmount;
		pmtl = paymentsLeft;
		t = type;
		if (type == 2) {
			Prop = Prop;
		}
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
	
	public int getType() {
		return t;
	}
	
	public boolean involvesPropety() {
		if (!(t==1)) {
			return true;
		}else {
			return false;
		}
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
