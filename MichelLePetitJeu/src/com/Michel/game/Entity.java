package com.Michel.game;

import com.Michel.engine.GameContainer;
import com.Michel.engine.Renderer;
import com.Michel.pages.GamePage;

public class Entity extends GameObject {

	protected float vX;
	protected float vY=0;
	protected boolean ground;
	protected int fallSpeed=10;
	protected boolean reverse=false;

	public Entity(int posX, int posY) {
		this.posX=posX;this.posY=posY;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub

	}

	public void hurt(int fromX, int fromY, int damage,int knockBack) {

	}

	public float getvX() {
		return vX;
	}

	public void setvX(int vX) {
		this.vX = vX;
	}

	public float getvY() {
		return vY;
	}

	public void setvY(int vY) {
		this.vY = vY;
	}
	
	protected void move(float dt) {
		
		float newX,newY;
		vY+=dt*fallSpeed;
		
		newX=posX+dt*vX;
		
		if(vX!=0) {
			if(vX>0) newX+=width;
			int tileX=(int)newX/GamePage.TS;
			int tileY=(int)posY/GamePage.TS;
			
			if(newX<0) tileX=-1;
			if(posY<0) tileY=-1;
			boolean collision=false;
			int i=0;
			while(i*GamePage.TS<height) {
				if(GamePage.getCollision(tileX, tileY)) {
					collision=true;break;
				}
				tileY++;i++;
				
			}
			
			if(!collision&&GamePage.getCollision(tileX, (int)(posY+height-1)/GamePage.TS)) {
				collision=true;
			}
			if(collision) {
				if(vX>0) {
					newX=tileX*GamePage.TS-width;
					vX=0;
				}else {
					newX=(tileX+1)*GamePage.TS;
					vX=0;
				}
			}else {
				newX=posX+dt*vX;
			}
		}
		
		posX=newX;
		
		newY=posY+vY;
		if(vY>=0)newY+=height;

		int tileX=(int)posX/GamePage.TS;
		int tileY=(int)newY/GamePage.TS;
		if(posX<0) tileX=-1;
		if(newY<0) tileY=-1;
		
		boolean collision=false;
		int i=0;
		while(i*GamePage.TS<width) {
			if(GamePage.getCollision(tileX, tileY)) {
				collision=true;break;
			}
			tileX++;i++;
		}
		if(!collision&&GamePage.getCollision((int)(posX+width-1)/GamePage.TS, tileY)) {
			collision=true;
		}
		if(collision) {
			if(vY>0) {
				newY=tileY*GamePage.TS-height;
				vY=0;
				ground=true;
			}else {
				newY=(tileY+1)*GamePage.TS;
				vY=0;
				ground=true;
			}
		}else {
			newY=posY+vY;
			ground=false;
		}
		
		posY=newY;
		
		/*if(vX>0) {
			if(offY>=paddU||offY<=paddD) {sigY=0;}
			if(offY<paddU) {sigY=-1;}
			if(offY>paddD) {sigY=1;}
			if(GamePage.getCollision(tileX+1, tileY)
					||GamePage.getCollision(tileX+1, tileY+1)
					||GamePage.getCollision(tileX+1, tileY+sigY)
					||GamePage.getCollision(tileX+1,1+ tileY+sigY)) {
				offX+=dt*vX;
				if(offX>paddR) {
					offX=paddR;
				}
			}else {
				offX+=dt*vX;
			}
		}else if(vX<0) {
			if(offY>=paddU||offY<=paddD) {sigY=0;}
			if(offY<paddU) {sigY=-1;}
			if(offY>paddD) {sigY=1;}
			if(GamePage.getCollision(tileX-1, tileY)
					||GamePage.getCollision(tileX-1, tileY+1)
					||GamePage.getCollision(tileX-1, tileY+sigY)
					||GamePage.getCollision(tileX-1,1+ tileY+sigY)) {
				offX+=dt*vX;
				if(offX<paddL) {
					offX=paddL;
				}
			}else {
				offX+=dt*vX;
			}
		}

		vY+=dt*fallSpeed;
		offY+=vY;

		if(offX>=paddL||offX<=paddR) {sigX=0;}
		if(offX<paddL) {sigX=-1;}
		if(offX>paddR) {sigX=1;}

		if(vY<0) {
			if((GamePage.getCollision(tileX, tileY-1)||GamePage.getCollision(tileX+sigX, tileY-1))&&offY<paddU) {
				vY=0;offY=paddU;
				}
			}

		if(vY>0) {
			if((GamePage.getCollision(tileX, tileY+height/GamePage.TS)||GamePage.getCollision(tileX+sigX, tileY+height/GamePage.TS))&&offY>paddD) {
				vY=0;ground=true;offY=paddD;
			}
			else {
				if(vY>5*dt*fallSpeed) {
					ground=false;
					}
				}
			}

		while (offY < -GamePage.TS/2) {offY += GamePage.TS;tileY--;}
		while (offY > GamePage.TS/2) {offY -= GamePage.TS;tileY++;}
		while (offX < -GamePage.TS/2) {offX += GamePage.TS;tileX--;}
		while (offX > GamePage.TS/2) {offX -= GamePage.TS;tileX++;}

		posX=tileX*GamePage.TS+offX;
		posY=tileY*GamePage.TS+offY;*/
	}

	/*public int getTileX() {
		return tileX;
	}

	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}*/
}
