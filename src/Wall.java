
import java.awt.Color;
import java.awt.Graphics;
import java.io.*;
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

    private List<Node> currentMap;
    private List<Node> snakeBody;
    private String currentMapName;
    public static final String FIRSTNAME = "squareMap.csv";
    public static final String SECONDNAME = "map.csv";

    public Wall(List<Node> snakeBody) {

        this.snakeBody = snakeBody;
        currentMap = new ArrayList<>();
    }

    public List getList() {
        return currentMap;
    }

    public void insetNodes() {

        int cont = 0;
        int nodesCount = 0;
        while (cont < 100) {
            nodesCount = 0;
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
                currentMap.add(wallNode);
                cont++;
            }
        }
    }

    public void mapChosen(int index) {
        switch (index) {
            case 1:
                currentMapName = FIRSTNAME;
                createMapFromFile(currentMapName);
                break;
            case 2:
                currentMapName = SECONDNAME;
                createMapFromFile(currentMapName);
                break;
            default:
                insetNodes();
                break;
        }
    }

    public void paint(Graphics g2d, int squareWidth, int squareHeight) {
        for (Node node : currentMap) {
            Util.drawSquare(g2d, node.getRow(), node.getCol(), squareWidth, squareHeight, new Color(153, 102, 0));
        }
    }

    public boolean contains(int row, int col) {
        for (Node node : currentMap) {
            if (node.getRow() == row && node.getCol() == col) {
                return true;
            }
        }
        return false;
    }

    private void createMapFromFile(String mapName) {
        BufferedReader in = null;
        try {
            try {
                in = new BufferedReader(new FileReader(mapName));
                String line;
                while ((line = in.readLine()) != null) {
                    String[] coordenates = line.split(";");
                    int initRow = Integer.parseInt(coordenates[0]);
                    System.out.println(initRow);
                    int initCol = Integer.parseInt(coordenates[1]);
                    System.out.println(initCol);
                    int destRow = Integer.parseInt(coordenates[2]);
                    System.out.println(destRow);
                    int destCol = Integer.parseInt(coordenates[3]);
                    System.out.println(destCol);
                    makeNodes(initRow,initCol,destRow,destCol);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (IOException ex) {
        }
    }
    
    private void makeNodes(int initRow, int initCol, int endRow, int endCol) {
       for (int row = initRow; row <= endRow; row++) {
           for (int col = initCol; col <= endCol; col++) {
               currentMap.add(new Node(row, col));
           }
       }
   }

}
