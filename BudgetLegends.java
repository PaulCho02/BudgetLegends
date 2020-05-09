/**
 * @(#)BudgetLegends.java
 *
 *
 * @author 
 * @version 1.00 2020/5/1
 */

/*---------need to do------------
 menu
 setting
 credit screen
 instruction screen
 ---------------------------(in game)
 collision in each maps
 make energy bolt distance (when it hit, give damage and maybe slowdown
 spell 'c'(cleanser or all damage x2)
 skill for character 'c' rn it is just 
 damage boost -> damage boost and armor maybe
 
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
		setSize(1300,900);

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
	private Rectangle [] charectarray;
	private Rectangle [] spellrectarray;
	private String [] chaarray;
	private String p1cha,p2cha;
	private int p1spell,p2spell;
	private Image [] chaimage;
	private Image [] spellimage;
	public static final int MENU=1, SELECT=2, GAME=3, INSTRUCTIONS=4, CREDIT=5;
	private Image buttonUp, buttonDown; 
	public GamePanel(){
		keys = new boolean[KeyEvent.KEY_LAST+1];
		screen = SELECT;
		buttonUp = new ImageIcon("buttons/button1_up.png").getImage();
		buttonDown = new ImageIcon("buttons/button1_down.png").getImage();
		p1cha = "scorpion";
		p2cha = "scorpion";
		p1spell = 1;
		p2spell = 1;
		//p1 = new Player(380,400,"scorpion",9,4,1);//second last integer is spell,last integer is player number (player 1)
		//p2 = new Player(700,400,"scorpion",9,1,2);//(player 2)
		instructionrect = new Rectangle (200,100,100,50);
		gamerect = new Rectangle(400,300,100,50);
		readyrect = new Rectangle(460,30,350,90);
		chaarray = new String[] {"scorpion","B","C","D"};
		//setPreferredSize( new Dimension(800, 600));
		addKeyListener(new moveListener());
		addMouseListener(new clickListener());
		setPreferredSize( new Dimension(1280, 768));
		charectarray = new Rectangle [4]; 
		spellrectarray = new Rectangle [4];
		for (int i=380,n = 0; n<4; i+=140,n++){//make character rectangle
			charectarray[n] = new Rectangle(i,250,120,120);
		}
		for (int i=500,n = 0; n<4; i+=75,n++){//make spell rectangle
			spellrectarray[n] =(new Rectangle(i,430,50,50));
		}
		
		
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
		if (screen != GAME){
			mouse = MouseInfo.getPointerInfo().getLocation();
			//Point offset = getLocationOnScreen();
			//mouse.translate(-offset.x, -offset.y);
		}
		
	}
    public void move() {
		if(keys[KeyEvent.VK_RIGHT] ){//p1 movement
			p2.move(5,0);
		}
		if(keys[KeyEvent.VK_LEFT] ){
			p2.move(-5,0);
		}
		if(keys[KeyEvent.VK_UP] ){
			p2.move(0,-5);
		}
		if(keys[KeyEvent.VK_DOWN] ){
			p2.move(0,5);
		}
		if(keys[KeyEvent.VK_NUMPAD1] ){//basic attack
			p2.attack();
		}
		if(keys[KeyEvent.VK_NUMPAD2] ){//skill
			p2.skill();
		}
		if(keys[KeyEvent.VK_NUMPAD3] ){//common spell
			p2.spell();
		}
		if(keys[KeyEvent.VK_D] ){//p2 movement
			p1.move(5,0);
		}
		if(keys[KeyEvent.VK_A] ){
			p1.move(-5,0);
		}
		if(keys[KeyEvent.VK_W] ){
			p1.move(0,-5);
		}
		if(keys[KeyEvent.VK_S] ){
			p1.move(0,5);
		}
		if(keys[KeyEvent.VK_C] ){//basic attack
			p1.attack();
		}
		if(keys[KeyEvent.VK_V] ){//skill
			p1.skill();
		}
		if(keys[KeyEvent.VK_B] ){//common spell
			p1.spell();
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
					screen = SELECT;	
				}
				
				if(gamerect.contains(mouse)){
					screen = SELECT;
				}
				
			}
			if (screen == SELECT){//set p1,p2 and map here
				if (readyrect.contains(mouse)){
					p1 = new Player(380,400,p1cha,9,p1spell,1);
					p2 = new Player(700,400,p2cha,9,p2spell,2);
					screen = GAME;
				}
				//using if statement to defined user did right click or left click(left click = p1,right click = p2)
				if (SwingUtilities.isLeftMouseButton(e)){
					for (int i = 0; i<4; i++){
						if (charectarray[i].contains(mouse)){//choose character(click the character rect)
							p1cha = chaarray[i];
						}
						if (spellrectarray[i].contains(mouse)){//choose spell
							p1spell = i+1;//spell integer is start with 1 not 0
							System.out.println(p1spell);
						}
					}
				}
				if (SwingUtilities.isRightMouseButton(e)){
					System.out.println("right click");
					for (int i = 0; i<4; i++){
						if (charectarray[i].contains(mouse)){//choose character(click the character rect)
							p2cha = chaarray[i];
						}
						if (spellrectarray[i].contains(mouse)){//choose spell
							
							p2spell = i+1;//spell integer is start with 1 not 0
							System.out.println(p2spell);
						}
					}
				}
			}
			if (screen == INSTRUCTIONS){//blitz screen(image)
				//go back to menu
			}
			if (screen == CREDIT){//blitz screen(image)
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
    	
    	g.setColor(new Color(0xB1C4DF));  
		g.fillRect(0,0,1100,600);
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
		}
    	/*
    	g.setColor(new Color(50,200,222));  
        g.fillRect(0,0,getWidth(),getHeight());*/
        g.setColor(new Color(50,200,50));
        g.drawRect(instructionrect.x,instructionrect.y,instructionrect.width,instructionrect.height);
        g.drawRect(gamerect.x,gamerect.y,gamerect.width,gamerect.height);
    }
    
    public void drawGame(Graphics g){
    	g.setColor(new Color(222,255,222));  
        g.fillRect(0,0,getWidth(),getHeight());  
		p1.draw(g);
		p2.draw(g);
    }
    
    public void drawSelect(Graphics g){
    	g.setColor(new Color(100,100,100));
    	g.fillRect(0,0,1280,900);
    	g.setColor(new Color(200,200,255));
    	for (int i=0; i<4;i++){
    		g.fillRect(charectarray[i].x,charectarray[i].y,charectarray[i].width,charectarray[i].height);
    		g.fillRect(spellrectarray[i].x,spellrectarray[i].y,spellrectarray[i].width,spellrectarray[i].height);
    		//imageInRect(g,chaimage[i],charectarray[i]);
    		//imageInRect(g,spellimage[i],spellrectarray[i]);
    	}
    	g.fillRect(readyrect.x,readyrect.y,readyrect.width,readyrect.height);
    	
    	//need code for map(arrow keys)
    	
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
	private Image [][] pics;
	private int dir, delay, move, newmove;
	private double frame;
	private int maxrapid,rapid;
	private int basicdamage;
	private int damage;
	private int maxskillcool;
	private int skillcool;
	private boolean stunned;
	private int stuncool;
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
	private Stun stun;
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	public static final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3, SHOOTL = 4, SHOOTR = 5,SHOOTU = 6,SHOOTD = 7, SKILLL = 8,SKILLR = 9,SKILLU = 10,SKILLD = 11, WAIT = 5;
		
	public Player(int x, int y, String n, int num, int s, int p){
		this.x=x;
		this.y=y;
		player = p;
		extraspeed = 0;
		dir = RIGHT;
		newmove = 0;
		frame = 1;
		delay = 0;
		name = n;
		spell = s;
		stunned = false;
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
		healcool = 1500;
		speedcool = 1200;
		flashcool = 2000;
		
		pics = new Image[12][10];
		if (name == "scorpion"){
			for (int i = 1; i<10;i++){ 
				pics[0][i] = new ImageIcon("yellow"+"/"+"yellowwalkL"+i+".png").getImage();
				pics[1][i] = new ImageIcon("yellow"+"/"+"yellowwalkR"+i+".png").getImage();
				pics[2][i] = new ImageIcon("yellow"+"/"+"yellowwalkUp"+i+".png").getImage();
				pics[3][i] = new ImageIcon("yellow"+"/"+"yellowwalkDown"+i+".png").getImage();
			}
			/*
			for (int i = 1; i<4;i++){
				pics[4][i] = new ImageIcon("yellow"+"/"+"yellowshootL"+i+".png").getImage();
				pics[5][i] = new ImageIcon("yellow"+"/"+"yellowshootR"+i+".png").getImage();
				pics[6][i] = new ImageIcon("yellow"+"/"+"yellowshootUp"+i+".png").getImage();
				pics[7][i] = new ImageIcon("yellow"+"/"+"yellowshootDown"+i+".png").getImage();
			}
			for (int i = 1; i<4;i++){
				pics[8][i] = new ImageIcon("yellow"+"/"+"yellowslashL"+i+".png").getImage();
				pics[9][i] = new ImageIcon("yellow"+"/"+"yellowslashR"+i+".png").getImage();
				pics[10][i] = new ImageIcon("yellow"+"/"+"yellowslashUp"+i+".png").getImage();
				pics[11][i] = new ImageIcon("yellow"+"/"+"yellowslashDown"+i+".png").getImage();
			*/
				
		}
		
		/*
		ArrayList<String> movelist = new ArrayList<String>();
		movelist.add("walk");
		movelist.add("shoot");
		movelist.add("skill");
		ArrayList<String> dirlist = new ArrayList<String>();
		movelist.add("R");
		movelist.add("L");
		movelist.add("Up");
		movelist.add("Down");
		for(int i = 0; i<num; i++){
			for(String move:movelist){
					for (int k = 0;k<num;k++){
						pics[i][k] = new ImageIcon(name+"/"+name+move+direction+i+".png").getImage();
					}
				}
			}
		}
		*/
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
	public Energybolt getEb(){
		return energyb;
	}
	public void setEb(Energybolt e){
		energyb = null;
	}
	public Stun getStun(){
		return stun;
	}
	public void setStun(){
		stun = null;
	}
	public int getDamage(){
		return damage;
	}
	public void move(int dx,int dy){
		if (speedcool>300){
			extraspeed = 0;
		}
		if (stunned == false){//////////////////////////////
		if(dx>0 && x<1220){
			x += dx;
			x += extraspeed;
			newmove = RIGHT;
			dir = RIGHT;
		}
		else if(dx<0 && x>5){
			x += dx;
			x -= extraspeed;
			newmove = LEFT;
			dir = LEFT;
		}
		
		else if(dy>0 && y<730){
			y += dy;
			y += extraspeed;
			newmove = DOWN;
			dir = DOWN;
		}
		else if (dy<0 && y>5){
			y += dy;
			y -= extraspeed;
			newmove = UP;
			dir = UP;
		}
		delay += 1;
		if(delay % WAIT == 0){
			frame = (frame + 1) % pics[move].length;
			if (frame == 0){
				frame = 1;
			}
		}
		if (newmove != move){
			move = newmove;
			frame = 1;
		}
		}/////////////////////////////////
		
	}

	public void cooldown(){
		if (rapid<maxrapid){//cooldown for basic attack
			rapid+=1;
		}
		if (skillcool<maxskillcool){//cooldown for skill
			skillcool+=1;
		}
		if (healcool<1500){//cooldown for spells
			healcool+=1;
		}
		if (speedcool<1200){
			speedcool+=1;
		}
		if (flashcool<2000){
			flashcool+=1;
		}
		if (stunned == true){
			stuncool +=1;
			if (stuncool == 100){
				stuncool = 0;
				stunned = false;
			}
		}
		
	}
	
	public void attack(){//basic attack
		if (name == "scorpion"){
			if (rapid == maxrapid){
				rapid = 0;
				bullets.add(new Bullet(x,y,10,dir));
				if (dir == LEFT){
					newmove = SHOOTL;
				}
				else if (dir == RIGHT){
					newmove = SHOOTR;
				}
				else if (dir == UP){
					newmove = SHOOTU;
				}
				else if (dir == DOWN){
					newmove = SHOOTD;
				}
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
		if (stunned == false){
			if (name == "scorpion"){//energy bolt
				if (skillcool == maxskillcool){
					if (dir == LEFT){
						newmove = SKILLL;
						stun = new Stun(x-5,y+5,dir);
					}
					else if (dir == RIGHT){
						newmove = SKILLR;
						stun = new Stun(x+15,y+5,dir);
					}
					else if (dir == UP){
						newmove = SKILLU;
						stun = new Stun(x+5,y-5,dir);
					}
					else if (dir == DOWN){
						newmove = SKILLD;
						stun = new Stun(x+5,y+20,dir);
					}
					skillcool = 0;
				}
				/*
				if (skillcool == maxskillcool){
					if (dir == LEFT){
						newmove = SKILLL;
						energyb = new Energybolt(x-5,y+5,dir);
					}
					else if (dir == RIGHT){
						newmove = SKILLR;
						energyb = new Energybolt(x+15,y+5,dir);
					}
					else if (dir == UP){
						newmove = SKILLU;
						energyb = new Energybolt(x+5,y-5,dir);
					}
					else if (dir == DOWN){
						newmove = SKILLD;
						energyb = new Energybolt(x+5,y+20,dir);
					}
					skillcool = 0;
				}*/
			}
			else if (name == "B"){//teleport
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
			else if (name == "C"){//damage boost
				if (skillcool == maxskillcool){
					damage = basicdamage+15;
					skillcool = 0;
				}		
			}
			else if (name == "D"){// stun skill
				if (skillcool == maxskillcool){
					if (dir == LEFT){
						newmove = SKILLL;
						stun = new Stun(x-5,y+5,dir);
					}
					else if (dir == RIGHT){
						newmove = SKILLR;
						stun = new Stun(x+15,y+5,dir);
					}
					else if (dir == UP){
						newmove = SKILLU;
						stun = new Stun(x+5,y-5,dir);
					}
					else if (dir == DOWN){
						newmove = SKILLD;
						stun = new Stun(x+5,y+20,dir);
					}
					skillcool = 0;
				}
			}
		}
	}
	
	public void spell(){//////////////////
		//player only can use heal spell even if player is stunned
		if (spell == 1){//heal 30         
			if (healcool == 1500){
				if (health+30<=maxhealth){
					health+=30;
				}
				else if (health+30>maxhealth){
					health = maxhealth;
				}
				healcool = 0;
			}
		}
		if (stunned == false){/////////////////
			if (spell == 2){//speed up
				if (speedcool == 1200){
					extraspeed = 1;
					speedcool = 0;
				}
			}
			else if (spell == 3){//
			
			}
			else if (spell == 4){//flash
				if (flashcool == 2000){
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
		}//////////////////////////
	}
	
	public void checkhit(Player enemy){
		ArrayList<Bullet> blist = enemy.getBullets();
		String enemycha = enemy.getName();//enemy character
		for (Bullet b: blist){//check basic attack
			if (b != null){
				int p = blist.indexOf(b);
				if (enemycha == "scorpion"){
					bullrect = new Rectangle(b.getbullx(),b.getbully(),5,5);
				
					if (name == "scorpion"){
						playerrect = new Rectangle(x,y,50,50);
						if (bullrect.intersects(playerrect)){//enemy basic attack
							blist.set(p,null);
							enemy.setBullets(blist);
							health = health-enemy.getDamage();
						}
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
		if (enemycha == "scorpion"){
			if (enemy.getStun() != null){
				Rectangle stunrect = new Rectangle(enemy.getStun().getx(),enemy.getStun().gety(),15,15);
				if (name == "scorpion"){
					playerrect = new Rectangle(x,y,50,50);
				}
				else if(name == "B"){
					playerrect = new Rectangle(x,y,50,50);
				}
				else if(name == "C"){
					playerrect = new Rectangle(x,y,50,50);
				}
				else if(name == "D"){
					playerrect = new Rectangle(x,y,50,50);
				}
				if (stunrect.intersects(playerrect)){
						health = health-10;
						stunned = true;
						enemy.setStun();
				}
			}
			/*
			if (enemy.getEb() != null){
				Rectangle ebrect = new Rectangle(enemy.getEb().getebx(),enemy.getEb().geteby(),10,10);
				if (name == "scorpion"){
					playerrect = new Rectangle(x,y,50,50);
				}
				else if(name == "B"){
					playerrect = new Rectangle(x,y,50,50);
				}
				else if(name == "C"){
					playerrect = new Rectangle(x,y,50,50);
				}
				else if(name == "D"){
					playerrect = new Rectangle(x,y,50,50);
				}
				if (ebrect.intersects(playerrect)){
						health = health-30;
						enemy.setEb(null);
				}
				
			}*/
		}
		else if (enemycha == "D"){
			if (enemy.getStun() != null){
				Rectangle stunrect = new Rectangle(enemy.getStun().getx(),enemy.getStun().gety(),15,15);
				if (name == "scorpion"){
					playerrect = new Rectangle(x,y,50,50);
				}
				else if(name == "B"){
					playerrect = new Rectangle(x,y,50,50);
				}
				else if(name == "C"){
					playerrect = new Rectangle(x,y,50,50);
				}
				else if(name == "D"){
					playerrect = new Rectangle(x,y,50,50);
				}
				if (stunrect.intersects(playerrect)){
						health = health-10;
						stunned = true;
						enemy.setStun();
				}
			}
		}							
	}
	
	public void movebullets(){//not only bullets ,skills..etc
		for (Bullet b: bullets){
			if(b!=null){///////////
				int p = bullets.indexOf(b);
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
				if (b.getbullx()<0 || b.getbullx()>1280 || b.getbully()<0 || b.getbully()>768){
					bullets.set(p,null);
				}
			}/////////////
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
		if (stun != null){
			if (stun.getd() == 0){//left
				stun.setx(stun.getx()-10);
			}
			else if (stun.getd() == 1){//right
				stun.setx(stun.getx()+10);
			}
			else if (stun.getd() == 2){//up
				stun.sety(stun.gety()-10);
			}
			else if (stun.getd() == 3){//down
				stun.sety(stun.gety()+10);
			}
		}
	}
	
	
	public void draw(Graphics g){
		g.drawImage(pics[newmove][(int)frame], x, y, null);//draw character

		g.setColor(new Color(0,0,0));//black
		for (Bullet b:bullets){//draw bullets
			if (b!=null){
				g.fillRect(b.getbullx(),b.getbully(),5,5);//need to change to image
			}
		}
		if (energyb != null){//draw energy bolt
			g.fillRect(energyb.getebx(),energyb.geteby(),10,10);//need to change to image
		}
		if (stun != null){//draw stunshot
			g.fillRect(stun.getx(),stun.gety(),15,15);//need to change to image
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
			g.fillRect(30,30,(int)((double)health/(double)maxhealth*500),50);//health bar
		}
		else if (player == 2){
			g.fillRect(740,30,(int)((double)health/(double)maxhealth*500),50);
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
		public void setx(int X){
			x = X;
		}
		public void sety(int Y){
			y = Y;
		}
	}
	class Stun{
		private int x,y,dir;
		public Stun(int X, int Y, int d){
			x = X;
			y = Y;
			dir = d;
		}
		public int getx(){
			return x;
		}
		public int gety(){
			return y;
		}
		public int getd(){
			return dir;
		}
		public void setx(int X){
			x = X;
		}
		public void sety(int Y){
			y = Y;
		}
	}
}


