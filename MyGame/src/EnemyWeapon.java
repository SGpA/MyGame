import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

// 2���� �������� �ƴ�, 1���忡�� ���� �߱���Ʈ�� �����ٴ� ������ EnemyWeapon Ŭ����
public class EnemyWeapon {
    private int x;			// �ʱ� ���� x��ǥ
    private int y;			// �ʱ� ���� y��ǥ
    private int width;		// �� ���� ��
    private int height;		// �� ���� ����
    private Image image;	// ���� �̹��� ����
    private int speed;		// ���ư��� �ӵ�

    public EnemyWeapon(int x, int y, int width, int height, String imagePath, int speed) {	    // ������ ���� ���� �ʱ�ȭ
        this.x = x;											// �����г� ����ŭ
        this.y = y;											// ���� ũ�� ����, (�����г� �� - 75) ũ�� �� ���� y ��
        this.width = width;									// �׸� �� 75
        this.height = height;								// �׸� ���� 75
        this.image = new ImageIcon(imagePath).getImage();	// bat.gif �̹���
        this.speed = speed;									// �ӵ� 3 ����
    }
    
    // �޼���
    public int getX () {
    	return x;
    }
    
    public int getY () {
    	return y;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);		// �̹��� �׷���
    }

    public void move() {									
        x -= speed;											// ���ǵ� 3���� ���� �̵�
    }

    public boolean isOutOfBounds(int panelWidth) {			// ȭ���� ������� Ȯ��
        return x + width < 0 || x > panelWidth;				// ȭ�� ���� ���� �ε����ų�, ȭ�� ������ ū ���϶�
    }

}
