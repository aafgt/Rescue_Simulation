package exceptions;

import model.disasters.Disaster;

@SuppressWarnings("serial")
public abstract class DisasterException extends SimulationException{
	Disaster disaster;

	public DisasterException(Disaster disaster) {
		super();
		this.disaster = disaster;
	}
	
	public DisasterException(Disaster disaster, String message) {
		super(message);
		this.disaster = disaster;
	}
	
	public Disaster getDisaster() {
		return disaster;
	}
	
	

}
