import java.awt.*;


public class PaddlesAI extends Rectangle{
    int yVelocity;
    int speed = 10;
    Ball ball;
    PaddlesAI(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, Ball ball){
        super(x,y,PADDLE_WIDTH,PADDLE_HEIGHT);
        this.ball = ball;
        
    }

    public void setYDirection(int yDirection){
        yVelocity = yDirection;
    }

    public void move(){
        if(ball.x >= GamePanel.GAME_WIDTH/3){
        if (ball.y < this.y) {
            y -= speed;
        } else if (ball.y > this.y + height) {
            y += speed;
        }
        y = Math.max(0, Math.min(y, GamePanel.GAME_HEIGHT - height));
    }
    }

    public void draw(Graphics g){
            g.setColor(Color.green);
        g.fillRect(x, y, width, height);   
    }
}
