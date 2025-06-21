import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameFrame extends JFrame implements ActionListener {
    private GamePanel aiPanel;
    private GamePanelPVP pvpPanel;
    private leaderboard leaderboardPanel;
    private JPanel currentPanel;

    private JMenuItem PvP;
    private JMenuItem PvA;
    private JMenuItem WinCon;
    private JMenuItem viewLeaderboardItem;
    public GameFrame() {
        this.setTitle("Pongo");
        this.setResizable(false);
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout()); // need it to change the layout (choose between AI and PVP)
        this.setSize(1000, 600); // default size

        // makes the starting panel PVP
        pvpPanel = new GamePanelPVP();
        currentPanel = pvpPanel;
        this.add(currentPanel, BorderLayout.CENTER);

        createMenuBar(); //creates the menu bar to choose

        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar(); // adds the menu bar thing on top
        PvP = new JMenuItem("Player vs Player"); // names the item inside the menubar
        PvA = new JMenuItem("Player vs AI"); // ^^ same deal
       
        JMenu WinConMenu = new JMenu("Set Win Condition"); // to set win condition
        WinCon = new JMenuItem("Win Condition");
        WinConMenu.add(WinCon);

        JMenu gameMenu = new JMenu("Game Type"); // menu bar button to show the items listed ^^
        gameMenu.add(PvP); // adds to game type bar
        gameMenu.add(PvA);

        JMenu leaderboardMenu = new JMenu("Leaderboard"); 
        // leaderboard menu
       viewLeaderboardItem = new JMenuItem("View Leaderboard");
       leaderboardMenu.add(viewLeaderboardItem);

        PvP.addActionListener(this); // makes it so that it can listen to the input
        PvA.addActionListener(this);
        WinCon.addActionListener(this);
        viewLeaderboardItem.addActionListener(this);

        menuBar.add(WinConMenu);
        menuBar.add(gameMenu);
        menuBar.add(leaderboardMenu);
        this.setJMenuBar(menuBar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.remove(currentPanel); //gets rid of the current panel
        if (e.getSource() == PvP) {
            pvpPanel = new GamePanelPVP(); // load pvp
            currentPanel = pvpPanel;
            pvpPanel.resetScore();
        } else if (e.getSource() == PvA) {
            aiPanel = new GamePanel(); // load p v ai
            currentPanel = aiPanel;
            aiPanel.resetScore();
        } else if (e.getSource() == WinCon) {       
    // Pause the current panel
    if (currentPanel instanceof GamePanel) {
        ((GamePanel) currentPanel).setPaused(true);
    } else if (currentPanel instanceof GamePanelPVP) {
        ((GamePanelPVP) currentPanel).setPaused(true);
    }

    // Show input dialog
    String input = JOptionPane.showInputDialog(this, "Enter win condition (e.g. 5):");
   // Error handling for invalid input
    try {
        int winScore = Integer.parseInt(input);
        if (currentPanel instanceof GamePanel) {
            ((GamePanel) currentPanel).setWinCondition(winScore);
           
        } else if (currentPanel instanceof GamePanelPVP) {
            ((GamePanelPVP) currentPanel).setWinCondition(winScore);
           
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid number format. Try again.");
    }

    // Resume the game
    if (currentPanel instanceof GamePanel) {
        ((GamePanel) currentPanel).setPaused(false);
    } else if (currentPanel instanceof GamePanelPVP) {
        ((GamePanelPVP) currentPanel).setPaused(false);
    }
} else if (e.getSource() == viewLeaderboardItem) {
    // Show the leaderboard panel
    leaderboard leaderboardPanel = new leaderboard();
    currentPanel = leaderboardPanel;
}



        this.add(currentPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
        currentPanel.requestFocusInWindow();
    }
}
