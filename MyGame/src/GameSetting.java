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

// OS���α׷��� ���Ƹ� ���� ����
// ���ð��� �����
// �帣 : ���ð��� , ���� : �÷��ð��� �������� ���ð���, �ʻ�� ����. ���� '������'�� ���� ����

// 0. ���� ����
public class GameSetting {

	private boolean isWin;						// ���� �¸� ���� �޼� ����
	private boolean isFail;						// ���� �й� ���� �޼� ����
	private boolean isAttacking = false;		// �������� �����ϰ� �ִ��� ���� (���� ��� ���� ����)
	private boolean isShooting = false;			// �������� �ָ��� ������ �ִ��� ���� (���� ��� �ָ� �߻�ü ����)
	private boolean isBoss = false;				// ���� ���������� ����	
	private Image image;						// ������ �̹���
	private Image gameBackgroundImage;			// ���� �г� ������ ������ ����
	private String imagePath = "src/pic/main.PNG";	// ���� �г� �̹��� ��� ����
	private String imagePath2 = "src/pic/win.png";	// �¸� �г� �̹��� ��� ����
	private String imagePath3 = "src/pic/fail.png";	// �й� �г� �̹��� ��� ����
	private final int SIZE = 270;				// �̹��� ũ�� (������, ��)
	private int playerX = 10;					// ������ ������ �ʱ� x��ǥ ����
	private int playerY = 450;					// �������� ������ �ʱ� y��ǥ ���� 
	private int bulletSpeed = 30;				// �ָ��� �̵� �ӵ� 30
	private int enemySpawnCount = 0;			// ���� ��� �����ƴ��� Ȯ���ϴ� ����
	private int count = 0;						// �������� ���� ü���� ������ ��
	private int maLife = 5;					// �������� ������ ��
	private Timer timerAttack; 					// ���� �ð� ���� Ÿ�̸�(�������� ���� ��� �ð�)
	private Timer timerEnemyMove;				// ���� �����̴� �ð� ���� Ÿ�̸�
	private Timer timerBullet;					// �ָ� �����̴� Ÿ�̸�
	private Timer timerEnemyWeapon;				// ���ƿ��� ����� Ÿ�̸�
	private Timer timerEnemySpawn;				// ���� 4�ʸ��� �����Ǵ� Ÿ�̸�
	private JLabel maLifeLabel;					// �������� �������� ǥ���� �� �߰�
	private JLabel bossHPLabel;					// ������ HP�� ǥ���� �� 
	private JLabel score;						// ���ھ� ǥ�� ��
	private JFrame frame = new JFrame("���˵��� ���ð���");	// ������ ����
	private JPanel panBut, panMain, panGame;	// ��ư, ���� ȭ��, ���� ȭ�� ������ �г�
	private JPanel panWin, panFail;				// �¸�, �й� ȭ�� �г�
	private JButton start, exit, goal, sound;	// ����, ����, ��ǥ, ���� ��ư
	private Clip punchSound; 					// ��ġ ȿ���� Clip ����
	private Clip backgroundMusic;				// ������� ���� clip ����
	private Clip umSound;						// �ǰ��� ����
	private Clip hit;							// Ÿ���� ����
	private float volume = 0.0f; 			    // ������� ���� ���� ���� (0.0f���� -80.0f����, -80�� ���Ұ�)

	Boss bs = new Boss(); 										// ���� Ŭ���� ��ü ����
	ArrayList <MaIcon> enemyList = new ArrayList<>();			// ������ ������ �����ϴ� ����Ʈ
	ArrayList <EnemyWeapon> enemyWeapons = new ArrayList<>();	// �������� ���ƴٴϴ� ����� ������ �����ϴ� ����Ʈ
	ArrayList <MaWeapon> bullets = new ArrayList<>();			// �������� �ָ� �����ϴ� ����Ʈ

	// 1. ������ ���� ������, �г� ����
	public GameSetting () {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
		frame.setPreferredSize(new Dimension(1500, 900));		// ȭ�� ũ�� 1500 x 900, Ⱦ ����
		frame.setResizable(false);								// ȭ�� ũ�� ���� �Ұ��ϰ� ����
		panBut = new JPanel();									// ��ư �г� ����
		panMain = new MainPanel(imagePath); 					// ���� ȭ�� �г� ����
		panGame = new DrawPanel(); 								// ���� ���� �г� ����(���� ��ư Ŭ�� �� ��ȯ)
		panWin = new WinPanel(imagePath2);						// �¸��� �г� ����
		panFail = new FailPanel(imagePath3);					// �й�� �г� ����
		frame.setLayout(new BorderLayout());					// ���̾ƿ� ����
		frame.add(panBut, BorderLayout.SOUTH);					// ���� �г� �Ʒ��� ��ġ
		frame.add(panMain, BorderLayout.CENTER);				// ���� �г� ��� ��ġ

		maLifeLabel = new JLabel("������ ������: " + maLife);		// �������� ������ ��
		maLifeLabel.setForeground(Color.GREEN);
		maLifeLabel.setFont(new Font("����", Font.BOLD, 20));
		panGame.add(maLifeLabel);
		score = new JLabel("[" + "ü���� ������ �� : " + count + " / 20" + "]");	// ���� ü���� ī��Ʈ �� ��
		score.setForeground(Color.WHITE);
		score.setFont(new Font("����", Font.BOLD, 20));
		panGame.add(score);
		bossHPLabel = new JLabel("���� HP: " + bs.getHP());		// ���� ��þ�� ������ ��
		bossHPLabel.setForeground(Color.RED);
		bossHPLabel.setFont(new Font("����", Font.BOLD, 20));
		panGame.add(bossHPLabel);

		start = new JButton("���� ����!");			// ��ư �߰�
		exit = new JButton("���� ����..");
		goal = new JButton("������ ��ǥ");
		sound = new JButton("���� �ѱ�/����");
		panBut.add(start);						// ������ ���� ����
		panBut.add(exit);						// ������ ���� ����
		panBut.add(goal);						// ������ ������ ��ǥ�� ������
		panBut.add(sound);						// ������ ���� �ѱ� �Ǵ� ����

		BtnListener bt = new BtnListener();		// ��ư ������ �߰�, ��ü ����
		MyKey kl = new MyKey();					// Ű ������ �߰�, ��ü ����
		start.addActionListener(bt);
		exit.addActionListener(bt);
		goal.addActionListener(bt);
		sound.addActionListener(bt);			// ������ ��ư���� ������ �߰�
		panGame.addKeyListener(kl);				// ���� ȭ�� �гο� Ű ������ �߰�

		// Ÿ�̸� ����
		timerAttack = new Timer(300, new AtListener());				// 300����ũ���� ���� ���� ��� ������
		timerEnemyMove = new Timer(50, new EmListener());			// ������ 50����ũ���ʸ��� �̵�
		timerBullet = new Timer(100, new BltListener());			// �ָ� 5����ũ���ʸ��� �̵�
		timerEnemyWeapon = new Timer(1500, new EnWpListener());		// 1.5�ʸ��� �� ����(�߱���Ʈ) ����
		timerEnemySpawn = new Timer(4000, new SpawnListener());		// 4�ʸ��� �� ����


		panMain.setVisible(true);								// �ʱ⿡ ���� �г� ���̰� ����
		frame.pack();
		frame.setVisible(true);
	}

	// Ű ������ ����
	private class MyKey implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_UP) {		// ���� ����Ű ������ ���� 10��ŭ �̵�
				if(playerY >= 0) {
					playerY -= 20;
					panGame.repaint();
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN) {	// �Ʒ��� ����Ű ������ �Ʒ��� 10��ŭ �̵�
				if(playerY <= 600) {
					playerY += 20;
					panGame.repaint();
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {	// �����̽��� ������ ����

				if(!isAttacking) {
					isAttacking = true;			// �����ϰ� �ִ��� ���� true (���� ���)
					isShooting = true;			// �Ѿ��� ������ �ִ��� ���� true (�ָ� �̹��� �� ����)
					timerAttack.start();		// ���� ��� Ÿ�̸� ����
					timerBullet.start();		// �ָ� �߻� Ÿ�̸� ����
					timerEnemySpawn.start();	// ȭ�鿡 �� ���� ����
					panGame.repaint();

					// ���ݽ� ��ġ ȿ���� ���
					punchSound.setFramePosition(0); // punch ȿ������ �Ź� ó������ ����ϱ� ���� ������ ��ġ�� 0���� ����
					punchSound.start();				// punch ȿ���� ����
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// �����̽��� ���� �Ѿ� �߻� ����
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				isShooting = true;

				// �� �Ѿ� ��ü ����, ����Ʈ�� �߰�
				int newBulletX = playerX + 200;	// ������ �� ��ġ���� �ָ� ����
				int newBulletY = playerY + 50;	// ������ �� ��ġ���� �ָ� ����
				MaWeapon newBullet = new MaWeapon(newBulletX, newBulletY, 30, 30);
				bullets.add(newBullet);

			}
		}
	}


	// X. ���� ȭ�� �гο� ��� ����
	public class MainPanel extends JPanel {
		private Image backgroundImage; // ������ ������ ����

		public MainPanel(String imagePath) {
			backgroundImage = new ImageIcon(imagePath).getImage();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backgroundImage, 0, 0, panMain.getWidth(), panMain.getHeight(), this);
		}
	}
	// �¸��� ������ �г�
	public class WinPanel extends JPanel {
		private Image backgroundImage; 			// �г��� �̹��� ������ ����
		public WinPanel(String imagePath2) {
			backgroundImage = new ImageIcon(imagePath2).getImage();		// �¸� �̹��� ��� ����
			isWin = false; 												// ���� Ŭ���� ���� �ʱ�ȭ
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backgroundImage, 0, 0, panMain.getWidth(), panMain.getHeight(), panWin);

			image = new ImageIcon("win.png").getImage();
			g.drawImage(image, 0, 0, panGame.getWidth(), panGame.getHeight(), panWin);
		}
	}
	
	// �й�� ������ �г�
	public class FailPanel extends JPanel {
		private Image backgroundImage; 				// �г��� �̹��� ������ ����
		public FailPanel(String imagePath3) {
			backgroundImage = new ImageIcon(imagePath3).getImage();		// �й� �̹��� ��� ����
			isFail = false; 											// ���� Ŭ���� ���� �ʱ�ȭ
		}
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backgroundImage, 0, 0, panMain.getWidth(), panMain.getHeight(), panFail);

			image = new ImageIcon("fail.png").getImage();
			g.drawImage(image, 0, 0, panGame.getWidth(), panGame.getHeight(), panWin);
		}
	}


	// ��ư ������ ����
	private class BtnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == start) {					// ���۹�ư �������
				frame.add(panGame, BorderLayout.CENTER);	// ���۹�ư�� ������ ����ȭ������ �Ѿ
				panMain.setVisible(false);
				panGame.setVisible(true);
				timerEnemyMove.start();					// ���� ������ ����
				timerEnemyWeapon.start();		 		// ����� ���� ����
				timerEnemySpawn.start();				// ���� ���� ����
				panGame.repaint();
				
				if(isWin == true || isFail == true) {
					count = 0;
					new GameSetting();
					
				}
			}
			if(e.getSource() == goal) {	// ������ ��ǥ�� ������
				showGameGoal();
			}
			if(e.getSource() == exit) {	// �����ư �������
				System.exit(0);
			}

			if(e.getSource() == sound) {	// ���� �ѱ�/���� ��ư �������
				if(backgroundMusic.isActive() == true)
					backgroundMusic.stop();
				else
					backgroundMusic.start();
			}
		}
	}

	// Ÿ�̸� ������ ����
	// ������ ���ݸ�� Ÿ�̸��� AtListener
	private class AtListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			isAttacking = false;	// ���� ���� ���� ������� ���ư�
		}
	}
	// ���� 4�ʸ��� 5�� �����ϴ� SpawnListener
	private class SpawnListener implements ActionListener{	
		@Override
		public void actionPerformed(ActionEvent e) {			
			for (int i = 0; i < 5; i++) {
				int getEnX = (int)(Math.random() * 800 + 400);	// �� ���� x��ǥ �����ϰ�
				int getEnY = (int)(Math.random() * 400 + 1);	// �� ���� y��ǥ �����ϰ�
				MaIcon enemy = new MaIcon("src/pic/enemy1.PNG", getEnX, getEnY, SIZE - 50, SIZE - 50);	
				enemyList.add(enemy);							// �� ��ü ������ �� ����Ʈ�� ����
			}
			enemySpawnCount += 5;

			if (enemySpawnCount >= 15) {
				timerEnemySpawn.stop();		// �� 15���� ���� �����Ǿ����� �� ���� Ÿ�̸� ����
			}
		}
	}

	// ���� ������ ���� Ÿ�̸� EmListener
	private class EmListener implements ActionListener {	
		@Override
		public void actionPerformed(ActionEvent e) {
			for (MaIcon enemy : enemyList) {
				int enemyX = enemy.getX();		// ���� ���� x��ǥ ������
				int enemyY = enemy.getY();		// ���� ���� y��ǥ ������
				int dirX = enemy.getDirX();		// ���� ���� x ���� ������ ������
				int dirY = enemy.getDirY();		// ���� ���� y ���� ������ ������

				if (enemyX + SIZE - 50 >= panGame.getWidth() || enemyX <= 0) {	// ���� ���� �ε����� �ݻ�Ǿ� ����
					dirX *= -1;
				}
				if (enemyY + SIZE-50 >= panGame.getHeight() || enemyY <= 0) {	// �� �Ʒ��� ���� �ε����� �ݻ�Ǿ� ����
					dirY *= -1;
				}
				enemyX += dirX;					 
				enemyY += dirY;					
				enemy.setPos(enemyX, enemyY);	
				enemy.setDirX(dirX);			// ���� �ε����� ���� �ٲ�� �� ����
				enemy.setDirY(dirY);			// ���� �ε����� ���� �ٲ�� �� ����
			}
		}
	}

	private class BltListener implements ActionListener {	// �ָ� ������ Ÿ�̸� BltListener
		@Override
		public void actionPerformed(ActionEvent e) {
			maTouched();	// �������� ���� ���� �浹 üũ
			BatTouched();	// �������� �߱���Ʈ�� �浹 üũ
			
			if (isShooting) {								// ��� �����̸�
				int bulletX = playerX + 250;				// ������ �� ��ġ
				bulletX += bulletSpeed;						// �ָ��� ���� �ʸ��� ���ǵ常ŭ ���ư�

				// �Ѿ� �̵� �� ����Ʈ���� ��� �Ѿ� ����
				for (int i = 0; i < bullets.size(); i++) {
					MaWeapon bullet = bullets.get(i);
					bullet.move();
					if (bullet.isOutOfBounds(panGame.getWidth())) {		// ȭ�� ������ ������ ���ǿ� �ش��ϸ�
						bullets.remove(i);								// �ش� �ָ� ����
						i--;											// �ݺ�
					}
				}
				// �ָ԰� ���� �浹 üũ
				for (int i = 0; i < bullets.size(); i++) {			// ���� for�� �ٱ���
					MaWeapon bullet = bullets.get(i);				
					if (bs.isColliding(bullet.getX(), bullet.getY(), 30) && isBoss == true) { // ������ �ָ��� ��ǥ�� ���Ͽ� �浹 üũ
						hit.setFramePosition(0);
						hit.start();
						bs.decreaseHP(); 								// �ָ԰� ������ �浹�ϸ� ������ HP ����
						bullets.remove(i);								// �浹�� �ָ��� ����Ʈ���� ����
						bossHPLabel.setText("���� HP: " + bs.getHP()); 	// ������ HP ǥ�� ������Ʈ
						i--;
					}
					for (int j = 0; j < enemyList.size(); j++) {	// ���� for�� ����
						MaIcon enemy = enemyList.get(j);
						int enemySize = SIZE - 50;

						// �ָ԰� ���� ��ǥ�� ���Ͽ� �浹 üũ
						if (bullet.getX() + 30 >= enemy.getX() && bullet.getX() <= enemy.getX() + enemySize
								&& bullet.getY() + 30 >= enemy.getY() && bullet.getY() <= enemy.getY() + enemySize) {
							bullets.remove(i);				// �ָ԰� ���� �浹�ϸ� �ָ��� ����Ʈ���� ����
							i--;
							hit.setFramePosition(0);	// ����� ������ ó������ ȿ���� ����
							hit.start();				// Ÿ�ݽ� ���� ���
							enemyList.remove(j); 		// �Ѿ��� ���� �浹�ϸ� ���� ����Ʈ���� ����	
							count++;					// ����(ü��)�� ������ count�� ����
							j--;	
							score.setText("ü���� ������ �� : " + count + " / 20");
							break; 						// �ش� �ָ��� �������� �浹 �� �ٽ� �浹�� �ʿ� �����Ƿ� break
						}
					}
				}
			}
			panGame.repaint();
		}
	}

	// ���� ������ ����� ������ ����, ���� Ÿ�̸�
	private class EnWpListener implements ActionListener {			
		@Override
		public void actionPerformed(ActionEvent e) {
			int randomHeight = (int)(Math.random()*(panGame.getHeight()-75)+1);	// ȭ�� ������ ������ ������ ������ ��ġ(ȭ�� ������ʴ� ����)���� ����� ����
			EnemyWeapon enemyWeapon = new EnemyWeapon(panGame.getWidth(),randomHeight, 75, 75, "src/pic/bat.gif", 3);
			enemyWeapons.add(enemyWeapon);

			for (int j = 0; j < enemyWeapons.size(); j++) {	// ����̿� �������� �浹 üũ
				EnemyWeapon ew = enemyWeapons.get(j);		// �浹 ������ ����� ��ü
				int weaponX = ew.getX();
				int weaponY = ew.getY();
				int weaponSize = 75;

				// �������� ��Ʈ�� ��ġ�� ��
				if (playerX + 50 < weaponX + weaponSize && playerX + 200 > weaponX
						&& playerY + 20 < weaponY + weaponSize && playerY + 190 > weaponY) {
					umSound.setFramePosition(0);
					umSound.start();
					enemyWeapons.remove(j);	// ��Ʈ�� �������� �ε����� �ش� ��Ʈ�� ����Ʈ���� ����
					j--;
					maLife--;	// ��Ʈ�� �������� �浹�ϸ� �������� ������ �ϳ� ����

					break;		// �ش� �ָ��� �ٸ� ������ �浹 üũ�� �ʿ� �����Ƿ� �ݺ����� ��������
				}
			}
		}		
	}

	// �޼���	
	private void setupSounds() {	// ���� ���� ����
		try {
			AudioInputStream um = AudioSystem.getAudioInputStream(new File("src/sound/umph.wav"));			// ��, �ָ��� �浹�� �ε�
			umSound = AudioSystem.getClip();
			umSound.open(um);

			AudioInputStream hits = AudioSystem.getAudioInputStream(new File("src/sound/punch.wav"));		 // ��ġ Ÿ���� �ε�
			hit = AudioSystem.getClip();
			hit.open(hits);

			AudioInputStream punch = AudioSystem.getAudioInputStream(new File("src/sound/fistpunch.wav"));		 // ��ġ ȿ���� �ε�
			punchSound = AudioSystem.getClip();
			punchSound.open(punch);

			AudioInputStream bgm2 = AudioSystem.getAudioInputStream(new File("src/sound/bgm2.wav"));	// ������� - ����
			backgroundMusic = AudioSystem.getClip();
			backgroundMusic.open(bgm2);
			backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

			// ������� ���� ����
			FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(volume);
		} catch (Exception e) {
			System.out.println("������ �߻��߽��ϴ�.");
		}
	}

	// ���� �޼��� ����
	public static void main(String[] args) {	
		GameSetting gs = new GameSetting();		// ��ü ����
		gs.setupSounds();						// ���� ����

	}

	// ��ǥâ ����
	public static void showGameGoal() {
        String goalMessage = "[�� ������ ��ǥ]\n " +
                             "����� �������Դϴ�. ������ ������ ���ϸ鼭 \n" +
                             "����� ������ �ָ����� ������ ü���ϼ���!.\n" +
                             "���� ���� ��þ�� ü���ϸ� �ȴ�ϴ�~\n" +
                             "����ȣ���... ����ϴ�!" +
                             "TIP: '�����̽��ٷ� ����', ���Ʒ� ����Ű�� �̵��Դϴ�";
        JOptionPane.showMessageDialog(null, goalMessage, "���� ��ǥ", JOptionPane.INFORMATION_MESSAGE);
    }
	
	// ����� ������, ����� ����
	private void updateEnemyWeapons() {	
		for (int i = 0; i < enemyWeapons.size(); i++) {
			EnemyWeapon enemyWeapon = enemyWeapons.get(i);
			enemyWeapon.move();										// ����� ������ ����
			if (enemyWeapon.isOutOfBounds(panGame.getWidth())) {	// �������� ���ư� ���� ������ 
				enemyWeapons.remove(i);								// ����Ʈ���� ������
				i--;
			}
		}
	}
	
	// ������ �����ؼ� �¾Ҵ��� ����
	public void maTouched() {	
		int damageCount = 0;
		if(bs.isTouched(playerX, playerY) == true && damageCount < 1) {
			umSound.setFramePosition(0);
			umSound.start();
			maLife--;
			damageCount++;
		}
	}
	
	// ������ ���� �߱���Ʈ �¾Ҵ��� ����
	public void BatTouched() {
		int damageCount = 0;
		if(bs.isBatTouched(playerX, playerY) == true && damageCount < 1) {
			umSound.setFramePosition(0);
			umSound.start();
			maLife--;
			damageCount++;
		}
	}

	// �׸��� �׷��� DrawPanel ����
	public class DrawPanel extends JPanel {										
		public DrawPanel() {													// ������ ���� ��� �̹��� �ʱ�ȭ
			gameBackgroundImage = new ImageIcon("src/pic/����.png").getImage(); 		// ����.png�� ��� �̹��� �ʱ�ȭ
			if(count >= 20) {
				gameBackgroundImage = new ImageIcon("src/pic/pic����.png").getImage();	// ����� ü��������(20��) ����.png�� ��� �̹��� �ʱ�ȭ
			}
		}

		@Override
		public void paintComponent (Graphics draw) {		// paintComponent ���� �׸��� �ٽ� �׷���
			super.paintComponent(draw);
			updateEnemyWeapons();							// ���� �߱���Ʈ ��ġ��ȭ �׷���
			maLifeLabel.setText("������ ������: " + maLife);	// �������� ������ ǥ�� ������Ʈ
			bossHPLabel.setText("���� HP: " + bs.getHP());	// ������ HP ǥ�� ������Ʈ
		
			// ��� �̹��� ����
			if (count < 20) {
				draw.drawImage(gameBackgroundImage, 0, 0, panGame.getWidth(), panGame.getHeight(), this);
			}
			else {
				isBoss = true;
				enemyList.clear();			// �� ����Ʈ �ʱ�ȭ
				timerEnemySpawn.stop();		// �� ���� Ÿ�̸� ����
				timerEnemyWeapon.stop();	// �� ���� ����(�߱���Ʈ) Ÿ�̸� ����
				enemyWeapons.clear();		// �� ���� ���� ����Ʈ �ʱ�ȭ

				Image bossBackgroundImage = new ImageIcon("src/pic/����.png").getImage();				// ���ȭ�� ���������� �����ϱ� ���� ��ü ����
				draw.drawImage(bossBackgroundImage, 0, 0, panMain.getWidth(), panMain.getHeight(), this);	// ��ü�� ������ġ, ũ�� ����
			}
			
			// ������ �̹��� ����	
			if(isAttacking == false) {	// ���� ���� �ƴϸ�, �̵� �̹��� maWalk �׷���
				MaIcon mi = new MaIcon("src/pic/maWalk.PNG",playerX, playerY, SIZE, SIZE);	
				Graphics2D g2d = (Graphics2D) draw;
				mi.draw(draw);
			}
			else {						// ���� ���̸�, ���ݽ� �̹��� maAttack �׷���
				MaIcon mi = new MaIcon("src/pic/maAttack.PNG",playerX,playerY,SIZE,SIZE);	
				Graphics2D g2d = (Graphics2D) draw;
				mi.draw(draw);
			}

			// �������� ���� �̹��� ����
			if(isShooting) {							// ������ ���� �� ������ �ָ�, �߻� ���� true�� �� �̹��� �߻�
				for (MaWeapon bullet : bullets)
					bullet.draw(draw);
			}

			// ��, �� ���� �̹��� ����
			for(MaIcon enemy : enemyList) {				// �� �̹��� �׷���.
				enemy.draw(draw);
			}
			for(EnemyWeapon weapon : enemyWeapons) { 	// �߱� ��Ʈ �׷���(������ > ���� ���ƿ�)
				Graphics2D g2d = (Graphics2D) draw;
				weapon.draw(draw);
			}

			// ���� ��þ �̹��� ����
			if(count >= 20) {				// ���� ���� ����, �� 20�� ü�� �޼� ��
				isBoss = true;
				bs.performAction();			// ���� ���� ���� (Boss Ŭ������ performAction ����)
				bs.draw(draw); 				// ���� �̹��� �׸��� (Boss Ŭ������ draw ����)
			}

			// ���� ���� ����
			if(bs.getHP() <= 0) {			// ���� �¸�, ���� ����� 0 ������ ��
				if(isBoss == true)
					backgroundMusic.stop();
				isWin = true;				// �¸� ���� Ȱ��ȭ��
				frame.add(panWin);			// �¸� �г� ������
				if(isWin == true) {
					panGame.setVisible(false);	// ���� �г� ����
					panWin.setVisible(true);	// �¸� �г� ������
				}
			}
			if(maLife <= 0) {				// ���� �й�, ������ ����� 0 ������ ��
				if(isBoss == true)
					backgroundMusic.stop();
				isFail = true;				// �й� ���� Ȱ��ȭ
				frame.add(panFail);			// �й� �г� ������
				if(isFail == true) {
					panGame.setVisible(false);	// ���� �г� ����
					panFail.setVisible(true);	// �й� �г� ������
				}
			}

			panGame.requestFocusInWindow();	// �гο� ��Ŀ�� ����
			panGame.repaint();
		}
	}
}