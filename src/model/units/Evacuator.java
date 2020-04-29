package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;

public class Evacuator extends PoliceUnit {

	public Evacuator(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener, int maxCapacity) {
		super(unitID, location, stepsPerCycle, worldListener, maxCapacity);

	}

	@Override
	public void treat() {
		
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0
				|| target.getOccupants().size() == 0) {
			jobsDone();
			return;
		}

		for (int i = 0; getPassengers().size() != getMaxCapacity()
				&& i < target.getOccupants().size(); i++) {
			getPassengers().add(target.getOccupants().remove(i));
			i--;
		}

		setDistanceToBase(target.getLocation().getX()
				+ target.getLocation().getY());

	}
	
	public void respond(Rescuable r) throws Exception{
		//try {
		if(r instanceof Citizen)
			throw new IncompatibleTargetException(this, r, "Evacuator can only handle Buildings.");
		
		if(!canTreat(r))
			throw new CannotTreatException(this,r,"Building is already safe.");
		
		if( ((ResidentialBuilding)r).getDisaster() instanceof Fire )
			throw new CannotTreatException(this,r,"Building is on fire.");
		
		if( ((ResidentialBuilding)r).getDisaster() instanceof GasLeak )
			throw new CannotTreatException(this,r,"Building has a gas leak.");
		
		super.respond(r);
		
	//}
	//catch(IncompatibleTargetException e){
	//	e.getMessage();
	//}
	//catch(IncompatibleTargetException e){
	//	e.getMessage();
	//}
	//catch(CannotTreatException e) {
	//	e.getMessage();
	//}	
		
	}
	
	public String unitInfo() {
		String s = super.unitInfo();
		String s2 = "";
		for(int i=0; i < getPassengers().size(); i++) {
			s2 += getPassengers().get(i).citizenBInfo() + "\n";
		}
	
		s += "\nPassengers Size: " + getPassengers().size() + "\n" + s2;
		return s;
	}

}
