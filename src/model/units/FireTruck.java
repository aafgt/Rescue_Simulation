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

public class FireTruck extends FireUnit {

	public FireTruck(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getFireDamage() > 0)

			target.setFireDamage(target.getFireDamage() - 10);

		if (target.getFireDamage() == 0)

			jobsDone();

	}
	
	public void respond(Rescuable r) throws Exception{
		//try {
		if(r instanceof Citizen)
			throw new IncompatibleTargetException(this, r, "FireTruck can only handle Buildings.");
		
		if(!canTreat(r))
			throw new CannotTreatException(this,r,"Building is already safe.");
		
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
	}

}
