import java.awt.*;
import java.util.ArrayList;

public class Snake {

    private final ArrayList<Node> snakeBody;

    public Snake (){
        snakeBody = new ArrayList<>();
        snakeBody.add(new Node(80,0));
        snakeBody.add(new Node(60,0));
        snakeBody.add(new Node(40,0));
        snakeBody.add(new Node(20,0));
    }

    public ArrayList<Node> getSnakeBody() {
        return snakeBody;
    }
    public void drawSnake(Graphics g){
        for (int i = 0; i < snakeBody.size(); i++) {
            if (i == 0) {
                g.setColor(Color.GREEN); // 蛇頭顏色
            } else {
                g.setColor(Color.ORANGE); // 蛇身顏色
            }

            Node n = snakeBody.get(i);
            // 控制蛇身x, y座標不超過視窗，若超過則"穿牆"
            if (n.x > Main.width){
                n.x = 0;
            } else if (n.x < 0){
                n.x = Main.width - Main.CELL_SIZE;
            }
            if (n.y > Main.height){
               n.y = 0;
            } else if (n.y < 0) {
                n.y = Main.height - Main.CELL_SIZE;
            }

            g.fillOval(n.x, n.y, Main.CELL_SIZE, Main.CELL_SIZE);
        }
    }
}
