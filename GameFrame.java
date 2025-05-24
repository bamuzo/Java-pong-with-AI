import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameFrame extends JFrame implements ActionListener {
    GamePanel aiPanel;
    GamePanelPVP pvpPanel;
    JPanel currentPanel;

    JMenuItem PvP;
    JMenuItem PvA;

    GameFrame() {
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

    void createMenuBar() {
        JMenuBar menuBar = new JMenuBar(); // adds the menu bar thing on top
        PvP = new JMenuItem("Player vs Player"); // names the item inside the menubar
        PvA = new JMenuItem("Player vs AI"); // ^^ same deal

        JMenu gameMenu = new JMenu("Game Type"); // menu bar button to show the items listed ^^
        gameMenu.add(PvP); // adds to game type bar
        gameMenu.add(PvA);

        PvP.addActionListener(this); // makes it so that it can listen to the input
        PvA.addActionListener(this);

        menuBar.add(gameMenu);
        this.setJMenuBar(menuBar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.remove(currentPanel); //gets rid of the current panel
        if (e.getSource() == PvP) {
            pvpPanel = new GamePanelPVP(); // load pvp
            currentPanel = pvpPanel;
        } else if (e.getSource() == PvA) {
            aiPanel = new GamePanel(); // load p v ai
            currentPanel = aiPanel;
        }

        this.add(currentPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
        currentPanel.requestFocusInWindow();
    }
}
