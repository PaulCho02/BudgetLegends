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
 music and sound 0%
 end screen 5%
 ---------------------------(in game)
 
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
import sun.audio.*;

public class BudgetLegends extends JFrame{
	javax.swing.Timer myTimer; 
	GamePanel game;
		
    public BudgetLegends() {
		super("Move the Box");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280,900);

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
	
	public static void music () {
		AudioPlayer ap = AudioPlayer.player;
		AudioStream as;
		AudioData ad;
		ContinuousAudioDataStream loop = null;
		
		as = new AudioStream(new FileInputStream(""));//insert background music file
		ad = as.getData();
		loop = new ContinuousAudioDataStream(ad);
		ap.start(loop);
	}
	
    public static void main(String[] arguments) {
		BudgetLegends frame = new BudgetLegends();		
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
	private Rectangle undorect;//rectangle that user can go back to previous screen page
	private Rectangle maprect,leftarrowrect,rightarrowrect;//rectangle that are using for select page
	private Rectangle p1charect;//rectangle that display what character p1 choose
	private Rectangle p2charect;//rectangle that display what character p2 choose
	private Rectangle p1spellrect;//rectangle that displayt what spell p1 choose
	private Rectangle p2spellrect;//rectangle that displayt what spell p2 choose
	private int p1cha,p2cha;//character number that players choose
	private int p1spell,p2spell;//spell number that players choose
	private int mappos;//map number that players will play
	private Image [] screenimage;//array that has menu,credit, and instruction page images
	private Image [] undoimage;//array that has button images that leads to previous screen
	private Image [] chaimage;//array that has all characters 
	private Image [] chadieimage;//array that has all characters' die sprtes
	private Image [] spellimage;//array taht has all spell images 
	private Image [] mapimage;//array that has all 4 maps
	private Font font;//set the font that will use in menu and select page
	public static final int MENU=1, SELECT=2, GAME=3, INSTRUCTION=4, CREDIT=5, WAIT=5, END=6;//defined screens as number
	private Image buttonUp, buttonDown,leftarrow,leftarrow2,rightarrow,rightarrow2;//images that are using in menu and select page
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
		screenimage = new Image[] {new ImageIcon("screen/menu.png").getImage(),new ImageIcon("screen/credit.png").getImage(),new ImageIcon("screen/instruction.png").getImage(),new ImageIcon("screen/credit.png").getImage()};
		undoimage = new Image[] {new ImageIcon("undo/0.png").getImage(),new ImageIcon("undo/1.png").getImage(),new ImageIcon("undo/2.png").getImage(),new ImageIcon("undo/3.png").getImage()};
		chaimage = new Image[] {new ImageIcon("charimage/0.png").getImage(),new ImageIcon("charimage/1.png").getImage(),
					new ImageIcon("charimage/2.png").getImage(),new ImageIcon("charimage/3.png").getImage()};//characters' images
		//chadieimage = new Image[] {new ImageIcon("chadie/0.png").getImage(),new ImageIcon("chadie/1.png").getImage(),new ImageIcon("chadie/2.png").getImage(),new ImageIcon("chadie/3.png").getImage()}
		spellimage = new Image[]{new ImageIcon("spell/0.png").getImage(),new ImageIcon("spell/1.png").getImage(),
					new ImageIcon("spell/2.png").getImage(),new ImageIcon("spell/3.png").getImage()};//load spell images
		mapimage = new Image[] {new ImageIcon("maps/map1.png").getImage(),new ImageIcon("maps/map2.jpg").getImage(),
					new ImageIcon("maps/map3.jpg").getImage(),new ImageIcon("maps/map4.png").getImage()};//load map images
		
		instructionrect = new Rectangle (600,450,150,50);//make(defined) rectangles
		creditrect = new Rectangle (600,550,150,50);
		gamerect = new Rectangle(600,350,150,50);
		readyrect = new Rectangle(470,30,350,90);
		undorect = new Rectangle(80,780,60,60);
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
	
	public static void music () {
		AudioPlayer ap = AudioPlayer.player;
		AudioStream as;
		AudioData ad;
		ContinuousAudioDataStream loop = null;
		if (screen == MENU) { //differing music for some screens
			as = new AudioStream(new FileInputStream("AdHeroes/menusong.mp3"));//insert background music file
		}
		if (screen == SELECT) {
			as = new AudioStream(new FileInputStream("AdHeroes/select.mp3"));
		}
		if (screen == GAME) {
			if (mappos == 0) { //different audio for each map
				as = new AudioStream(new FileInputStream("AdHeroes/firstmap.mp3"));
			}
			if (mappos == 1) {
				as = new AudioStream(new FileInputStream("AdHeroes/secondmap.mp3"));
			}
			if (mappos == 2) {
				as = new AudioStream(new FileInputStream("AdHeroes/thirdmap.mp3"));
			}
			//if (mappos == 3) {
			//	as = new AudioStream(new FileInputStream(""));
			//}
		}	
		if (screen == INSTRUCTION) {
			as = new AudioStream(new FileInputStream("AdHeroes/menusong.mp3"));
		}
		if (screen == CREDIT) {
			as = new AudioStream(new FileInputStream("AdHeroes/creditsong.mp3"));
		}
		//if (screen == WAIT) {
		//	as = new AudioStream(new FileInputStream(""));
		//}
		if (screen == END) {
			as = new AudioStream(new FileInputStream("AdHeroes/menusong.mp3"));
		}
		ad = as.getData();
		loop = new ContinuousAudioDataStream(ad);
		ap.start(loop);
		
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
    			if(gamerect.contains(mouse)){//move to other screen
					screen = SELECT;
				}
				if(instructionrect.contains(mouse)){
					screen = INSTRUCTION;	
				}
				if(creditrect.contains(mouse)){
					screen = CREDIT;
				}
			}
			if (screen == SELECT){//set p1,p2 and map here
				if (readyrect.contains(mouse)){//game start button
					p1 = new Player(320,400,p1cha,9,p1spell,1);//using player class to make p1 and p2
					p2 = new Player(950,400,p2cha,9,p2spell,2);
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
			if (screen == INSTRUCTION){
				if (undorect.contains(mouse)){
					screen = MENU;
				}
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
			my = evt.getY()+20;
		}
		public void mouseDragged(MouseEvent evt){
			mx = evt.getX();
			my = evt.getY()+20;		
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
    	g.setColor(new Color(0xB1C4DF)); //set colour 
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
        g.drawImage(mapimage[mappos],0,0,1280,900,null);
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
    		imageInRect(g,chaimage[i],charectarray[i]);
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
   		g.drawImage(screenimage[2], 0,0,1280,870,null);
   		if (undorect.contains(mouse)){
   			imageInRect(g,undoimage[1],undorect);
   		}
   		else{
   			imageInRect(g,undoimage[0],undorect);
   		}
	}
	
	public void drawCredit(Graphics g){//method to draw credit screen
		g.drawImage(screenimage[3], 0,0,1300,900,null);
		if (undorect.contains(mouse)){
   			imageInRect(g,undoimage[3],undorect);
   		}
   		else{
   			imageInRect(g,undoimage[2],undorect);
   		}
	}
	
	public void drawEnd(Graphics g){//method to draw end screen
		g.setColor(new Color(0,100,50)); 
		g.fillRect(0,0,1280,900);
		Rectangle p1r = new Rectangle(200,250,300,450);
		Rectangle p2r = new Rectangle(900,250,300,450);
		/*
		if (p1.gethealth()<0){//p2 win
			imageInRect(g,chadieimage[p1cha],p1r);
			imageInRect(g,chaimage[p2cha],p2r);
			//print p2 win
		}
		else{//p1 win
			imageInRect(g,chadieimage[p2cha],p2r);
			imageInRect(g,chaimage[p1cha],p1r);
			//print p1 win
		}*/
		g.fillRect(550,350,200,100);
		
		//g.fillRect(playagainrect);
	}
	
    public void paint(Graphics g){//method to paint based on the screen
    	if(screen == MENU){
    		drawMenu(g);
    	}
    	else if(screen == SELECT){
    		drawSelect(g);
    	}
    	else if(screen == INSTRUCTION){
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
	private Image [] bullimage;//array that has bullet images
	private Image [] bullimage2;//character C has two types of bullet
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
	private int maxspellcool;//maximum cooldown for spell
	private int spellcool;//cooldown for spell
	private String name;//character name that player choose
	private int nameint;//convert name(string) to integer
	private int health,maxhealth;//set present health and maximum health
	private Rectangle playerrect;//rectangle of the player 
	private Rectangle skillrect;//rectangle that will show skill available 
	private Rectangle spellrect;//rectangle that will show spell available
	private Energybolt energyb;//one of the characters skill
	private Teleport tp;//one of the characters skill
	private Stun stun;//one of the characters skill
	private Image skillimage;
	private Image [] spellimage;//array that has all spell images
	private Image [] stunimage;//array that has stun images
	private Image [] ebimage;//array that has energybolt images
	private Image teleportimage;
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();//arraylist to add bullet that user shoot
	public static final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3, SHOOTL = 4, SHOOTR = 5,SHOOTU = 6,SHOOTD = 7, SKILLL = 8,SKILLR = 9,SKILLU = 10,SKILLD = 11, WAIT = 5;
		
	public Player(int x, int y, int n, int num, int s, int p){//method to set basic information of the player
		this.x=x;//set location
		this.y=y;
		player = p;//set the player's number
		extraspeed = 0;//set extraspeed as 0 unless player use spell(ghost)
		if (p == 1){//set the direction depends on the player's number
			dir = RIGHT;
			move = 1;
			skillrect = new Rectangle(230,750,60,60);
			spellrect = new Rectangle(310,750,60,60);
		}
		else{
			dir = LEFT;
			move = 0;
			skillrect = new Rectangle(960,750,60,60);
			spellrect = new Rectangle(1040,750,60,60);
		}
		frame = 1;//set frame
		delay = 0;//set delay(time for each frame)
		String [] chaarray = new String [] {"A","B","C","D"};//array that has all characters' name
		name = chaarray[n];//set what character does user using
		spell = s;//set what spell does user using 
		damageboost = 1;//damageboost as 1 unless player use spell(damage x2)
		stunned = false;//set stun status
		playerrect = new Rectangle(x,y,60,95);//set rectangle for player box
		if (name == "A"){//set basic health, skill cooldown, attack rapid and damage based on their character(each character has different setting)
			nameint = 0;
			maxskillcool = 300;
			skillcool = 300;
			maxhealth = 100;
			health = 100;//each character will have diff maxhealth
			rapid = 20;//change to minimum rapid of all characters
			maxrapid = 20;
			basicdamage = 15;
			damage = 15;
		}
		else if(name == "B"){
			nameint = 1;
			maxskillcool = 300;
			skillcool = 300;
			maxhealth = 300;
			health = 300;
			rapid = 37;
			maxrapid = 37;
			basicdamage = 8;
			damage = 8; 
		}
		else if(name == "C"){
			nameint = 2;
			maxskillcool = 500;
			skillcool = 500;
			maxhealth = 220;
			health = 220;
			rapid = 10;
			maxrapid = 10;
			basicdamage = 3;
			damage = 3;
		}
		else if(name == "D"){
			nameint = 3;
			maxskillcool = 250;
			skillcool = 250;
			maxhealth = 150;
			health = 150;
			rapid = 35;
			maxrapid = 35;
			basicdamage = 15;
			damage = 15;
		}
		if (spell == 0){//heal
			maxspellcool = 1500;
			spellcool = 1500;
		}
		else if (spell == 1){//ghost(speed up)
			maxspellcool = 1200;
			spellcool = 1200;
		}
		else if (spell == 2){//damagedouble
			maxspellcool = 1200;
			spellcool = 1200;
		}
		else if (spell == 3){//flash
			maxspellcool = 800;
			spellcool = 800;
		}
		spellimage = new Image[]{new ImageIcon("spell/0.png").getImage(),new ImageIcon("spell/1.png").getImage(),
					new ImageIcon("spell/2.png").getImage(),new ImageIcon("spell/3.png").getImage()};//load spell images
		pics = new Image[12][10];//set doublearray for character sprites
		bullimage = new Image[4];
		if (name == "A"){//load images based on character that player choose
			for (int i = 1; i<10;i++){ 
				pics[0][i] = new ImageIcon("charA/walk/left"+"/"+"charAWalkLeft"+i+".png").getImage();
				pics[1][i] = new ImageIcon("charA/walk/right"+"/"+"charAWalkRight"+i+".png").getImage();
				pics[2][i] = new ImageIcon("charA/walk/up"+"/"+"charAWalkUp"+i+".png").getImage();
				pics[3][i] = new ImageIcon("charA/walk/down"+"/"+"charAWalkDown"+i+".png").getImage();
			}
			for (int i = 1; i<5;i++){//do it 5 times to prevent glitch
				pics[4][i] = new ImageIcon("charA/shoot/left"+"/"+"charAShootLeft"+1+".png").getImage();
				pics[5][i] = new ImageIcon("charA/shoot/right"+"/"+"charAShootRight"+1+".png").getImage();
				pics[6][i] = new ImageIcon("charA/shoot/up"+"/"+"charAShootUp"+1+".png").getImage();
				pics[7][i] = new ImageIcon("charA/shoot/down"+"/"+"charAShootDown"+1+".png").getImage();
			}
			for (int i = 1; i<5;i++){ 
				pics[8][i] = new ImageIcon("charA/spellcast/left"+"/"+"charASpellcastLeft"+1+".png").getImage();
				pics[9][i] = new ImageIcon("charA/spellcast/right"+"/"+"charASpellcastRight"+1+".png").getImage();
				pics[10][i] = new ImageIcon("charA/spellcast/up"+"/"+"charASpellcastUp"+1+".png").getImage();
				pics[11][i] = new ImageIcon("charA/spellcast/down"+"/"+"charASpellcastDown"+1+".png").getImage();
			}
			for (int i = 0;i<4;i++){
				bullimage[i] = new ImageIcon("charA/bullet/"+i+".png").getImage();
			}	
			skillimage = new ImageIcon("skill/0.png").getImage();//load skill image
			stunimage = new Image[4];
			for (int i = 0; i<4;i++){//load stun images
				stunimage[i] = new ImageIcon("stun/"+i+".png").getImage();
			}
		}
		else if(name == "B"){
			for (int i = 1; i<10; i++){
				pics[0][i] = new ImageIcon("charB/walk/left/charBWalkLeft"+i+".png").getImage();
				pics[1][i] = new ImageIcon("charB/walk/right/charBWalkRight"+i+".png").getImage();
				pics[2][i] = new ImageIcon("charB/walk/up/charBWalkUp"+i+".png").getImage();
				pics[3][i] = new ImageIcon("charB/walk/down/charBWalkDown"+i+".png").getImage();
			}
			for (int i = 1; i<5; i++){
				pics[4][i] = new ImageIcon("charB/shoot/left/charB"+1+".png").getImage();
				pics[5][i] = new ImageIcon("charB/shoot/right/charB"+1+".png").getImage();
				pics[6][i] = new ImageIcon("charB/shoot/up/charB"+1+".png").getImage();
				pics[7][i] = new ImageIcon("charB/shoot/down/charB"+1+".png").getImage();
			}
			for (int i = 0; i<4; i++){
				bullimage[i] = new ImageIcon("charB/bullet/"+i+".png").getImage();
			}
			skillimage = new ImageIcon("skill/1.png").getImage();//load skill image
			teleportimage = new ImageIcon("teleport/teleportimage.png").getImage();//load image that shows save point
		}
		else if(name == "C"){
			for (int i = 0; i<9; i++){
				pics[0][i+1] = new ImageIcon("charC/walk/left/charC07"+i+".png").getImage();
				pics[1][i+1] = new ImageIcon("charC/walk/right/charC0"+(88+i)+".png").getImage();
				pics[2][i+1] = new ImageIcon("charC/walk/up/charC06"+(i+1)+".png").getImage();
				pics[3][i+1] = new ImageIcon("charC/walk/down/charC0"+(79+i)+".png").getImage();
			}
			for (int i = 1; i<5; i++){
				pics[4][i] = new ImageIcon("charC/slash/charC1.png").getImage();
				pics[5][i] = new ImageIcon("charC/slash/charC2.png").getImage();
				pics[6][i] = new ImageIcon("charC/slash/charC3.png").getImage();
				pics[7][i] = new ImageIcon("charC/slash/charC4.png").getImage();
			}
			bullimage2 = new Image[4];
			for (int i = 0; i<4; i++){
				bullimage[i] = new ImageIcon("charC/bullet/"+i+".png").getImage();
				bullimage2[i] = new ImageIcon("charC/bullet2/"+i+".png").getImage();
			}
			skillimage = new ImageIcon("skill/2.png").getImage();//load skill image
		}
		else if(name == "D"){
			for (int i = 0; i<9; i++){
				pics[0][i+1] = new ImageIcon("charD/walk/left/charD07"+i+".png").getImage();
				pics[1][i+1] = new ImageIcon("charD/walk/right/charD0"+(88+i)+".png").getImage();
				pics[2][i+1] = new ImageIcon("charD/walk/up/charD06"+(i+1)+".png").getImage();
				pics[3][i+1] = new ImageIcon("charD/walk/down/charD0"+(79+i)+".png").getImage();
			}
			for (int i = 1; i<5; i++){
				pics[4][i] = new ImageIcon("charD/spellcast/left/charD1.png").getImage();
				pics[5][i] = new ImageIcon("charD/spellcast/right/charD1.png").getImage();
				pics[6][i] = new ImageIcon("charD/spellcast/up/charD1.png").getImage();
				pics[7][i] = new ImageIcon("charD/spellcast/down/charD1.png").getImage();
			}
			for (int i = 1; i<5; i++){
				pics[8][i] = new ImageIcon("charD/slash/left/charD1.png").getImage();
				pics[9][i] = new ImageIcon("charD/slash/right/charD1.png").getImage();
				pics[10][i] = new ImageIcon("charD/slash/up/charD1.png").getImage();
				pics[11][i] = new ImageIcon("charD/slash/down/charD1.png").getImage();
			}
			for (int i = 0; i<4; i++){
				bullimage[i] = new ImageIcon("charD/bullet/"+i+".png").getImage();
			}
			skillimage = new ImageIcon("skill/3.png").getImage();//load skill image
			ebimage = new Image[4];
			for (int i = 0;i<4;i++){
				ebimage[i] = new ImageIcon("energybolt/"+i+".png").getImage();//load energy bolt images
			}
		}
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
		playerrect = new Rectangle(x,y,60,95);//everytime character move, player's hitbox also change together
	}

	public void cooldown(){//method to measure cooldown for basic attack, skill, and spell
		if (rapid<maxrapid){//cooldown(rapid) for basic attack
			rapid+=1;
		}
		if (skillcool<maxskillcool){//cooldown for skill
			skillcool+=1;
		}
		if (spellcool<maxspellcool){
			if (spell == 1){//ghost(speed up)
				if(spellcool>300){//speed up for up to 300 counts
					extraspeed = 0;
				}
			}
			else if (spell == 2){//damage double for up to 300 counts
				if (spellcool>300){
					damageboost = 1;
				}
			}
			spellcool+=1;
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
			if (name == "A"){//each character has different speed for bullet
				if (dir == LEFT){
					bullets.add(new Bullet(x-60,y+40,10,dir));//add bullet object into bullets arraylist
				}
				else if (dir == RIGHT){
					bullets.add(new Bullet(x+70,y+40,10,dir));//add bullet object into bullets arraylist
				}
				else if (dir == UP){
					bullets.add(new Bullet(x+20,y-60,10,dir));//add bullet object into bullets arraylist
				}
				else if (dir == DOWN){
					bullets.add(new Bullet(x+20,y+60,10,dir));//add bullet object into bullets arraylist
				}
				
			}
			else if (name == "B"){
				if (dir == LEFT){
					bullets.add(new Bullet(x-60,y+40,25,dir));//add bullet object into bullets arraylist
				}
				else if (dir == RIGHT){
					bullets.add(new Bullet(x+70,y+40,25,dir));//add bullet object into bullets arraylist
				}
				else if (dir == UP){
					bullets.add(new Bullet(x+20,y-60,25,dir));//add bullet object into bullets arraylist
				}
				else if (dir == DOWN){
					bullets.add(new Bullet(x+20,y+60,25,dir));//add bullet object into bullets arraylist
				}
			}
			else if (name == "C"){
				if (skillcool>150){//skill is 15 damage and armor increase so after 200 counts damage go back to normal
					damage = basicdamage;
				}
				if (dir == LEFT){
					bullets.add(new Bullet(x-150,y+45,10,dir));//add bullet object into bullets arraylist
				}
				else if (dir == RIGHT){
					bullets.add(new Bullet(x+145,y+45,10,dir));//add bullet object into bullets arraylist
				}
				else if (dir == UP){
					bullets.add(new Bullet(x+22,y-130,10,dir));//add bullet object into bullets arraylist
				}
				else if (dir == DOWN){
					bullets.add(new Bullet(x+12,y+130,10,dir));//add bullet object into bullets arraylist
				}
			}
			else if (name == "D"){
				if (dir == LEFT){
					bullets.add(new Bullet(x-30,y+40,14,dir));//add bullet object into bullets arraylist
				}
				else if (dir == RIGHT){
					bullets.add(new Bullet(x+70,y+40,14,dir));//add bullet object into bullets arraylist
				}
				else if (dir == UP){
					bullets.add(new Bullet(x+30,y-20,14,dir));//add bullet object into bullets arraylist
				}
				else if (dir == DOWN){
					bullets.add(new Bullet(x+30,y+60,14,dir));//add bullet object into bullets arraylist
				}
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
		if(delay % WAIT == 0){//go to next frame based on delay
			if (frame>3){
				frame = 0;
			}
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
	
	public void skill(){//method to perform skill
		if (stunned == false){//player can use skill only if they are not stunned
			if (skillcool == maxskillcool){//can use skill
				if (name == "A"){//stun skill
					if (dir == LEFT){//based on the direction the start point of the stun object is different
						newmove = SKILLL;
						stun = new Stun(x-65,y+15,dir);
					}
					else if (dir == RIGHT){
						newmove = SKILLR;
						stun = new Stun(x+45,y+15,dir);
					}
					else if (dir == UP){
						newmove = SKILLU;
						stun = new Stun(x+20,y-60,dir);
					}
					else if (dir == DOWN){
						newmove = SKILLD;
						stun = new Stun(x+20,y+80,dir);
					}
					skillcool = 0;
					if(delay % WAIT == 0){//go to next frame based on delay
						if (frame>3){
							frame = 0;
						}
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
				else if (name == "C"){//10 more damage
					damage = basicdamage+10;//add 15 damage
					skillcool = 0;
				}
				else if (name == "D"){//energy bolt skill
					if (dir == LEFT){//check direction and set energybolt object
						newmove = SKILLL;//set new newmove(action)
						energyb = new Energybolt(x-100,y+30,dir);//start point might be different based on where player heading
					}
					else if (dir == RIGHT){
						newmove = SKILLR;
						energyb = new Energybolt(x+50,y+30,dir);
					}
					else if (dir == UP){
						newmove = SKILLU;
						energyb = new Energybolt(x+20,y-100,dir);
					}
					else if (dir == DOWN){
						newmove = SKILLD;
						energyb = new Energybolt(x+20,y+95,dir);
					}
					skillcool = 0;//reset the cooldown
					if(delay % WAIT == 0){//go to next frame based on delay
						if (frame>3){
							frame = 0;
						}
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
		}
	}
	
	public void spell(){//method to use spell
		//player can use heal spell even if the player is stunned
		if (spell == 0){//heal 30         
			if (spellcool == maxspellcool){//can use spell
				if (health+30<=maxhealth){
					health+=30;
				}
				else if (health+30>maxhealth){
					health = maxhealth;
				}
				spellcool = 0;
			}
		}
		if (stunned == false){//can't use spell if player is stunned
			if (spellcool == maxspellcool){//can use spell
				if (spell == 1){//ghost(speed up)
					extraspeed = 2;
				}
				else if (spell == 2){//damage double
					damageboost = 2;//damage double(ex. damage*damageboost)
				}
				else if (spell == 3){
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
				}
				spellcool = 0;//reset spell cool
			}
		}
	}
	
	public void checkhit(Player enemy){//method to check the player get hit
		ArrayList<Bullet> blist = enemy.getBullets();//get enemy's bullets arraylist 
		String enemycha = enemy.getName();//enemy character
		Rectangle enemybullrect = new Rectangle(0,0,0,0);//Rectangle for enemy bullet
		for (Bullet b: blist){//go through bullet list
			if (b != null){//if bullet is not null
				int p = blist.indexOf(b);//get position of the bullet in the arraylist
				if (enemycha == "A"){//each character has different bullet size
					enemybullrect = b.getbullrect("A");//get rectangle for bullet
					
				}
				else if (enemycha == "B"){
					enemybullrect = b.getbullrect("B");
					
				}
				else if (enemycha == "C"){
					enemybullrect = b.getbullrect("C");
				}
				else if (enemycha == "D"){
					enemybullrect = b.getbullrect("D");
				}
				
				if (enemybullrect.intersects(playerrect)){//player get hit
					blist.set(p,null);//set the bullet as null
					enemy.setBullets(blist);//set the bullet arraylist to new version
					health = health-enemy.getDamage()*enemy.getDamageboost();//reset the player's health
				}
			}				
		}
		if (enemycha == "A"){//check player hit by energy bolt
			if (enemy.getStun() != null){//check the enemy use stun skill
				Rectangle stunrect = enemy.getStun().getstunrect();//get rectangle of stunshot
				if (name == "A"){//set player's hitbox
					playerrect = new Rectangle(x,y,60,95);
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
		else if (enemycha == "D"){//check player hit by stun
			if (enemy.getEb() != null){//if enemy use energybolt
				Rectangle ebrect = enemy.getEb().getebrect();//make the rectangle of energybolt
				if (name == "A"){//set player hitbox
					playerrect = new Rectangle(x,y,60,95);
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
	public void imageInRect(Graphics g, Image img, Rectangle area){//method to draw image in rectangle
		g.drawImage(img, area.x, area.y, area.width, area.height, null);
    }
	public void draw(Graphics g){//method to draw character,bullets, and skills on the screen
		if (name == "B"){//if player use teleport skill it shows where the player will go back
			if (tp != null){
				imageInRect(g,teleportimage,new Rectangle(tp.getx()+10,tp.gety()+20,30,30));
			}
		}
		if (name == "C"){//C has different motion for basic attack so need set the sprites' location again
			if (move == SHOOTL){
				g.drawImage(pics[move][(int)frame], x-140, y, null);//draw character
			}
			else if(move == SHOOTR){
				g.drawImage(pics[move][(int)frame], x, y, null);//draw character
			}
			else if(move == SHOOTU){
				g.drawImage(pics[move][(int)frame], x, y-130, null);//draw character
			}
			else if(move == SHOOTD){
				g.drawImage(pics[move][(int)frame], x+10, y-20, null);//draw character
			}
			else{
				g.drawImage(pics[move][(int)frame], x, y, null);//draw character
			}
		}
		else if (name == "D"){//energy bolt sprite shows a little bit off from its location
			if (move == SKILLL){
				g.drawImage(pics[move][(int)frame], x-150, y, null);//draw character
			}
			else if(move == SKILLR){
				g.drawImage(pics[move][(int)frame], x+20, y, null);//draw character
			}
			else if(move == SKILLU){
				g.drawImage(pics[move][(int)frame], x-50, y-40, null);//draw character
			}
			else if(move == SKILLD){
				g.drawImage(pics[move][(int)frame], x-50, y+20, null);//draw character
			}
			else{
				g.drawImage(pics[move][(int)frame], x, y, null);//draw character
			}
		}
		else{
			g.drawImage(pics[move][(int)frame], x, y, null);//draw character
		}
		
		g.setColor(new Color(0,0,0));//black
		for (Bullet b:bullets){//draw bullets
			if (b!=null){
				if (name == "C"){
					if (damage != basicdamage){
						imageInRect(g,bullimage2[b.getbulld()],b.getbullrect(name));//draw bullet image
					}
					else{
						imageInRect(g,bullimage[b.getbulld()],b.getbullrect(name));//draw bullet image
					}
				}
				else{
					imageInRect(g,bullimage[b.getbulld()],b.getbullrect(name));//draw bullet image	
				}
				
			}
		}
		if (energyb != null){//draw energy bolt
			imageInRect(g,ebimage[energyb.getebd()],energyb.getebrect());
		}
		if (stun != null){//draw stunshot
			imageInRect(g,stunimage[stun.getd()],stun.getstunrect());
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
		g.setColor(new Color(0,0,0,127)); 
		imageInRect(g,spellimage[spell],spellrect);//draw spell image
		if (spellcool < maxspellcool){//can't use spell and spell in cooldown
			g.fillRect(spellrect.x,spellrect.y,60-(int)((double)spellcool/maxspellcool*60),60);//(hovering)draw translucent rectangle on the top to show the cooldown
		}
		imageInRect(g,skillimage,skillrect);//draw skill image
		if (skillcool<maxskillcool){
			g.fillRect(skillrect.x,skillrect.y,60-(int)((double)skillcool/maxskillcool*60),60);//(hovering)draw translucent rectangle to show the cooldown
		}
		movebullets();//call movebullets method when program draw player
	}
	
	
	class Bullet{//class for bullet that user shoot(basic attack)
		private int x,y,speed;//bullet's postion and speed
		private int dir;//bullet's direction
		private Rectangle bullrect;
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
		public Rectangle getbullrect(String cha){//method to return the rectangle of the bullet
			if (cha == "A"){//each character has different bullet size
				if (dir==LEFT){//based on the direction bullet's rectangle direction is also different
					bullrect = new Rectangle(x,y,60,15);
				}
				else if (dir==RIGHT){
					bullrect = new Rectangle(x,y,60,15);
				}
				else if (dir==UP){
					bullrect = new Rectangle(x,y,15,60);
				}
				else if (dir==DOWN){
					bullrect = new Rectangle(x,y,15,60);
				}
			}
			else if (cha == "B"){
				if (dir==LEFT){//based on the direction bullet's rectangle direction is also different
					bullrect = new Rectangle(x,y,60,15);
				}
				else if (dir==RIGHT){
					bullrect = new Rectangle(x,y,60,15);
				}
				else if (dir==UP){
					bullrect = new Rectangle(x,y,15,60);
				}
				else if (dir==DOWN){
					bullrect = new Rectangle(x,y,15,60);
				}
			}
			else if (cha == "C"){
				if (dir==LEFT){//based on the direction bullet's rectangle direction is also different
					bullrect = new Rectangle(x,y,60,30);
				}
				else if (dir==RIGHT){
					bullrect = new Rectangle(x,y,60,30);
				}
				else if (dir==UP){
					bullrect = new Rectangle(x,y,30,60);
				}
				else if (dir==DOWN){
					bullrect = new Rectangle(x,y,30,60);
				}
			}
			else if (cha == "D"){
				bullrect = new Rectangle(x,y,20,20);
			}
			return bullrect;
		}
		
	}
	class Energybolt{//class for energybolt skill
		private int x,y;//energybolt location
		private int dir;//direction
		private int distance;//total distance that energybolt move
		private Rectangle ebrect;
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
		public Rectangle getebrect(){
			if (dir == 0){
				ebrect = new Rectangle(x,y,100,35);
			}
			else if (dir == 1){
				ebrect = new Rectangle(x,y,100,35);
			}
			else if (dir == 2){
				ebrect = new Rectangle(x,y,35,100);
			}
			else if (dir == 3){
				ebrect = new Rectangle(x,y,35,100);
			}
			return ebrect;
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
		private Rectangle stunrect;//rectangle for stun
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
		public Rectangle getstunrect(){
			if (dir == 0){
				stunrect = new Rectangle(x,y,100,35);
			}
			else if (dir == 1){
				stunrect = new Rectangle(x,y,100,35);
			}
			else if (dir == 2){
				stunrect = new Rectangle(x,y,35,100);
			}
			else if (dir == 3){
				stunrect = new Rectangle(x,y,35,100);
			}
			return stunrect;
		}
	}
}
