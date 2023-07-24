import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

// 보스 장첸의 공격 방식 중, 야구배트를 날리는 모션을 구현하는 클래스
public class BaseballBat {
    private int x; 			// 야구배트의 x 좌표
    private int y; 			// 야구배트의 y 좌표
    private int speedX; 	// 야구배트의 x 방향 속도
    private int speedY; 	// 야구배트의 y 방향 속도
    private int width; 		// 야구배트의 폭
    private int height;		// 야구배트의 높이
    private Image batImage; // 야구배트 이미지 변수

    public BaseballBat(int startX, int startY, int speedX, int speedY, int width, int height) {
        this.x = startX;		
        this.y = startY;
        this.speedX = speedX;	
        this.speedY = speedY;	
        this.width = width;
        this.height = height;
        this.batImage = new ImageIcon("src/pic/bat.gif").getImage();     // 야구배트 이미지 로드
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
   
    public void move() {	// 야구배트 움직임 제어 메서드
        x -= speedX;		// speedX 값 만큼 움직임
        y -= speedY;		// speedY 값 만큼 움직임
    }
    
    public void draw(Graphics g) {    // 야구배트 그리기
        g.drawImage(batImage, x, y, width, height, null);
    }
}
