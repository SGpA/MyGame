import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.Timer;


// 보스인 장첸의 움직임, 공격 패턴을 구성하는 클래스
public class Boss {
	private int x;				// 보스의 초기 x좌표
	private int y;				// 보스의 초기 y좌표
	private int moveX;			// 공격 패턴 이후 이동할 위치 변수
	private int moveY;			// 공격 패턴 이후 이동할 위치 변수
	private int stack = 0;		// 공격 패턴 정하기 위한 변수
	private int countBat = 0;	// 배트 개수 변수
	private int hp = 50;		// 보스의 초기 HP 설정
	private int pattern;		// 이동방향 결정위한 랜덤값 저장 변수(1~2)
	private int bossPosX;		// 보스의 X 좌표
	private int bossPosY; 		// 보스의 Y 좌표
	private Timer takeRest;		 // 공격 패턴 사이 5초간 간격 부여
	private Timer takeMove;
	private Timer bossCharge;	// 돌진 공격 타이머
	private Timer throwBat;		// 뚜러뻥 던지기 타이머
	private ImageIcon boss;
	private boolean isCollisionDetected = false;

	ArrayList <BaseballBat> weapon = new ArrayList<>();

	// 생성자 통해 보스의 처음 좌표 등 정보 설정
	public Boss () {

		this.x = 1000;										// 보스 초기 x좌표
		this.y = 350;										// 보스 초기 y좌표
		this.moveX = x;										// 보스 초기 x좌표 복사본(돌진 패턴 끝나고 원위치할 좌표)
		this.moveY = y;										// 보스 초기 y좌표 복사본(돌진 패턴 끝나고 원위치할 좌표)
		this.boss = new ImageIcon("src/pic/chen.png");			// 보스 이미지 경로 지정
		this.bossPosX = x; 									// 보스의 X 좌표 설정
		this.bossPosY = y;									// 보스의 Y 좌표 설정
		takeRest = new Timer(3000, new TrListener());		// 3초동안 휴식
		takeMove = new Timer(5, new MvListener());			// 5마이크로초마다 휴식 중 움직임 그려줌
		bossCharge = new Timer(5, new BcListener());		// 5마이크로초마다 돌진모션 그려줌 (패턴 1)
		throwBat = new Timer(5, new TbListener());			// 5마이크로초마다 야구배트 그려줌 (패턴 2)
	}

	// 타이머 설정
	public class TrListener implements ActionListener {		// 패턴 0: 3초 휴식 타이머
		@Override
		public void actionPerformed(ActionEvent e) {
			if(stack == 0) {
				bossCharge.start();	 // 3초 휴식 타이머가 끝나면 돌진 패턴 시작
				takeMove.stop();	 // 휴식 동안의 움직임 제어 타이머 중지
			}
			if(stack == 1) {		// 3초 휴식 타이머 끝나면 야구배트 패턴 시작
				countBat = 0;		// 시작 시 배트 리스트 초기화
				weapon.clear();		// 배트 리스트 초기화
				throwBat.start();	// 배트 던지기 패턴 시작
				takeMove.stop();	 // 휴식 동안의 움직임 제어 타이머 중지
			}
		}
	}

	public class MvListener implements ActionListener {		// 패턴 0: 3초 휴식 타이밍에 움직임 제어
		@Override
		public void actionPerformed(ActionEvent e) {
			if(pattern == 1) 	// 이동패턴 1일 경우 아래로 이동
				y -= 1;
			if(pattern == 2) 	// 이동패턴 2일 경우 아래로 이동
				y += 1;

			bossPosX = x; 		// 보스의 좌표를 업데이트
			bossPosY = y;		// 보스의 좌표를 업데이트
		}
	}

	public class BcListener implements ActionListener {		// 패턴 1: 돌진 공격, 리스너
		@Override
		public void actionPerformed(ActionEvent e) {
			x -= 15;				// x축으로 -15만큼 돌진
			bossPosX -= 15;			// 돌진 중에도 맞을 수 있도록 설정

			if(x <= 0) {			// 돌진 중 좌표 0 이하되는 순간 (벽에 부딪히는 순간)
				bossCharge.stop();	// 돌진 패턴 종료
				stack++;			// 스택 == 1
				takeRest.start();	// 공격 쉬는 타이머 시작
				takeMove.start();	// 추가로 휴식 중 움직이기 타이머 시작
				x = moveX;			// 원위치 이동 좌표 지정(복사본)
				y = moveY;			// 원위치 이동 좌표 지정(복사본)
				pattern = (int)(Math.random()*2+1);		// 돌진 후 원위치하고 이동할 방향 결정
			}
		}
	}

	public class TbListener implements ActionListener {			// 패턴2 : 야구배트 투척, 리스너
		@Override
		public void actionPerformed(ActionEvent e) {
			// 야구배트 생성 및 카운트 증가, 보스의 손 위치에서 발사
			BaseballBat bb = new BaseballBat(x, y + 100, 4, 	// 야구배트가 4만큼의 속도로 x방향으로 날아감
					(int)(Math.random()*11 - 5), 70, 70);		// 야구배트가 -5 ~ 6 사이 랜덤한 y방향으로 날아감
			weapon.add(bb);		// 70 x 70 크기 배트가 생성됨
			countBat++;			// 배트 개수 셈
			isCollisionDetected = false;	// 돌진 데미지 한번만 주는 것 반복 위해 false값으로 전환

			// 야구배트가 10개 생성되었으면 야구배트 타이머를 중지하고 다음 동작으로 이동
			if (countBat == 10) {			
				throwBat.stop();	// 야구배트 패턴 종료
				stack--;			// 스택 == 0
				takeRest.start(); 	// 5초 휴식 타이머 시작
				return;      
			}
		}
	} 

	// 메서드
	// 돌진 공격으로 인한 마동석과 충돌 판정 메서드 (보스전)
	public boolean isTouched(int maX, int maY) {	

		if (!isCollisionDetected && maX + 50 < bossPosX + 270 && maX + 200 > bossPosX &&
				maY + 20 < bossPosY + 270 && maY + 190 > bossPosY) {	// 피격 범위 판정
			isCollisionDetected = true;		// true값으로 변경해 데미지 한번만 받게함 (true 한번 반환)
			return true;		 // 충돌 발생
		}
		else
			return false;		 // 충돌 없음
	}

	// 야구배트 공격과 마동석의 충돌 판정 메서드 (보스전)
	public boolean isBatTouched(int maX, int maY) {	
		for(int i=0;i<weapon.size();i++) {
			// 야구배트 객체와의 위치 비교 통해 충돌 여부 결정
			BaseballBat bbb = weapon.get(i);
			if(maX + 50 < bbb.getX() + bbb.getWidth() && maX + 200 > bbb.getX() 
					&& maY + 20 < bbb.getY() + bbb.getHeight() && maY + 190 > bbb.getY()) {
				weapon.remove(i);
				i--;
				return true;
			}
		}
		return false;	// 충돌하지 않았다면 false 반환
	}

	// 주먹과 보스의 충돌 체크 메서드 (보스전)
	public boolean isColliding(int bulletX, int bulletY, int bulletSize) {		
		if (bulletX + bulletSize > bossPosX && bulletX < bossPosX + 270 &&
				bulletY + bulletSize > bossPosY && bulletY < bossPosY + 270)	// 주먹 크기 계산 위해 주먹의 x, y, 크기 가져옴
			return true;	// 충돌 발생
		else
			return false;	// 충돌 없음
	}

	// 보스의 HP를 반환하는 메서드 (GameSetting에서 호출)
	public int getHP() { 				
		return hp;
	}

	// 보스의 HP를 감소시키는 메서드 (GameSetting에서 호출)
	public void decreaseHP() {			
		hp--;
	}

	// 공격패턴 시작 (GameSetting에서 호출)
	public void performAction() {		
		takeRest.start();				// 휴식 타이머로 시작
	}

	// 보스 이미지, 야구배트 이미지 그려줌
	public void draw (Graphics g) {		
		g.drawImage(boss.getImage(), x, y, 270, 270, null);

		for(BaseballBat wp : weapon) {
			wp.move();
			wp.draw(g); // 주먹 그리기
		}
	}
}