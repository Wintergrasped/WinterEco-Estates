package net.wintereco.Wintergrasped;

import org.bukkit.entity.Player;

public class Loan {

	public Player p;
	public int amt;
	public int pamt;
	public int pmtl;
	public int t;
	public long NP;
	public String Prop;
	public String I;
	public int fgv;
	
	
	public Loan(String ID, Player P, int Ammount, int paymentAmmount, int paymentsLeft, int type, long NextPayment, int forgivness, String property) {
		
		p = P;
		amt= Ammount;
		pamt = paymentAmmount;
		pmtl = paymentsLeft;
		t = type;
		I = ID;
		NP = NextPayment;
		fgv = forgivness;
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
	
	public long getNextPay() {
		return NP;
	}
	
	public String getProperty() {
		return Prop;
	}
	
	public String getID() {
		return I;
	}
	
	public int getForgivness() {
		return fgv;
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
	
	public void setForgivness(int i) {
		fgv = i;
	}
	
	public void setPaymentAmount(int i) {
		pamt = i;
	}
	
	public void setPaymentsRemaining(int i) {
		pmtl = i;
	}

}
