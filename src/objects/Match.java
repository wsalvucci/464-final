package objects;

import java.util.ArrayList;

public class Match {
	
	public Team team1;
	public Team team2;
	public ArrayList<Connector> connections = new ArrayList<Connector>();

	public Match(Team team1, Team team2) {
		this.team1 = team1;
		this.team2 = team2;
	}
}
