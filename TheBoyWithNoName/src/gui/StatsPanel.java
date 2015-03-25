package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import logic.Boy;

public class StatsPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public StatsPanel(){
		this.setSize(GameFrame.WIDTH, STATS_HEIGHT);
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		loadInformations();
	}
	
	private void loadInformations() {
		try {
			statsPanel=ImageIO.read(getClass().getResource("../images/statsBar.png"));
			livingHeart=ImageIO.read(getClass().getResource("../images/livingHeart.png"));
			deadHeart=ImageIO.read(getClass().getResource("../images/deadHeart.png"));
			pixelFont=Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("../fonts/pixel.ttf")).deriveFont(35.0f);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(Color.WHITE);
		g2.setFont(pixelFont);
		g2.drawImage(statsPanel,0,0,GameFrame.WIDTH-5,STATS_HEIGHT,null);
		
		for(int i=0; i<Boy.MAX_LIFE; i++){
			if(boy.getLife()>i){
				g2.drawImage(livingHeart,HEARTS_START_X+HEARTS_X_DISTANCE*i,HEARTS_START_Y,HEARTS_SIZE,HEARTS_SIZE,null);
			} else {
				g2.drawImage(deadHeart,HEARTS_START_X+HEARTS_X_DISTANCE*i,HEARTS_START_Y,HEARTS_SIZE,HEARTS_SIZE,null);
			}
		}
		
		g2.drawString("x"+boy.getDarts(), DARTS_COUNT_START_X, DARTS_COUNT_START_Y);
	}
	

	public void addBoy(Boy boy) {
		this.boy=boy;
	}
	
	private Font pixelFont;
	private BufferedImage livingHeart;
	private BufferedImage deadHeart;
	private BufferedImage statsPanel;
	public static final int STATS_HEIGHT=40;
	private Boy boy;
	private static final int HEARTS_X_DISTANCE=60;
	private static final int HEARTS_START_X=84;
	private static final int HEARTS_START_Y=4;
	private static final int DARTS_COUNT_START_X=785;
	private static final int DARTS_COUNT_START_Y=30;
	private static final int HEARTS_SIZE=32;
}
