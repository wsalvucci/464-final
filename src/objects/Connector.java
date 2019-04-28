package objects;

public class Connector {
	
	public Team team;
	public Match match;
	public boolean win = false;
	public boolean set = false;

	public Connector(Team team, Match match) {
		this.team = team;
		this.match = match;
	}
	
	public Connector copy() {
		return new Connector(this.team, this.match);
	}
}
