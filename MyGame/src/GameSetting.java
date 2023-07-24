import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

// OS프로그래밍 동아리 방학 과제
// 슈팅게임 만들기
// 장르 : 슈팅게임 , 컨셉 : 플래시게임 느낌나는 슈팅게임, 필살기 없음. 오직 '마동석'에 의한 게임

// 0. 게임 세팅
public class GameSetting {

	private boolean isWin;						// 게임 승리 조건 달성 여부
	private boolean isFail;						// 게임 패배 조건 달성 여부
	private boolean isAttacking = false;		// 마동석이 공격하고 있는지 여부 (공격 모션 제어 관련)
	private boolean isShooting = false;			// 마동석의 주먹이 나가고 있는지 여부 (공격 담당 주먹 발사체 관련)
	private boolean isBoss = false;				// 현재 보스전인지 여부	
	private Image image;						// 보여줄 이미지
	private Image gameBackgroundImage;			// 게임 패널 배경사진 지정할 변수
	private String imagePath = "src/pic/main.PNG";	// 메인 패널 이미지 경로 지정
	private String imagePath2 = "src/pic/win.png";	// 승리 패널 이미지 경로 지정
	private String imagePath3 = "src/pic/fail.png";	// 패배 패널 이미지 경로 지정
	private final int SIZE = 270;				// 이미지 크기 (마동석, 적)
	private int playerX = 10;					// 마동석 생성될 초기 x좌표 지정
	private int playerY = 450;					// 마동석이 생성될 초기 y좌표 지정 
	private int bulletSpeed = 30;				// 주먹의 이동 속도 30
	private int enemySpawnCount = 0;			// 적이 몇명 생성됐는지 확인하는 변수
	private int count = 0;						// 마동석에 의해 체포된 범죄자 수
	private int maLife = 5;					// 마동석의 라이프 수
	private Timer timerAttack; 					// 공격 시간 제어 타이머(마동석의 공격 모션 시간)
	private Timer timerEnemyMove;				// 적이 움직이는 시간 제어 타이머
	private Timer timerBullet;					// 주먹 움직이는 타이머
	private Timer timerEnemyWeapon;				// 날아오는 방망이 타이머
	private Timer timerEnemySpawn;				// 적이 4초마다 생성되는 타이머
	private JLabel maLifeLabel;					// 마동석의 라이프를 표시할 라벨 추가
	private JLabel bossHPLabel;					// 보스의 HP를 표시할 라벨 
	private JLabel score;						// 스코어 표시 라벨
	private JFrame frame = new JFrame("범죄도시 슈팅게임");	// 프레임 생성
	private JPanel panBut, panMain, panGame;	// 버튼, 메인 화면, 게임 화면 구성할 패널
	private JPanel panWin, panFail;				// 승리, 패배 화면 패널
	private JButton start, exit, goal, sound;	// 시작, 종료, 목표, 사운드 버튼
	private Clip punchSound; 					// 펀치 효과음 Clip 변수
	private Clip backgroundMusic;				// 배경음악 최종 clip 변수
	private Clip umSound;						// 피격음 변수
	private Clip hit;							// 타격음 변수
	private float volume = 0.0f; 			    // 배경음악 볼륨 조절 변수 (0.0f에서 -80.0f까지, -80이 음소거)

	Boss bs = new Boss(); 										// 보스 클래스 객체 생성
	ArrayList <MaIcon> enemyList = new ArrayList<>();			// 생성된 적들을 저장하는 리스트
	ArrayList <EnemyWeapon> enemyWeapons = new ArrayList<>();	// 랜덤으로 날아다니는 방망이 구현해 저장하는 리스트
	ArrayList <MaWeapon> bullets = new ArrayList<>();			// 마동석의 주먹 저장하는 리스트

	// 1. 생성자 통한 프레임, 패널 설정
	public GameSetting () {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
		frame.setPreferredSize(new Dimension(1500, 900));		// 화면 크기 1500 x 900, 횡 슈팅
		frame.setResizable(false);								// 화면 크기 변경 불가하게 설정
		panBut = new JPanel();									// 버튼 패널 구성
		panMain = new MainPanel(imagePath); 					// 메인 화면 패널 구성
		panGame = new DrawPanel(); 								// 게임 동작 패널 구성(시작 버튼 클릭 후 전환)
		panWin = new WinPanel(imagePath2);						// 승리시 패널 구성
		panFail = new FailPanel(imagePath3);					// 패배시 패널 구성
		frame.setLayout(new BorderLayout());					// 레이아웃 설정
		frame.add(panBut, BorderLayout.SOUTH);					// 메인 패널 아래쪽 배치
		frame.add(panMain, BorderLayout.CENTER);				// 메인 패널 가운데 배치

		maLifeLabel = new JLabel("마동석 라이프: " + maLife);		// 마동석의 라이프 라벨
		maLifeLabel.setForeground(Color.GREEN);
		maLifeLabel.setFont(new Font("돋움", Font.BOLD, 20));
		panGame.add(maLifeLabel);
		score = new JLabel("[" + "체포된 범죄자 수 : " + count + " / 20" + "]");	// 적을 체포한 카운트 수 라벨
		score.setForeground(Color.WHITE);
		score.setFont(new Font("돋움", Font.BOLD, 20));
		panGame.add(score);
		bossHPLabel = new JLabel("보스 HP: " + bs.getHP());		// 보스 장첸의 라이프 라벨
		bossHPLabel.setForeground(Color.RED);
		bossHPLabel.setFont(new Font("돋움", Font.BOLD, 20));
		panGame.add(bossHPLabel);

		start = new JButton("게임 시작!");			// 버튼 추가
		exit = new JButton("게임 종료..");
		goal = new JButton("게임의 목표");
		sound = new JButton("사운드 켜기/끄기");
		panBut.add(start);						// 누르면 게임 시작
		panBut.add(exit);						// 누르면 게임 종료
		panBut.add(goal);						// 누르면 게임의 목표를 보여줌
		panBut.add(sound);						// 누르면 사운드 켜기 또는 끄기

		BtnListener bt = new BtnListener();		// 버튼 리스너 추가, 객체 생성
		MyKey kl = new MyKey();					// 키 리스너 추가, 객체 생성
		start.addActionListener(bt);
		exit.addActionListener(bt);
		goal.addActionListener(bt);
		sound.addActionListener(bt);			// 각각의 버튼마다 리스너 추가
		panGame.addKeyListener(kl);				// 게임 화면 패널에 키 리스너 추가

		// 타이머 설정
		timerAttack = new Timer(300, new AtListener());				// 300마이크로초 동안 공격 모션 보여줌
		timerEnemyMove = new Timer(50, new EmListener());			// 적들이 50마이크로초마다 이동
		timerBullet = new Timer(100, new BltListener());			// 주먹 5마이크로초마다 이동
		timerEnemyWeapon = new Timer(1500, new EnWpListener());		// 1.5초마다 적 공격(야구배트) 생성
		timerEnemySpawn = new Timer(4000, new SpawnListener());		// 4초마다 적 생성


		panMain.setVisible(true);								// 초기에 메인 패널 보이게 설정
		frame.pack();
		frame.setVisible(true);
	}

	// 키 리스너 설정
	private class MyKey implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_UP) {		// 위쪽 방향키 누르면 위로 10만큼 이동
				if(playerY >= 0) {
					playerY -= 20;
					panGame.repaint();
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN) {	// 아래쪽 방향키 누르면 아래로 10만큼 이동
				if(playerY <= 600) {
					playerY += 20;
					panGame.repaint();
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {	// 스페이스바 누르면 공격

				if(!isAttacking) {
					isAttacking = true;			// 공격하고 있는지 여부 true (공격 모션)
					isShooting = true;			// 총알이 나가고 있는지 여부 true (주먹 이미지 및 공격)
					timerAttack.start();		// 공격 모션 타이머 시작
					timerBullet.start();		// 주먹 발사 타이머 시작
					timerEnemySpawn.start();	// 화면에 적 생성 시작
					panGame.repaint();

					// 공격시 펀치 효과음 재생
					punchSound.setFramePosition(0); // punch 효과음을 매번 처음부터 재생하기 위해 프레임 위치를 0으로 설정
					punchSound.start();				// punch 효과음 시작
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// 스페이스바 떼도 총알 발사 유지
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				isShooting = true;

				// 새 총알 객체 생성, 리스트에 추가
				int newBulletX = playerX + 200;	// 마동석 손 위치에서 주먹 생성
				int newBulletY = playerY + 50;	// 마동석 손 위치에서 주먹 생성
				MaWeapon newBullet = new MaWeapon(newBulletX, newBulletY, 30, 30);
				bullets.add(newBullet);

			}
		}
	}


	// X. 메인 화면 패널에 배경 설정
	public class MainPanel extends JPanel {
		private Image backgroundImage; // 배경사진 지정할 변수

		public MainPanel(String imagePath) {
			backgroundImage = new ImageIcon(imagePath).getImage();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backgroundImage, 0, 0, panMain.getWidth(), panMain.getHeight(), this);
		}
	}
	// 승리시 구현할 패널
	public class WinPanel extends JPanel {
		private Image backgroundImage; 			// 패널의 이미지 지정할 변수
		public WinPanel(String imagePath2) {
			backgroundImage = new ImageIcon(imagePath2).getImage();		// 승리 이미지 경로 지정
			isWin = false; 												// 게임 클리어 여부 초기화
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backgroundImage, 0, 0, panMain.getWidth(), panMain.getHeight(), panWin);

			image = new ImageIcon("win.png").getImage();
			g.drawImage(image, 0, 0, panGame.getWidth(), panGame.getHeight(), panWin);
		}
	}
	
	// 패배시 구현할 패널
	public class FailPanel extends JPanel {
		private Image backgroundImage; 				// 패널의 이미지 지정할 변수
		public FailPanel(String imagePath3) {
			backgroundImage = new ImageIcon(imagePath3).getImage();		// 패배 이미지 경로 지정
			isFail = false; 											// 게임 클리어 여부 초기화
		}
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backgroundImage, 0, 0, panMain.getWidth(), panMain.getHeight(), panFail);

			image = new ImageIcon("fail.png").getImage();
			g.drawImage(image, 0, 0, panGame.getWidth(), panGame.getHeight(), panWin);
		}
	}


	// 버튼 리스너 설정
	private class BtnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == start) {					// 시작버튼 눌린경우
				frame.add(panGame, BorderLayout.CENTER);	// 시작버튼이 눌리면 게임화면으로 넘어감
				panMain.setVisible(false);
				panGame.setVisible(true);
				timerEnemyMove.start();					// 적들 움직임 시작
				timerEnemyWeapon.start();		 		// 방망이 생성 시작
				timerEnemySpawn.start();				// 적들 생성 시작
				panGame.repaint();
				
				if(isWin == true || isFail == true) {
					count = 0;
					new GameSetting();
					
				}
			}
			if(e.getSource() == goal) {	// 게임의 목표를 보여줌
				showGameGoal();
			}
			if(e.getSource() == exit) {	// 종료버튼 눌린경우
				System.exit(0);
			}

			if(e.getSource() == sound) {	// 사운드 켜기/끄기 버튼 눌린경우
				if(backgroundMusic.isActive() == true)
					backgroundMusic.stop();
				else
					backgroundMusic.start();
			}
		}
	}

	// 타이머 리스너 설정
	// 마동석 공격모션 타이머의 AtListener
	private class AtListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			isAttacking = false;	// 공격 이후 원래 모션으로 돌아감
		}
	}
	// 적들 4초마다 5명씩 생성하는 SpawnListener
	private class SpawnListener implements ActionListener{	
		@Override
		public void actionPerformed(ActionEvent e) {			
			for (int i = 0; i < 5; i++) {
				int getEnX = (int)(Math.random() * 800 + 400);	// 적 생성 x좌표 랜덤하게
				int getEnY = (int)(Math.random() * 400 + 1);	// 적 생성 y좌표 랜덤하게
				MaIcon enemy = new MaIcon("src/pic/enemy1.PNG", getEnX, getEnY, SIZE - 50, SIZE - 50);	
				enemyList.add(enemy);							// 적 객체 생성된 것 리스트에 저장
			}
			enemySpawnCount += 5;

			if (enemySpawnCount >= 15) {
				timerEnemySpawn.stop();		// 총 15명의 적이 생성되었으면 적 생성 타이머 중지
			}
		}
	}

	// 적의 움직임 구현 타이머 EmListener
	private class EmListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			for (MaIcon enemy : enemyList) {
				int enemyX = enemy.getX();		// 적의 현재 x좌표 가져옴
				int enemyY = enemy.getY();		// 적의 현재 y좌표 가져옴
				int dirX = enemy.getDirX();		// 적의 현재 x 방향 움직임 가져옴
				int dirY = enemy.getDirY();		// 적의 현재 y 방향 움직임 가져옴

				if (enemyX + SIZE - 50 >= panGame.getWidth() || enemyX <= 0) {	// 양쪽 벽에 부딪히면 반사되어 나옴
					dirX *= -1;
				}
				if (enemyY + SIZE-50 >= panGame.getHeight() || enemyY <= 0) {	// 위 아래쪽 벽에 부딪히면 반사되어 나옴
					dirY *= -1;
				}
				enemyX += dirX;					 
				enemyY += dirY;					
				enemy.setPos(enemyX, enemyY);	
				enemy.setDirX(dirX);			// 벽에 부딪히면 방향 바뀌는 것 적용
				enemy.setDirY(dirY);			// 벽에 부딪히면 방향 바뀌는 것 적용
			}
		}
	}

	private class BltListener implements ActionListener {	// 주먹 움직임 타이머 BltListener
		@Override
		public void actionPerformed(ActionEvent e) {
			maTouched();	// 마동석과 적의 돌진 충돌 체크
			BatTouched();	// 마동석과 야구배트의 충돌 체크
			
			if (isShooting) {								// 사격 상태이면
				int bulletX = playerX + 250;				// 마동석 손 위치
				bulletX += bulletSpeed;						// 주먹이 지정 초마다 스피드만큼 날아감

				// 총알 이동 및 리스트에서 벗어난 총알 제거
				for (int i = 0; i < bullets.size(); i++) {
					MaWeapon bullet = bullets.get(i);
					bullet.move();
					if (bullet.isOutOfBounds(panGame.getWidth())) {		// 화면 밖으로 나가는 조건에 해당하면
						bullets.remove(i);								// 해당 주먹 제거
						i--;											// 반복
					}
				}
				// 주먹과 적의 충돌 체크
				for (int i = 0; i < bullets.size(); i++) {			// 다중 for문 바깥쪽
					MaWeapon bullet = bullets.get(i);				
					if (bs.isColliding(bullet.getX(), bullet.getY(), 30) && isBoss == true) { // 보스와 주먹의 좌표를 비교하여 충돌 체크
						hit.setFramePosition(0);
						hit.start();
						bs.decreaseHP(); 								// 주먹과 보스가 충돌하면 보스의 HP 감소
						bullets.remove(i);								// 충돌한 주먹을 리스트에서 제거
						bossHPLabel.setText("보스 HP: " + bs.getHP()); 	// 보스의 HP 표시 업데이트
						i--;
					}
					for (int j = 0; j < enemyList.size(); j++) {	// 다중 for문 안쪽
						MaIcon enemy = enemyList.get(j);
						int enemySize = SIZE - 50;

						// 주먹과 적의 좌표를 비교하여 충돌 체크
						if (bullet.getX() + 30 >= enemy.getX() && bullet.getX() <= enemy.getX() + enemySize
								&& bullet.getY() + 30 >= enemy.getY() && bullet.getY() <= enemy.getY() + enemySize) {
							bullets.remove(i);				// 주먹과 적이 충돌하면 주먹을 리스트에서 제거
							i--;
							hit.setFramePosition(0);	// 실행될 때마다 처음부터 효과음 시작
							hit.start();				// 타격시 음향 출력
							enemyList.remove(j); 		// 총알이 적과 충돌하면 적을 리스트에서 제거	
							count++;					// 제거(체포)될 때마다 count값 증가
							j--;	
							score.setText("체포된 범죄자 수 : " + count + " / 20");
							break; 						// 해당 주먹이 마동석과 충돌 후 다시 충돌할 필요 없으므로 break
						}
					}
				}
			}
			panGame.repaint();
		}
	}

	// 적이 날리는 방망이 움직임 생성, 제어 타이머
	private class EnWpListener implements ActionListener {			
		@Override
		public void actionPerformed(ActionEvent e) {
			int randomHeight = (int)(Math.random()*(panGame.getHeight()-75)+1);	// 화면 오른쪽 벽에서 랜덤한 높이의 위치(화면 벗어나지않는 높이)에서 방망이 생성
			EnemyWeapon enemyWeapon = new EnemyWeapon(panGame.getWidth(),randomHeight, 75, 75, "src/pic/bat.gif", 3);
			enemyWeapons.add(enemyWeapon);

			for (int j = 0; j < enemyWeapons.size(); j++) {	// 방망이와 마동석의 충돌 체크
				EnemyWeapon ew = enemyWeapons.get(j);		// 충돌 판정할 방망이 객체
				int weaponX = ew.getX();
				int weaponY = ew.getY();
				int weaponSize = 75;

				// 마동석과 배트의 위치를 비교
				if (playerX + 50 < weaponX + weaponSize && playerX + 200 > weaponX
						&& playerY + 20 < weaponY + weaponSize && playerY + 190 > weaponY) {
					umSound.setFramePosition(0);
					umSound.start();
					enemyWeapons.remove(j);	// 배트와 마동석이 부딪히면 해당 배트를 리스트에서 제거
					j--;
					maLife--;	// 배트와 마동석이 충돌하면 마동석의 라이프 하나 제거

					break;		// 해당 주먹이 다른 적과의 충돌 체크는 필요 없으므로 반복문을 빠져나옴
				}
			}
		}		
	}

	// 메서드	
	private void setupSounds() {	// 게임 사운드 설정
		try {
			AudioInputStream um = AudioSystem.getAudioInputStream(new File("src/sound/umph.wav"));			// 적, 주먹의 충돌음 로드
			umSound = AudioSystem.getClip();
			umSound.open(um);

			AudioInputStream hits = AudioSystem.getAudioInputStream(new File("src/sound/punch.wav"));		 // 펀치 타격음 로드
			hit = AudioSystem.getClip();
			hit.open(hits);

			AudioInputStream punch = AudioSystem.getAudioInputStream(new File("src/sound/fistpunch.wav"));		 // 펀치 효과음 로드
			punchSound = AudioSystem.getClip();
			punchSound.open(punch);

			AudioInputStream bgm2 = AudioSystem.getAudioInputStream(new File("src/sound/bgm2.wav"));	// 배경음악 - 최종
			backgroundMusic = AudioSystem.getClip();
			backgroundMusic.open(bgm2);
			backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

			// 배경음악 볼륨 조절
			FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(volume);
		} catch (Exception e) {
			System.out.println("오류가 발생했습니다.");
		}
	}

	// 메인 메서드 구현
	public static void main(String[] args) {	
		GameSetting gs = new GameSetting();		// 객체 생성
		gs.setupSounds();						// 사운드 설정

	}

	// 목표창 띄우기
	public static void showGameGoal() {
        String goalMessage = "[이 게임의 목표]\n " +
                             "당신은 마동석입니다. 적들의 공격을 피하면서 \n" +
                             "당신의 강력한 주먹으로 적들을 체포하세요!.\n" +
                             "이후 보스 장첸도 체포하면 된답니다~\n" +
                             "전변호사는... 없답니다!" +
                             "TIP: '스페이스바로 공격', 위아래 방향키로 이동입니다";
        JOptionPane.showMessageDialog(null, goalMessage, "게임 목표", JOptionPane.INFORMATION_MESSAGE);
    }
	
	// 방망이 움직임, 사라짐 구현
	private void updateEnemyWeapons() {	
		for (int i = 0; i < enemyWeapons.size(); i++) {
			EnemyWeapon enemyWeapon = enemyWeapons.get(i);
			enemyWeapon.move();										// 방망이 움직임 시작
			if (enemyWeapon.isOutOfBounds(panGame.getWidth())) {	// 왼쪽으로 날아가 벽에 닿으면 
				enemyWeapons.remove(i);								// 리스트에서 제거함
				i--;
			}
		}
	}
	
	// 보스가 돌진해서 맞았는지 판정
	public void maTouched() {	
		int damageCount = 0;
		if(bs.isTouched(playerX, playerY) == true && damageCount < 1) {
			umSound.setFramePosition(0);
			umSound.start();
			maLife--;
			damageCount++;
		}
	}
	
	// 보스가 던진 야구배트 맞았는지 판정
	public void BatTouched() {
		int damageCount = 0;
		if(bs.isBatTouched(playerX, playerY) == true && damageCount < 1) {
			umSound.setFramePosition(0);
			umSound.start();
			maLife--;
			damageCount++;
		}
	}

	// 그림을 그려줄 DrawPanel 설정
	public class DrawPanel extends JPanel {										
		public DrawPanel() {													// 생성자 통해 배경 이미지 초기화
			gameBackgroundImage = new ImageIcon("src/pic/시장.png").getImage(); 		// 시장.png로 배경 이미지 초기화
			if(count >= 20) {
				gameBackgroundImage = new ImageIcon("src/pic/pic최종.png").getImage();	// 충분히 체포했으면(20명) 최종.png로 배경 이미지 초기화
			}
		}

		@Override
		public void paintComponent (Graphics draw) {		// paintComponent 통해 그림을 다시 그려줌
			super.paintComponent(draw);
			updateEnemyWeapons();							// 적의 야구배트 위치변화 그려줌
			maLifeLabel.setText("마동석 라이프: " + maLife);	// 마동석의 라이프 표시 업데이트
			bossHPLabel.setText("보스 HP: " + bs.getHP());	// 보스의 HP 표시 업데이트
		
			// 배경 이미지 구현
			if (count < 20) {
				draw.drawImage(gameBackgroundImage, 0, 0, panGame.getWidth(), panGame.getHeight(), this);
			}
			else {
				isBoss = true;
				enemyList.clear();			// 적 리스트 초기화
				timerEnemySpawn.stop();		// 적 생성 타이머 종료
				timerEnemyWeapon.stop();	// 적 무기 공격(야구배트) 타이머 종료
				enemyWeapons.clear();		// 적 무기 공격 리스트 초기화

				Image bossBackgroundImage = new ImageIcon("src/pic/최종.png").getImage();				// 배경화면 최종장으로 변경하기 위한 객체 생성
				draw.drawImage(bossBackgroundImage, 0, 0, panMain.getWidth(), panMain.getHeight(), this);	// 객체와 생성위치, 크기 지정
			}
			
			// 마동석 이미지 구현	
			if(isAttacking == false) {	// 공격 중이 아니면, 이동 이미지 maWalk 그려줌
				MaIcon mi = new MaIcon("src/pic/maWalk.PNG",playerX, playerY, SIZE, SIZE);	
				Graphics2D g2d = (Graphics2D) draw;
				mi.draw(draw);
			}
			else {						// 공격 중이면, 공격시 이미지 maAttack 그려줌
				MaIcon mi = new MaIcon("src/pic/maAttack.PNG",playerX,playerY,SIZE,SIZE);	
				Graphics2D g2d = (Graphics2D) draw;
				mi.draw(draw);
			}

			// 마동석의 공격 이미지 구현
			if(isShooting) {							// 마동석 공격 시 나가는 주먹, 발사 여부 true일 때 이미지 발생
				for (MaWeapon bullet : bullets)
					bullet.draw(draw);
			}

			// 적, 적 공격 이미지 구현
			for(MaIcon enemy : enemyList) {				// 적 이미지 그려줌.
				enemy.draw(draw);
			}
			for(EnemyWeapon weapon : enemyWeapons) { 	// 야구 배트 그려줌(오른쪽 > 왼쪽 날아옴)
				Graphics2D g2d = (Graphics2D) draw;
				weapon.draw(draw);
			}

			// 보스 장첸 이미지 구현
			if(count >= 20) {				// 보스 등장 조건, 적 20명 체포 달성 시
				isBoss = true;
				bs.performAction();			// 보스 동작 실행 (Boss 클래스의 performAction 실행)
				bs.draw(draw); 				// 보스 이미지 그리기 (Boss 클래스의 draw 실행)
			}

			// 게임 오버 구현
			if(bs.getHP() <= 0) {			// 게임 승리, 보스 생명력 0 이하일 때
				if(isBoss == true)
					backgroundMusic.stop();
				isWin = true;				// 승리 조건 활성화됨
				frame.add(panWin);			// 승리 패널 붙혀줌
				if(isWin == true) {
					panGame.setVisible(false);	// 게임 패널 감춤
					panWin.setVisible(true);	// 승리 패널 보여줌
				}
			}
			if(maLife <= 0) {				// 게임 패배, 마동석 생명력 0 이하일 때
				if(isBoss == true)
					backgroundMusic.stop();
				isFail = true;				// 패배 조건 활성화
				frame.add(panFail);			// 패배 패널 붙혀줌
				if(isFail == true) {
					panGame.setVisible(false);	// 게임 패널 감춤
					panFail.setVisible(true);	// 패배 패널 보여줌
				}
			}

			panGame.requestFocusInWindow();	// 패널에 포커스 설정
			panGame.repaint();
		}
	}
}