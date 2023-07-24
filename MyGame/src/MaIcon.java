import javax.swing.*;
import java.awt.*;


// 마동석 이미지 + 적의 위치 및 움직임 구성
public class MaIcon extends ImageIcon {
	int pX;				// 마동석, 적의 X좌표
	int pY;				// 마동석, 적의 y좌표
	int width;			// 마동석, 적의 폭
	int height;			// 마동석, 적의 길이
	int dirX;			// 적의 x 이동방향
	int dirY;			// 적의 y 이동방향

	public MaIcon(String img, int x, int y, int width, int height) {
		super(img);		// 이미지 초기화
		pX=x;			
		pY=y;
		this.width = width;
		this.height = height;
		this.dirX = (int)(Math.random()*10-5);	// 이동방향 랜덤 설정
		this.dirY = (int)(Math.random()*10-5);	// 이동방향 랜덤 설정
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
	// 이미지의 좌표를 움직이는 메소드
	public void move(int x, int y) {
		pX += x;
		pY += y;
	}

	public void draw(Graphics g) {
		g.drawImage(this.getImage(), pX, pY, width, height, null);
	}
}