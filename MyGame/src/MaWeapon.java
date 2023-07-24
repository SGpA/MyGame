import java.awt.Graphics;
import javax.swing.ImageIcon;

public class MaWeapon {		// ¸¶µ¿¼®ÀÇ ¹«±â "ÁÖ¸Ô"À» Á¤ÀÇÇÏ´Â Å¬·¡½º

	private int x;		// ÁÖ¸Ô x ÁÂÇ¥
	private int y;		// ÁÖ¸Ô y ÁÂÇ¥
	private int width;	// ÁÖ¸Ô Æø ÁÂÇ¥
	private int height;	// ÁÖ¸Ô ±æÀÌ ÁÂÇ¥
	private int bulletSpeed = 20;	// ÁÖ¸Ô ¼Óµµ
	private ImageIcon punchIcon;	// ÁÖ¸Ô ÀÌ¹ÌÁö ¾ÆÀÌÄÜ

	public MaWeapon(int x, int y, int width, int height) {
		this.x = x;			// xÁÂÇ¥ ¹Þ¾Æ¿Í¼­ ¼³Á¤
		this.y = y;
		this.width = 50;
		this.height = 50;
		
		punchIcon = new ImageIcon("src/pic/punch.png");	 // punch ÀÌ¹ÌÁö ·Îµå À§ÇÑ °´Ã¼
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
