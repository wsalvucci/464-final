package objects;

import java.util.ArrayList;

public class Match {
	
	public Team team1;
	public Team team2;
	public ArrayList<Connector> connections = new ArrayList<Connector>();
	public Team winner = null;

	public Match(Team team1, Team team2) {
		this.team1 = team1;
		this.team2 = team2;
	}
	
	private Match(Team team1, Team team2, ArrayList<Connector> connections, Team winner) {
		this.team1 = team1;
		this.team2 = team2;
		this.connections = connections;
		this.winner = winner;
	}
	
	public Match copy() {
		return new Match(this.team1, this.team2, this.connections, this.winner);
	}
	
	public Match copyTeams() {
		return new Match(this.team1, this.team2);
	}
}
