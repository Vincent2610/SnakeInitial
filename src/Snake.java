
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author victoralonso
 */
public class Snake {
    
    private Direction direction;
    private List<Node> body;
    private int remainingNodesToCreate = 0;
    
    
    public Snake(int row, int col, int size) { 
        body = new ArrayList<>();
        Node firstNode = new Node(row,col);
        body.add(firstNode);
        for(int i = 1;i<size;i++){
            Node restOfBody = new Node(row+i,col);
            body.add(restOfBody);
        }
        
        direction =  Direction.UP;
    }
    
    public boolean canMove(int row, int col) {
        // Finish this method
        return true;
    }
    
    public void paint(Graphics g, int squareWidth, int squareHeight) {
        // Finish this method. Call Util.drawSquare()
    }
    
    public void move() {
        // Finish this method
    }
    
    public List getList(){
        return body;
    }
    
    
    
}
