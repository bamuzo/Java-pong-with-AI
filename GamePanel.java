import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
    
    public static final int GAME_WIDTH = 1000;
    public static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
    private static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
    private static final int BALL_DIAMETER = 20;

    private static final int PADDLE_WIDTH = 25;
    private static final int PADDLE_HEIGHT = 100;

    private Thread gameThread;
    private Image image;
    private Graphics graphics;
    private Random random;
    private Paddles paddle1;
    private Paddles paddle2;
    private Ball ball;
    private Score score;
    private int winCondition = 1000000000;
    private int winConditionAI = 3;
    private PaddlesAI paddlesAI;
    private String playerName;
    private leaderboard lb;

    private boolean paused;

    GamePanel(){
        newBall();
        newPaddles();

        lb = new leaderboard();

        score = new Score(GAME_WIDTH ,GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();

    }


    public void newBall(){
        random = new Random();
        ball = new Ball((GAME_WIDTH/2) - (BALL_DIAMETER/2),(GAME_HEIGHT/2)-(BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER);
        
        if(paddlesAI!=null){
            paddlesAI.ball = ball;
        }
    
    }



    public void newPaddles(){
        paddle1 = new Paddles(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);
        paddlesAI = new PaddlesAI(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT, ball);
    }

    public void paint(Graphics g){
        image = createImage(getWidth(),getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);

    }

    public void draw(Graphics g){
        paddle1.draw(g);
        paddlesAI.draw(g);
        ball.draw(g);
        score.draw(g);
    }

    public void move(){
        paddle1.move();
        paddlesAI.move();
        ball.move();
       
    }

    public void checkCollision(){
        //stops paddles at window edges
        if(paddle1.y <= 0){
            paddle1.y = 0;
        }
        if(paddle1.y >= (GAME_HEIGHT-PADDLE_HEIGHT)){
            paddle1.y = (GAME_HEIGHT-PADDLE_HEIGHT);

        }
        // random comment to check for push
        if(paddlesAI.y <=0){
            paddlesAI.y=0;
        }
        
        if(paddlesAI.y >= (GAME_HEIGHT-PADDLE_HEIGHT)){
            paddlesAI.y = (GAME_HEIGHT-PADDLE_HEIGHT);
        }
        //ball bouncer
        if(ball.y <=0){
            ball.setYDirection(-ball.yVelo);
        }
        if(ball.y >= GAME_HEIGHT - BALL_DIAMETER){
            ball.setYDirection(-ball.yVelo);
        }

        //paddle bounce

        if(ball.intersects(paddle1)){
            ball.xVelo = Math.abs(ball.xVelo);
            ball.xVelo++;
        }
        if(ball.intersects(paddlesAI)){
            ball.xVelo = -ball.xVelo;
        }

        //point and new paddles and new ball

        if(ball.x <= 0){
            score.player2++;
            newPaddles();
            newBall();
            checkWinner();
        }
        if(ball.x >= (GAME_WIDTH - BALL_DIAMETER)){
            score.player1++;
            newPaddles();
            newBall();
            checkWinner();
        }
    }
     public void setWinCondition(int winScore) {
    this.winCondition = winScore;
    }

    public void checkWinner(){
        if(score.player2 >= winConditionAI){
            JOptionPane.showMessageDialog(this, "AI Wins");
            playerName = JOptionPane.showInputDialog(this, "Enter your name to save the score:");
            lb.addPlayer(playerName, score.player1);

            
            System.exit(0);

        }
    }

    public void setPaused(boolean val){
        this.paused = val;
    }


    public void run(){
        //the game loop
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while(true){
            long now = System.nanoTime();
            delta +=(now-lastTime)/ns;
            lastTime=now;
            if(delta>=1){
               
                move();
                checkCollision();
                checkWinner();
                repaint();
                delta--;
                
            }
        }

    }

    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }

        public void keyReleased(KeyEvent e){
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
        
    }

    public void resetScore(){
        score.player1 = 0;
        score.player2 = 0;
    }
}
