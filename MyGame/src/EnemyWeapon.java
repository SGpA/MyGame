import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

// 2라운드 보스전이 아닌, 1라운드에서 적이 야구배트를 던진다는 설정의 EnemyWeapon 클래스
public class EnemyWeapon {
    private int x;			// 초기 무기 x좌표
    private int y;			// 초기 무기 y좌표
    private int width;		// 적 무기 폭
    private int height;		// 적 무기 높이
    private Image image;	// 무기 이미지 변수
    private int speed;		// 날아가는 속도

    public EnemyWeapon(int x, int y, int width, int height, String imagePath, int speed) {	    // 생성자 통해 정보 초기화
        this.x = x;											// 게임패널 폭만큼
        this.y = y;											// 무기 크기 제외, (게임패널 폭 - 75) 크기 중 랜덤 y 값
        this.width = width;									// 그림 폭 75
        this.height = height;								// 그림 높이 75
        this.image = new ImageIcon(imagePath).getImage();	// bat.gif 이미지
        this.speed = speed;									// 속도 3 지정
    }
    
    // 메서드
    public int getX () {
    	return x;
    }
    
    public int getY () {
    	return y;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);		// 이미지 그려줌
    }

    public void move() {									
        x -= speed;											// 스피드 3으로 왼쪽 이동
    }

    public boolean isOutOfBounds(int panelWidth) {			// 화면을 벗어났는지 확인
        return x + width < 0 || x > panelWidth;				// 화면 왼쪽 벽에 부딪히거나, 화면 폭보다 큰 값일때
    }

}
