package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Fire;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;

public class GasControlUnit extends FireUnit {

	public GasControlUnit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getGasLevel() > 0) 
			target.setGasLevel(target.getGasLevel() - 10);

		if (target.getGasLevel() == 0)
			jobsDone();

	}
	
	public void respond(Rescuable r) throws Exception{
		//try {
		if(r instanceof Citizen)
			throw new IncompatibleTargetException(this, r, "GasControlUnit can only handle Buildings.");
		
		if(!canTreat(r))
			throw new CannotTreatException(this,r,"Building is already safe.");
		
		if( ((ResidentialBuilding)r).getDisaster() instanceof Fire )
			throw new CannotTreatException(this,r,"Building is on fire.");
		
		super.respond(r);
		
	//}
	//catch(IncompatibleTargetException e){
	//	e.getMessage();
	//}
	//catch(IncompatibleTargetException e){
	//	e.getMessage();
	//}
	}

}
