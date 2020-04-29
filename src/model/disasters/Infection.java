package model.disasters;

import exceptions.CitizenAlreadyDeadException;
import model.people.Citizen;
import model.people.CitizenState;


public class Infection extends Disaster {

	public Infection(int startCycle, Citizen target) {
		super(startCycle, target);
	}
@Override
public void strike() throws Exception
{
	Citizen target = (Citizen)getTarget();
	
	//try {
		if(target.getState().equals(CitizenState.DECEASED))
			throw new CitizenAlreadyDeadException(this, "Citizen is already dead.");
		
		target.setToxicity(target.getToxicity()+25);
		super.strike();
	//}
	//catch(CitizenAlreadyDeadException e) {
	//	e.getMessage();
	//}
	
}
	@Override
	public void cycleStep() {
		Citizen target = (Citizen)getTarget();
		target.setToxicity(target.getToxicity()+15);
		
	}

}
