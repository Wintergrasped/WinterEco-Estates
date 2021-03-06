package net.wintereco.Wintergrasped;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.milkbowl.vault.economy.Economy;


public class Main extends JavaPlugin implements Listener {

	
	
	public String curVersion = "1.13";
	public String UpdateVersion = "";
	
	
	
	
	public boolean debug = true;
	
	public FileConfiguration conf = this.getConfig();
	public int badcredit = 450;
	public int okaycredit = 600;
	public long ServerTime = 0;
	public String Evictor = "5d1ead21-099d-3a92-baac-2a53cab17220";
	
	public Banking  B;
	
	public int FoundingCity = 4500000;
	public int creditLock = 0;
	public List<Loan> Loans = new ArrayList();
	public List<String> LoansList = new ArrayList();
	
	public ArrayList<String> Propertys = new ArrayList();
	public ArrayList<String> PropertysForSale = new ArrayList();
	public ArrayList<String> PropertysForRent = new ArrayList();
	public ArrayList<String> Citys = new ArrayList();
	
	public ArrayList<Player> Online = new ArrayList();
	public ArrayList<String> inDebt = new ArrayList();
	
	public String TAG = ChatColor.GOLD+"["+ChatColor.BLUE+"WinterEco"+ChatColor.GOLD+"]";
	public Economy econ;
	
	public int curLoan = 315;
	
	public void onEnable() {
		this.saveDefaultConfig();
		this.saveConfig();
		setupEconomy();
		runDuty();
		load();
		VersionCheck();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		B = new Banking();
	}
	
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    public void load() {
    	
    	
    	if (this.getConfig().contains("Tag")) {
    		String TG = this.getConfig().getString("Tag");
    		TG.replace("&0", ChatColor.BLACK+"");
    		TG.replace("&1", ChatColor.DARK_BLUE+"");
    		TG.replace("&2", ChatColor.DARK_GREEN+"");
    		TG.replace("&3", ChatColor.DARK_AQUA+"");
    		TG.replace("&4", ChatColor.DARK_RED+"");
    		TG.replace("&5", ChatColor.DARK_PURPLE+"");
    		TG.replace("&6", ChatColor.GOLD+"");
    		TG.replace("&7", ChatColor.GRAY+"");
    		TG.replace("&8", ChatColor.DARK_GRAY+"");
    		TG.replace("&9", ChatColor.BLUE+"");
    		TG.replace("&A", ChatColor.GREEN+"");
    		TG.replace("&B", ChatColor.AQUA+"");
    		TG.replace("&C", ChatColor.RED+"");
    		TG.replace("&D", ChatColor.GRAY+"");
    		TG.replace("&E", ChatColor.YELLOW+"");
    		TG.replace("&F", ChatColor.WHITE+"");
    		TG.replace("&M", ChatColor.STRIKETHROUGH+"");
    		TG.replace("&N", ChatColor.UNDERLINE+"");
    		TG.replace("&L", ChatColor.BOLD+"");
    		TG.replace("&O", ChatColor.ITALIC+"");
    		TAG = TG;
    		
    	}else {
    		TAG = ChatColor.GOLD+"["+ChatColor.BLUE+"WinterEco"+ChatColor.GOLD+"]";
    	}
    	
    	for (String PRS : this.getConfig().getStringList("PropertySaves")) {
    		Propertys.add(PRS);
    	}
    	
    	for (String PRS : this.getConfig().getStringList("PropertyForSaleSaves")) {
    		PropertysForSale.add(PRS);
    	}
    	
    	for (String PRS : this.getConfig().getStringList("PropertyForRentSaves")) {
    		PropertysForRent.add(PRS);
    	}
    	
    	for (String PRS : this.getConfig().getStringList("InDebt")) {
    		inDebt.add(PRS);
    	}
    	
    	for (String PRS : this.getConfig().getStringList("LoanList")) {
    		Loans.add(new Loan(PRS, 
    				Bukkit.getPlayer(UUID.fromString(conf.getString("LoanInfo."+PRS+".Player"))), 
    				conf.getInt("LoanInfo."+PRS+".Amount"), 
    				conf.getInt("LoanInfo."+PRS+".PayAmount"), 
    				conf.getInt("LoanInfo."+PRS+".PayRemaining"), 
    				conf.getInt("LoanInfo."+PRS+".Type"), 
    				conf.getLong("LoanInfo."+PRS+".NextPay"),
    				conf.getInt("LoanInfo."+PRS+".Forgivness"),
    				conf.getString("LoanInfo."+PRS+".Property")));
    	}
    	
    	if (!this.getConfig().contains("curLoan")) {
    		this.getConfig().set("curLoan", curLoan);
    	}
    	
    	if (this.getConfig().contains("ServerTime")) {
    	ServerTime = this.getConfig().getLong("ServerTime");
    	}
    	
    	if (this.getConfig().contains("ServerOwner.UUID")) {
    		Evictor = "5d1ead21-099d-3a92-baac-2a53cab17220";
    	}else {
    	Evictor = this.getConfig().getString("ServerOwner.UUID");
    	}
    }
	
	public void onDisable() {
		this.getConfig().set("PropertySaves", Propertys);
		this.getConfig().set("PropertyForSaleSaves", PropertysForSale);
		this.getConfig().set("PropertyForRentSaves", PropertysForRent);
		this.getConfig().set("InDebt", inDebt);
		this.getConfig().set("ServerTime", ServerTime);
		this.getConfig().set("ServerOwner.UUID", Evictor);
		this.getConfig().set("LoanList", LoansList);
		saveLoanSys();
		this.saveConfig();
	}
	
	
	
	/***********
	 *COMMANDS**
	 * START****
	 ***********/
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	
		Player P = Bukkit.getPlayer(sender.getName());
		
		
		
		if (cmd.getName().equalsIgnoreCase("wec")) {
			if (args.length == 0) {
				P.sendMessage(ChatColor.GREEN+" Made by Wintergrasped");
				P.sendMessage(ChatColor.GREEN+" Version: "+ curVersion);
				P.sendMessage(ChatColor.GREEN+" Latest Version: "+ UpdateVersion);
				P.sendMessage(ChatColor.GREEN+" Server of Origin CrushCraft");
				P.sendMessage(ChatColor.GREEN+" Initial relase 9/22/2017 by Wintergrasped on Spigotmc.org");
				P.sendMessage(ChatColor.GREEN+" Source: https://github.com/Wintergrasped/WinterEco-Estates");
			}
			if (args[0].equalsIgnoreCase("Creditinfo")) {
				int Credit = credit(Bukkit.getPlayer(sender.getName()));
				
				if (Credit <= badcredit) {
					sender.sendMessage(TAG+ChatColor.RED+"Your credit score is "+Credit);
				}else if (Credit >= (badcredit+1) && Credit <= okaycredit){
					sender.sendMessage(TAG+ChatColor.YELLOW+"Your credit score is "+Credit);
				} else if (Credit >= (okaycredit+1)) {
					sender.sendMessage(TAG+ChatColor.GREEN+"Your credit score is "+Credit);
				}
				
				sender.sendMessage(TAG+ChatColor.GREEN+"You have made "+getPayments(P)+" payments on time");
				sender.sendMessage(TAG+ChatColor.GREEN+"You have made "+getLatePayments(P)+" late payments");
				if (isHighRisk(P)) {
					sender.sendMessage(TAG+ChatColor.RED+"You are classified HIGH Risk");
				}else {
					sender.sendMessage(TAG+ChatColor.GREEN+"You are not classified High Risk");
				}
				
				int TPS = getTimePlayed(P);
				if (TPS >= 60) {
					sender.sendMessage(TAG+ChatColor.GREEN+"You have played for "+(TPS /60)+" Hours");
				}else {
					sender.sendMessage(TAG+ChatColor.GREEN+"You have played for "+TPS+" Minutes");
				}
				
			} else 
				if (args[0].equalsIgnoreCase("Add")) {
					Set<ProtectedRegion> RNN = getWorldGuard().getRegionManager(Bukkit.getPlayer(sender.getName()).getWorld()).getApplicableRegions(Bukkit.getPlayer(sender.getName()).getLocation()).getRegions();
					
					ProtectedRegion R = null;
			          for (ProtectedRegion RSS : RNN)
			          {
			            RNN.remove(RSS.getParent());
			            R = RSS;
			          }
			         
			         String RegionName = R.getId();
			         
			         
			         if (this.getConfig().contains("Propertys."+RegionName)) {
			        	 sender.sendMessage(TAG+ChatColor.RED+"This Property Already Exists!");
			         } else {
			         this.getConfig().set("Propertys."+RegionName+".Owner", Bukkit.getPlayer(sender.getName()).getUniqueId().toString());
			         this.saveConfig();
			         Propertys.add(RegionName);
			         sender.sendMessage(TAG+ChatColor.GREEN+"Property "+RegionName+" Added.");
			         }
			       } else 
			       
			       if (args[0].equalsIgnoreCase("Sell")){
			    	   if (PropertysForSale.contains(args[1])) {
			    		   sender.sendMessage(TAG+ChatColor.RED+"That property is already for sale!");
			    	   }else {
			    		   if (args[1].isEmpty()) {
			    			   sender.sendMessage(TAG+ChatColor.RED+" You need to specifiy a property name!");
			    		   }else {
			    			   if (args[2].isEmpty()) {
			    				   sender.sendMessage(TAG+ChatColor.RED+" You need to have a price.");
			    			   }else {
			    		   if (Propertys.contains(args[1])) {
			    			   if (this.getConfig().getString("Propertys."+args[1]+".Owner").equals(Bukkit.getPlayer(sender.getName()).getUniqueId().toString())) {
			    			   PropertysForSale.add(args[1]);
			    			   this.getConfig().set("Propertys."+args[1]+".Seller", Bukkit.getPlayer(sender.getName()).getUniqueId().toString());
			    			   this.getConfig().set("Propertys."+args[1]+".Price", Integer.parseInt(args[2]));
			    			   this.saveConfig();
			    			   sender.sendMessage(TAG+ChatColor.GREEN+" "+args[1]+" is now For Sale for "+args[2]);
			    			   } else {
			    				   sender.sendMessage(TAG+ChatColor.RED+"You don't own that property");
			    			   }
			    		   }else {
			    			   sender.sendMessage(TAG+ChatColor.RED+"That property is dose not exist!");
			    		   }
			    	   }
			    	   }
			    	   }
			       }else 
			    	   
			    	   if (args[0].equalsIgnoreCase("buy")) {
			    		   
			    		   if (args[1].isEmpty()) {
			    			   sender.sendMessage(TAG+ChatColor.RED+" You need to specify a property!");
			    		   }else {
			    		   
			    		   if (PropertysForSale.contains(args[1])) {
			    			   if (econ.has(Bukkit.getPlayer(sender.getName()), this.getConfig().getInt("Propertys."+args[1]+".Price"))) {
			    				   if (this.getConfig().getString("Propertys."+args[1]+".Owner").equals(Bukkit.getPlayer(sender.getName()).getUniqueId().toString())) {
			    					   sender.sendMessage(TAG+ChatColor.RED+"You own that property........ Hows that Suposed to work?");
			    					   sender.sendMessage(TAG+ChatColor.RED+"You paid the bank a $50 service fee");
			    					   econ.withdrawPlayer(Bukkit.getPlayer(sender.getName()), 50);
			    				   }else{
			    					   econ.withdrawPlayer(Bukkit.getPlayer(sender.getName()), this.getConfig().getInt("Propertys."+args[1]+".Price"));
			    					   econ.depositPlayer(Bukkit.getPlayer(UUID.fromString(this.getConfig().getString("Propertys."+args[1]+".Owner"))), this.getConfig().getInt("Propertys."+args[1]+".Price"));
			    					   this.getConfig().set("Propertys."+args[1]+".Owner", Bukkit.getPlayer(sender.getName()).getUniqueId().toString());
			    					   this.saveConfig();
			    					   PropertysForSale.remove(args[1]);
			    					   sender.sendMessage(TAG+ChatColor.GREEN+"You bought "+args[1]+" for "+this.getConfig().getInt("Propertys."+args[1]+".Price"));
			    					   
			    					   
			    					   if (conf.getBoolean("FireworkEffect")) {
			    						   spawnFirework(P);
			    						   spawnFirework(P);
			    						   spawnFirework(P);
			    						   spawnFirework(P);
			    					   }
			    					   
			    					   DefaultDomain OWNS = new DefaultDomain();
			    						OWNS.removeAll();
			    						OWNS.addPlayer(P.getUniqueId());
			    						getWorldGuard().getRegionManager(P.getWorld()).getRegion(getRegionName(P)).setOwners(OWNS);
			    						inDebt.add(P.getUniqueId().toString());
			    						this.saveConfig();
			    					   
			    					   
			    					   int Payments = conf.getInt("PlayerData."+P.getUniqueId()+".Payments");
			    					   Payments = Payments+5;
			    					   conf.set("PlayerData."+P.getUniqueId()+".Payments", Payments);
			    				   }
			    			   }
			    		
			    	   		} else {
			    	   			if (Propertys.contains(args[1])) {
			    	   				sender.sendMessage(TAG+ChatColor.RED+"That property is not for sale");
			    	   			}else{
			    	   				sender.sendMessage(TAG+ChatColor.RED+"That is not a valid Property.");
			    	   			}
			    	   		}
			    	   }
			    	   }
		
		
		
		if (args[0].equalsIgnoreCase("city")) {
			if (args[1].isEmpty()) {
				sender.sendMessage(TAG+ChatColor.RED+" Your missing some information");
			}else{
				
				
				if (args[1].equalsIgnoreCase("join")) {
					if (this.getConfig().contains("Citys."+args[1]+".Mayor")) {
						sender.sendMessage(ChatColor.RED+"That city Dose not exist.");
					}else {
						ArrayList<String> C = new ArrayList();
						C = (ArrayList<String>) this.getConfig().getList("Citys."+args[1]+".Citizens");
						
						if (C == null) {
							C = new ArrayList();
						}
						
						C.add(sender.getName());
						this.getConfig().set("Citys."+args[1]+".Citizens", C);
						sender.sendMessage(ChatColor.GREEN+"You Joined "+args[2]);
						this.getConfig().set("PlayerData."+Bukkit.getPlayer(sender.getName()).getUniqueId().toString()+".Citizen", args[2]);
					}
				}
				
				if (args[1].equalsIgnoreCase("setTax")) {
					if (this.getConfig().getString("Citys."+args[2]+".Mayor").equalsIgnoreCase(Bukkit.getPlayer(sender.getName()).getUniqueId().toString())) {
						this.getConfig().set("Citys."+args[2]+".Tax", args[3]);
						sender.sendMessage(TAG+ChatColor.GREEN+" Tax set to "+ args[3]);
					}
				}
				

				}
			return true;
		}
		}
		
		if (args[0].equalsIgnoreCase("info")) {
			
			String RN = getRegionName(Bukkit.getPlayer(sender.getName()));
			
			if (this.getConfig().contains("Propertys."+RN+".Owner")) {
				sender.sendMessage(TAG+" Property Owned By: "+ Bukkit.getPlayer(UUID.fromString(this.getConfig().getString("Propertys."+RN+".Owner"))));
				if (PropertysForSale.contains(RN)) {
					sender.sendMessage(TAG+ChatColor.GREEN+" This Property is for sale for $"+Long.parseLong(this.getConfig().getString("Propertys."+args[1]+".Price")));
				}else {
					sender.sendMessage(TAG+ChatColor.RED+" This property is not for sale.");
					sender.sendMessage(TAG+ChatColor.GREEN+"Contact the owner to potentially sell.");
				}
			}else {
				sender.sendMessage(TAG+ChatColor.RED+" This is not a property.");
			}
			
		}
		
		if (args[0].equalsIgnoreCase("found")) {
			
			if (args[1].isEmpty()) {
				sender.sendMessage(TAG+ChatColor.RED+" Your Mssing Information.");
			}else {
			
			if (args[1].equalsIgnoreCase("city")) {
				if (econ.has(Bukkit.getPlayer(sender.getName()), FoundingCity)) {
					Citys.add(args[2]);
					if (conf.getBoolean("FireworkEffect")) {
						   spawnFirework(P);
						   spawnFirework(P);
						   spawnFirework(P);
						   spawnFirework(P);
					   }
					sender.sendMessage(TAG+ChatColor.GREEN+" You have founded City of "+args[2]);
					sender.sendMessage(TAG+ChatColor.GREEN+" You can set your Tax with /weco city settax <City Name> <Ammount> (Ammount Between 0 - 0.30)");
					this.getConfig().set("Citys."+args[2]+".Mayor", Bukkit.getPlayer(sender.getName()).getUniqueId().toString());
				}else {
					sender.sendMessage(TAG+ChatColor.RED+" Not Enough Money");
				}
			}
		}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		*TODO BEGINING OF CREDIT SYSTEM
		*==========================================================*
		*==========================================================*
		*==========================================================*
		*============BEGINING OF CREDIT SYSEM======================*
		*==========================================================*
		*==========================================================*
		*==========================================================*
		*/
		
		if (args[0].equalsIgnoreCase("credit")) {
			if (args[1].equalsIgnoreCase("property")) {
				
				int creditscore = credit(P);
				
				if (!args[2].isEmpty()) {
					
					int Price = this.getConfig().getInt("Propertys."+args[2]+".Price");
					
					   econ.depositPlayer(Bukkit.getPlayer(UUID.fromString(this.getConfig().getString("Propertys."+args[2]+".Owner"))), this.getConfig().getInt("Propertys."+args[1]+".Price"));
					   this.getConfig().set("Propertys."+args[2]+".Owner", Bukkit.getPlayer(sender.getName()).getUniqueId().toString());
					   this.getConfig().set("Propertys."+args[2]+".Loaned", Price);
					   this.saveConfig();
					   PropertysForSale.remove(args[2]);
					   sender.sendMessage(TAG+ChatColor.GREEN+"You bought "+args[2]+" for "+this.getConfig().getInt("Propertys."+args[2]+".Price"));
					   
					   DefaultDomain OWNS = new DefaultDomain();
						OWNS.removeAll();
						OWNS.addPlayer(P.getUniqueId());
						getWorldGuard().getRegionManager(P.getWorld()).getRegion(getRegionName(P)).setOwners(OWNS);
						inDebt.add(P.getUniqueId().toString());
						this.saveConfig();
					   
					   
					   int Payments = conf.getInt("PlayerData."+P.getUniqueId()+".Payments");
					   Payments++;
					   conf.set("PlayerData."+P.getUniqueId()+".Payments", Payments);
					   curLoan++;
					   long NPM = ServerTime+720;
					   Loan CL = new Loan(curLoan+"", P, Price, (Price/24), 24, 2, NPM,conf.getInt("ForgivnessPoints"), args[2]);
					   Loans.add(CL);
					   List<String> lids = new ArrayList();
					   if (conf.contains("PlayerData."+P.getUniqueId().toString()+".loans")) {
					   lids = conf.getStringList("PlayerData."+P.getUniqueId().toString()+".loans");
					   }
					   lids.add(CL.getID()+"");
					   conf.set("PlayerData."+P.getUniqueId().toString()+".loans", lids);
					   P.sendMessage(TAG+ChatColor.GREEN+" Your loan ID is "+CL.getID()+" You will need this to make any extra payments. Your regular payments will automatically be deducted from your account. If you do not have the money in your account your property will be marked For Sale");
					   List<String> LDS = new ArrayList();
					   for (Loan LID : Loans) {
						   
						   String IDL = LID.getID()+"";
						   LDS.add(IDL);
						   conf.set("LoanList", LDS);
						   
					   }
					   if (conf.getBoolean("FireworkEffect")) {
						   spawnFirework(P);
						   spawnFirework(P);
						   spawnFirework(P);
						   spawnFirework(P);
					   }
					   
					
					
					
					
				}else {
					P.sendMessage(TAG+ChatColor.RED+" You need to specify a property ID");
				}
				
			}else if (args[1].equalsIgnoreCase("pay")) {
				Banking  B = new Banking();
				B.payBill(this, econ, args[2]);
			}else if (args[1].equalsIgnoreCase("info")) {
				if (1==2) {
					
				}else{
					int crds = credit(P);
					int LNs = credit(P)*10;
					double payms = 0;
					
					if (crds <= badcredit) {
						LNs = LNs/2;
						payms = (LNs*0.8);
					}else if (crds >= okaycredit && crds <= okaycredit) {
						LNs = LNs * 5;
						payms = (LNs*0.25);
					}else if(crds >= okaycredit) {
						LNs = LNs * 100;
						payms = (LNs*0.08);
					}
					
					if (isHighRisk(P)) {
						LNs = LNs / 5;
					}
					
					
					sender.sendMessage(TAG+ChatColor.GREEN+"Your credit application would land you $"+LNs);
					sender.sendMessage(TAG+ChatColor.GREEN+"Payment term, 12 payments of $"+payms);
					sender.sendMessage(TAG+ChatColor.GREEN+"do '/wec credit apply' to accept this offer");
					
					
				}
			}
			
			this.saveConfig();
		}
		
		/*
		*TODO END OF CREDIT SYSTEM
		*==========================================================*
		*==========================================================*
		*==========================================================*
		*============END OF CREDIT SYSEM===========================*
		*==========================================================*
		*==========================================================*
		*==========================================================*
		*/
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
			
			if (args[0].equalsIgnoreCase("rent")) {
				if (args[1].equalsIgnoreCase("add")) {
					if (Propertys.contains(args[2])) {
		    			   if (this.getConfig().getString("Propertys."+args[2]+".Owner").equals(Bukkit.getPlayer(sender.getName()).getUniqueId().toString())) {
		    			   PropertysForRent.add(args[2]);
		    			   this.getConfig().set("Propertys."+args[2]+".Seller", Bukkit.getPlayer(sender.getName()).getUniqueId().toString());
		    			   this.getConfig().set("Propertys."+args[2]+".Rent", Integer.parseInt(args[3]));
		    			   this.saveConfig();
		    			   sender.sendMessage(TAG+ChatColor.GREEN+" "+args[2]+" is now For Rent for "+args[3]);
		    			   } else {
		    				   sender.sendMessage(TAG+ChatColor.RED+"You don't own that property");
		    			   }
		    		   }else {
		    			   sender.sendMessage(TAG+ChatColor.RED+"That property is dose not exist!");
		    		   }
				}else {
					
					if (args[1].isEmpty()) {
						   
					}else {
						if (args[1].equalsIgnoreCase("stop")) {
							
							if (args[2].isEmpty()) {
								
								String PR = this.getConfig().getString("PlayerData."+P.getUniqueId().toString()+".Renting");
							
			    				   this.getConfig().set("Propertys."+args[2]+".Renter", "None");
			    				   this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".Renting", "None");
			   					DefaultDomain OWNS = new DefaultDomain();
								OWNS.removePlayer(P.getName());
								getWorldGuard().getRegionManager(P.getWorld()).getRegion(getRegionName(P)).setOwners(OWNS);
								PropertysForRent.add(PR);
								
							}else {
								if (PropertysForRent.contains(args[2])) {
					    			   if (this.getConfig().getString("Propertys."+args[2]+".Owner").equals(Bukkit.getPlayer(sender.getName()).getUniqueId().toString())) {
					    			   PropertysForRent.add(args[2]);
					    			   this.getConfig().set("Propertys."+args[2]+".Seller", Bukkit.getPlayer(sender.getName()).getUniqueId().toString());
					    			   this.getConfig().set("Propertys."+args[2]+".Rent", 0);
					    			   this.saveConfig();
					    			   sender.sendMessage(TAG+ChatColor.GREEN+" "+args[2]+" is no longer For Rent");
					    			   } else {
					    				   String PR = this.getConfig().getString("PlayerData."+P.getUniqueId().toString()+".Renting");
											
					    				   this.getConfig().set("Propertys."+args[2]+".Renter", "None");
					    				   this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".Renting", "None");
					   					DefaultDomain OWNS = new DefaultDomain();
										OWNS.removePlayer(P.getName());
										getWorldGuard().getRegionManager(P.getWorld()).getRegion(args[2]).setOwners(OWNS);
										PropertysForRent.add(PR);
										sender.sendMessage(TAG+ChatColor.GREEN+" "+args[2]+" is no longer For Rent");
					    			   }
					    		   }else {
					    			   sender.sendMessage(TAG+ChatColor.RED+"That property is dose not exist!");
					    		   }
								
							}
							
						}
						
						
					}
				}
			}
			
			if (args[0].equalsIgnoreCase("srent")) {
				if (PropertysForRent.contains(getRegionName(P))) {
					Player ld = Bukkit.getPlayer(UUID.fromString(this.getConfig().getString("Propertys."+getRegionName(P)+".Seller")));
	    			   int rent = this.getConfig().getInt("Propertys."+getRegionName(P)+".Rent");
				
	    			   if (econ.has(P, rent)) {
	    				   
	    				   econ.withdrawPlayer(P, rent);
	    				   econ.depositPlayer(ld, rent);
	    				   P.sendMessage(TAG+ChatColor.GREEN+" You rented "+getRegionName(P)+" for $"+rent);
	    				   ld.sendMessage(TAG+ChatColor.GREEN+" Rent payment $"+rent+" from "+P.getName()+" ");
	    				   this.getConfig().set("Propertys."+getRegionName(P)+".Renter", P.getUniqueId().toString());
	    				   this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".Renting", getRegionName(P));
	   					DefaultDomain OWNS = new DefaultDomain();
						OWNS.removeAll();
						OWNS.addPlayer(P.getUniqueId());
						getWorldGuard().getRegionManager(P.getWorld()).getRegion(getRegionName(P)).setOwners(OWNS);
	    			   }else {
	    				   
	    			   }
				}else {
					P.sendMessage(TAG+ChatColor.RED+" Property Not For Rent!");
				}
			}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		this.saveConfig();
		return false;
	}
	
	
	/***********
	 *COMMANDS**
	 ****END****
	 ***********/
	
	
	public void spawnFirework(Player p) {
		//Spawn the Firework, get the FireworkMeta.
        Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
       
        //Our random generator
        Random r = new Random();   

        //Get the type
        int rt = r.nextInt(5) + 1;
        Type type = Type.BALL;       
        if (rt == 1) type = Type.BALL;
        if (rt == 2) type = Type.BALL_LARGE;
        if (rt == 3) type = Type.BURST;
        if (rt == 4) type = Type.CREEPER;
        if (rt == 5) type = Type.STAR;
       
        //Get our random colours   
        int r1i = r.nextInt(17) + 1;
        int r2i = r.nextInt(17) + 1;
        Color c1 = getColor(r1i);
        Color c2 = getColor(r2i);
       
        //Create our effect with this
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
       
        //Then apply the effect to the meta
        fwm.addEffect(effect);
       
        //Generate some random power and set it
        int rp = r.nextInt(2) + 1;
        fwm.setPower(rp);
       
        //Then apply this to our rocket
        fw.setFireworkMeta(fwm);           
	}
	
	private Color getColor(int i) {
		Color c = null;
		if(i==1){
		c=Color.AQUA;
		}
		if(i==2){
		c=Color.BLACK;
		}
		if(i==3){
		c=Color.BLUE;
		}
		if(i==4){
		c=Color.FUCHSIA;
		}
		if(i==5){
		c=Color.GRAY;
		}
		if(i==6){
		c=Color.GREEN;
		}
		if(i==7){
		c=Color.LIME;
		}
		if(i==8){
		c=Color.MAROON;
		}
		if(i==9){
		c=Color.NAVY;
		}
		if(i==10){
		c=Color.OLIVE;
		}
		if(i==11){
		c=Color.ORANGE;
		}
		if(i==12){
		c=Color.PURPLE;
		}
		if(i==13){
		c=Color.RED;
		}
		if(i==14){
		c=Color.SILVER;
		}
		if(i==15){
		c=Color.TEAL;
		}
		if(i==16){
		c=Color.WHITE;
		}
		if(i==17){
		c=Color.YELLOW;
		}
		 
		return c;
		}
		 

	
	public void evictPlayer(Player P, String Property, int Amount) {
		
		
		//Remove Player from Property
		DefaultDomain OWNS = new DefaultDomain();
		OWNS.removeAll();
		getWorldGuard().getRegionManager(P.getWorld()).getRegion(getRegionName(P)).setOwners(OWNS);	
		
		//Post Property For Sale
		PropertysForSale.add(Property);
		   this.getConfig().set("Propertys."+Property+".Seller", Evictor);
		   this.getConfig().set("Propertys."+Property+".Price", Amount);
		   this.saveConfig();
		   
		   P.sendMessage(TAG+ChatColor.RED+"You've Been Evicted");
		   
		
	}
	
	
	public void savePlayerData(Player P) {
		
			int PTT = this.getConfig().getInt("PlayerData."+P.getUniqueId().toString()+".PlayTime");
			PTT++;
			this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".PlayTime", PTT);
			this.saveConfig();
			
			if (debug) {
				Bukkit.getLogger().info("PTT "+PTT);
				}

		
		this.saveConfig();
	}
	
	public ProtectedRegion RDSVS;
	public String getRegionName(Player P) {
		Set<ProtectedRegion> RNN = getWorldGuard().getRegionManager(P.getWorld()).getApplicableRegions(P.getLocation()).getRegions();
		
          for (ProtectedRegion RSS : RNN)
          {
            RNN.remove(RSS.getParent());
            RDSVS = RSS;
          }
         
         String RegionName = RDSVS.getId();
         
         return RegionName;
	}
	
	public int getPayments(Player P) {
		if (conf.contains("PlayerData."+P.getUniqueId()+".Payments")) {
			return conf.getInt("PlayerData."+P.getUniqueId()+".Payments");
		}else {
			return 0;
		}
	}
	
	public void saveToConf() {
		
	}
	
	public int getLatePayments(Player P) {
		if (conf.contains("PlayerData."+P.getUniqueId()+".LatePayments")) {
			return conf.getInt("PlayerData."+P.getUniqueId()+".LatePayments");
		}else {
			return 0;
		}
	}
	
	public boolean isHighRisk(Player P) {
		if (getLatePayments(P) >= 3) {
			return true;
		}else {
		return false;
		}
	}
	
	public int getTimePlayed(Player P) {
		return conf.getInt("PlayerData."+P.getUniqueId()+".PlayTime");
	}
	
	public int credit(Player P) {
		int creditLine = 20;
		int PlayTime = conf.getInt("PlayerData."+P.getUniqueId()+".PlayTime");
		int Payments = 0;
		int CreditScore = 0;
		int latepayments = 1;
		boolean highrisk = false;
		
		if (conf.contains("PlayerData."+P.getUniqueId()+".Payments")) {
			Payments = conf.getInt("PlayerData."+P.getUniqueId()+".Payments");
		}
		
		if (conf.contains("PlayerData."+P.getUniqueId()+".LatePayments")) {
			latepayments = conf.getInt("PlayerData."+P.getUniqueId()+".LatePayments");
		}
		
		if (latepayments >= 3) {
			highrisk = true;
		}
		
		if (latepayments <= 1) {
			latepayments=1;
		}
		
		if (!highrisk) {
			if (PlayTime >= 60) {
				CreditScore = (PlayTime/60)*(10*Payments)/latepayments+200;
			}else {
		CreditScore = PlayTime*(3*Payments)/latepayments+100;
			}
		} else {
			CreditScore = CreditScore/(latepayments*3);
		}
		
		return CreditScore;
	}
	
	
	public void runDuty() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			
		 
			
			@Override
			public void run() {
				saveAll();
				if (debug) {
				Bukkit.getLogger().info("RUN!");
				}
				
				VersionCheck();
			}
			
			},0, 1200);
	}
	
	public void saveLoanSys() {
		for (Loan HG : Loans) {
			LoansList.add(HG.getID());
					String PRS = HG.getID(); 
    				conf.set("LoanInfo."+PRS+".Player", HG.getOwner().getUniqueId().toString()); 
    				conf.set("LoanInfo."+PRS+".Amount", HG.getAmount()); 
    				conf.set("LoanInfo."+PRS+".PayAmount", HG.getPaymentAmount()); 
    				conf.set("LoanInfo."+PRS+".PayRemaining", HG.getPaymentsRemaining()); 
    				conf.set("LoanInfo."+PRS+".Type", HG.getType());
    				conf.set("LoanInfo."+PRS+".NextPay", HG.getNextPay()); 
    				conf.set("LoanInfo."+PRS+".Forgivness", HG.getForgivness());
    				if (HG.involvesPropety()) {
    					conf.set("LoanInfo."+PRS+".Property", HG.getProperty());
    				}else{
    					conf.set("LoanInfo."+PRS+".Property", "");
    				}
		}
		this.saveConfig();
	}
	
	public void saveAll() {
		
		B.AutoPay(this, econ);
		
		for (Loan HG : Loans) {
			LoansList.add(HG.getID());
					String PRS = HG.getID(); 
    				conf.set("LoanInfo."+PRS+".Player", HG.getOwner().getUniqueId().toString()); 
    				conf.set("LoanInfo."+PRS+".Amount", HG.getAmount()); 
    				conf.set("LoanInfo."+PRS+".PayAmount", HG.getPaymentAmount()); 
    				conf.set("LoanInfo."+PRS+".PayRemaining", HG.getPaymentsRemaining()); 
    				conf.set("LoanInfo."+PRS+".Type", HG.getType());
    				conf.set("LoanInfo."+PRS+".NextPay", HG.getNextPay()); 
    				conf.set("LoanInfo."+PRS+".Forgivness", HG.getForgivness());
    				if (HG.involvesPropety()) {
    					conf.set("LoanInfo."+PRS+".Property", HG.getProperty());
    				}else{
    					conf.set("LoanInfo."+PRS+".Property", "");
    				}
		}
		this.saveConfig();
		
		ServerTime++;
		if (debug) {
		Bukkit.getLogger().info("SavedAll");
		}
		for (Player P : Online) {
			if (debug) {
			Bukkit.getLogger().info("Save "+P.getName());
			}
			
			/*
			if (this.getConfig().contains("PlayerData."+P.getUniqueId().toString()+".NextBill")) {
				if (this.getConfig().getInt("PlayerData."+P.getUniqueId().toString()+".PlayTime") >= this.getConfig().getInt("PlayerData."+P.getUniqueId().toString()+".NextBill")) {
					if (econ.has(P, this.getConfig().getInt("PlayerData."+P.getUniqueId().toString()+".PaymentAmmount"))) {
						econ.withdrawPlayer(P, this.getConfig().getInt("PlayerData."+P.getUniqueId().toString()+".PaymentAmmount"));
						P.sendMessage(TAG+ChatColor.GREEN+"Loan Payment of $"+this.getConfig().getInt("PlayerData."+P.getUniqueId().toString()+".PaymentAmmount"));
						this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".LoanAmmount", (this.getConfig().getInt("PlayerData."+P.getUniqueId().toString()+".PaymentAmmount")-this.getConfig().getInt("PlayerData."+P.getUniqueId().toString()+".LoanAmmount")));
						conf.set("PlayerData."+P.getUniqueId()+".Payments", (conf.getInt("PlayerData."+P.getUniqueId()+".Payments")+1));
					}else {
						P.sendMessage(TAG+ChatColor.RED+ChatColor.BOLD+ChatColor.UNDERLINE+" You MISSED a Loan Payment of $"+this.getConfig().getInt("PlayerData."+P.getUniqueId().toString()+".PaymentAmmount"));
						conf.set("PlayerData."+P.getUniqueId()+".Payments", (conf.getInt("PlayerData."+P.getUniqueId()+".LatePayments")+1));
					}
				}
			}
			*/
			if (this.getConfig().contains("PlayerData."+P.getUniqueId().toString()+".NextRent")) {
				this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".NextRent", "None");
			}
			if (this.getConfig().getString("PlayerData."+P.getUniqueId().toString()+".NextRent").equalsIgnoreCase("None")) {
			}else {
				if (this.getConfig().getInt("PlayerData."+P.getUniqueId().toString()+".PlayTime") >= this.getConfig().getInt("PlayerData."+P.getUniqueId().toString()+".NextRent")) {
					if (econ.has(P, this.getConfig().getInt("PlayerData."+P.getUniqueId().toString()+".Rent"))) {
						econ.withdrawPlayer(P, this.getConfig().getInt("PlayerData."+P.getUniqueId().toString()+".Rent"));
						P.sendMessage(TAG+ChatColor.GREEN+"Loan Payment of $"+this.getConfig().getInt("PlayerData."+P.getUniqueId().toString()+".Rent"));
						conf.set("PlayerData."+P.getUniqueId()+".Payments", (conf.getInt("PlayerData."+P.getUniqueId()+".Payments")+1));
						conf.set("PlayerData."+P.getUniqueId()+".NextRent", (conf.getInt("PlayerData."+P.getUniqueId()+".PlayTime")+(12*60)));
					}else {
						String PR = this.getConfig().getString("PlayerData."+P.getUniqueId().toString()+".Renting");
						Player ld = Bukkit.getPlayer(UUID.fromString(this.getConfig().getString("Propertys."+PR+".Seller")));
		    			   int rent = this.getConfig().getInt("Propertys."+PR+".Rent");
		    			   this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".Renting", "None");
		    			   this.getConfig().set("Propertys."+PR+".Rent", 0);
		    			   this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".NextRent", 0);
						
						//TODO REMOVE PROPETY RIGHTS
						DefaultDomain OWNS = new DefaultDomain();
						OWNS.removeAll();
						OWNS.addPlayer(ld.getUniqueId());
						getWorldGuard().getRegionManager(P.getWorld()).getRegion(getRegionName(P)).setOwners(OWNS);
						P.sendMessage(TAG+ChatColor.RED+ChatColor.UNDERLINE+ChatColor.BOLD+" YOU HAVE BEEN EVICTED!");
						this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".NextRent", "None");
						this.saveConfig();
					}
				}
			}
			
	    	if (this.getConfig().contains("PlayerData."+P.getUniqueId().toString()+".NextRent")) {
	    		
	    	}else {
	    		this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".NextRent", "None");
	    	}
			
			savePlayerData(P);
			
			
		}
	}
	
	
	private WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
	 
	    return (WorldGuardPlugin) plugin;
	}
	

	
	
	@EventHandler
	public void onJoin(PlayerLoginEvent E) {
		
		Player P = E.getPlayer();
		
		Online.add(P);
		
		if (this.getConfig().contains("PlayerData."+P.getUniqueId().toString())) {
			int PT = 0;
			PT = conf.getInt("PlayerData."+P.getUniqueId().toString()+".PlayTime");
			PT = (PT+1);
			this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".PlayTime", PT);
			this.saveConfig();
		}else {
			this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".PlayTime", 1);
		}
		savePlayerData(P);
		this.saveConfig();
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent E) {
		
		Player P = E.getPlayer();
		savePlayerData(P);
		Online.remove(P);
		this.saveConfig();
	}
	
	public void VersionCheck() {
		
		
		URL url = null;
		try {
			url = new URL("https://raw.githubusercontent.com/Wintergrasped/WinterEco-Estates/master/version");
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

        // Get the input stream through URL Connection
        URLConnection con = null;
		try {
			con = url.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        InputStream is = null;
		try {
			is = con.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        // Once you have the Input Stream, it's just plain old Java IO stuff.

        // For this case, since you are interested in getting plain-text web page
        // I'll use a reader and output the text content to System.out.

        // For binary content, it's better to directly read the bytes from stream and write
        // to the target file.


        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;

        // read each line and write to System.out
        try {
			while ((line = br.readLine()) != null) {
			    UpdateVersion = line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if (!curVersion.equalsIgnoreCase(UpdateVersion)) {
        	Bukkit.getLogger().log(Level.WARNING, "*===========================================*");
        	Bukkit.getLogger().log(Level.WARNING, "*==WinterEco is out of date Please Update!==*");
        	Bukkit.getLogger().log(Level.WARNING, "*===========================================*");
        	Bukkit.getLogger().log(Level.WARNING, "Curent Version: "+curVersion+" UpdatedVersion: "+UpdateVersion);
        	Bukkit.getLogger().log(Level.WARNING, "Download Latest Version Here: https://www.spigotmc.org/resources/wintereco-estates.47510/");
        }
    }
		
	
	
	public void collectDebt() {
		/*
		this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".LoanAmmount", LN);
		this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".PaymentAmmount", paym);
		this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".NextPayment", paym);
		this.getConfig().set("PlayerData."+P.getUniqueId().toString()+".CreditLock", true);
		*/
		
		for (String DOS : inDebt) {
			Player DO = Bukkit.getPlayer(UUID.fromString(DOS));
			int ammount = this.getConfig().getInt("PlayerData."+DO.getUniqueId().toString()+".LoanAmmount");
			int payammount = this.getConfig().getInt("PlayerData."+DO.getUniqueId().toString()+".PaymentAmmount");

			econ.withdrawPlayer(DO, payammount);
			this.getConfig().set("PlayerData."+DO.getUniqueId().toString()+".LoanAmmount", (ammount-payammount));
			
			
		}
		
	}
	
	public int getNextBillCycle(Player P) {
		
		int PT = this.getConfig().getInt("PlayerData."+P.getUniqueId().toString()+".PlayTime");
		int PST = (1200*60)*12;
		PT= PT+PST;
		return PT;
	}
	
	
	public void updateWG() {
		
		for (String PR : Propertys) {
			
			Player P = Bukkit.getPlayer(UUID.fromString(this.getConfig().getString("Propertys."+PR+".Owner")));
			
			
			if (P.isOnline()) {
			DefaultDomain OWNS = new DefaultDomain();
			OWNS.removeAll();
			OWNS.addPlayer(UUID.fromString(this.getConfig().getString("Propertys."+PR+".Owner")));
			getWorldGuard().getRegionManager(P.getWorld()).getRegion(PR).setOwners(OWNS);
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
