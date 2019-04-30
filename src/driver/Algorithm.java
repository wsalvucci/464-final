package driver;

import java.util.ArrayList;

import objects.Match;
import objects.Team;

public class Algorithm {
	
	private ArrayList<Team> listOfTeams;
	private ArrayList<Match> listOfMatches;
	private int gamesPerSeason = 82;
	
	public Algorithm(ArrayList<Team> listOfTeams, ArrayList<Match> listOfMatches) {
		System.out.println("Loading algorithm");
		this.listOfTeams = listOfTeams;
		this.listOfMatches = listOfMatches;
	}
	
	public ArrayList<Team> findViableTeams() {
		System.out.println("Searching for viable teams");
		ArrayList<Team> possibleTeams = removeObvious(this.listOfTeams);
		ArrayList<Team> viableTeams = new ArrayList<Team>();
		
		for (Team team : possibleTeams) {
			System.out.println("Checking viablity for " + team.teamName);
			//Assume the team being checked wins the rest of their games
			System.out.println("Assuming that " + team.teamName + " wins remaining games");
			ArrayList<Match> matches = copyMatches();
			for (Match match : matches) {
				if (match.team1.equals(team) || match.team2.equals(team))
					match.winner = team;
			}
			//Make sure letting them win didn't clear everything
			if (areAllMatchesSet(matches)) {
				System.out.println(team.teamName + " winning all their matches results in clearing");
				viableTeams.add(team);
			}
			else {
				//Check if the team is viable
				if (checkViability(team, copyTeams(), matches)) {
					System.out.println(team.teamName + " is a viable team");
					viableTeams.add(team);
				}
			}
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
		System.out.println(" ");
		return viableTeams;
	}
	
	private ArrayList<Team> copyTeams() {
		ArrayList<Team> teams = new ArrayList<Team>();
		
		for (Team team : this.listOfTeams)
			teams.add(team.copy());
		
		return teams;
	}
	
	private ArrayList<Match> copyMatches() {
		ArrayList<Match> matches = new ArrayList<Match>();
		
		for (Match match : this.listOfMatches)
			matches.add(match.copy());
		
		return matches;
	}
	
	private ArrayList<ArrayList<Match>> getAllCombinations(ArrayList<Match> matches, Team team) {
		ArrayList<ArrayList<Match>> allCombinations = new ArrayList<ArrayList<Match>>();

		//Get all the connectors connecting to the opposing team that haven't been set
		ArrayList<Integer> matchesToProcess = new ArrayList<Integer>();
		System.out.println("Processing matches for " + team.teamName);
		for (int i=0; i < matches.size(); i++) {
			if (matches.get(i).team1.teamName.equals(team.teamName) || matches.get(i).team2.teamName.equals(team.teamName)) {
				if (matches.get(i).winner == null)
					matchesToProcess.add(i);
			}
		}
		
		int numOfElements = matchesToProcess.size();
		System.out.println("Need to process " + numOfElements + " matches");
		int numOfCombinations = 0;
		if (numOfElements != 0)
			numOfCombinations = (int) Math.pow(2, numOfElements);
		
		System.out.println(team.teamName + " has " + numOfCombinations + " match combination(s) to process");
		
		//TODO: Get all combinations
		if (numOfElements != 0) {
			for (int i=0; i < numOfCombinations; i++) {
				ArrayList<Match> combination = new ArrayList<Match>();
				char[] flags = Integer.toBinaryString(i).toCharArray();
				for (int j=0; j < numOfElements;j++)
					//System.out.print("L");
				for (Character flag : flags) {
					switch(flag) {
					case '0':
						//System.out.print("L");
						break;
					case '1':
						//System.out.println("W");
						break;
					}
				}
				//System.out.println(" ");
				for (int j = flags.length; j < Math.sqrt(numOfCombinations); j++) {
					char[] newFlags = new char[flags.length+1];
					newFlags[0] = '0';
					for (int k=0; k < flags.length; k++)
						newFlags[k+1] = flags[k];
					flags = newFlags;
				}
				for(int j=0; j < matchesToProcess.size(); j++) {
					Match match = matches.get(matchesToProcess.get(j)).copy();
					if (flags[j] == '1') {
						match.winner = team;
					} else {
						if (!match.team1.teamName.equals(team.teamName))
							match.winner = match.team1;
						else
							match.winner = match.team2;
					}
					combination.add(match);
				}
				allCombinations.add(combination);
			}
		}
		
		System.out.println("\n");
		return allCombinations;
	}
	
	private boolean areAllMatchesSet(ArrayList<Match> matches) {
		for (Match match : matches) {
			if (match.winner == null)
				return false;
		}
		return true;
	}
	
	private boolean checkViability(Team team, ArrayList<Team> teamList, ArrayList<Match> matches) {
		
		if (teamList.isEmpty())
			return true;
		
		System.out.println("Num of teams to process: " + teamList.size());
		System.out.println(" ");
		
		for (int i=0; i < teamList.size(); i++) {
			if (!team.teamName.equals(teamList.get(i).teamName)) {
				System.out.println("Team " + teamList.get(i).teamName + ": ");
				//Get the max number of wins this team can win without eliminating the team being checked
				int winsAllowed = this.gamesPerSeason + 1 - teamList.get(i).wins - team.losses;
				System.out.println(team.teamName + " can afford to have " + teamList.get(i).teamName + " to win " + winsAllowed + " more games");
				
				//Get the current weight of all connectors that have been set connecting to the opposing team
				int currentWeight = 0;
				for (Match match : matches) {
					if (match.winner != null && match.winner.teamName.equals(teamList.get(i).teamName))
						currentWeight++;
				}
				System.out.println(teamList.get(i).teamName + " has already been given " + currentWeight + " games in this combination");
				
				ArrayList<ArrayList<Match>> combinations = getAllCombinations(matches, teamList.get(i));
				
				//Check all combinations of weights
				for (ArrayList<Match> combination : combinations) {
					System.out.println("Trying a combination for " + teamList.get(i).teamName + " against " + team.teamName + " of size " + combination.size());
					//Check if combination gives the team too many wins
					
					boolean fail = false;
					for (Team opp : teamList) {
						if (!opp.teamName.equals(team.teamName)) {
							int curOppWins = opp.wins;
							int curLosses = team.losses;
							System.out.println(team.teamName + " can afford to give " + opp.teamName + " " + (this.gamesPerSeason + 1 - curOppWins - curLosses) + " wins");
							int affordedWins = this.gamesPerSeason + 1 - curOppWins - curLosses;
							int givenWins = 0;
							for (Match match : combination) {
								if (match.winner.teamName.equals(opp.teamName))
									givenWins++;
							}
							if (givenWins > affordedWins)
								fail = true;
							System.out.println(givenWins + " win(s) have been given to " + opp.teamName);
						}
					}
					if (!fail) {
						System.out.println("Are all matches set? " + areAllMatchesSet(combination));
						//If all matches have been set, return true
						if (areAllMatchesSet(combination))
							return true;
						
						//If not all combinations set, make a copy of the current list without the already checked team
						ArrayList<Team> teamListCopy = teamList;
						teamListCopy.remove(i);
						
						//Recursively check with the new list of connectors given by the combination currently selected
						if (checkViability(team, teamList, combination))
							return true;
					} else {
						System.out.println("Combination failed: Gave too many wins to a team");
					}
					System.out.println(" ");
				}
			}
		}
		System.out.println("\n");
		System.out.println("Team " + team.teamName + " eliminated");
		return false;
	}
}
