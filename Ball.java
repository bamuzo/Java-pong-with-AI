import java.awt.*;
import java.util.*;


public class Ball extends Rectangle {
    Random random;

    int xVelo;
    int yVelo;
    int initialSpeed = 10;
    
    
    Ball(int x, int y, int width, int height){
    super(x,y,width,height);
    random = new Random();
    // fixed to change how the ball moves, made it actually random
    // instead of always going to the right and down or left and down
    int randomXDirection = random.nextBoolean() ? 1 : -1;
    setXDirection(randomXDirection * initialSpeed);
    
    int randomYDirection = random.nextBoolean() ? 1 : -1;
    setYDirection(randomYDirection * initialSpeed);
}


    public void setXDirection(int randomXDirection){
        xVelo = randomXDirection;
    }
    public void setYDirection(int randomYDirection){
        yVelo = randomYDirection;
    }

    public void move(){
        x+=xVelo;
        y+=yVelo;

    }

    public void draw(Graphics g){
        g.setColor(Color.white);
        g.fillOval(x, y, height,width);
    }
}
