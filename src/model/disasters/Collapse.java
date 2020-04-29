package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;
import model.people.CitizenState;


public class Collapse extends Disaster {

	public Collapse(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);
		
	}
	public void strike() throws Exception
	{
		ResidentialBuilding target= (ResidentialBuilding)getTarget();
		
		//try {
			if(target.getStructuralIntegrity() == 0)
				throw new BuildingAlreadyCollapsedException(this, "Building already collapsed.");
			
			target.setFoundationDamage(target.getFoundationDamage()+10);
			super.strike();
		//}
		//catch(BuildingAlreadyCollapsedException e) {
		//	e.getMessage();
		//}
		
	}
	public void cycleStep()
	{
		ResidentialBuilding target= (ResidentialBuilding)getTarget();
		target.setFoundationDamage(target.getFoundationDamage()+10);
	}

}
