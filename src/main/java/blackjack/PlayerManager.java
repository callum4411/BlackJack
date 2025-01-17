package blackjack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private static final String FILE_PATH = "/Users/callumsmith/IdeaProjects/save/src/main/resources/blackjack/players.txt";
    private List<Player> players;

    public PlayerManager() {
        players = loadPlayers();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player createPlayer(String name) {
        Player newPlayer = new Player(name);
        players.add(newPlayer);
        savePlayers();
        return newPlayer;
    }
    // this saves the players to the text file that has all info on players
    public void savePlayers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(players);
        } catch (IOException e) {
            System.out.println("Error saving players: " + e.getMessage());
        }
    }

    public List<Player> loadPlayers() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Player>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading players: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    // this updates info about players
    public void updatePlayer(Player player) {
        for (Player p : players) {
            if (p.getName().equals(player.getName())) {
                p.setWins(player.getWins());
                p.setLosses(player.getLosses());
                break;
            }
        }
        savePlayers();
    }
}
