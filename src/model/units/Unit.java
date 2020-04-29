package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Disaster;
import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;
	private WorldListener worldListener;

	public Unit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state = UnitState.IDLE;
		this.worldListener = worldListener;
	}

	public void setWorldListener(WorldListener listener) {
		this.worldListener = listener;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}

	@Override
	public void respond(Rescuable r) throws Exception{
		
		//try {
			if(!canTreat(r))
				throw new CannotTreatException(this,r,"Target is already safe.");
			
			if (target != null && state == UnitState.TREATING)
				reactivateDisaster();
			finishRespond(r);
		//}
		//catch(CannotTreatException e){
		//	e.getMessage();
			//System.out.println("Target is already safe.");
		//}

	}

	public void reactivateDisaster() {
		Disaster curr = target.getDisaster();
		curr.setActive(true);
	}

	public void finishRespond(Rescuable r) {
		target = r;
		state = UnitState.RESPONDING;
		Address t = r.getLocation();
		distanceToTarget = Math.abs(t.getX() - location.getX())
				+ Math.abs(t.getY() - location.getY());

	}

	public abstract void treat();

	public void cycleStep() {
		if (state == UnitState.IDLE)
			return;
		if (distanceToTarget > 0) {
			distanceToTarget = distanceToTarget - stepsPerCycle;
			if (distanceToTarget <= 0) {
				distanceToTarget = 0;
				Address t = target.getLocation();
				worldListener.assignAddress(this, t.getX(), t.getY());
			}
		} else {
			state = UnitState.TREATING;
			treat();
		}
	}

	public void jobsDone() {
		target = null;
		state = UnitState.IDLE;

	}
	
	public boolean canTreat(Rescuable r) {
		
		if(r instanceof Citizen) {
			Citizen c = (Citizen)r;
			
			if(c.getBloodLoss() == 0 && c.getToxicity() == 0)
				return false;
		}
		else if(r instanceof ResidentialBuilding) {
			ResidentialBuilding b = (ResidentialBuilding)r;
			
			if(b.getFireDamage() == 0 && b.getGasLevel() == 0)
				return false;
		}
		
		return true;
	}
	
	public String unitInfo() {
		String s2 = "Type: ";
		if(this instanceof Ambulance) 
			s2 += "Ambulance";
		else if(this instanceof DiseaseControlUnit) 
			s2 += "Disease Control Unit";
		else if(this instanceof Evacuator)
			s2 += "Evacuator";
		else if(this instanceof FireTruck)
			s2 += "Fire Truck";
		else if(this instanceof GasControlUnit)
			s2 += "Gas Control Unit";
		
		String s = "ID: " + getUnitID() + "\n" + s2 + "\nLocation: " + getLocation() + "\nSteps per Cycle: "
				+ getStepsPerCycle() + "\nState: " + getState();
		
		if(!(getTarget() == null)) {
			String s3 = "\nTarget: " + getTarget() + "\nTarget Location: " + getTarget().getLocation(); 
			s += s3;
		}
		
		return s;
	}
	
}
