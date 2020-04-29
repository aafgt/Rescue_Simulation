package model.people;

import model.disasters.Disaster;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSListener;
import model.events.WorldListener;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public class Citizen implements Rescuable,Simulatable{
	private CitizenState state;
	private Disaster disaster;
	private String name;
	private String nationalID;
	private int age;
	private int hp;
	private int bloodLoss;
	private int toxicity;
	private Address location;
	private SOSListener emergencyService;
	private WorldListener worldListener;
	public Citizen(Address location,String nationalID, String name, int age
			,WorldListener worldListener) {
		this.name = name;
		this.nationalID = nationalID;
		this.age = age;
		this.location = location;
		this.state=CitizenState.SAFE;
		this.hp=100;
		this.worldListener = worldListener;
	}
	
	public WorldListener getWorldListener() {
		return worldListener;
	}

	public void setWorldListener(WorldListener listener) {
		this.worldListener = listener;
	}

	public CitizenState getState() {
		return state;
	}
	public void setState(CitizenState state) {
		this.state = state;
	}
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
		if(this.hp>=100)
			this.hp=100;
		else if(this.hp<=0){
			this.hp = 0;
			state=CitizenState.DECEASED;
		}
	}
	public int getBloodLoss() {
		return bloodLoss;
	}
	public void setBloodLoss(int bloodLoss) {
		this.bloodLoss = bloodLoss;
		if(bloodLoss<=0)
			this.bloodLoss=0;
		else if(bloodLoss>=100)
		{
			this.bloodLoss=100;
			setHp(0);
		}
	}
	public int getToxicity() {
		return toxicity;
	}
	public void setToxicity(int toxicity) {
		this.toxicity = toxicity;
		if(toxicity>=100)
		{
			this.toxicity=100;
			setHp(0);
		}
		else if(this.toxicity<=0)
			this.toxicity=0;
	}
	public Address getLocation() {
		return location;
	}
	public void setLocation(Address location) {
		this.location = location;
	}
	public Disaster getDisaster() {
		return disaster;
	}
	public String getNationalID() {
		return nationalID;
	}
	
	public void setEmergencyService(SOSListener emergency) {
		this.emergencyService = emergency;
	}
	@Override
	public void cycleStep() {
		if(bloodLoss>0 && bloodLoss<30)
			setHp(hp-5);
		else if(bloodLoss>=30 && bloodLoss<70)
			setHp(hp-10);
		else if(bloodLoss >=70)
			setHp(hp-15);
		if (toxicity >0 && toxicity < 30)
			setHp(hp-5);
		else if(toxicity>=30 &&toxicity<70)
			setHp(hp-10);
		else if(toxicity>=70)
			setHp(hp-15);
	}
	@Override
	public void struckBy(Disaster d) {
		if(disaster!=null)
			disaster.setActive(false);
		disaster=d;
		state= CitizenState.IN_TROUBLE;
		emergencyService.receiveSOSCall(this);
		
	}
	
	public String citizenInfo() {
		String s = "Location: " + getLocation() + "\nName: " + getName() + "\nAge: " + getAge() 
				 + "\nNational ID: " + getNationalID() + "\nHp: " + getHp() + "\nBloodLoss: "
				 + getBloodLoss() + "\nToxicity: " + getToxicity() + "\nState: " 
				 + getState() + "\n";
				
		if(!(getDisaster() == null)) {
			String s2 = "Disaster: " + display(getDisaster()); 
			s += s2;
		}
		
		return s;
	}
	
	public String citizenBInfo() {
		String s = "Name: " + getName() + "\nAge: " + getAge() 
				 + "\nNational ID: " + getNationalID() + "\nHp: " + getHp() + "\nBloodLoss: "
				 + getBloodLoss() + "\nToxicity: " + getToxicity() + "\nState: " 
				 + getState() + "\n";
				
		if(!(getDisaster() == null)) {
			String s2 = "Disaster: " + display(getDisaster()); 
			s += s2;
		}
		
		return s;
	}
	
	public String display(Disaster d) {
		if(d instanceof Infection) {
			return "Infection";
		}else if(d instanceof Injury) {
			return "Injury";
		}
		
		return "No Disaster";
	}
}
