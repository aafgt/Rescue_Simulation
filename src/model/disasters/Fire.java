package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import model.infrastructure.ResidentialBuilding;


public class Fire extends Disaster {

	public Fire(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);
		
	}
	@Override
	public void strike() throws Exception
	{
		ResidentialBuilding target= (ResidentialBuilding)getTarget();
		
		//try {
			if(target.getStructuralIntegrity() == 0)
				throw new BuildingAlreadyCollapsedException(this, "Building already collapsed.");
			
			target.setFireDamage(target.getFireDamage()+10);
			super.strike();
		//}
		//catch(BuildingAlreadyCollapsedException e) {
		//	e.getMessage();
		//}
		
	}

	@Override
	public void cycleStep() {
		ResidentialBuilding target= (ResidentialBuilding)getTarget();
		target.setFireDamage(target.getFireDamage()+10);
		
	}

}
