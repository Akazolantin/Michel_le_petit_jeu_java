package com.Michel.pages;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.Michel.engine.GameContainer;
import com.Michel.engine.OpenSimplexNoise;
import com.Michel.engine.Renderer;
import com.Michel.engine.gfx.Image;
import com.Michel.engine.gfx.ImageTile;
import com.Michel.game.Block;
import com.Michel.game.Button;
import com.Michel.game.Camera;
import com.Michel.game.Chunk;
import com.Michel.game.Ennemy;
import com.Michel.game.Entity;
import com.Michel.game.GameManager;
import com.Michel.game.GameObject;
import com.Michel.game.Player;

public class GamePage extends Page {
	
	public static final int TS = 16;
	public static final int L=16;
	
	private static ArrayList<Entity> entities = new ArrayList<Entity>();
	private static ArrayList<Button> buttons=new ArrayList<Button>();
	
	private static ArrayList<Chunk> level=new ArrayList<Chunk>();
	
	private static ArrayList<Block> blocks=new ArrayList<Block>();
	private ImageTile faces,sides,tops;
	private int border = 2;
	private Image diag = new Image("/Diagonal.png");
	private Image background;
	
	private Camera camera;
	private Player player;

	private static int lvlH;
	private static int lvlW;
	private int lvlX;
	private int lvlY;
	private int seed;
	
	public GamePage() {
		
		setBiome();
		
		//generateLevel("/levelTest.png");
		//lvlX=1;lvlY=1;
		
		seed =(int) Math.random() *100;
		lvlW=20;lvlH=3;lvlX=1;lvlY=1;
		startLevel();
		
		entities.add(new Ennemy(10,220,16,32));
		background=new Image("/background.png");
		player=new Player(0, 0);
		entities.add(player);
		buttons.add(new Button(20,20,280,20,"Menu","menu"));
		camera= new Camera(player);
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		if(gc.getInput().isButtonUp(MouseEvent.BUTTON1)) {
			for(Button button : buttons) {
				button.update(gc, gm, dt);
			}
		}
		for(Button button : buttons) {
			if(button.isDead()) buttons.remove(button);
		}
		
		for(Entity entity : entities) {
			entity.update(gc, gm, dt);
			if(entity.isDead()) entities.remove(entity);
		}
		
		while(player.getTileX()/L<lvlX&&lvlX>1) {
			lvlX--;loadLevel();
		}
		while(player.getTileX()/L>lvlX&&lvlX<lvlW-2) {
			lvlX++;loadLevel();
		}
		while(player.getTileY()/L<lvlY&&lvlY>1) {
			lvlY--;loadLevel();
		}
		while(player.getTileY()/L>lvlY&&lvlY<lvlH-2) {
			lvlY++;loadLevel();
		}
		camera.update(gc, gm, dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.setzDepth(0);
		//r.drawImage(background, r.getCamX(), r.getCamY());
		
		for (int y = (lvlY+2)*L; y >=(lvlY-1)*L; y--) {
			for (int x =(lvlX+2)*L; x >=(lvlX-1)*L-1; x--) {
				if(getBlock(x,y)>=0){
					int tileX=getBlock(x,y)%(faces.getW()/faces.getTileW());
					int tileY=getBlock(x,y)/(faces.getW()/faces.getTileW());
					Block block=blocks.get(getBlock(x,y));
					
					//simple 3D
					r.setzDepth(10);
					r.drawImageTile(faces, x * TS+1, y * TS+2,tileX,tileY);
					
					boolean top = getBlock(x,y-1)<0||(!block.isTransparent()&&blocks.get(getBlock(x,y-1)).isTransparent());
					boolean left = getBlock(x-1,y)<0||(!block.isTransparent()&&blocks.get(getBlock(x-1,y)).isTransparent());
					boolean bottom = getBlock(x,y+1)<0||(!block.isTransparent()&&blocks.get(getBlock(x,y+1)).isTransparent());
					boolean right = getBlock(x+1,y)<0||(!block.isTransparent()&&blocks.get(getBlock(x+1,y)).isTransparent());
					
					if(top) {
						r.setzDepth(0);
						r.drawImageTile(tops, x * TS-2, y * TS-4,tileX,tileY);
						
						r.setzDepth(11);
						r.drawFilledRect(x * TS+1, y * TS+2, TS, 1, 0xff000000);
						
						r.setzDepth(1);
						if(getBlock(x+1,y-1)>=0) {
							r.drawImage(diag,  x * TS+14, y * TS-4);
						}else if(right||(!block.isTransparent()&&blocks.get(getBlock(x+1,y)).isTransparent())) {
							r.drawImage(diag,  x * TS+13, y * TS-4);
						}
						
						if(left&&(getBlock(x-1,y-1)<0||!block.isTransparent()&&blocks.get(getBlock(x-1,y-1)).isTransparent())) {
							r.drawImage(diag,  x * TS-2, y * TS-4);
						}
					}
					
					
					if(left) {
						r.setzDepth(0);
						r.drawImageTile(sides,  x * TS-2, y * TS-4,tileX,tileY);
						r.setzDepth(11);
					
						r.drawFilledRect(x * TS+1, y * TS+2, 1, TS, 0xff000000);
						
						r.setzDepth(1);
						if(bottom&&(getBlock(x-1,y+1)<0||!block.isTransparent()&&blocks.get(getBlock(x-1,y+1)).isTransparent())) {
							r.drawImage(diag,  x * TS-2, y * TS+12);
						}
					}
					
					if(bottom) {
						r.setzDepth(11);
						r.drawFilledRect(x * TS+1, y * TS+17, TS, 1, 0xff000000);
					}
					
					if(right) {
						r.setzDepth(11);
						r.drawFilledRect(x * TS+16, y * TS+2, 1, TS, 0xff000000);
					}

					//Z=> face : 10 / ligne : 11 
					/*
					boolean top = block.getTop()!=0||getBlock(x,y-1)<0||blocks.get(getBlock(x,y-1)).getBottom()!=0||(!block.isTransparent()&&blocks.get(getBlock(x,y-1)).isTransparent())||blocks.get(getBlock(x,y-1)).getLeft()>block.getLeft()||blocks.get(getBlock(x,y-1)).getRight()>block.getRight();
					boolean left = block.getLeft()!=0||getBlock(x-1,y)<0||blocks.get(getBlock(x-1,y)).getRight()!=0||(!block.isTransparent()&&blocks.get(getBlock(x-1,y)).isTransparent())||blocks.get(getBlock(x-1,y)).getTop()>block.getTop()||blocks.get(getBlock(x-1,y)).getBottom()>block.getBottom();
					boolean bottom = block.getBottom()!=0||getBlock(x,y+1)<0||blocks.get(getBlock(x,y+1)).getTop()!=0||(!block.isTransparent()&&blocks.get(getBlock(x,y+1)).isTransparent())||blocks.get(getBlock(x,y+1)).getLeft()>block.getLeft()||blocks.get(getBlock(x,y+1)).getRight()>block.getRight();
					boolean right = block.getRight()!=0||getBlock(x+1,y)<0||blocks.get(getBlock(x+1,y)).getLeft()!=0||(!block.isTransparent()&&blocks.get(getBlock(x+1,y)).isTransparent())||blocks.get(getBlock(x+1,y)).getTop()>block.getTop()||blocks.get(getBlock(x+1,y)).getBottom()>block.getBottom();
					
					//2D
					r.setzDepth(10);
					r.drawImageTile(faces, x * TS, y * TS,tileX,tileY);
					r.setzDepth(11);
					if(top) {
						if(getBlock(x,y-1)>=0&&blocks.get(getBlock(x,y-1)).getBottom()==0&&(block.isTransparent()==blocks.get(getBlock(x,y-1)).isTransparent())) {
							Block topBlock = blocks.get(getBlock(x,y-1));
							if(block.getLeft()<topBlock.getLeft()) {
								r.drawFilledRect(x * TS+block.getLeft(), y * TS+block.getTop(), block.getLeft()-topBlock.getLeft(), 1, 0xff000000);
							}
							if(block.getRight()<topBlock.getRight()) {
								r.drawFilledRect(x * TS+(TS-block.getRight()), y * TS+block.getTop(), block.getRight()-topBlock.getRight(), 1, 0xff000000);
							}
						}else {
							r.drawFilledRect(x * TS+block.getLeft(), y * TS+block.getTop(), TS-block.getLeft()-block.getRight(), 1, 0xff000000);
						}
					}
					
					if(left) {
						if(getBlock(x-1,y)>=0&&blocks.get(getBlock(x-1,y)).getRight()==0&&(block.isTransparent()==blocks.get(getBlock(x-1,y)).isTransparent())) {
							Block leftBlock = blocks.get(getBlock(x-1,y));
							r.setzDepth(11);
							if(block.getTop()<leftBlock.getTop()) {
								r.drawFilledRect(x * TS+block.getLeft(), y * TS+block.getTop(),1, block.getTop()-leftBlock.getTop(), 0xff000000);
							}
							if(block.getBottom()<leftBlock.getBottom()) {
								r.drawFilledRect(x * TS+block.getLeft(), y * TS+(TS-leftBlock.getBottom()), 1, leftBlock.getBottom()-block.getBottom(), 0xff000000);
							}
						}else {
							r.drawFilledRect(x * TS+block.getLeft(), y * TS+block.getTop(), 1, TS-block.getBottom()-block.getTop(), 0xff000000);
						}
					}
					
					if(bottom) {
						r.setzDepth(11);
						if(getBlock(x,y+1)>=0&&blocks.get(getBlock(x,y+1)).getTop()==0&&(block.isTransparent()==blocks.get(getBlock(x,y+1)).isTransparent())) {
							Block bottomBlock = blocks.get(getBlock(x,y+1));
							
							if(block.getLeft()<bottomBlock.getLeft()) {
								r.drawFilledRect(x * TS+block.getLeft(), (y+1) * TS-block.getBottom()-1, bottomBlock.getLeft()-block.getLeft(), 1, 0xff000000);
							}
							if(block.getRight()<bottomBlock.getRight()) {
								r.drawFilledRect(x * TS+(TS-block.getRight()), (y+1) * TS-(TS-bottomBlock.getRight())-1, bottomBlock.getRight()-block.getRight(), 1, 0xff000000);
							}
						}else {
							r.drawFilledRect(x * TS+block.getLeft(), (y+1) * TS-block.getBottom()-1, TS-block.getLeft()-block.getRight(), 1, 0xff000000);
						}
					}
					
					if(right) {
						r.setzDepth(11);
						
						if(getBlock(x+1,y)>=0&&blocks.get(getBlock(x+1,y)).getLeft()==0&&(block.isTransparent()==blocks.get(getBlock(x+1,y)).isTransparent())) {
							Block rightBlock = blocks.get(getBlock(x+1,y));
							r.setzDepth(11);
							if(block.getTop()<rightBlock.getTop()) {
								r.drawFilledRect((x+1) * TS-block.getRight()-1, y * TS+block.getTop(), 1,rightBlock.getTop()- block.getTop(), 0xff000000);
							}
							if(block.getBottom()<rightBlock.getBottom()) {
								r.drawFilledRect((x+1) * TS-block.getRight()-1, (y+1) * TS-(rightBlock.getBottom()-block.getBottom()), 1, rightBlock.getBottom()-block.getBottom(), 0xff000000);
							}
						}else {
							r.drawFilledRect((x+1) * TS-block.getRight()-1, y * TS+block.getTop(), 1, TS-block.getBottom()-block.getTop(), 0xff000000);
						}
					}*/
					/*
					//"3D"
					 
					r.setzDepth(10);
					r.drawImageTile(faces, x * TS+1, y * TS+2,tileX,tileY);
					
					if(top) {
						r.setzDepth(0);
						r.drawImageTile(tops, x * TS-2, y * TS-4+block.getTop(),tileX,tileY);
						
						r.setzDepth(11);
						if(getBlock(x,y-1)>=0&&blocks.get(getBlock(x,y-1)).getBottom()==0&&(block.isTransparent()==blocks.get(getBlock(x,y-1)).isTransparent())) {
							Block topBlock = blocks.get(getBlock(x,y-1));
							if(block.getLeft()<topBlock.getLeft()) {
								r.drawFilledRect(x * TS+1+block.getLeft(), y * TS+2+block.getTop(), block.getLeft()-topBlock.getLeft(), 1, 0xff000000);
								r.drawImage(diag,  x * TS-1+block.getLeft()-topBlock.getLeft(), y * TS-4+block.getTop());
							}
							if(block.getRight()<topBlock.getRight()) {
								r.drawFilledRect(x * TS+1+(TS-block.getRight()), y * TS+2+block.getTop(), block.getRight()-topBlock.getRight(), 1, 0xff000000);
							}
						}else {
							r.drawFilledRect(x * TS+1+block.getLeft(), y * TS+2+block.getTop(), TS-block.getLeft()-block.getRight(), 1, 0xff000000);
						}
						
						boolean top_right=false;
						r.setzDepth(1);
						if(block.getTop()==0&&block.getRight()==0) {
							if(getBlock(x+1,y-1)>=0&&blocks.get(getBlock(x+1,y-1)).getBottom()==0&&blocks.get(getBlock(x+1,y-1)).getLeft()==0) {
								r.drawImage(diag,  x * TS+14-block.getRight(), y * TS-4+block.getTop());
								top_right=true;
							}
						}else if(block.getTop()==0) {
							if(getBlock(x,y-1)>=0&&blocks.get(getBlock(x,y-1)).getBottom()==0&&blocks.get(getBlock(x,y-1)).getLeft()==TS-block.getRight()) {
								r.drawImage(diag,  x * TS+14-block.getRight(), y * TS-4+block.getTop());
								top_right=true;
							}
						}else{
							if(getBlock(x+1,y)>=0&&blocks.get(getBlock(x+1,y)).getLeft()==0&&block.getTop()==TS-blocks.get(getBlock(x+1,y)).getBottom()) {
								r.drawImage(diag,  x * TS+14-block.getRight(), y * TS-4+block.getTop());
								top_right=true;
							}
						}
						
						if(!top_right&&right&&(block.getRight()!=0||getBlock(x+1,y)<0||blocks.get(getBlock(x+1,y)).getTop()!=block.getTop()||(!block.isTransparent()&&blocks.get(getBlock(x+1,y)).isTransparent()))) {
							r.drawImage(diag,  x * TS+13-block.getRight(), y * TS-4+block.getTop());
						}
						
						if(left&&(block.getLeft()!=0||getBlock(x-1,y)<0||blocks.get(getBlock(x-1,y)).getTop()!=block.getTop()||(!block.isTransparent()&&blocks.get(getBlock(x-1,y)).isTransparent()))) {
							if(block.getTop()==0&&block.getLeft()==0) {
								if(getBlock(x-1,y-1)<0||blocks.get(getBlock(x-1,y-1)).getBottom()!=0||blocks.get(getBlock(x-1,y-1)).getRight()!=0||(!block.isTransparent()&&blocks.get(getBlock(x-1,y-1)).isTransparent())) {
									r.drawImage(diag,  x * TS-2+block.getLeft(), y * TS-4+block.getTop());
								}
							}else if(block.getTop()==0) {
								if(getBlock(x,y-1)<0||blocks.get(getBlock(x,y-1)).getLeft()>block.getLeft()||TS-blocks.get(getBlock(x,y-1)).getRight()<block.getLeft()||(!block.isTransparent()&&blocks.get(getBlock(x-1,y-1)).isTransparent())) {
									r.drawImage(diag,  x * TS-2+block.getLeft(), y * TS-4+block.getTop());
								}
							}else {
								if(getBlock(x-1,y)<0||blocks.get(getBlock(x-1,y)).getTop()>block.getTop()||TS-blocks.get(getBlock(x-1,y)).getBottom()<block.getBottom()||(!block.isTransparent()&&blocks.get(getBlock(x-1,y)).isTransparent())) {
									r.drawImage(diag,  x * TS-2+block.getLeft(), y * TS-4+block.getTop());
								}
							}
						}
					}
					
					
					if(left) {
						r.setzDepth(0);
						r.drawImageTile(sides,  x * TS-2+block.getLeft(), y * TS-4,tileX,tileY);
						r.setzDepth(11);
						if(getBlock(x-1,y)>=0&&blocks.get(getBlock(x-1,y)).getRight()==0&&(block.isTransparent()==blocks.get(getBlock(x-1,y)).isTransparent())) {
							Block leftBlock = blocks.get(getBlock(x-1,y));
							r.setzDepth(11);
							if(block.getTop()<leftBlock.getTop()) {
								r.drawFilledRect(x * TS+block.getLeft()+1, y * TS+2+block.getTop(),1, block.getTop()-leftBlock.getTop(), 0xff000000);
								r.drawImage(diag,  x * TS-2+block.getLeft(), y * TS+13+block.getTop()-leftBlock.getTop());
							}
							if(block.getBottom()<leftBlock.getBottom()) {
								r.drawFilledRect(x * TS+block.getLeft()+1, y * TS+2+(TS-leftBlock.getBottom()), 1, leftBlock.getBottom()-block.getBottom(), 0xff000000);
							}
						}else {
							r.drawFilledRect(x * TS+1+block.getLeft(), y * TS+2+block.getTop(), 1, TS-block.getBottom()-block.getTop(), 0xff000000);
						}
						
						r.setzDepth(1);
						if(bottom&&(block.getBottom()!=0||getBlock(x,y+1)<0||blocks.get(getBlock(x,y+1)).getLeft()!=block.getLeft()||(!block.isTransparent()&&blocks.get(getBlock(x,y+1)).isTransparent()))) {
							r.drawImage(diag,  x * TS-2+block.getLeft(), y * TS+12-block.getBottom());
						}
					}
					
					if(bottom) {
						r.setzDepth(11);
						if(getBlock(x,y+1)>=0&&blocks.get(getBlock(x,y+1)).getTop()==0&&(block.isTransparent()==blocks.get(getBlock(x,y+1)).isTransparent())) {
							Block bottomBlock = blocks.get(getBlock(x,y+1));
							
							if(block.getLeft()<bottomBlock.getLeft()) {
								r.drawFilledRect(x * TS+1+block.getLeft(), y * TS+17-block.getBottom(), bottomBlock.getLeft()-block.getLeft(), 1, 0xff000000);
							}
							if(block.getRight()<bottomBlock.getRight()) {
								r.drawFilledRect(x * TS+1+(TS-block.getRight()), y * TS+17-(TS-bottomBlock.getRight()), bottomBlock.getRight()-block.getRight(), 1, 0xff000000);
							}
						}else {
							r.drawFilledRect(x * TS+1+block.getLeft(), y * TS+17-block.getBottom(), TS-block.getLeft()-block.getRight(), 1, 0xff000000);
						}
					}
					
					if(right) {
						r.setzDepth(11);
						
						if(getBlock(x+1,y)>=0&&blocks.get(getBlock(x+1,y)).getLeft()==0&&(block.isTransparent()==blocks.get(getBlock(x+1,y)).isTransparent())) {
							Block rightBlock = blocks.get(getBlock(x+1,y));
							r.setzDepth(11);
							if(block.getTop()<rightBlock.getTop()) {
								r.drawFilledRect(x * TS+16-block.getRight(), y * TS+1+block.getTop(), 1,rightBlock.getTop()- block.getTop(), 0xff000000);
							}
							if(block.getBottom()<rightBlock.getBottom()) {
								r.drawFilledRect(x * TS+16-block.getRight(), y * TS+2+TS-(rightBlock.getBottom()-block.getBottom()), 1, rightBlock.getBottom()-block.getBottom(), 0xff000000);
							}
						}else {
							r.drawFilledRect(x * TS+16-block.getRight(), y * TS+2+block.getTop(), 1, TS-block.getBottom()-block.getTop(), 0xff000000);
						}
					}
				*/
				}
			}
		}
		r.setzDepth(9);
		for (Entity entity : entities) {
			entity.render(gc, r);
		}
		r.setzDepth(15);
		for(Button button : buttons) {
			button.render(gc, r);
		}
	}

	public void loadLevel() {
		for (int Y=lvlY-1;Y<=lvlY+1;Y++) {
			for (int X=lvlX-1;X<=lvlX+1;X++) {
				if(level.get(X+Y*lvlW)==null) {generateChunk(X,Y);}
			}
		}
	}
	
	public void startLevel(){

		for (int Y=0;Y<lvlH;Y++) {
			for (int X=0;X<lvlW;X++) {
				level.add(null);
			}
		}
		
		for (int Y=lvlY-1;Y<=lvlY+1;Y++) {
			for (int X=lvlX-1;X<=lvlX+1;X++) {
				generateChunk(X,Y);
			}
		}
	}
	
	public void generateChunk(int X, int Y) {
		OpenSimplexNoise noise = new OpenSimplexNoise();
		int[] p = new int[L*L];
		for (int x = 0; x <L; x++) {
			int height = (int)(noise.eval((X*L+x)/20, seed)*3);
			for (int y = 0; y <L; y++) {
				if(y+Y*L==height+L) {
					p[x+y*L]=1;
				}else if(y+Y*L>height+L+2) {
					p[x+y*L]=2;
				}else if(y+Y*L>height+L) {
						p[x+y*L]=0;
				}else {
					p[x+y*L]=-1;
				}
			}
		}
		level.set(X+lvlW*Y,new Chunk(p,X,Y));
	}
	
	public void generateLevel(String path) {
		Image levelImage = new Image(path);
		lvlW=levelImage.getW()/L;
		lvlH=levelImage.getH()/L;
		if(levelImage.getW()%L>0) lvlW++;
		if(levelImage.getH()%L>0)lvlH++;
		
		int[] p = new int[L*L];
		int[] imageP = levelImage.getP();
		for(int Y=0;Y<lvlH;Y++) {
			for(int X=0;X<lvlW;X++) {
				for(int x=0;x<L;x++) {
					for(int y=0;y<L;y++) {
						if(x+L*X>levelImage.getW()||y+L*Y>levelImage.getH()) {
							p[x+y*L]=-1;
						}else {
							if(imageP[X*L+x+(Y*L+y)*levelImage.getW()]==0xffFFFFFF) {
								p[x+y*L]=-1;
							}else {
								p[x+y*L]=2;
							}
							
						}
					}
				}
				level.add(new Chunk(p,X,Y));
			}
		}
	}
	
	public static boolean getCollision(int x,int y) {
		if(x<0||y<0||x>=lvlW*L||y>=lvlH*L) {return true;}
		int X=x/L;int Y=y/L;
		x=x%L;y=y%L;
		if(level.get(X+Y*lvlW)==null) return true;
		return level.get(X+Y*lvlW).getCollision(x,y);
	}
	
	public int getBlock(int x,int y) {
		if(x<0||y<0||x>=lvlW*L||y>=lvlH*L) {return border;}
		int X=x/L;int Y=y/L;
		x=x%L;y=y%L;
		if(level.get(X+Y*lvlW)==null) return border;
		return level.get(X+Y*lvlW).getBlock(x,y);
	}
	
	public void setBiome() {
		tops = new ImageTile("/Biome1t.png",19,6);
		faces= new ImageTile("/Biome1f.png",16,16);
		sides = new ImageTile("/Biome1s.png",3,20);
		blocks.add(new Block());
		blocks.add(new Block());
		blocks.add(new Block());
		blocks.add(new Block(0,0,0,11,false));
	}
	
	public static void attack(int fromX,int fromY,int x,int y, int w,int h,int damage,int knockBack) {
		for(Entity entity : entities) {
			if(entity.getPosX()<x+w&&entity.getPosX()+entity.getWidth()>x&&entity.getPosY()<y+h&&entity.getPosY()+entity.getHeight()>y) {
				entity.hurt(fromX, fromY, damage,knockBack);
			}
		}
	}

	public static int getLvlH() {
		return lvlH;
	}

	public static int getLvlW() {
		return lvlW;
	}

	public int getLvlX() {
		return lvlX;
	}

	public void setLvlX(int lvlX) {
		this.lvlX = lvlX;
	}

	public int getLvlY() {
		return lvlY;
	}

	public void setLvlY(int lvlY) {
		this.lvlY = lvlY;
	}
	
}
