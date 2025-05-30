import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements  Runnable {
    
    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;

    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;

    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddles paddle1;
    Paddles paddle2;
    Ball ball;
    Score score;

    PaddlesAI paddlesAI;


    
    GamePanel(){
        newBall();
        newPaddles();

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
        }
        if(ball.x >= (GAME_WIDTH - BALL_DIAMETER)){
            score.player1++;
            newPaddles();
            newBall();
        }
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
}
