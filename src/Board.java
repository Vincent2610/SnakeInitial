
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.Timer;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author victoralonso
 */
public class Board extends javax.swing.JPanel {

    

    class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (snake.getDirection() != Direction.RIGHT && setDirection) {
                        snake.setDirection(Direction.LEFT);
                        setDirection=false;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (snake.getDirection() != Direction.LEFT && setDirection) {
                        snake.setDirection(Direction.RIGHT);
                        setDirection=false;
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (snake.getDirection() != Direction.DOWN && setDirection) {
                        snake.setDirection(Direction.UP);
                        setDirection=false;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (snake.getDirection() != Direction.UP && setDirection) {
                        snake.setDirection(Direction.DOWN);
                        setDirection=false;
                    }
                    break;
                case KeyEvent.VK_P:
                    stopTimers();
                    pauseGame.setVisible(true);
            }
            repaint();
        }

    }

    private int numRows;
    private int numCols;
    private int levelSelected;
    private int deltaTime;
    private int foodDeltaTime;
    private int timesLevelUp;
    private boolean setDirection;
    private boolean specialFoodVisible;
    private boolean createdMap;
    private String playerName;
    private Node next;
    //private Node[][] playBoard;
    private Snake snake;
    private Food food;
    private Food specialFood;
    private Wall walls;
    private Timer snakeTimer;
    private Timer specialFoodTimer;
    private ScoreBoardIncrementer scoreBoard;
    private StartGame startGame;
    private PauseGame pauseGame;
    public static final int VALOR_COMIDA_NORMAL = 1;
    public static final int VALOR_COMIDA_ESPECIAL = 4;
    public static final int VALOR_RESTA_DELAY_DELTATIME = 25;
    public static final int SCOREWALL = 5;

    /**
     * Creates new form Board
     */
    public Board() {
        setFocusable(true);
        initComponents();
        myInit();

        snakeTimer = new Timer(deltaTime, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                next = nextNode();
                try {
                    gameOver();
                } catch (IOException ex) {

                }
                //if (snake.canMove(next.getRow(), next.getCol())) {
                if (colideFood()) {
                    if (colideNormalFood()) {
                        snake.setRemainingNodesToCreate(VALOR_COMIDA_NORMAL);
                        food = new Food(snake, false,walls);
                        scoreBoard.incrementScore(VALOR_COMIDA_NORMAL);
                    } else {
                        snake.setRemainingNodesToCreate(VALOR_COMIDA_ESPECIAL);
                        specialFood.delete();
                        scoreBoard.incrementScore(VALOR_COMIDA_ESPECIAL);
                    }
                }
                
                snake.move();
                if(!createdMap && scoreBoard.getScore() > SCOREWALL){
                    walls.mapChosen(levelSelected);
                    createdMap=true;
                }
                levelUpVelocity();
                setDirection=true;
                repaint();
                //}

            }

        });

        specialFoodTimer = new Timer(foodDeltaTime, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!specialFoodVisible) {
                    specialFood = new Food(snake, true,walls);
                    specialFoodVisible = true;
                    int randomTimer = (int) (Math.random() * 10000 + 30000);
                    specialFoodTimer.setDelay(randomTimer);
                } else {
                    specialFood.delete();
                    specialFoodVisible = false;
                    int randomTimer = (int) (Math.random() * 5000 + 7000);
                    specialFoodTimer.setDelay(randomTimer);

                }
                repaint();
            }
        });

        MyKeyAdapter keyAdepter = new MyKeyAdapter();
        addKeyListener(keyAdepter);
    }

    private void myInit() {
        setDirection = true;
        snake = new Snake(24, 24, 4);
        walls = new Wall(snake.getList());
        food = new Food(snake, false,walls);
        createdMap = false;
        deltaTime = 200;
        foodDeltaTime = 15000;
        specialFoodVisible = false;
        timesLevelUp = 1;
        //levelSelected;
    }

    public void initGame() {
        myInit();
        startTimers();
        scoreBoard.setScore(0);
    }

    void takeStartGameFields(String playerName,int levelSelected) {
        this.playerName = playerName;
        this.levelSelected=levelSelected;
    }

    private void levelUpVelocity() {
        if (scoreBoard.getScore() / VALOR_RESTA_DELAY_DELTATIME == timesLevelUp) {
            deltaTime -= VALOR_RESTA_DELAY_DELTATIME;
            snakeTimer.setDelay(deltaTime);
            timesLevelUp++;
        }
    }

    public Board(int numRows, int numCols, ScoreBoardIncrementer scb, JFrame parent) {
        this();
        //playBoard = new Node[numRows][numCols];
        scoreBoard = scb;
        startGame = new StartGame(parent, true, this);
        pauseGame = new PauseGame(parent, true, this);
        this.numCols = numCols;
        this.numRows = numRows;
    }

    private Node nextNode() {
        Node reference = (Node) (snake.getList().get(0));
        Node nextNode = new Node(reference.getRow(), reference.getCol());

        switch (snake.getDirection()) {
            case UP:
                nextNode.setRow(nextNode.getRow() - 1);
                break;
            case DOWN:
                nextNode.setRow(nextNode.getRow() + 1);
                break;
            case RIGHT:
                nextNode.setCol(nextNode.getCol() + 1);
                break;
            case LEFT:
                nextNode.setCol(nextNode.getCol() - 1);
                break;

        }

        return nextNode;
    }

    public boolean colideFood() {
        return colideNormalFood() || colideSpecialFood();

    }

    private boolean colideNormalFood() {
        return food.getPosition().getRow() == next.getRow() && food.getPosition().getCol() == next.getCol();
    }

    private boolean colideSpecialFood() {
        if (specialFoodVisible) {
            return specialFood.getPosition().getRow() == next.getRow() && specialFood.getPosition().getCol() == next.getCol();
        }
        return false;
    }

    public void gameOver() throws IOException {
        if (colideBorders() || colideBody() || colideWalls()) {
            stopTimers();
            updateScores();
        }
    }

    public void updateScores() throws IOException {
        Player p = new Player(playerName, scoreBoard.getScore());
        startGame.startGameCalls(p);
    }

    public boolean colideBorders() {
        return next.getRow() < 0 || next.getRow() >= numRows || next.getCol() < 0 || next.getCol() >= numCols;
    }

    public boolean colideWalls() {
        List<Node> wallsList = walls.getList();
        if (scoreBoard.getScore() > SCOREWALL) {
            for (Node node : wallsList) {
                if (next.getRow() == node.getRow() && next.getCol() == node.getCol()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean colideBody() {
        List<Node> body = snake.getList();
        for (Node node : body) {
            if (next.getRow() == node.getRow() && next.getCol() == node.getCol()) {
                return true;
            }
        }
        return false;
    }

    private int squareWidth() {
        if (numCols != 0) {
            return getWidth() / numCols;
        }
        return getWidth() / 50;
    }

    private int squareHeight() {
        if (numRows != 0) {
            return getHeight() / numRows;
        }
        return getHeight() / 50;
    }

    public void startTimers() {
        snakeTimer.start();
        specialFoodTimer.start();
    }

    public void stopTimers() {
        snakeTimer.stop();
        specialFoodTimer.stop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //paintPlayBoard(g2d);
        snake.paint(g2d, squareWidth(), squareHeight());
        food.paint(g2d, squareWidth(), squareHeight());
        if (specialFoodVisible) {
            specialFood.paint(g2d, squareWidth(), squareHeight());
        }
        if (scoreBoard.getScore() > SCOREWALL) {
            walls.paint(g, squareWidth(), squareHeight());
        }
    }

    /*public void paintPlayBoard( Graphics2D g){
        for (int row =0;row < playBoard.length; row++ ){
            for(int col = 0;col < playBoard[0].length;col++){
                drawSquare(g, row, col, Color.PINK);
            }
        }
    }*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 356, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 310, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /*private void paintPlayBoard(Graphics2D g2d) {
        for (int row = 0; row < playBoard.length; row++) {
            for (int col = 0; col < playBoard[0].length; col++) {
                drawSquare(g2d, row, col, new Color(51, 255, 51));
            }
        }
    }*/
 /*private void drawSquare(Graphics2D g, int row, int col, Color color) {
        
        int x = col * squareWidth();
        int y = row * squareHeight();
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
