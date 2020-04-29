package controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import model.units.UnitState;
import simulation.Rescuable;
import simulation.Simulator;
import view.RSView;

public class CommandCenter implements SOSListener, ActionListener, MouseListener {		

	private Simulator engine;
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private ArrayList<Unit> emergencyUnits;
	
	private ArrayList<Disaster> disasters;
	
	private RSView rsView;
	
	private ArrayList<JButton> unitsbtns;
	private ArrayList<Unit> eunits;
	
	private ArrayList<JButton> buildingsbtns;
	private ArrayList<ResidentialBuilding> buildings;
	
	private ArrayList<JButton> citizensbtns;
	private ArrayList<Citizen> citizens;

	private ArrayList<JButton> idlebtns;
	private ArrayList<JButton> respondingbtns;
	private ArrayList<JButton> treatingbtns;
	
	//private boolean r = false;
	private JButton b1;
	
	private JButton base;
	
	public CommandCenter() throws Exception {
		engine = new Simulator(this);
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		emergencyUnits = engine.getEmergencyUnits();
		
		rsView = new RSView();
		rsView.getNxtCycle().addActionListener(this);
		rsView.getNxtCycle().addMouseListener(this);
		rsView.getCallafriend().addActionListener(this);
		rsView.getCallafriend().addMouseListener(this);
		//rsView.getRespond().addActionListener(this);
		//rsView.getRespond().addMouseListener(this);
		
		idlebtns = new ArrayList<JButton>();
		respondingbtns = new ArrayList<JButton>();
		treatingbtns = new ArrayList<JButton>();
		
		unitsbtns = new ArrayList<JButton>();
		eunits = new ArrayList<Unit>();
		addUnits();
		rsView.revalidate();
		rsView.repaint();
		
		buildingsbtns = new ArrayList<JButton>();
		buildings = new ArrayList<ResidentialBuilding>();
		
		citizensbtns = new ArrayList<JButton>();
		citizens = new ArrayList<Citizen>();
		
		base = new JButton("Base");
		base.setPreferredSize(new Dimension(50, 50));
		base.addActionListener(this);
		base.addMouseListener(this);
		rsView.addBuildingsCitizens(base, 0, 0);
		
	}

	@Override
	public void receiveSOSCall(Rescuable r) {
		
		if (r instanceof ResidentialBuilding) {
			
			if (!visibleBuildings.contains(r)) {
				visibleBuildings.add((ResidentialBuilding) r);
				addBuilding((ResidentialBuilding) r);
				rsView.revalidate();
				rsView.repaint();
			}
				
			
		} else {
			
			if (!visibleCitizens.contains(r)) {
				visibleCitizens.add((Citizen) r);
				addCitizen((Citizen) r);
				rsView.revalidate();
				rsView.repaint();
			}
				
		}

	}
	
	public void addUnits() {
		for(int i=0; i < engine.getEmergencyUnits().size(); i++) {
			JButton b = new JButton();
			b.setPreferredSize(new Dimension(50, 50));
			Unit u = engine.getEmergencyUnits().get(i);
			if(u instanceof Ambulance) 
				b.setText("AMB");
			else if(u instanceof DiseaseControlUnit) 
				b.setText("DCU");
			else if(u instanceof Evacuator)
				b.setText("EVC");
			else if(u instanceof FireTruck)
				b.setText("FTK");
			else if(u instanceof GasControlUnit)
				b.setText("GCU");
			
			b.addActionListener(this);
			b.addMouseListener(this);
			rsView.addIdleUnit(b);		
			unitsbtns.add(b);
			eunits.add(u);
			
			idlebtns.add(b);
		}
	}
	
	public void addBuilding(ResidentialBuilding r) {
		JButton b = new JButton();
		b.setPreferredSize(new Dimension(50, 50));
		
		if(r.getOccupants().size() == 0) {
			b.setText("E.B");
		} else {
			b.setText("B");
		}
		
		int x = r.getLocation().getX();
		int y = r.getLocation().getY();
		
		b.addActionListener(this);
		b.addMouseListener(this);
		rsView.addBuildingsCitizens(b, x, y);
		rsView.revalidate();
		rsView.repaint();
		
		buildingsbtns.add(b);
		buildings.add(r);
	}
	
	public void addCitizen(Citizen r) {
		JButton b = new JButton();
		b.setPreferredSize(new Dimension(50, 50));
		b.setText("C");
		
		int x = r.getLocation().getX();
		int y = r.getLocation().getY();
		
		b.addActionListener(this);
		b.addMouseListener(this);
		rsView.addBuildingsCitizens(b, x, y);
		rsView.revalidate();
		rsView.repaint();
		
		citizensbtns.add(b);
		citizens.add(r);
	}
	
	public void updateC() {
		int c = engine.calculateCasualties();
		rsView.getNumberOfC2().setText(c + "");
	}
	
	public void checkDC() {
		for(int i=0; i < citizens.size(); i++) {
			if(citizens.get(i).getState().equals(CitizenState.DECEASED)) {
				citizensbtns.get(i).setText("C.D");
			}
		}
	}
	
	public void checkDB() {
		for(int i=0; i < citizens.size(); i++) {
			if(buildings.get(i).getStructuralIntegrity() == 0) {
				buildingsbtns.get(i).setText("B.D");
			}
		}
	}
	
	public void updateUnitsBasedOnState() {
		for(int i=0; i < eunits.size(); i++) {
			if(eunits.get(i).getState().equals(UnitState.IDLE)) {
				JButton b = unitsbtns.get(i);
				
				if(!unitIdleButtonContained(b)) {
					rsView.addIdleUnit(b);
				}
				
				if(unitRespondingButtonContained(b)) {
					rsView.removeRespondingUnit(b);
				}
				
				if(unitTreatingButtonContained(b)) {
					rsView.removeTreatingUnit(b);
				}
				
			}else if(eunits.get(i).getState().equals(UnitState.RESPONDING)) {
				JButton b = unitsbtns.get(i);
				
				if(unitIdleButtonContained(b)) {
					rsView.removeIdleUnit(b);
				}
				
				if(!unitRespondingButtonContained(b)) {
					rsView.addRespondingUnit(b);
				}
				
				if(unitTreatingButtonContained(b)) {
					rsView.removeTreatingUnit(b);
				}
				
			}else if(eunits.get(i).getState().equals(UnitState.TREATING)) {
				JButton b = unitsbtns.get(i);

				if(unitIdleButtonContained(b)) {
					rsView.removeIdleUnit(b);
				}
				
				if(unitRespondingButtonContained(b)) {
					rsView.removeRespondingUnit(b);
				}
				
				if(!unitTreatingButtonContained(b)) {
					rsView.addTreatingUnit(b);
				}
			}
		}
	}
	
	public boolean unitIdleButtonContained(JButton b) {
		for(int i=0; i < idlebtns.size(); i++) {
			if(idlebtns.get(i).equals(b)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean unitRespondingButtonContained(JButton b) {
		for(int i=0; i < respondingbtns.size(); i++) {
			if(respondingbtns.get(i).equals(b)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean unitTreatingButtonContained(JButton b) {
		for(int i=0; i < treatingbtns.size(); i++) {
			if(treatingbtns.get(i).equals(b)) {
				return true;
			}
		}
		return false;
	}
	
	/*public void updateDisastersInfo() {
		System.out.println("aaaaaaa");
		//String s = rsView.getDisastersInfo() + "";
		for(int i=0; i < engine.getExecutedDisasters().size(); i++) {
			System.out.println("bbbbbbbbb");
			Disaster d = engine.getExecutedDisasters().get(i);
			if(!disasterContained(d)) {
				System.out.println("cccccc");
				disasters.add(d);
				String s = display(d) + ", Target: " + d.getTarget().getLocation() + "\n";
				rsView.getDisastersInfo().append(s);
			}
		}
		//rsView.getDisastersInfo().setText(s);
	}
	public boolean disasterContained(Disaster d) {
		for(int i=0; i < disasters.size(); i++) {
			if(d.equals(disasters.get(i))) {
				return true;
			}
		}
		return false;
	}
	public String display(Disaster d) {
		if(d instanceof Infection) {
			return "Infection";
		}else if(d instanceof Injury) {
			return "Injury";
		}else if(d instanceof Fire) {
			return "Fire";
		}else if(d instanceof GasLeak) {
			return "Gas Leak";
		}else if(d instanceof Collapse) {
			return "Collapse";
		}
		
		return "No Disaster";
	}*/
	
	/*public void updateUnits() {
		
		for(int i=0; i < eunits.size(); i++) {
			if(eunits.get(i).getState().equals(UnitState.IDLE)) {
				
			}else if(eunits.get(i).getState().equals(UnitState.RESPONDING)) {
				
			}else if(eunits.get(i).getState().equals(UnitState.TREATING)) {
				
			}
		}
	}*/
	
	/*public void addBuildings() {
		for(int i=0; i < engine.getBuildings().size(); i++) {
			JButton b = new JButton();
			b.setPreferredSize(new Dimension(50, 50));
			b.setText("B");
			
			ResidentialBuilding r = engine.getBuildings().get(i);
			
			int x = r.getLocation().getX();
			int y = r.getLocation().getY();
			
			b.addActionListener(this);
			b.addMouseListener(this);
			rsView.addBuildingsCitizens(b, x, y);
			rsView.revalidate();
			rsView.repaint();
			
			buildingsbtns.add(b);
			buildings.add(r);
		}
	}*/
	
	/*public void addCitizens() {
		for(int i=0; i < engine.getCitizens().size(); i++) {
			JButton b = new JButton();
			b.setPreferredSize(new Dimension(50, 50));
			b.setText("C");
			
			Citizen r = engine.getCitizens().get(i);
			
			int x = r.getLocation().getX();
			int y = r.getLocation().getY();
			
			b.addActionListener(this);
			b.addMouseListener(this);
			rsView.addBuildingsCitizens(b, x, y);
			rsView.revalidate();
			rsView.repaint();
			
			citizensbtns.add(b);
			citizens.add(r);
		}
	}*/
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Next Cycle")) {
			try {
				engine.nextCycle();
				rsView.updateCurrentCycle();
				updateC();
				checkDC();
				checkDB();
				
				updateUnitsBasedOnState();
				
				if(engine.checkGameOver()) {
					JOptionPane.showMessageDialog(rsView.getRs(), "Game Over\n" + "Casualties: " + engine.calculateCasualties());
					System.exit(0);
				}
				
				rsView.revalidate();
				rsView.repaint();
				//updateDisastersInfo();
			} catch(BuildingAlreadyCollapsedException e1) {
				//e1.getMessage();
				JOptionPane.showMessageDialog(rsView, e1.getMessage());
			} catch(CannotTreatException e1) {
				//e1.getMessage();
				JOptionPane.showMessageDialog(rsView, e1.getMessage());
			} catch(CitizenAlreadyDeadException e1) {
				//e1.getMessage();
				JOptionPane.showMessageDialog(rsView, e1.getMessage());
			} catch(IncompatibleTargetException e1) {
				//e1.getMessage();
				JOptionPane.showMessageDialog(rsView, e1.getMessage());
			} catch(Exception e1) {
				e1.getMessage();
			}
		}
		
		/*if(e.getActionCommand().equals("Respond")) {
			r = true;
		}*/
		
		if( (e.getActionCommand().equals("AMB") || e.getActionCommand().equals("DCU") || e.getActionCommand().equals("EVC") || e.getActionCommand().equals("FTK") || e.getActionCommand().equals("GCU")) ) {
			b1 = (JButton) e.getSource();
		}
		
		if(e.getActionCommand().equals("E.B")) {
			if(!(b1 == null)) {
				JButton b2 = (JButton) e.getSource();
				int x1 = unitsbtns.indexOf(b1);
				Unit u = eunits.get(x1);
				int x2 = buildingsbtns.indexOf(b2);
				ResidentialBuilding bldg = buildings.get(x2);
				try {
					u.respond(bldg);
				} catch(BuildingAlreadyCollapsedException e1) {
					JOptionPane.showMessageDialog(rsView, e1.getMessage());
				} catch(CannotTreatException e1) {
					JOptionPane.showMessageDialog(rsView, e1.getMessage());
				} catch(CitizenAlreadyDeadException e1) {
					JOptionPane.showMessageDialog(rsView, e1.getMessage());
				} catch(IncompatibleTargetException e1) {
					JOptionPane.showMessageDialog(rsView, e1.getMessage());
				} catch(Exception e1) {
					e1.getMessage();
				}
				
				b1 = null;
			}
		}
		
		if(e.getActionCommand().equals("B")) {
			if(!(b1 == null)) {
				JButton b2 = (JButton) e.getSource();
				int x1 = unitsbtns.indexOf(b1);
				Unit u = eunits.get(x1);
				int x2 = buildingsbtns.indexOf(b2);
				ResidentialBuilding bldg = buildings.get(x2);
				try {
					u.respond(bldg);
				} catch(BuildingAlreadyCollapsedException e1) {
					JOptionPane.showMessageDialog(rsView, e1.getMessage());
				} catch(CannotTreatException e1) {
					JOptionPane.showMessageDialog(rsView, e1.getMessage());
				} catch(CitizenAlreadyDeadException e1) {
					JOptionPane.showMessageDialog(rsView, e1.getMessage());
				} catch(IncompatibleTargetException e1) {
					JOptionPane.showMessageDialog(rsView, e1.getMessage());
				} catch(Exception e1) {
					e1.getMessage();
				}
				
				b1 = null;
			}
		} 
		
		if(e.getActionCommand().equals("C")) {
			if(!(b1 == null)) {
				JButton b2 = (JButton) e.getSource();
				int x1 = unitsbtns.indexOf(b1);
				Unit u = eunits.get(x1);
				int x2 = citizensbtns.indexOf(b2);
				Citizen ctzn = citizens.get(x2);
				try {
					u.respond(ctzn);
				} catch(BuildingAlreadyCollapsedException e1) {
					JOptionPane.showMessageDialog(rsView, e1.getMessage());
				} catch(CannotTreatException e1) {
					JOptionPane.showMessageDialog(rsView, e1.getMessage());
				} catch(CitizenAlreadyDeadException e1) {
					JOptionPane.showMessageDialog(rsView, e1.getMessage());
				} catch(IncompatibleTargetException e1) {
					JOptionPane.showMessageDialog(rsView, e1.getMessage());
				} catch(Exception e1) {
					e1.getMessage();
				}
				
				b1 = null;
			}
		}
		
		if(e.getActionCommand().equals("Base")) {
			String s = "";
			for(int i = 0; i < eunits.size(); i++) {
				Unit u = eunits.get(i);
				int x = u.getLocation().getX();
				int y = u.getLocation().getY();
				if(x == 0 && y == 0) {
					s += u.unitInfo() + "\n";
				}
			}
			rsView.getInfo().setText(s);
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int clickcount = e.getClickCount();
	
		if(clickcount == 1) {
			for(int i=0; i < buildingsbtns.size(); i++) {
				if(e.getSource().equals(buildingsbtns.get(i))) {
					ResidentialBuilding r = buildings.get(i);
					String s = r.BuildingInfo();
					rsView.getInfo().setText(s);
					//rsView.getInfo().append(s);
				}
			}
			
			for(int i=0; i < citizensbtns.size(); i++) {
				if(e.getSource().equals(citizensbtns.get(i))) {
					Citizen r = citizens.get(i);
					String s = r.citizenInfo();
					rsView.getInfo().setText(s);
					//rsView.getInfo().append(s);
				}
			}
			
			for(int i=0; i < unitsbtns.size(); i++) {
				if(e.getSource().equals(unitsbtns.get(i))) {
					Unit r = eunits.get(i);
					String s = r.unitInfo();
					rsView.getInfo().setText(s);
					//rsView.getInfo().append(s);
				}
			}
			
			
			if(e.getSource().equals(rsView.getNxtCycle())) {
				//updateDisastersInfo();
			
				for(int i=0; i < buildingsbtns.size(); i++) {
					ResidentialBuilding r = buildings.get(i);
					String s = r.BuildingInfo();
					//rsView.getDisastersInfo().setText(s);
					rsView.getDisastersInfo().append(s);
					
				}
			}
			
			for(int i=0; i < citizensbtns.size(); i++) {
				if(e.getSource().equals(citizensbtns.get(i))) {
					Citizen r = citizens.get(i);
					String s = r.citizenInfo();
					//rsView.getInfo().setText(s);
					rsView.getDisastersInfo().append(s);
				}
			}
			
		} 
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}
	
	public static void main(String[] args) throws Exception{
		new CommandCenter();
	}

}
