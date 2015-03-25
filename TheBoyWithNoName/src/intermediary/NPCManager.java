package intermediary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import logic.NPC;
import logic.Tile;

public class NPCManager {
	public NPCManager(int currentLevel){
		this.currentLevel=currentLevel;
		currentNPCs=new ArrayList<NPC>();
		loadInformations();
	}
	
	public void initializeStage(int currentLevel) {
		currentNPCs.clear();
		this.currentLevel=currentLevel;
		loadInformations();
	}
	
	private void loadInformations() {
		InputStream is=this.getClass().getResourceAsStream("/npc_info/level"+String.valueOf(currentLevel)+".txt");
		if(is==null){
			return;
		}
		BufferedReader reader=new BufferedReader(new InputStreamReader(is));
		String line=null;
		String[] singleNPCInfo;
		try {
			while((line=reader.readLine())!=null){
				singleNPCInfo=line.split(" ");
				currentNPCs.add(new NPC(singleNPCInfo[0],Integer.valueOf(singleNPCInfo[1]),
						Integer.valueOf(singleNPCInfo[2]),Integer.valueOf(singleNPCInfo[3]),currentLevel));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<NPC> getNPCs() {
		return currentNPCs;
	}
	
	//returns the closest enemy within two tiles of distance (which is 
	//the maximum distance allowed for an interaction with a NPC)
	public NPC closestNPC(int boyRow, int boyCol) {
		NPC closestNPC=null;
		int currentDistance;
		for(int i=0; i<currentNPCs.size(); i++){
			if(boyRow!=currentNPCs.get(i).getRow()){
				continue;
			}
			if((Math.abs(currentNPCs.get(i).getCol()-boyCol))<=MAXIMUM_TALKING_DISTANCE){
				currentDistance=Math.abs(currentNPCs.get(i).getCurrentX()-(boyCol*Tile.TILE_SIZE));
				if(closestNPC==null){
					closestNPC=currentNPCs.get(i);
				} else {
					if(currentDistance<Math.abs(closestNPC.getCurrentX()-boyCol+Tile.TILE_SIZE)){
						closestNPC=currentNPCs.get(i);
					}
				}
			}
		}
		
		return closestNPC;
	}	
	
	private static final int MAXIMUM_TALKING_DISTANCE=2;
	private ArrayList<NPC> currentNPCs;
	private int currentLevel;
}
