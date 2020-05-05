/**
 * @(#)BudgetLegends.java
 *
 *
 * @author 
 * @version 1.00 2020/4/7
 */



    
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*; 
import java.io.*; 
import javax.imageio.*; 
import java.util.*;
import java.awt.MouseInfo;

public class BudgetLegends extends JFrame{
	javax.swing.Timer myTimer; 
	GamePanel game;
		
    public BudgetLegends() {
		super("Move the Box");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280,768);

		myTimer = new javax.swing.Timer(10, new TickListener());	 // trigger every 100 ms
		myTimer.start();

		game = new GamePanel();
		add(game);
		setResizable(false);
		setVisible(true);
    }
    
	class TickListener implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			if(game!= null ){
				game.update();
				game.repaint();
			}			
		}
	}
	
    public static void main(String[] arguments) {
		BudgetLegends frame = new BudgetLegends();		
    }
}

class GamePanel extends JPanel {
	private Player p1;
	private Player p2;
	private int screen;
	private int mousex,mousey;
	public boolean ready=false;
	private boolean []keys;
	private Point mouse;
	private Rectangle gamerect,readyrect,instructionrect,creditrect;
	public static final int MENU=1, SELECT=2, GAME=3, INSTRUCTIONS=4, CREDIT=5;
	private Image buttonUp, buttonDown; 
	public GamePanel(){
		keys = new boolean[KeyEvent.KEY_LAST+1];
		screen = GAME;
		buttonUp = new ImageIcon("buttons/button1_up.png").getImage();
		buttonDown = new ImageIcon("buttons/button1_down.png").getImage();
		p1 = new Player(380,400,"scorpion",9,4,1);//second last integer is spell,last integer is player number (player 1)
		p2 = new Player(380,400,"scorpion",9,2,2);//(player 2)
		gamerect = new Rectangle(400,300,100,50);
		readyrect = new Rectangle(500,500,100,100);
		//setPreferredSize( new Dimension(800, 600));
		addKeyListener(new moveListener());
		addMouseListener(new clickListener());
		setSize(1280,768);
		//i will make character list 
		// will make spell list
		// so that i can use integer instead of string
	}
	
    public void addNotify() {
        super.addNotify();
        requestFocus();
        ready = true;
    }
	public void update(){
		if (screen == GAME){
			move();
		}
		mouse = MouseInfo.getPointerInfo().getLocation();
		//Point offset = getLocationOnScreen();
		//mouse.translate(-offset.x, -offset.y);
		
	}
    public void move() {
		if(keys[KeyEvent.VK_RIGHT] ){//p1 movement
			p1.move(5,0);
		}
		if(keys[KeyEvent.VK_LEFT] ){
			p1.move(-5,0);
		}
		if(keys[KeyEvent.VK_UP] ){
			p1.move(0,-5);
		}
		if(keys[KeyEvent.VK_DOWN] ){
			p1.move(0,5);
		}
		if(keys[KeyEvent.VK_NUMPAD1] ){//basic attack
			p1.attack();
		}
		if(keys[KeyEvent.VK_NUMPAD2] ){//skill
			p1.skill();
		}
		if(keys[KeyEvent.VK_NUMPAD3] ){//common spell
			p1.spell();
		}
		if(keys[KeyEvent.VK_D] ){//p2 movement
			p2.move(5,0);
		}
		if(keys[KeyEvent.VK_A] ){
			p2.move(-5,0);
		}
		if(keys[KeyEvent.VK_W] ){
			p2.move(0,-5);
		}
		if(keys[KeyEvent.VK_S] ){
			p2.move(0,5);
		}
		if(keys[KeyEvent.VK_C] ){//basic attack
			p2.attack();
		}
		if(keys[KeyEvent.VK_V] ){//skill
			p2.skill();
		}
		if(keys[KeyEvent.VK_B] ){//common spell
			p2.spell();
		}
		p1.cooldown();
		p2.cooldown();
		p1.checkhit(p2);
		p2.checkhit(p1);
		

    }
    class clickListener implements MouseListener{
    	public void mousePressed(MouseEvent e){
    		mousex = e.getX();
    		mousey = e.getY();
    		if(screen == MENU){
    			
    			
				if(instructionrect.contains(mouse)){
					System.out.println("1234");
					//screen = INSTRUCTIONS;	
				}
				/*
				if(gamerect.contains(mouse)){
					screen = SELECT;
					System.out.println("23");
				}
				*/
			}
			if (screen == SELECT){
				if (readyrect.contains(mouse)){
					screen = GAME;
				}
			}
			if (screen == INSTRUCTIONS){
				//go back to menu
			}
			if (screen == CREDIT){
				//go back to menu
			}
		}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
		public void mouseClicked(MouseEvent e){}
    }
    class moveListener implements KeyListener{
	    public void keyTyped(KeyEvent e) {}
	
	    public void keyPressed(KeyEvent e) {
	        keys[e.getKeyCode()] = true;
	    }
	    
	    public void keyReleased(KeyEvent e) {
	        keys[e.getKeyCode()] = false;
	    }
    }    
    	
    public void imageInRect(Graphics g, Image img, Rectangle area){
		g.drawImage(img, area.x, area.y, area.width, area.height, null);
    }
    public void drawMenu(Graphics g){
    	/*
    	g.setColor(new Color(0xB1C4DF));  
		g.fillRect(0,0,800,600);
		if(instructionrect.contains(mouse)){
			imageInRect(g, buttonUp, instructionrect);
		}
		else{
			imageInRect(g, buttonDown, instructionrect);			
		}
		if(gamerect.contains(mouse)){
			imageInRect(g, buttonUp, gamerect);
		}
		else{
			imageInRect(g, buttonDown, gamerect);
		}*/
    	
    	g.setColor(new Color(50,200,222));  
        g.fillRect(0,0,getWidth(),getHeight());
        g.setColor(new Color(50,200,50));
        g.drawRect(gamerect.x,gamerect.y,gamerect.width,gamerect.height);
    }
    
    public void drawGame(Graphics g){
    	g.setColor(new Color(222,255,222));  
        g.fillRect(0,0,getWidth(),getHeight());  
		p1.draw(g);
		p2.draw(g);
    }
    
    public void drawSelect(Graphics g){
    	
    }
    
    public void drawInstructions(Graphics g){
		g.setColor(new Color(200,200,200));
		g.fillRect(0,0,800,600);
	}
	
    public void paint(Graphics g){
    	if(screen == MENU){
    		drawMenu(g);
    	}
    	else if(screen == SELECT){
    		drawSelect(g);
    	}
    	else if(screen == GAME){
    		drawGame(g);
    	}
    	else if(screen == INSTRUCTIONS){
    		drawInstructions(g);
    	}
		
    }
    
}

class Player{
	private int x,y;
	private int player;
	private int extraspeed;
	private Image[]pics;
	private int dir, frame, delay, move, newmove;
	private int maxrapid,rapid;
	private int basicdamage;
	private int damage;
	private int maxskillcool;
	private int skillcool;
	private int spell;
	private int healcool;
	private int speedcool;//speed spell cooldown
	private int flashcool;//flash spell cooldown
	private String name;
	private int health,maxhealth;
	private Rectangle playerrect;
	private Rectangle bullrect;
	private Energybolt energyb;
	private Teleport tp;
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	public static final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3, WAIT = 5;
		
	public Player(int x, int y, String n, int num, int s, int p){
		this.x=x;
		this.y=y;
		player = p;
		extraspeed = 0;
		dir = RIGHT;
		frame = 0;
		delay = 0;
		name = n;
		spell = s;
		if (n == "scorpion"){
			maxskillcool = 300;
			skillcool = 300;
			maxhealth = 100;
			health = 100;//each character will have diff maxhealth
			rapid = 18;//change to minimum rapid of all characters
			maxrapid = 24;
			basicdamage = 10;
			damage = 10;
		}
		healcool = 2000;
		speedcool = 1500;
		flashcool = 3000;
		
		pics = new Image[num];		
		for(int i = 0; i<num; i++){
			pics[i] = new ImageIcon(name+"/"+name+i+".png").getImage();
		}
	}
	public void setHealth(int h){
		health = h;
	}
	public int getHealth(){
		return health;
	}
	public int getMaxhealth(){
		return maxhealth;
	}
	public ArrayList<Bullet> getBullets(){
		return bullets;
	}
	public void setBullets(ArrayList<Bullet> blist){
		bullets = blist;
	}
	public String getName(){
		return name;
	}
	
	public void move(int dx,int dy){
		x += dx;
		y += dy;
		if (speedcool>300){
			extraspeed = 0;
		}
		if(dx>0){
			x += extraspeed;
			dir = RIGHT;
		}
		else if(dx<0){
			x -= extraspeed;
			dir = LEFT;
		}
		
		else if(dy>0){
			y += extraspeed;
			dir = DOWN;
		}
		else if (dy<0){
			y -= extraspeed;
			dir = UP;
		}
		delay += 1;
		if(delay % WAIT == 0){
			frame = (frame + 1) % pics.length;
		}
	}

	public void cooldown(){
		if (rapid<maxrapid){//cooldown for basic attack
			rapid+=1;
		}
		if (skillcool<maxskillcool){//cooldown for skill
			skillcool+=1;
		}
		if (healcool<2000){//cooldown for spells
			healcool+=1;
		}
		if (speedcool<1500){
			speedcool+=1;
		}
		if (flashcool<3000){
			flashcool+=1;
		}
	}
	
	public void attack(){//basic attack
		if (name == "scorpion"){
			if (rapid == maxrapid){
				rapid = 0;
				bullets.add(new Bullet(x,y,10,dir));
			}
			if (skillcool>200){
				damage = basicdamage;
			}
		}
		else if (name == "B"){
			maxrapid = 18;
		}
		else if (name == "C"){
			if (skillcool>200){
				damage = basicdamage;
			}
			maxrapid = 18;
		}
		else if (name == "D"){
			maxrapid = 18;
		}
	}
	
	public void skill(){
		if (name == "scorpion"){
			if (skillcool == maxskillcool){
				if (dir == LEFT){
					energyb = new Energybolt(x-5,y+5,dir);
				}
				else if (dir == RIGHT){
					energyb = new Energybolt(x+15,y+5,dir);
				}
				else if (dir == UP){
					energyb = new Energybolt(x+5,y-5,dir);
				}
				else if (dir == DOWN){
					energyb = new Energybolt(x+5,y+20,dir);
				}
				skillcool = 0;
			}
		}
		else if (name == "B"){
			if (skillcool == maxskillcool){
				if (tp == null){//set position
					tp = new Teleport(x,y);
					skillcool = maxskillcool-40;
				}
				else{//use teleport skill
					x = tp.getx();
					y = tp.gety();
					tp = null;
					skillcool = 0;
				}
			}
		}
		else if (name == "C"){
			if (skillcool == maxskillcool){
				damage = basicdamage+15;
				skillcool = 0;
			}		
		}
		else if (name == "D"){
			
		}
	}
	
	public void spell(){
		if (spell == 1){//heal 30
			if (healcool == 2000){
				if (health+30<=maxhealth){
					health+=30;
				}
				else if (health+30>maxhealth){
					health = maxhealth;
				}
				healcool = 0;
			}
		}
		else if (spell == 2){//speed up
			if (speedcool ==1500){
				extraspeed = 1;
				speedcool = 0;
			}
			
		}
		else if (spell == 3){//
			
		}
		else if (spell == 4){//flash
			if (flashcool==3000){
				if (dir == LEFT){
					x -= 200;
				}
				else if(dir == RIGHT){
					x += 200;
				}
				else if(dir == UP){
					y -= 200;
				
				}
				else if(dir == DOWN){
					y += 200;
				}
				flashcool = 0;
			}
		}
	}
	
	public void checkhit(Player enemy){
		ArrayList<Bullet> blist = enemy.getBullets();
		String enemycha = enemy.getName();//enemy character
		for (Bullet b: blist){
			int p = blist.indexOf(b);
			if (enemycha == "scorpion"){
				bullrect = new Rectangle(b.getbullx(),b.getbully(),5,5);
				
				if (name == "scorpion"){
					playerrect = new Rectangle(x,y,50,50);
					if (bullrect.intersects(playerrect)){
						//System.out.println(blist.indexOf(b));
						//System.out.println(p);
						//blist.remove(b);
			
						enemy.setBullets(blist);
						enemy.setHealth(enemy.getHealth()-damage);
					}
					//System.out.println(enemy.getHealth());
				}
				
			}
			else if (enemycha == "B"){
				//bullrect = Rectangle()
			}
			else if (enemycha == "C"){
				//bullrect = Rectangle()
			}
			else if (enemycha == "D"){
				//bullrect = Rectangle()
			}
				
		}
	}
	
	public void movebullets(){//not only bullets ,skills..etc
		for (Bullet b: bullets){
			if (b.getbulld() == 0){//bullet moves left
				b.setbullx(b.getbullx()-b.getbulls());
			}
			else if (b.getbulld() == 1){//bullet moves right
				b.setbullx(b.getbullx()+b.getbulls());
			}
			else if (b.getbulld() == 2){//bullet moves up
				b.setbully(b.getbully()-b.getbulls());
			}
			else if (b.getbulld() == 3){//bullet moves down
				b.setbully(b.getbully()+b.getbulls());
			}
			if (b.getbullx()<0){
				bullets.remove(b);
			}
		}
		if (energyb != null){
			if (energyb.getebd() == 0){//left
				energyb.setebx(energyb.getebx()-15);
			}
			else if (energyb.getebd() == 1){//right
				energyb.setebx(energyb.getebx()+15);
			}
			else if (energyb.getebd() == 2){//up
				energyb.seteby(energyb.geteby()-15);
			}
			else if (energyb.getebd() == 3){//down
				energyb.seteby(energyb.geteby()+15);
			}
		}
	}
	
	
	public void draw(Graphics g){
		if(dir == RIGHT){
			g.drawImage(pics[frame], x, y, null);
		}
		else{
			int w = pics[frame].getWidth(null);
			int h = pics[frame].getHeight(null);
			g.drawImage(pics[frame], x + w, y, -w, h, null);
		}
		g.setColor(new Color(0,0,0));
		for (Bullet b:bullets){
			g.fillRect(b.getbullx(),b.getbully(),5,5);
		}
		if (energyb != null){
			g.fillRect(energyb.getebx(),energyb.geteby(),15,5);
		}
		g.setColor(new Color(255,0,0));//red
		if (player == 1){
			g.fillRect(30,30,500,50);//health bar background
		}
		else if (player == 2){
			g.fillRect(740,30,500,50);
		}
		g.setColor(new Color(0,255,0));//green
		if (player == 1){
			//System.out.println(double(health/maxhealth));
			g.fillRect(30,30,(int)((double)(health/maxhealth)*500),50);//health bar
		}
		else if (player == 2){
			g.fillRect(740,30,(int)(health/(double)maxhealth)*500,50);
		}
		
		movebullets();
	}
	
	
	class Bullet{
		private int x,y,speed;
		private int dir;
		public static final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3;
		public Bullet(int X, int Y, int s,int d){
			x=X;
			y = Y;
			speed = s;
			dir = d;
		}
		public int getbullx(){
			return x;
		}
		public int getbully(){
			return y;
		}
		public int getbulls(){//return the bullet's speed
			return speed;
		}
		public int getbulld(){//return the bullet's direction
			return dir;
		}
		public void setbullx(int X){
			x = X;
		}
		public void setbully(int Y){
			y = Y;
		}
		
	}
	class Energybolt{
		private int x,y;
		private int dir;
		private int distance;
		public static final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3;
		public Energybolt(int X, int Y, int d){
			x = X;
			y = Y;
			dir = d;
		}
		public int getebd(){
			return dir;
		}
		public int getebx(){
			return x;
		}
		public int geteby(){
			return y;
		}
		public void setebx(int X){
			x = X;
		}
		public void seteby(int Y){
			y = Y;
		}
	}
	class Teleport{
		private int x,y;
		public Teleport(int X, int Y){
			x = X;
			y = Y;
		}
		public int getx(){
			return x;
		}
		public int gety(){
			return y;
		}
	}
}




/*
class Guy2{
	private int x2,y2;
	private Image[]pics;
	private int dir, frame, delay;
	public static final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3, WAIT = 5;
		
	public Guy2(int x, int y, String name, int n){
		this.x2=x;
		this.y2=y;
		dir = RIGHT;
		frame = 0;
		delay = 0;
		
		pics = new Image[n];		
		for(int i = 0; i<n; i++){
			pics[i] = new ImageIcon(name+"/"+name+i+".png").getImage();
		}
	}

	public void move(int dx,int dy){
		x2 += dx;
		y2 += dy;
		if(dx>0){
			dir = RIGHT;
		}
		else if(dx<0){
			dir = LEFT;
		}
		
		else if(dy>0){
			dir = DOWN;
		}
		else if (dy<0){
			dir = UP;
		}
		delay += 1;
		if(delay % WAIT == 0){
			frame = (frame + 1) % pics.length;
		}
	}

	public void draw(Graphics g){
		if(dir == RIGHT){
			g.drawImage(pics[frame], x2, y2, null);
		}
		else{
			int w = pics[frame].getWidth(null);
			int h = pics[frame].getHeight(null);
			g.drawImage(pics[frame], x2 + w, y2, -w, h, null);
		}
	}
}*/