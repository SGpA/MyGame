import javax.swing.*;
import java.awt.*;


// ������ �̹��� + ���� ��ġ �� ������ ����
public class MaIcon extends ImageIcon {
	int pX;				// ������, ���� X��ǥ
	int pY;				// ������, ���� y��ǥ
	int width;			// ������, ���� ��
	int height;			// ������, ���� ����
	int dirX;			// ���� x �̵�����
	int dirY;			// ���� y �̵�����

	public MaIcon(String img, int x, int y, int width, int height) {
		super(img);		// �̹��� �ʱ�ȭ
		pX=x;			
		pY=y;
		this.width = width;
		this.height = height;
		this.dirX = (int)(Math.random()*10-5);	// �̵����� ���� ����
		this.dirY = (int)(Math.random()*10-5);	// �̵����� ���� ����
	}

	public int getX() {
		return pX;
	}

	public int getY() {
		return pY;
	}

	public int getDirX() {
		return dirX;
	}

	public int getDirY() {
		return dirY;
	}

	public void setPos(int x, int y) {
		this.pX= x;
		this.pY = y;
	}

	public void setDirX(int dirX) {
		this.dirX = dirX;
	}

	public void setDirY(int dirY) {
		this.dirY = dirY;
	}
	// �̹����� ��ǥ�� �����̴� �޼ҵ�
	public void move(int x, int y) {
		pX += x;
		pY += y;
	}

	public void draw(Graphics g) {
		g.drawImage(this.getImage(), pX, pY, width, height, null);
	}
}