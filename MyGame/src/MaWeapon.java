import java.awt.Graphics;
import javax.swing.ImageIcon;

public class MaWeapon {		// �������� ���� "�ָ�"�� �����ϴ� Ŭ����

	private int x;		// �ָ� x ��ǥ
	private int y;		// �ָ� y ��ǥ
	private int width;	// �ָ� �� ��ǥ
	private int height;	// �ָ� ���� ��ǥ
	private int bulletSpeed = 20;	// �ָ� �ӵ�
	private ImageIcon punchIcon;	// �ָ� �̹��� ������

	public MaWeapon(int x, int y, int width, int height) {
		this.x = x;			// x��ǥ �޾ƿͼ� ����
		this.y = y;
		this.width = 50;
		this.height = 50;
		
		punchIcon = new ImageIcon("src/pic/punch.png");	 // punch �̹��� �ε� ���� ��ü
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void move() {
		x += bulletSpeed;
	}

	public boolean isOutOfBounds(int frameWidth) {
		return x > frameWidth;
	}

	public void draw(Graphics g) {
		g.drawImage(punchIcon.getImage(), x, y, width, height, null);
	}
}
