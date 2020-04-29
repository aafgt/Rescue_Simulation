package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class RSView extends JFrame{
	private JPanel rs;
	private JPanel e;
	private JPanel w;
	
	private JPanel grid;
	//private JButton buttons[][];
	private JPanel panelHolder[][];
	
	private JPanel units;
	private JTextArea info;
	
	private JPanel aunits2;
	private JLabel idleunits;
	private JPanel aunits;
	
	private JPanel runits2;
	private JLabel respondingunits;
	private JPanel runits;
	
	private JPanel tunits2;
	private JLabel treatingunits;
	private JPanel tunits;
	
	private JButton nxtCycle;
	
	private JPanel numberofc3;
	private JLabel numberofc;
	private JTextArea numberofc2;
	
	private JPanel currentcycle3;
	private JLabel currentcycle;
	private JTextArea currentcycle2;
	
	//private JButton respond;
	
	//private JPanel allInfo;
	private JTextArea disastersInfo;
	
	private JScrollPane sp;
	private JPanel infoPanel;
	
	private JScrollPane sp2;
	private JPanel disastersPanel;
	
	private JButton callafriend;
	
	public RSView() {
		this.setVisible(true);
		this.setTitle("Rescue Simulation");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setLayout(new BorderLayout());
	
		rs = new JPanel();
		rs.setLayout(new BorderLayout());
		this.add(rs, BorderLayout.CENTER);
		revalidate();
		repaint();
		
		e = new JPanel();
		e.setLayout(new BorderLayout());
		rs.add(e, BorderLayout.EAST);
		revalidate();
		repaint();
		
		w = new JPanel();
		w.setLayout(new GridLayout(4,1));
		rs.add(w, BorderLayout.WEST);
		revalidate();
		repaint();
		
		grid = new JPanel();
		grid.setLayout(new GridLayout(10, 10));
		rs.add(grid, BorderLayout.CENTER);
		//grid.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
		revalidate();
		repaint();
		
		//buttons = new JButton[10][10];
		//addGridButtons();
		
		panelHolder = new JPanel[10][10];
		addGridPanels();
		
		units = new JPanel();
		units.setLayout(new BorderLayout());
		e.add(units, BorderLayout.NORTH);
		revalidate();
		repaint();
		
		aunits2 = new JPanel();
		aunits2.setLayout(new BorderLayout());
		units.add(aunits2, BorderLayout.NORTH);
		revalidate();
		repaint();
		
		idleunits = new JLabel("Available Units");
		aunits2.add(idleunits, BorderLayout.NORTH);
		revalidate();
		repaint();
		
		aunits = new JPanel();
		aunits.setLayout(new FlowLayout());
		aunits2.add(aunits, BorderLayout.CENTER);
		revalidate();
		repaint();
		
		runits2 = new JPanel();
		runits2.setLayout(new BorderLayout());
		units.add(runits2, BorderLayout.CENTER);
		revalidate();
		repaint();
		
		respondingunits = new JLabel("Responding Units");
		runits2.add(respondingunits, BorderLayout.NORTH);
		revalidate();
		repaint();
		
		runits = new JPanel();
		runits.setLayout(new FlowLayout());
		runits2.add(runits, BorderLayout.CENTER);
		revalidate();
		repaint();
		
		tunits2 = new JPanel();
		tunits2.setLayout(new BorderLayout());
		units.add(tunits2, BorderLayout.SOUTH);
		revalidate();
		repaint();
		
		treatingunits = new JLabel("Treating Units");
		tunits2.add(treatingunits, BorderLayout.NORTH);
		revalidate();
		repaint();
		
		tunits = new JPanel();
		tunits.setLayout(new FlowLayout());
		tunits2.add(tunits, BorderLayout.CENTER);
		revalidate();
		repaint();
		
//		allInfo = new JPanel();
//		allInfo.setLayout(new BorderLayout());
//		w.add(allInfo, BorderLayout.CENTER);
//		revalidate();
//		repaint();
		
		infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		w.add(infoPanel);
		
		disastersPanel = new JPanel();
		disastersPanel.setLayout(new BorderLayout());
		w.add(disastersPanel);
		
		info = new JTextArea(30,20);
		//info = new JTextArea();
		info.setEditable(false);
		info.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		infoPanel.add(info, BorderLayout.CENTER);
		//allInfo.add(info, BorderLayout.CENTER);
		revalidate();
		repaint();
		
		disastersInfo = new JTextArea(30,20);
		disastersInfo.setEditable(false);
		info.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		disastersPanel.add(disastersInfo, BorderLayout.CENTER);
		revalidate();
		repaint();
		
		//respond = new JButton("Respond");
		//w.add(respond);
		
		nxtCycle = new JButton("Next Cycle");
		nxtCycle.setPreferredSize(new Dimension(50, 100));
		e.add(nxtCycle, BorderLayout.SOUTH);
		revalidate();
		repaint();
		
		numberofc3 = new JPanel();
		numberofc3.setLayout(new BorderLayout());
		w.add(numberofc3);
		revalidate();
		repaint();
		
		numberofc = new JLabel("Number of casualties");
		numberofc3.add(numberofc, BorderLayout.NORTH);
		revalidate();
		repaint();
		
		numberofc2 = new JTextArea();
		numberofc2.setEditable(false);
		numberofc2.setText("0");
		numberofc3.add(numberofc2, BorderLayout.CENTER);
		revalidate();
		repaint();
		
		currentcycle3 = new JPanel();
		currentcycle3.setLayout(new BorderLayout());
		e.add(currentcycle3, BorderLayout.CENTER);
		revalidate();
		repaint();
		
		currentcycle = new JLabel("Current Cycle");
		currentcycle3.add(currentcycle, BorderLayout.NORTH);
		revalidate();
		repaint();
		
		currentcycle2 = new JTextArea();
		currentcycle2.setEditable(false);
		currentcycle2.setText("0");
		currentcycle3.add(currentcycle2, BorderLayout.CENTER);
		revalidate();
		repaint();
		
		sp = new JScrollPane(info);
		infoPanel.add(sp);
		revalidate();
		repaint();
		
		sp2 = new JScrollPane(disastersInfo);
		disastersPanel.add(sp2);
		revalidate();
		repaint();
		
		callafriend = new JButton("Call a friend");
		w.add(callafriend);
		revalidate();
		repaint();
		
	}
	
	public void addIdleUnit(JButton b) {
		aunits.add(b);
	}
	
	public void removeIdleUnit(JButton b) {
		aunits.remove(b);
	}
	
	public void addRespondingUnit(JButton b) {
		runits.add(b);
	}
	
	public void removeRespondingUnit(JButton b) {
		runits.remove(b);
	}
	
	public void addTreatingUnit(JButton b) {
		tunits.add(b);
	}
	
	public void removeTreatingUnit(JButton b) {
		tunits.remove(b);
	}
	
	public void addBuildingsCitizens(JButton b, int i, int j) {
		//buttons[i][j] = b;
		(panelHolder[j][i]).add(b);
		revalidate();
		repaint();
	}
	
	/*public void addBuildingsCitizens(JButton b) {
		grid.add(b);
	}*/
	
	public void addGridPanels() {
		for(int i=0; i < 10; i++) {
			for(int j=0; j < 10; j++) {
				panelHolder[i][j] = new JPanel();
				(panelHolder[i][j]).setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
				//(panelHolder[i][j]).setPreferredSize(new Dimension(50,50));
				grid.add(panelHolder[i][j]);
			}
		}
		revalidate();
		repaint();
	}
	
	/*public void addGridButtons() {
		for(int i=0; i < 10; i++) {
			for(int j=0; j < 10; j++) {
				buttons[i][j] = new JButton(" ");
				(buttons[i][j]).setPreferredSize(new Dimension(50,50));
				grid.add(buttons[i][j]);
			}
		}
		revalidate();
		repaint();
	}*/
	
	public void updateCurrentCycle() {
		int c = Integer.parseInt(currentcycle2.getText());
		c++;
		currentcycle2.setText(c + "");
	}
	
	public JTextArea getNumberOfC2() {
		return numberofc2;
	}
	
	public JButton getNxtCycle() {
		return nxtCycle;
	}
	
	public JTextArea getInfo() {
		return info;
	}
	
	public JTextArea getDisastersInfo() {
		return disastersInfo;
	}
	
	public JPanel getRs() {
		return rs;
	}
	
	public JButton getCallafriend() {
		return callafriend;
	}
	
	/*public JButton getRespond() {
		return respond;
	}*/
	
	/*public static void main(String[] args) {
		new RSView();
	}*/

}
