import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class GamePanel extends JPanel implements ActionListener, KeyListener  {
    private class Tile{
       int x, y;
        Tile(int x, int y){
            this.x = x; 
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 20;
    //snake
    Tile snakeHead;
    ArrayList snakeBody;

    //food
    Tile food;
    Random random;

    int velocityX;
    int velocityY;
    boolean gameover = false;

    Timer gameLoop;



    GamePanel(int boardWidth, int boardHeight){
       this.boardWidth = boardWidth;
       this.boardHeight = boardHeight; 
       setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
       setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

       snakeHead = new Tile(5, 5);
       snakeBody = new ArrayList<Tile>();


       food = new Tile(10, 10);
       
       random = new Random();
       placefood();

       velocityX = 0;
       velocityY = 1;

       gameLoop = new Timer(50, this);
       gameLoop.start();



    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //draw grid
        // for(int i = 0; i < boardWidth/tileSize; i++){
        //     //vertical
        //     g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
        //     //horizontal
        //     g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        // }

        // snake HEAD
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y* tileSize, tileSize, tileSize);
        //snake Body
        for (int i = 0; i < snakeBody.size(); i++) {
           Tile snakeAdd = (GamePanel.Tile) snakeBody.get(i);
            g.fillRect(snakeAdd.x * tileSize, snakeAdd.y * tileSize, tileSize, tileSize);
            g.setColor(Color.green);   
        }

        //food
        g.setColor(Color.red);
        g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);

        //score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameover){
            g.setColor(Color.red);
            g.drawString("Game Over. SCORE: " + String.valueOf(snakeBody.size()), tileSize - 15, tileSize);
        }else{
            g.setColor(Color.green);
            g.drawString("Score: " + String.valueOf(snakeBody.size()),  tileSize - 15, tileSize );  
        }
    }

    public void placefood(){
       // 500/20 = 25 random entre 0 y 25 
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    public boolean Collition(Tile tile1, Tile tile2){
        return (tile1.x == tile2.x && tile1.y == tile2.y);
    }

    public void move(){
       //eatFood
       if(Collition(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placefood(); 
       }

       //snake body
       for (int i = snakeBody.size() - 1; i >= 0 ; i--) {
            Tile snakePart = (GamePanel.Tile) snakeBody.get(i);
            if(i == 0){
                snakePart.x = snakeHead.x; 
                snakePart.y = snakeHead.y;
            } else{
                Tile prev = (GamePanel.Tile) snakeBody.get(i-1);
                snakePart.x = prev.x;
                snakePart.y = prev.y;
            }
       }
       snakeHead.x += velocityX;
       snakeHead.y += velocityY; 

       //game over
       for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = (GamePanel.Tile) snakeBody.get(i);
            if(Collition(snakeHead, snakePart)){
                gameover = true;
            }
       }
       if(snakeHead.x* tileSize< 0 || snakeHead.x* tileSize > boardWidth
       || snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight){
            gameover = true;
       }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameover){
            gameLoop.stop();
        }
       
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityY = 0;
            velocityX = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}


    @Override
    public void keyReleased(KeyEvent e) {}
}
