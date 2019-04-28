package objects;

public class Match {
	
	public Team team1;
	public Team team2;
	public Team winner = null;

	public Match(Team team1, Team team2) {
		this.team1 = team1;
		this.team2 = team2;
	}
	
	private Match(Team team1, Team team2, Team winner) {
		this.team1 = team1;
		this.team2 = team2;
		this.winner = winner;
	}
	
	public Match copy() {
		return new Match(this.team1, this.team2, this.winner);
	}
	
	public Match copyTeams() {
		return new Match(this.team1, this.team2);
	}
}
