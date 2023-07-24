import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

// ���� ��þ�� ���� ��� ��, �߱���Ʈ�� ������ ����� �����ϴ� Ŭ����
public class BaseballBat {
    private int x; 			// �߱���Ʈ�� x ��ǥ
    private int y; 			// �߱���Ʈ�� y ��ǥ
    private int speedX; 	// �߱���Ʈ�� x ���� �ӵ�
    private int speedY; 	// �߱���Ʈ�� y ���� �ӵ�
    private int width; 		// �߱���Ʈ�� ��
    private int height;		// �߱���Ʈ�� ����
    private Image batImage; // �߱���Ʈ �̹��� ����

    public BaseballBat(int startX, int startY, int speedX, int speedY, int width, int height) {
        this.x = startX;		
        this.y = startY;
        this.speedX = speedX;	
        this.speedY = speedY;	
        this.width = width;
        this.height = height;
        this.batImage = new ImageIcon("src/pic/bat.gif").getImage();     // �߱���Ʈ �̹��� �ε�
    }

    public int getX () {
    	return x;
    }
    
    public int getY () {
    	return y;
    }
    
    public int getWidth () {
    	return width;
    }
    
    public int getHeight () {
    	return height;
    }    
   
    public void move() {	// �߱���Ʈ ������ ���� �޼���
        x -= speedX;		// speedX �� ��ŭ ������
        y -= speedY;		// speedY �� ��ŭ ������
    }
    
    public void draw(Graphics g) {    // �߱���Ʈ �׸���
        g.drawImage(batImage, x, y, width, height, null);
    }
}
