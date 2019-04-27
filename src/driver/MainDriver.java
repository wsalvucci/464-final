package driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import objects.Connector;
import objects.Match;
import objects.Team;

public class MainDriver {
	
	static ArrayList<Team> listOfTeams = new ArrayList<Team>();
	static ArrayList<Match> listOfMatches = new ArrayList<Match>();
	static ArrayList<Connector> listOfConnectors = new ArrayList<Connector>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Scanner sc = new Scanner(new File("TeamList"));
			while (sc.hasNextLine()) {
				String[] teamInfo = sc.nextLine().split(",");
				String teamName = teamInfo[0];
				int wins = Integer.parseInt(teamInfo[1]);
				int losses = Integer.parseInt(teamInfo[2]);
				listOfTeams.add(new Team(teamName, wins, losses));
			}
			sc.close();
			
			sc = new Scanner(new File("MatchList"));
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
					System.out.println("Team in match list not found in team list");
				Match newMatch = new Match(team1, team2);
				listOfMatches.add(newMatch);
				Connector connector1 = new Connector(team1, newMatch);
				Connector connector2 = new Connector(team2, newMatch);
				newMatch.connections.add(connector1);
				newMatch.connections.add(connector2);
				team1.connections.add(connector1);
				team2.connections.add(connector2);
				listOfConnectors.add(connector1);
				listOfConnectors.add(connector2);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Algorithm algorithm = new Algorithm(listOfTeams, listOfMatches, listOfConnectors);
		ArrayList<Team> viableTeams = algorithm.findViableTeams();
		System.out.println("Done");
	}

}
