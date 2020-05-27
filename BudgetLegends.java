/**
 * @(#)BudgetLegends.java
 *
 *
 * @author 
 * @version 1.00 2020/5/1
 */

/*---------need to do------------
 menu 40%	(buttons, background image and font) 
 setting 80% (buttons,font,background image and specific details such as position of rects)
 credit screen 0%  (credit image)
 instruction screen 0%  (instruction image)
 music and sound 0%
 end screen 5%
 ---------------------------(in game)
 map
 characters sprites(Jay)
 collision in each maps
 skill for character 'c' rn it is just 
 damage boost -> damage boost and armor maybe
 
*/
//do font today menu and select,work on instruction image
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
    class ClickStart implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent evt){
    		BudgetLegends game = new BudgetLegends();
    		setVisible(false);
    	}
    }
}

class GamePanel extends JPanel {
	private Player p1;//set player 1
	private Player p2;//set player 2
	private int screen;//set screen that is on right now
	private boolean []keys;//array of keys that user typed
	private Point mouse;//mouse location
	private int mx,my;
	private Rectangle gamerect,readyrect,instructionrect,creditrect;//rects for menu screen and select screen
	private Rectangle [] charectarray;//array that has rectangles to display characters in select page
	private Rectangle [] spellrectarray;//array that has rectangles to display spells in select page
	private Rectangle maprect,leftarrowrect,rightarrowrect;//rectangle that are using for select page
	private Rectangle p1charect;//rectangle that display what character p1 choose
	private Rectangle p2charect;//rectangle that display what character p2 choose
	private Rectangle p1spellrect;//rectangle that displayt what spell p1 choose
	private Rectangle p2spellrect;//rectangle that displayt what spell p2 choose
	private int p1cha,p2cha;//character number that players choose
	private int p1spell,p2spell;//spell number that players choose
	private int mappos;//map number that players will play
	private Image [] chaimage;//array that has all characters 
	private Image [] spellimage;//array taht has all spell images 
	private Image [] mapimage;//array that has all 4 maps
	public static final int MENU=1, SELECT=2, GAME=3, INSTRUCTIONS=4, CREDIT=5, WAIT=5, END=6;//defined screens as number
	private Image buttonUp, buttonDown,leftarrow,leftarrow2,rightarrow,rightarrow2;//images that are using in menu and select page
	private JLayeredPane layeredPane=new JLayeredPane();
	private Font font;
	
	public GamePanel(){
		keys = new boolean[KeyEvent.KEY_LAST+1];
		screen = WAIT;
		buttonUp = new ImageIcon("buttons/button1_up.png").getImage();//load images
		buttonDown = new ImageIcon("buttons/button1_down.png").getImage();
		leftarrow = new ImageIcon("arrows/leftarrow/png").getImage();
		leftarrow2 = new ImageIcon("arrows/leftarrow2/png").getImage();
		rightarrow = new ImageIcon("arrows/rightarrow/png").getImage();
		rightarrow2 = new ImageIcon("arrows/rightarrow2/png").getImage();
		p1cha = 0;//defined basic players and map info
		p2cha = 0;
		p1spell = 1;
		p2spell = 1;
		mappos = 0;
		chaimage = new Image[] {new ImageIcon("yellow/yellowwalkDown1.png").getImage(),new ImageIcon("yellow/yellowwalkL1.png").getImage(),
					new ImageIcon("yellow/yellowwalkR1.png").getImage(),new ImageIcon("yellow/yellowwalkUp1.png").getImage()};//need to change to each character image
		spellimage = new Image[]{new ImageIcon("spell/0.png").getImage(),new ImageIcon("spell/1.png").getImage(),
					new ImageIcon("spell/2.png").getImage(),new ImageIcon("spell/3.png").getImage()};//load spell images
		mapimage = new Image[] {new ImageIcon("maps/map1.png").getImage(),new ImageIcon("maps/map2.jpg").getImage(),
					new ImageIcon("maps/map3.jpg").getImage(),new ImageIcon("maps/map4.png").getImage()};//load map images
		
		instructionrect = new Rectangle (600,450,150,50);//make(defined) rectangles
		creditrect = new Rectangle (600,550,150,50);
		gamerect = new Rectangle(600,350,150,50);
		readyrect = new Rectangle(470,30,350,90);
		maprect = new Rectangle(400,240,500,250);
		leftarrowrect = new Rectangle(300,300,50,120);
		rightarrowrect = new Rectangle(1000,300,50,120);
		p1charect = new Rectangle(50,150,250,300);
		p2charect = new Rectangle(1000,150,250,300);
		p1spellrect = new Rectangle(150,600,100,100);
		p2spellrect = new Rectangle(1050,600,100,100);
		addKeyListener(new moveListener());//key and mouse listener
		addMouseListener(new clickListener());
		setPreferredSize( new Dimension(1280, 768));//defined size of the panel
		charectarray = new Rectangle [4];//make array
		spellrectarray = new Rectangle [4];
		
		for (int i=380,n = 0; n<4; i+=140,n++){//make character rectangles in array
			charectarray[n] = new Rectangle(i,550,120,120);
		}
		for (int i=500,n = 0; n<4; i+=75,n++){//make spell rectangles in array
			spellrectarray[n] =(new Rectangle(i,730,50,50));
		}
		
		font = new Font("Comic Sans MS",Font.PLAIN,96);
	}
	
    public void addNotify() {
        super.addNotify();
        requestFocus();
        screen = MENU;
    }
	public void update(){
		if (screen == GAME){//play game
			move();//call move method
		}
		if (screen != GAME){//mouse point isn't require for game but require for other screens
			mouse = MouseInfo.getPointerInfo().getLocation();//get mouse position
			//Point offset = getLocationOnScreen();
			//mouse.translate(-offset.x, -offset.y);
		}
		
	}
    public void move() {//method that player can move and use skills
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
		p1.cooldown();//call cooldown method for each player 
		p2.cooldown();
		p1.checkhit(p2);//call checkhit method to check if player get hit any skills or attacks
		p2.checkhit(p1);
		if (p1.getHealth()<=0){//check the game is over or not
			screen = END;
		}
		else if (p2.getHealth()<=0){
			screen = END;
		}
    }
    
    class clickListener implements MouseListener{
    	public void mousePressed(MouseEvent e){//mouse clicked
    		if (gamerect==null){
    			return;
    		}
    		if(screen == MENU){
    			if(gamerect.contains(mx,my)){//move to other screen
					screen = SELECT;
				}
				if(instructionrect.contains(mx,my)){
					screen = SELECT;	
				}
				if(creditrect.contains(mouse)){
					screen = CREDIT;
				}
			}
			if (screen == SELECT){//set p1,p2 and map here
				if (readyrect.contains(mouse)){//game start button
					p1 = new Player(380,400,p1cha,9,p1spell,1);//using player class to make p1 and p2
					p2 = new Player(700,400,p2cha,9,p2spell,2);
					screen = GAME;//game start
				}
				//using if statement to define user did right click or left click(left click = p1,right click = p2)
				if (SwingUtilities.isLeftMouseButton(e)){//left click
					if (leftarrowrect.contains(mouse)){//go to previous map
						if (mappos == 0){//if map is first map, go to last map
							mappos = 3;
						}
						else{
							mappos = Math.abs((mappos-1)%3);
						}
					}
					else if (rightarrowrect.contains(mouse)){//go to next map
						mappos = (mappos+1)%3;
					}
					for (int i = 0; i<4; i++){//go through rectangle array to check what character or spell did p1 choose
						if (charectarray[i].contains(mouse)){//choose character(click the character rect)
							p1cha = i;//set character for player 1
						}
						if (spellrectarray[i].contains(mouse)){//choose spell
							p1spell = i;//set spell for player 1
						}
					}
				}
				if (SwingUtilities.isRightMouseButton(e)){//right click
					for (int i = 0; i<4; i++){//go throught rectangle array to check what character or spell did p2 choose
						if (charectarray[i].contains(mouse)){//choose character(click the character rect)
							p2cha = i;//set character for player 2
						}
						if (spellrectarray[i].contains(mouse)){//choose spell
							p2spell = i;//set spell for player 2
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
		public void mouseMoved(MouseEvent evt){
			mx = evt.getX();
			my = evt.getY();
		}
		public void mouseDragged(MouseEvent evt){
			mx = evt.getX();
			my = evt.getY();		
		}
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
    	
    public void imageInRect(Graphics g, Image img, Rectangle area){//method to draw image in rectangle
		g.drawImage(img, area.x, area.y, area.width, area.height, null);
    }
    public void drawMenu(Graphics g){//method to draw menu screen
		/*
		 public MainMenu {
			//setSize(1300,900);
			
			ImageIcon background = new ImageIcon("menu.png");
			JLabel back = new JLabel(background);		
			back.setBounds(0, 0,background.getIconWidth(),background.getIconHeight());
			layeredPane.add(back,1);
			
			ImageIcon title = new ImageIcon("title.png");
			JButton tButton = new JButton(title);	
			tButton.addActionListener(new ClickStart());
			tButton.setBounds(300,600,title.getIconWidth(),title.getIconHeight());
			layeredPane.add(tButton,2);
			
			ImageIcon start = new ImageIcon("start.png");
			JButton sButton = new JButton(start);	
			sButton.addActionListener(new ClickStart());
			sButton.setBounds(300,400,start.getIconWidth(),start.getIconHeight());
			layeredPane.add(sButton,2);
			
			ImageIcon credits = new ImageIcon("credit.png");
			JButton cButton = new JButton(credits);	
			cButton.addActionListener(new ClickStart());
			cButton.setBounds(300,600,credits.getIconWidth(),credits.getIconHeight());
			layeredPane.add(cButton,2);
			
			ImageIcon instructions = new ImageIcon("instruction.png");
			JButton iButton = new JButton(instructions);	
			iButton.addActionListener(new ClickStart());
			iButton.setBounds(300,600,instructions.getIconWidth(),instructions.getIconHeight());
			layeredPane.add(iButton,2);
				
			setContentPane(layeredPane);        
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		}
		*/
    	
    	g.setColor(new Color(0xB1C4DF));  
		g.fillRect(0,0,1300,900);//background
		g.setColor(new Color(0x66586F));//complementary colour for buttons
		g.fillRect(600,350,150,50);//start
		g.fillRect(600,450,150,50);//instructions
		g.fillRect(600,550,150,50);//credits
		
		if(gamerect.contains(mouse)){
			g.setColor(new Color(0x9984A7));
			g.fillRect(600,350,150,50);
		}
		if(instructionrect.contains(mouse)){//draw button image and hovering 
			g.setColor(new Color(0x9984A7));
			g.fillRect(600,450,150,50);
		}
		if (creditrect.contains(mouse)){
			g.setColor(new Color(0x9984A7));
			g.fillRect(600,550,150,50);
		}
		
		g.setColor(new Color(255,255,255));
		g.setFont(font);
		g.drawString("Budget Legends",350,200);
		g.setFont(new Font("Comic Sans MS",Font.PLAIN,36));
		g.drawString("Play",640,384);
		g.drawString("Credits",611,588);
		g.setFont(new Font("Comic Sans MS",Font.PLAIN,24));
		g.drawString("Instructions",608,483);
		
    }
    
    public void drawGame(Graphics g){//method to draw game screen
        g.drawImage(mapimage[mappos],0,0,1300,950,null);
		p1.draw(g);//draw p1
		p2.draw(g);//draw p2
    }
    
    public void drawSelect(Graphics g){//method to draw select screen
    	g.setColor(new Color(100,100,100));//set colour for background
    	g.fillRect(0,0,1280,900);//draw background
    	g.setColor(new Color(200,200,255));//set colour for other rectangles
    	for (int i=0; i<4;i++){//go through rectangle array to display characters and spells
    		if (charectarray[i].contains(mouse)){
    			g.setColor(new Color(200,100,255));
    		}
    		g.fillRect(charectarray[i].x,charectarray[i].y,charectarray[i].width,charectarray[i].height);
    		g.setColor(new Color(200,200,255));
    		imageInRect(g,spellimage[i],spellrectarray[i]);//draw spell images
    		g.setColor(new Color(200,200,255));
    		//imageInRect(g,chaimage[i],charectarray[i]);
    		//imageInRect(g,spellimage[i],spellrectarray[i]);
    	}
    	if (leftarrowrect.contains(mouse)){//draw and hovering arrow buttons for choosing map
    		imageInRect(g,leftarrow2,leftarrowrect);
    	}
    	else{
    		g.fillRect(leftarrowrect.x,leftarrowrect.y,leftarrowrect.width,leftarrowrect.height);
    		imageInRect(g,leftarrow,leftarrowrect);
    	}
    	if (rightarrowrect.contains(mouse)){
    		imageInRect(g,rightarrow2,rightarrowrect);
    	}
    	else{
    		imageInRect(g,rightarrow,rightarrowrect);
    	}
    	imageInRect(g,mapimage[mappos],maprect);//draw map image
    	//imageInRect(g,rightarrow2,rightarrowrect);
    	imageInRect(g,chaimage[p1cha],p1charect);//draw character that p1 choose
    	imageInRect(g,chaimage[p2cha],p2charect);//draw character that p2 choose
    	imageInRect(g,spellimage[p1spell],p1spellrect);//draw spell that p1 choose
    	imageInRect(g,spellimage[p2spell],p2spellrect);//draw spell that p2 choose
    	g.fillRect(readyrect.x,readyrect.y,readyrect.width,readyrect.height);//draw game start button
    }

    public void drawInstructions(Graphics g){//method to draw instruction screen
		g.setColor(new Color(200,200,200));
		g.fillRect(0,0,800,600);
	}
	
	public void drawCredit(Graphics g){//method to draw credit screen
	}
	
	public void drawEnd(Graphics g){//method to draw end screen
		g.setColor(new Color(150,150,15));
		g.fillRect(0,0,1280,800);
		//g.fillRect(playagainrect);
	}
	
    public void paint(Graphics g){//method to paint based on the screen
    	if(screen == MENU){
    		drawMenu(g);
    	}
    	else if(screen == SELECT){
    		drawSelect(g);
    	}
    	else if(screen == INSTRUCTIONS){
    		drawInstructions(g);
    	}
    	else if(screen == CREDIT){
    		drawCredit(g);
    	}
    	else if(screen == GAME){
    		drawGame(g);
    	}
    	else if (screen == END){
    		drawEnd(g);
    	}
    	
		
    }
    
}

class Player{//class to make player 
	private int x,y;//player's position
	private int player;//player number (p1 or p2)
	private int extraspeed;//speedup if player use spell
	private Image [][] pics;//double array that has character's sprites
	private int dir, delay, move, newmove;//set player direction and action(move)
	private double frame;//set frame
	private int maxrapid,rapid;//rapid for basic attack
	private int basicdamage;//basic damge without any damage boost 
	private int damage;//damage that player has right now
	private int maxskillcool;//maximum skill cooldown
	private int skillcool;//skill cooldown 
	private boolean stunned;//stun status 
	private int stuncool;//cooldown that determine how long the player stunned
	private int spell;//spell that player choose
	private int damageboost;//damage double or not(1 or 2)
	private int healcool;//cooldown for heal spell cooldown
	private int speedcool;//speed spell cooldown
	private int damagedoublecool;//damage boost spell cooldown
	private int flashcool;//flash spell cooldown
	private String name;//character name that player choose
	private int health,maxhealth;//set present health and maximum health
	private Rectangle playerrect;//rectangle of the player 
	private Rectangle bullrect;//rectangle for basic attack bullet
	private Energybolt energyb;//one of the characters skill
	private Teleport tp;//one of the characters skill
	private Stun stun;//one of the characters skill
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();//arraylist to add bullet that user shoot
	public static final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3, SHOOTL = 4, SHOOTR = 5,SHOOTU = 6,SHOOTD = 7, SKILLL = 8,SKILLR = 9,SKILLU = 10,SKILLD = 11, WAIT = 5;
		
	public Player(int x, int y, int n, int num, int s, int p){//method to set basic information of the player
		this.x=x;//set location
		this.y=y;
		player = p;//set the player's number
		extraspeed = 0;//set extraspeed as 0 unless player use spell(ghost)
		if (p == 1){//set the direction depends on the player's number
			dir = RIGHT;
			newmove = 1;
		}
		else{
			dir = LEFT;
			newmove = 0;
		}
		frame = 1;//set frame
		delay = 0;//set delay(time for each frame)
		String [] chaarray = new String [] {"scorpion","B","C","D"};//array that has all characters' name
		name = chaarray[n];//set what character does user using
		spell = s;//set what spell does user using 
		damageboost = 1;//damageboost as 1 unless player use spell(damage x2)
		stunned = false;//set stun status
		if (name == "scorpion"){//set basic health, skill cooldown, attack rapid and damage based on their character(each character has different setting)
			maxskillcool = 300;
			skillcool = 300;
			maxhealth = 100;
			health = 100;//each character will have diff maxhealth
			rapid = 24;//change to minimum rapid of all characters
			maxrapid = 24;
			basicdamage = 10;
			damage = 10;
		}
		healcool = 1500;//spell cooldown
		speedcool = 1200;
		damagedoublecool = 1200;
		flashcool = 2000;
		
		pics = new Image[12][10];//set doublearray for character sprites
		if (name == "scorpion"){//load images based on character that player choose
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
	public void setHealth(int h){//method to set health
		health = h;
	}
	public int getHealth(){//method to return health
		return health;
	}
	public int getMaxhealth(){//method to return maximum health
		return maxhealth;
	}
	public ArrayList<Bullet> getBullets(){//method to return the bullets arraylist that player shoot
		return bullets;
	}
	public void setBullets(ArrayList<Bullet> blist){//method to set bullets arraylist
		bullets = blist;
	}
	public String getName(){//method to return what character does player playing
		return name;
	}
	public Energybolt getEb(){//method to return energybolt object(skill)
		return energyb;
	}
	public void setEb(Energybolt e){//method to set energybolt as null
		energyb = null;
	}
	public Stun getStun(){//method to return stun object(skill (shooting stun))
		return stun;
	}
	public void setStun(){//method to set stun object as null
		stun = null;
	}
	public int getDamage(){//method to return basic attack damage that player has
		return damage;
	}
	public int getDamageboost(){//method to return damageboost(1 or 2)
		return damageboost;
	}
	public void move(int dx,int dy){//character movement
		if (stunned == false){//if player get stunned, player can't move
			if(dx>0 && x<1220){//set the direction and new location of the player
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
			if(delay % WAIT == 0){//go to next frame based on delay
				frame = (frame + 1) % pics[move].length;
				if (frame == 0){//frame is start with 1
					frame = 1;
				}
			}
			if (newmove != move){//player did new action
				move = newmove;
				frame = 1;
			}
		}
	}

	public void cooldown(){//method to measure cooldown for basic attack, skill, and spell
		if (rapid<maxrapid){//cooldown(rapid) for basic attack
			rapid+=1;
		}
		if (skillcool<maxskillcool){//cooldown for skill
			skillcool+=1;
		}
		if (healcool<1500){//cooldown for spells
			healcool+=1;
		}
		if (speedcool<1200){
			if (speedcool>300){//speed up for up to 300 counts
				extraspeed = 0;
			}
			speedcool+=1;
		}
		if (damagedoublecool<1400){
			if (damagedoublecool>300){//damage double for up to 300 counts
				damageboost = 1;
			}
			damagedoublecool+=1;
		}
		if (flashcool<2000){
			flashcool+=1;
		}
		if (stunned == true){//check the player is stunned and if they are, then count stuncool
			stuncool +=1;
			if (stuncool == 100){//after 100 counts, player can move
				stuncool = 0;
				stunned = false;
			}
		}	
	}
	
	public void attack(){//basic attack
		if (rapid == maxrapid){//can shoot
			rapid = 0;//set rapid to 0
			if (name == "scorpion"){//each character has different speed for bullet
				bullets.add(new Bullet(x,y,10,dir));//add bullet object into bullets arraylist
			}
			else if (name == "B"){
				bullets.add(new Bullet(x,y,10,dir));//add bullet object into bullets arraylist
			}
			else if (name == "C"){
				if (skillcool>200){//skill is 15 damage and armor increase so after 200 counts damage go back to normal
					damage = basicdamage;
				}
				bullets.add(new Bullet(x,y,10,dir));//add bullet object into bullets arraylist
			}
			else if (name == "D"){
				bullets.add(new Bullet(x,y,10,dir));//add bullet object into bullets arraylist
			}
		}
		if (dir == LEFT){//player action type is determined based on their direction
			newmove = SHOOTL;//set action
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
	
	public void skill(){//method to perform skill
		if (stunned == false){//player can use skill only if they are not stunned
			if (skillcool == maxskillcool){//can use skill
				if (name == "scorpion"){//energy bolt
				/*
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
					}*/
				
					if (dir == LEFT){//check direction and set energybolt object
						newmove = SKILLL;//set new newmove(action)
						energyb = new Energybolt(x-5,y+5,dir);//start point might be different based on where player heading
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
					skillcool = 0;//reset the cooldown
				}
			}
			else if (name == "B"){//teleport
				if (tp == null){//set position
					tp = new Teleport(x,y);//set teleport object
					skillcool = maxskillcool-40;//after 40 counts,player can use teleport again
				}
				else{//use teleport skill
					x = tp.getx();//set player location to previous
					y = tp.gety();
					tp = null;//set teleport object to null
					skillcool = 0;//reset the cooldown
				}
			}
			else if (name == "C"){//15 more damage
				damage = basicdamage+15;//add 15 damage
				skillcool = 0;
			}
			else if (name == "D"){//stun skill
				if (dir == LEFT){//based on player direction, start point of stun is different
					newmove = SKILLL;
					stun = new Stun(x-5,y+5,dir);//set stun object
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
				skillcool = 0;//reset the cooldown
			}
		}
	}
	
	public void spell(){//method to use spell
		//player can use heal spell even if the player is stunned
		if (spell == 0){//heal 30         
			if (healcool == 1500){//can use spell
				if (health+30<=maxhealth){
					health+=30;
				}
				else if (health+30>maxhealth){
					health = maxhealth;
				}
				healcool = 0;
			}
		}
		if (stunned == false){//can't use spell if player is stunned
			if (spell == 1){//speed up
				if (speedcool == 1200){//can use spell
					extraspeed = 2;//move two more pixel
					speedcool = 0;//reset spell cool
				}
			}
			else if (spell == 2){//damage double
				if (damagedoublecool == 1400){//can use spell
					damageboost = 2;//damage double(ex. damage*damageboost)
					damagedoublecool = 0;//reset spell cool
				}
			}
			else if (spell == 3){//flash
				if (flashcool == 2000){//can use spell
					if (dir == LEFT){//change position based on player's direction
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
					flashcool = 0;//reset skill cooldown
				}
			}
		}
	}
	
	public void checkhit(Player enemy){//method to check the player get hit
		ArrayList<Bullet> blist = enemy.getBullets();//get enemy's bullets arraylist 
		String enemycha = enemy.getName();//enemy character
		for (Bullet b: blist){//go through bullet list
			if (b != null){//if bullet is not null
				int p = blist.indexOf(b);//get position of the bullet in the arraylist
				if (enemycha == "scorpion"){//each character has different bullet size
					bullrect = new Rectangle(b.getbullx(),b.getbully(),5,5);//set rectangle for bullet
				
					if (name == "scorpion"){//each character has different hit box size
						playerrect = new Rectangle(x,y,50,50);//set rectangle for player box
						if (bullrect.intersects(playerrect)){//player get hit
							blist.set(p,null);//set the bullet as null
							enemy.setBullets(blist);//set the bullet arraylist to new version
							health = health-enemy.getDamage()*enemy.getDamageboost();//reset the player's health
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
		if (enemycha == "scorpion"){//check player hit by energy bolt
			/*
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
						health = health-10*enemy.getDamageboost();
						stunned = true;
						enemy.setStun();
				}
			}*/
			
			if (enemy.getEb() != null){//if enemy use energybolt
				Rectangle ebrect = new Rectangle(enemy.getEb().getebx(),enemy.getEb().geteby(),10,10);//make the rectangle of energybolt
				if (name == "scorpion"){//set player hitbox
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
				if (ebrect.intersects(playerrect)){//player hit by energybolt(get damage)
					health = health-30*enemy.getDamageboost();//reset the player's health
					enemy.setEb(null);//set enemy's energybolt to null
				}
			}
		}
		else if (enemycha == "D"){//check player hit by stun
			if (enemy.getStun() != null){//check the enemy use stun skill
				Rectangle stunrect = new Rectangle(enemy.getStun().getx(),enemy.getStun().gety(),15,15);//set rectangle of stunshot
				if (name == "scorpion"){//set player's hitbox
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
				if (stunrect.intersects(playerrect)){//player get hit
					health = health-10*enemy.getDamageboost();//set player's health(get damage)
					stunned = true;//set player's stun status as true(player stunned)
					enemy.setStun();//set enemy's stun as null
				}
			}
		}							
	}
	
	public void movebullets(){//method to move not only bullets ,skills..etc
		for (Bullet b: bullets){//go through bullets arraylist
			if (b!=null){//if bullet exist
				int p = bullets.indexOf(b);//find the index of the bullet from the arraylist
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
				if (b.getbullx()<0 || b.getbullx()>1280 || b.getbully()<0 || b.getbully()>768){//check bullets locate outside of the map
					bullets.set(p,null);//set bullet's position to null
				}
			}
		}
		if (energyb != null){//check player use energy bolt or not
			if (energyb.getdis()<=700){
				if (energyb.getebd() == 0){//move left
					energyb.setebx(energyb.getebx()-15);
				}
				else if (energyb.getebd() == 1){//move right
					energyb.setebx(energyb.getebx()+15);
				}
				else if (energyb.getebd() == 2){//move up
					energyb.seteby(energyb.geteby()-15);
				}
				else if (energyb.getebd() == 3){//move down
					energyb.seteby(energyb.geteby()+15);
				}	
				energyb.adddis(15);//add 15 to total distance of energybolt
			}
			else{//if energybolt move more than 700 pixels set it to null
				energyb = null;
			}
			
		}
		if (stun != null){//check player use stun
			if (stun.getd() == 0){//move left
				stun.setx(stun.getx()-10);
			}
			else if (stun.getd() == 1){//move right
				stun.setx(stun.getx()+10);
			}
			else if (stun.getd() == 2){//move up
				stun.sety(stun.gety()-10);
			}
			else if (stun.getd() == 3){//move down
				stun.sety(stun.gety()+10);
			}
		}
	}
	
	public void draw(Graphics g){//method to draw character,bullets, and skills on the screen
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
		if (player == 1){//check player is p1 or p2
			g.fillRect(30,30,500,50);//health bar background
		}
		else if (player == 2){
			g.fillRect(740,30,500,50);
		}
		g.setColor(new Color(0,255,0));//green
		if (player == 1){
			g.fillRect(30,30,(int)((double)health/(double)maxhealth*500),50);//actual health bar
		}
		else if (player == 2){
			g.fillRect(740,30,(int)((double)health/(double)maxhealth*500),50);
		} 
		movebullets();//call movebullets method when program draw player
	}
	
	
	class Bullet{//class for bullet that user shoot(basic attack)
		private int x,y,speed;//bullet's postion and speed
		private int dir;//bullet's direction
		public static final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3;
		public Bullet(int X, int Y, int s,int d){//method to set basic information of bullet
			x = X;//set location
			y = Y;
			speed = s;//set speed
			dir = d;
		}
		public int getbullx(){//method to return x of bullet
			return x;
		}
		public int getbully(){//method to return y of bullet
			return y;
		}
		public int getbulls(){//return the bullet's speed
			return speed;
		}
		public int getbulld(){//return the bullet's direction
			return dir;
		}
		public void setbullx(int X){//method to set new bullet's location
			x = X;
		}
		public void setbully(int Y){
			y = Y;
		}
		
	}
	class Energybolt{//class for energybolt skill
		private int x,y;//energybolt location
		private int dir;//direction
		private int distance;//total distance that energybolt move
		public static final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3;
		public Energybolt(int X, int Y, int d){//method to set basic information of energybolt
			x = X;//set location
			y = Y;
			dir = d;//set direction
			distance = 0;//set distance
		}
		public int getebd(){//method to return direction
			return dir;
		}
		public int getebx(){//method to return x of energybolt
			return x;
		}
		public int geteby(){//method to return y of energybolt
			return y;
		}
		public void setebx(int X){//method to set new location
			x = X;
		}
		public void seteby(int Y){
			y = Y;
		}
		public int getdis(){//method to return total distance 
			return distance;
		}
		public void adddis(int i){//method to add distance that energybolt move
			distance += i;
		}
	}
	class Teleport{//class for teleport skill
		private int x,y;//location that player will teleport
		public Teleport(int X, int Y){//method to set basic information
			x = X;//set location that player will teleport when player use skill again
			y = Y;
		}
		public int getx(){//method to return x coordinate of teleport
			return x;
		}
		public int gety(){//method to return y coordinate of teleport
			return y;
		}
		public void setx(int X){//method to set new location 
			x = X;
		}
		public void sety(int Y){
			y = Y;
		}
	}
	class Stun{//class for stun skill
		private int x,y,dir;//skill position and direction
		public Stun(int X, int Y, int d){//method to set basic information of stun
			x = X;//set location(start point)
			y = Y;
			dir = d;//set direction
		}
		public int getx(){//method to return x value of stun
			return x;
		}
		public int gety(){//method to return y value of stun
			return y;
		}
		public int getd(){//method to return direction of stun
			return dir;
		}
		public void setx(int X){//method to set new location
			x = X;
		}
		public void sety(int Y){
			y = Y;
		}
	}
}
