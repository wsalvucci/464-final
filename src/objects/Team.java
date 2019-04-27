package objects;

import java.util.ArrayList;

public class Team {
	
	public String teamName;
	public int wins;
	public int losses;
	public ArrayList<Connector> connections = new ArrayList<Connector>();
	
	public Team(String teamName, int wins, int losses) {
		this.teamName = teamName;
		this.wins = wins;
		this.losses = losses;
	}
	
	public Team copy() {
		return new Team(this.teamName, this.wins, this.losses);
	}
	
}
