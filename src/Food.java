
import java.awt.Color;
import java.awt.Graphics;
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
public class Food {
    
    private Node position;
    private boolean isSpecial;
    
    public Food(Snake snake) {
        // We pass Snake to the constructor because if the randomnly generated food falls on the Snake you have to create another position for the food
        position = createRandomNode(snake);
        isSpecial = false;
        
    }
    
    public Food(Snake snake, boolean isSpecial) {
        this(snake);
        this.isSpecial = isSpecial;
    }
    
    public void paint(Graphics g, int squareWidth, int squareHeight) {
        if (isSpecial) {
            Util.drawSquare(g, position.getRow(), position.getCol(), squareWidth, squareHeight, new Color(0, 0, 0));
        } else {
            Util.drawSquare(g, position.getRow(), position.getCol(), squareWidth, squareHeight, new Color(255, 51, 51));
        }
    }

    public void delete() {
        position.setCol(-6);
        position.setRow(-6);
    }
    
    private Node createRandomNode(Snake snake) {
        List<Node> body = snake.getList();
        Boolean in = true;
        int row = 0;
        int col = 0;
        while (in) {
            row = (int) (Math.random() * 50);
            col = (int) (Math.random() * 50);
            
            for (Node node : body) {
                if (row == node.getRow() && col == node.getCol()) {
                    break;
                }
                
            }
            in = false;
        }
        
        Node food = new Node(row, col);
        return food;
    }

    public Node getPosition() {
        return position;
    }
    
    public boolean isSpecial() {
        return isSpecial;
    }
}
