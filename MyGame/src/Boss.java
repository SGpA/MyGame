import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.Timer;


// ������ ��þ�� ������, ���� ������ �����ϴ� Ŭ����
public class Boss {
	private int x;				// ������ �ʱ� x��ǥ
	private int y;				// ������ �ʱ� y��ǥ
	private int moveX;			// ���� ���� ���� �̵��� ��ġ ����
	private int moveY;			// ���� ���� ���� �̵��� ��ġ ����
	private int stack = 0;		// ���� ���� ���ϱ� ���� ����
	private int countBat = 0;	// ��Ʈ ���� ����
	private int hp = 50;		// ������ �ʱ� HP ����
	private int pattern;		// �̵����� �������� ������ ���� ����(1~2)
	private int bossPosX;		// ������ X ��ǥ
	private int bossPosY; 		// ������ Y ��ǥ
	private Timer takeRest;		 // ���� ���� ���� 5�ʰ� ���� �ο�
	private Timer takeMove;
	private Timer bossCharge;	// ���� ���� Ÿ�̸�
	private Timer throwBat;		// �ѷ��� ������ Ÿ�̸�
	private ImageIcon boss;
	private boolean isCollisionDetected = false;

	ArrayList <BaseballBat> weapon = new ArrayList<>();

	// ������ ���� ������ ó�� ��ǥ �� ���� ����
	public Boss () {

		this.x = 1000;										// ���� �ʱ� x��ǥ
		this.y = 350;										// ���� �ʱ� y��ǥ
		this.moveX = x;										// ���� �ʱ� x��ǥ ���纻(���� ���� ������ ����ġ�� ��ǥ)
		this.moveY = y;										// ���� �ʱ� y��ǥ ���纻(���� ���� ������ ����ġ�� ��ǥ)
		this.boss = new ImageIcon("src/pic/chen.png");			// ���� �̹��� ��� ����
		this.bossPosX = x; 									// ������ X ��ǥ ����
		this.bossPosY = y;									// ������ Y ��ǥ ����
		takeRest = new Timer(3000, new TrListener());		// 3�ʵ��� �޽�
		takeMove = new Timer(5, new MvListener());			// 5����ũ���ʸ��� �޽� �� ������ �׷���
		bossCharge = new Timer(5, new BcListener());		// 5����ũ���ʸ��� ������� �׷��� (���� 1)
		throwBat = new Timer(5, new TbListener());			// 5����ũ���ʸ��� �߱���Ʈ �׷��� (���� 2)
	}

	// Ÿ�̸� ����
	public class TrListener implements ActionListener {		// ���� 0: 3�� �޽� Ÿ�̸�
		@Override
		public void actionPerformed(ActionEvent e) {
			if(stack == 0) {
				bossCharge.start();	 // 3�� �޽� Ÿ�̸Ӱ� ������ ���� ���� ����
				takeMove.stop();	 // �޽� ������ ������ ���� Ÿ�̸� ����
			}
			if(stack == 1) {		// 3�� �޽� Ÿ�̸� ������ �߱���Ʈ ���� ����
				countBat = 0;		// ���� �� ��Ʈ ����Ʈ �ʱ�ȭ
				weapon.clear();		// ��Ʈ ����Ʈ �ʱ�ȭ
				throwBat.start();	// ��Ʈ ������ ���� ����
				takeMove.stop();	 // �޽� ������ ������ ���� Ÿ�̸� ����
			}
		}
	}

	public class MvListener implements ActionListener {		// ���� 0: 3�� �޽� Ÿ�ֿ̹� ������ ����
		@Override
		public void actionPerformed(ActionEvent e) {
			if(pattern == 1) 	// �̵����� 1�� ��� �Ʒ��� �̵�
				y -= 1;
			if(pattern == 2) 	// �̵����� 2�� ��� �Ʒ��� �̵�
				y += 1;

			bossPosX = x; 		// ������ ��ǥ�� ������Ʈ
			bossPosY = y;		// ������ ��ǥ�� ������Ʈ
		}
	}

	public class BcListener implements ActionListener {		// ���� 1: ���� ����, ������
		@Override
		public void actionPerformed(ActionEvent e) {
			x -= 15;				// x������ -15��ŭ ����
			bossPosX -= 15;			// ���� �߿��� ���� �� �ֵ��� ����

			if(x <= 0) {			// ���� �� ��ǥ 0 ���ϵǴ� ���� (���� �ε����� ����)
				bossCharge.stop();	// ���� ���� ����
				stack++;			// ���� == 1
				takeRest.start();	// ���� ���� Ÿ�̸� ����
				takeMove.start();	// �߰��� �޽� �� �����̱� Ÿ�̸� ����
				x = moveX;			// ����ġ �̵� ��ǥ ����(���纻)
				y = moveY;			// ����ġ �̵� ��ǥ ����(���纻)
				pattern = (int)(Math.random()*2+1);		// ���� �� ����ġ�ϰ� �̵��� ���� ����
			}
		}
	}

	public class TbListener implements ActionListener {			// ����2 : �߱���Ʈ ��ô, ������
		@Override
		public void actionPerformed(ActionEvent e) {
			// �߱���Ʈ ���� �� ī��Ʈ ����, ������ �� ��ġ���� �߻�
			BaseballBat bb = new BaseballBat(x, y + 100, 4, 	// �߱���Ʈ�� 4��ŭ�� �ӵ��� x�������� ���ư�
					(int)(Math.random()*11 - 5), 70, 70);		// �߱���Ʈ�� -5 ~ 6 ���� ������ y�������� ���ư�
			weapon.add(bb);		// 70 x 70 ũ�� ��Ʈ�� ������
			countBat++;			// ��Ʈ ���� ��
			isCollisionDetected = false;	// ���� ������ �ѹ��� �ִ� �� �ݺ� ���� false������ ��ȯ

			// �߱���Ʈ�� 10�� �����Ǿ����� �߱���Ʈ Ÿ�̸Ӹ� �����ϰ� ���� �������� �̵�
			if (countBat == 10) {			
				throwBat.stop();	// �߱���Ʈ ���� ����
				stack--;			// ���� == 0
				takeRest.start(); 	// 5�� �޽� Ÿ�̸� ����
				return;      
			}
		}
	} 

	// �޼���
	// ���� �������� ���� �������� �浹 ���� �޼��� (������)
	public boolean isTouched(int maX, int maY) {	

		if (!isCollisionDetected && maX + 50 < bossPosX + 270 && maX + 200 > bossPosX &&
				maY + 20 < bossPosY + 270 && maY + 190 > bossPosY) {	// �ǰ� ���� ����
			isCollisionDetected = true;		// true������ ������ ������ �ѹ��� �ް��� (true �ѹ� ��ȯ)
			return true;		 // �浹 �߻�
		}
		else
			return false;		 // �浹 ����
	}

	// �߱���Ʈ ���ݰ� �������� �浹 ���� �޼��� (������)
	public boolean isBatTouched(int maX, int maY) {	
		for(int i=0;i<weapon.size();i++) {
			// �߱���Ʈ ��ü���� ��ġ �� ���� �浹 ���� ����
			BaseballBat bbb = weapon.get(i);
			if(maX + 50 < bbb.getX() + bbb.getWidth() && maX + 200 > bbb.getX() 
					&& maY + 20 < bbb.getY() + bbb.getHeight() && maY + 190 > bbb.getY()) {
				weapon.remove(i);
				i--;
				return true;
			}
		}
		return false;	// �浹���� �ʾҴٸ� false ��ȯ
	}

	// �ָ԰� ������ �浹 üũ �޼��� (������)
	public boolean isColliding(int bulletX, int bulletY, int bulletSize) {		
		if (bulletX + bulletSize > bossPosX && bulletX < bossPosX + 270 &&
				bulletY + bulletSize > bossPosY && bulletY < bossPosY + 270)	// �ָ� ũ�� ��� ���� �ָ��� x, y, ũ�� ������
			return true;	// �浹 �߻�
		else
			return false;	// �浹 ����
	}

	// ������ HP�� ��ȯ�ϴ� �޼��� (GameSetting���� ȣ��)
	public int getHP() { 				
		return hp;
	}

	// ������ HP�� ���ҽ�Ű�� �޼��� (GameSetting���� ȣ��)
	public void decreaseHP() {			
		hp--;
	}

	// �������� ���� (GameSetting���� ȣ��)
	public void performAction() {		
		takeRest.start();				// �޽� Ÿ�̸ӷ� ����
	}

	// ���� �̹���, �߱���Ʈ �̹��� �׷���
	public void draw (Graphics g) {		
		g.drawImage(boss.getImage(), x, y, 270, 270, null);

		for(BaseballBat wp : weapon) {
			wp.move();
			wp.draw(g); // �ָ� �׸���
		}
	}
}