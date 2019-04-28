package driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import objects.Match;
import objects.Team;

public class MainDriver {
	
	static ArrayList<Team> listOfTeams = new ArrayList<Team>();
	static ArrayList<Match> listOfMatches = new ArrayList<Match>();

	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(new File("TeamList"));
			//For each line, split the line into team, wins, and losses, and build a Team object per line from those values
			while (sc.hasNextLine()) {
				String[] teamInfo = sc.nextLine().split(",");
				String teamName = teamInfo[0];
				int wins = Integer.parseInt(teamInfo[1]);
				int losses = Integer.parseInt(teamInfo[2]);
				listOfTeams.add(new Team(teamName, wins, losses));
			}
			sc.close();
			
			sc = new Scanner(new File("MatchList"));
			//For each line, split the line into team1 and team2, and build a Match object per line from those values
			while (sc.hasNextLine()) {
				String[] teams = sc.nextLine().split(",");
				String team1Name = teams[0];
				String team2Name = teams[1];
				Team team1 = null;
				Team team2 = null;
				for (Team team : listOfTeams) {
					if (team1Name.equals(team.teamName))
						team1 = team;
					else if (team2Name.equals(team.teamName))
						team2 = team;
				}
				if (team1 == null || team2 == null)
					System.err.println("Team in match list not found in team list: " + team1.teamName + " vs " + team2.teamName);
				Match newMatch = new Match(team1, team2);
				listOfMatches.add(newMatch);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//Build a new algorithm using the list of teams and matches and print the results of the algorithms
		Algorithm algorithm = new Algorithm(listOfTeams, listOfMatches);
		ArrayList<Team> viableTeams = algorithm.findViableTeams();
		System.out.println("Done");
		System.out.println(" ");
		System.out.println("List of Viable Teams:");
		for (Team team : viableTeams)
			System.out.println(team.teamName);
	}

}
