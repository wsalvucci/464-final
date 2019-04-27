package driver;

import java.util.ArrayList;

import objects.Connector;
import objects.Match;
import objects.Team;

public class Algorithm {
	
	private ArrayList<Team> listOfTeams;
	private ArrayList<Match> listOfMatches;
	private ArrayList<Connector> listOfConnectors;
	private int gamesPerSeason = 162;
	
	public Algorithm(ArrayList<Team> listOfTeams, ArrayList<Match> listOfMatches, ArrayList<Connector> listOfConnectors) {
		System.out.println("Loading algorithm");
		this.listOfTeams = listOfTeams;
		this.listOfMatches = listOfMatches;
		this.listOfConnectors = listOfConnectors;
	}
	
	public ArrayList<Team> findViableTeams() {
		System.out.println("Searching for viable teams");
		ArrayList<Team> viableTeams = removeObvious(this.listOfTeams);
		
		for (Team team : viableTeams) {
			System.out.println("Checking viablity for " + team.teamName);
			//Assume the team being checked wins the rest of their games
			System.out.println("Assuming that " + team.teamName + " wins remaining games");
			ArrayList<Connector> connectors = copyConnectors();
			for (Connector connector : connectors) {
				if (connector.team.equals(team))
					connector.win = true;
			}
			//Check if the team is viable
			if (checkViability(team, copyTeams(), connectors))
				viableTeams.add(team);
			System.out.println("\n");
		}
		
		return viableTeams;
	}
	
	private ArrayList<Team> removeObvious(ArrayList<Team> teamList) {
		System.out.println("Removing teams eliminated by magic numbers");
		ArrayList<Team> viableTeams = new ArrayList<Team>();
		
		Integer leaderWins = 0;
		for (Team team : teamList) {
			if (team.wins > leaderWins)
				leaderWins = team.wins;
		}
		System.out.println("Leader has " + leaderWins + " wins");
		for (Team team : teamList) {
			System.out.println("Team " + team.teamName + " has a magic number of " + (this.gamesPerSeason + 1 - leaderWins - team.losses));
			if (this.gamesPerSeason + 1 - leaderWins - team.losses > 0)
				viableTeams.add(team);
			else
				System.out.println("Team: " + team.teamName + " eliminated due to magic numbers");
		}
		
		return viableTeams;
	}
	
	private ArrayList<Team> copyTeams() {
		ArrayList<Team> teams = new ArrayList<Team>();
		
		for (Team team : this.listOfTeams)
			teams.add(team.copy());
		
		return teams;
	}
	
	private ArrayList<Connector> copyConnectors() {
		ArrayList<Connector> connectors = new ArrayList<Connector>();
		
		for (Connector connector : this.listOfConnectors)
			connectors.add(connector.copy());
		
		return connectors;
	}
	
	private ArrayList<ArrayList<Connector>> getAllCombinations(ArrayList<Connector> connectors, Team team) {
		ArrayList<ArrayList<Connector>> allCombinations = new ArrayList<ArrayList<Connector>>();

		//Get all the connectors connecting to the opposing team that haven't been set
		ArrayList<Connector> connectorsToProcess = new ArrayList<Connector>();
		System.out.println("Processing connections for " + team.teamName);
		for (Connector connector : connectors) {
			if (!connector.win && connector.team.equals(team))
				connectorsToProcess.add(connector);
		}
		
		int numOfElements = connectorsToProcess.size();
		int numOfCombinations = (int) Math.pow(2, numOfElements);
		
		System.out.println(team.teamName + " has " + numOfCombinations + " of combinations to process");
		
		//TODO: Get all combinations
		
		return allCombinations;
	}
	
	private boolean areAllConnectorsSet(ArrayList<Connector> connectors) {
		for (Connector connector : connectors) {
			boolean matchSet = false;
			for (Connector matchConnector : connector.match.connections) {
				if (matchConnector.win)
					matchSet = true;
			}
			if (!matchSet)
				return false;
		}
		return true;
	}
	
	private boolean checkViability(Team team, ArrayList<Team> teamList, ArrayList<Connector> connectors) {
		
		if (teamList.isEmpty())
			return true;
		
		for (int i=0; i < teamList.size(); i++) {
			if (!team.teamName.equals(teamList.get(i).teamName)) {
				System.out.print(teamList.get(i).teamName + ": ");
				//Get the max number of wins this team can win without eliminating the team being checked
				int winsAllowed = this.gamesPerSeason + 1 - teamList.get(i).wins - team.losses;
				
				//Get the current weight of all connectors that have been set connecting to the opposing team
				int currentWeight = 0;
				for (Connector connector : connectors) {
					if (connector.team.equals(teamList.get(i)) && connector.win)
						currentWeight++;
				}
				
				ArrayList<ArrayList<Connector>> combinations = getAllCombinations(connectors, teamList.get(i));
				
				//Check all combinations of weights
				for (ArrayList<Connector> combination : combinations) {
					System.out.println("Trying a combination for " + teamList.get(i).teamName + " against " + team.teamName);
					//Check if combination gives the team too many wins
					int winsGiven = 0;
					for (Connector connector : combination) {
						if (connector.team.equals(teamList.get(i)) && connector.win)
							winsGiven++;
					}
					
					if (winsGiven + currentWeight > winsAllowed) {
					
						//If all matches have been set, return true
						if (areAllConnectorsSet(connectors))
							return true;
						
						//If not all combinations set, make a copy of the current list without the already checked team
						ArrayList<Team> teamListCopy = teamList;
						teamListCopy.remove(i);
						
						//Recursively check with the new list of connectors given by the combination currently selected
						if (checkViability(team, teamList, combination))
							return true;
					} else {
						System.out.println("Combination failed: too many wins given to " + teamList.get(i));
					}
				}
			}
		}
		System.out.println("\n");
		System.out.println("Team: " + team.teamName + " eliminated");
		return false;
	}
}
