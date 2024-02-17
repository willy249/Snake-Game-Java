import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Fruit {
    private  int x;
    private  int y;
    private final ImageIcon img;


    public Fruit() {
        img = new ImageIcon(Objects.requireNonNull(getClass().getResource("fruit.png")));
        // 隨機果實位置
        this.x = (int) (Math.floor(Math.random() * Main.column) * Main.CELL_SIZE);
        this.y = (int) (Math.floor(Math.random() * Main.row) * Main.CELL_SIZE);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void drawFruit(Graphics g) {
        img.paintIcon(null, g, this.x, this.y);

    }

    public void setNewLocation(Snake s){
        int new_x;
        int new_y;
        boolean overlapping;
        do {
            new_x = (int)(Math.floor(Math.random() * Main.column) * Main.CELL_SIZE);
            new_y = (int)(Math.floor(Math.random() * Main.row) * Main.CELL_SIZE);
            overlapping = check_overlap(new_x, new_y, s);
        } while (overlapping);

        this.x = new_x;
        this.y = new_y;
    }

    public boolean check_overlap (int new_x, int new_y,Snake s) {
        for (int j = 0; j < s.getSnakeBody().size() ; j++) {
            if (new_x == s.getSnakeBody().get(j).x && new_y == s.getSnakeBody().get(j).y) {
                return true;
            }
        }
        return false;
    }
}
