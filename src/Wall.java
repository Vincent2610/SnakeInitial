
import java.awt.Color;
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
 * @author Vicente
 */
public class Wall {

    private List<Node> walls1;
    private List<Node> walls2;
    private List<Node> walls3;
    private List<Node> walls4;
    private List<Node> walls5;
    private List<Node> walls6;
    private List<Node> walls7;
    private List<Node> walls8;
    private List<Node> walls9;
    private List<Node> walls10;
    private List[] wallList;
    private List<Node> snakeBody;

    public Wall(List<Node> snakeBody) {
        wallList = new List[10];
        this.snakeBody = snakeBody;
        insetNodes();

    }

    public List getList() {
        return walls1;
    }

    private void insetNodes() {
        walls1 = new ArrayList<>();
        int cont = 0;
        int nodesCount = 0;
        while (cont < 100) {
            nodesCount=0;
            int x = (int) (Math.random() * 46 + 2);
            int y = (int) (Math.random() * 46 + 2);
            for (Node node : snakeBody) {
                if (x == node.getRow() && y == node.getCol()) {
                    break;
                } else {
                    nodesCount++;
                }
            }
            if (nodesCount == snakeBody.size()) {
                Node wallNode = new Node(x, y);
                walls1.add(wallNode);
                cont++;
            }
        }
    }

    public void paint(Graphics g2d, int squareWidth, int squareHeight) {
        for (Node node : walls1) {
            Util.drawSquare(g2d, node.getRow(), node.getCol(), squareWidth, squareHeight, new Color(153, 102, 0));
        }
    }

}
