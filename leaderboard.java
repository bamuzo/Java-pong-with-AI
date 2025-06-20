import java.awt.*;
import java.io.*;
import javax.swing.JPanel;

class Player implements Comparable<Player> {
    private String name;
    private int score;

    public Player(String name, int score) {
        this.name = name.length() > 3 ? name.substring(0, 3) : name; // takes the first 3 letters of the players name
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Player other) {
        return Integer.compare(other.score, this.score); // compares the score in descending order
    }

    @Override
    public String toString() {
        return name + " " + score;
    }
}

public class leaderboard extends JPanel{
    private static final int MAX_PLAYERS = 10;
    private static final String FILE_NAME = "leaderboard.txt";

    private Player[] players;
    private int playerCount = 0;

    public leaderboard() {
        players = new Player[MAX_PLAYERS];
        loadfromFile(); // need this here to load the leaderboard from file when the class is ran or like yknow used
    }

    public void addPlayer(String name, int score){
        Player newPlayer = new Player(name, score);

        int i;
        for (i = 0; i < playerCount; i++) {
            if (players[i].getScore() < score) {
                break;
            }
        } // this inserts the player into the array based on their score

        if (i < MAX_PLAYERS) {
            // because this array isnt dynamic, this moves all players down one index to make space for the new player
            for (int j = Math.min(playerCount, MAX_PLAYERS - 1); j > i; j--) {
                players[j] = players[j - 1];
            }
            players[i] = newPlayer;
            if (playerCount < MAX_PLAYERS) playerCount++;
        }
        saveToFile(); // saves the leaderboard to file after adding a new player
    }

    private void loadfromFile(){
         try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null && playerCount < MAX_PLAYERS) {
                String[] parts = line.split(" "); // splits the line into name and score
                if (parts.length == 2) { // checks if the line has both a name and a score
                    String name = parts[0]; // takes the first part as the name
                    int score = Integer.parseInt(parts[1]); // takes the second part as the score
                    // If the name is longer than 3 characters, it will take the first 3
                    players[playerCount++] = new Player(name, score); //adds to array
                }
            }

            // sorts the players again based on score
            sortPlayers();
        } catch (IOException | NumberFormatException e) {
            // File might not exist or be corupteed so we just clear the leaderboard instead
            System.err.println("Error loading leaderboard: " + e.getMessage());
        }
    }
    //function definition to sort the players ^^^ used above
    private void sortPlayers() {
        for (int i = 0; i < playerCount - 1; i++) {
            for (int j = i + 1; j < playerCount; j++) {
                if (players[j].getScore() > players[i].getScore()) {
                    Player temp = players[i];
                    players[i] = players[j];
                    players[j] = temp;
                }
            }
        }
    }
    //function that saves the players to a file
    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < playerCount; i++) {
                writer.write(players[i].getName() + " " + players[i].getScore());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g); // clears the panel first
    g.setColor(Color.WHITE);
    setBackground(Color.BLACK);
    g.setFont(new Font("Monospaced", Font.PLAIN, 16));
    g.drawString("Leaderboard", 50, 40); // header

    for (int i = 0; i < playerCount; i++) {
        g.drawString((i + 1) + ". " + players[i].getName() + ": " + players[i].getScore(), 50, 70 + i * 20);
    }
}

}


