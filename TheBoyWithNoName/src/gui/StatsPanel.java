package gui;

import java.awt.Color;
import javax.swing.JPanel;

public class StatsPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public StatsPanel(){
		this.setSize(GameFrame.WIDTH, STATS_HEIGHT);
		this.setBackground(Color.WHITE);
		this.setLayout(null);
	}
	
	public static final int STATS_HEIGHT=40;
}
