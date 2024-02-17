import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JPanel implements KeyListener {

    public static final int CELL_SIZE = 20;
    public static int width = 400;
    public static int height = 400;
    public static int row = height / CELL_SIZE;
    public static int column = width / CELL_SIZE;
    private static Timer t;
    private Snake snake;
    private Fruit fruit;
    private static String direction;
    private boolean allowKeyPress; // 用於控制 KeyListener，防止蛇移動時發生邏輯上的自殺
    private int score;
    private int highest_score;
    String desktop = System.getProperty("user.home") + "/Desktop/";  // 桌面路徑
    String myFile = desktop + "filename.txt";

    public Main() {
        read_highest_score();
        reset();
        addKeyListener(this);
    }

    public void reset() {
        score = 0;
        if (snake != null) {
            snake.getSnakeBody().clear();
        }
        allowKeyPress = true; // 預設啟動_KeyListener
        direction = "Right"; // 預設移動方向_右邊
        snake = new Snake();
        fruit = new Fruit();
        setTimer();
    }

    public void setTimer () {
        //TIP Timer 类是 Java 中用于执行定时任务的工具类，它可以在指定的时间点执行任务，也可以以固定的延迟时间或固定的周期执行任务。
        // <br/>可以使用 Timer 类的 scheduleAtFixedRate() 方法来创建一个周期性执行的定时任务。
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //TIP repaint() 是 Java 中用于触发组件重绘的方法，它通常用于更新 Swing 组件的显示。
                repaint();
            }
        }, 0, 100);
    }

    @Override
    public void paintComponent(Graphics g) {
        // 繪製前，先確認蛇頭是否碰觸到蛇身
        for (int i = 1; i < snake.getSnakeBody().size(); i++) {
            if (snake.getSnakeBody().getFirst().x == snake.getSnakeBody().get(i).x
                    && snake.getSnakeBody().getFirst().y == snake.getSnakeBody().get(i).y) {
                allowKeyPress = false; // 關閉_KeyListener
                t.cancel(); // 取消定時任務
                t.purge(); // 清理定時任務
                int response = JOptionPane.showOptionDialog(this, "Game Over!! Your score is " + score + ". The highest score was " + highest_score + " .Would you like to start over?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, JOptionPane.YES_OPTION);
                write_a_file(score);
                switch (response) {
                    case JOptionPane.CLOSED_OPTION:
                    case JOptionPane.NO_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.YES_OPTION:
                        reset();
                        return;
                }
            }
        }

        g.fillRect(0, 0, width, height);
        fruit.drawFruit(g); // 繪製_果實
        snake.drawSnake(g); // 繪製_蛇身

        // 移動蛇
        int snakeX = snake.getSnakeBody().getFirst().x;
        int snakeY = snake.getSnakeBody().getFirst().y;
        switch (direction) {
            case "Left":
                snakeX -= CELL_SIZE;
                break;
            case "Up":
                snakeY -= CELL_SIZE;
                break;
            case "Down":
                snakeY += CELL_SIZE;
                break;
            case "Right":
                snakeX += CELL_SIZE;
                break;
        }
        Node newHead = new Node(snakeX, snakeY); // 新_蛇頭座標

        // 確認新座標是否會吃到果實
        if (snake.getSnakeBody().getFirst().x == fruit.getX() && snake.getSnakeBody().getFirst().y == fruit.getY()) {
            fruit.setNewLocation(snake); // 新_果實位置
            fruit.drawFruit(g); // 繪製_新果實
            score ++;
        } else {
            snake.getSnakeBody().removeLast(); // 移除蛇尾
        }

        snake.getSnakeBody().addFirst(newHead); // 繪製_新蛇頭

        allowKeyPress = true; // 啟動_KeyListener
        requestFocusInWindow();
    }

    //TIP Dimension 是 Java AWT（Abstract Window Toolkit）中的一个类，
    // 用于表示组件的尺寸。Dimension 对象包含两个整数值，分别表示组件的宽度和高度。
    // <br/>
    // 在代码中，getPreferredSize() 方法被重写以返回一个 Dimension 对象，指定了容器的首选大小。
    // <br/>当调用 pack() 方法时，窗口会根据这个首选大小来自动调整大小，以确保所有组件都能被正确显示。
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Snake Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new Main());
        //TIP window.pack() 是 Swing 中的一个方法，用于调整窗口的大小以适应其包含的组件的首选大小。
        // <br/>會自動抓取 Dimension 所設定的大小。
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setResizable(false); // 鎖定視窗大小
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (allowKeyPress) {
            if ((e.getKeyCode() == 37 || e.getKeyCode() == 65) && !direction.equals("Right")) {
                direction = "Left";
            } else if ((e.getKeyCode() == 38 || e.getKeyCode() == 87) && !direction.equals("Down")) {
                direction = "Up";
            } else if ((e.getKeyCode() == 39 || e.getKeyCode() == 68) && !direction.equals("Left")) {
                direction = "Right";
            } else if ((e.getKeyCode() == 40 || e.getKeyCode() == 83) && !direction.equals("Up")) {
                direction = "Down";
            }
            allowKeyPress = false; // 關閉_KeyListener
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void read_highest_score () {
        try {
            File myObj = new File(myFile);
            Scanner myReader = new Scanner(myObj);
            highest_score = myReader.nextInt();
            myReader.close();
        } catch (FileNotFoundException e) {
            highest_score = 0;
            try {
                File myObj = new File(myFile);
                FileWriter myfileWriter = new FileWriter(myObj.getName());
                myfileWriter.write("" + 0);
                myfileWriter.close(); // 关闭 FileWriter 对象
            } catch (IOException err) {
                System.out.println("An error occurred");
                err.printStackTrace();
            }
        }
    }

    public void write_a_file (int score) {
        try {
            FileWriter myfileWriter = new FileWriter(myFile);
            if (score > highest_score) {
                myfileWriter.write("" + score);
                highest_score = score;
            } else {
                myfileWriter.write("" + highest_score);
            }
            myfileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}