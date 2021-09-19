package com.Michel.game;

import com.Michel.engine.GameContainer;
import com.Michel.pages.GamePage;

public class Camera {

	private float offX,offY;
	private float vX,vY;

	private Player player;
	private int limitR,limitL,limitD,limitU;
	private boolean init=false;
	private float toY;

	public Camera(Player player) {
		this.player=player;
	}

	public void update (GameContainer gc,GameManager gm,float dt) {
		if(!init) {
			limitR=2*gc.getWidth()/3;
			limitL=gc.getWidth()/3;
			limitU=gc.getHeight()/4;
			limitD=3*gc.getHeight()/4;
			offX=player.getPosX()-gc.getWidth()/2;
			offY=player.getPosY()-gc.getHeight()/2;
			init=true;
		}
		
		float playerX = (player.getPosX()+player.getWidth()/2)-gc.getWidth()/2;
		float playerY = (player.getPosY()+player.getHeight()/2)-gc.getHeight()/2;

		//if playerX>limitR && playervX>0 go to right until 
		
		if(player.getPosX()+player.getWidth()/2<offX+limitL) {
			limitL=3*gc.getWidth()/5;
			limitR=7*gc.getWidth()/10;
			offX-=dt*(offX-playerX+limitL)/1.5;
		}
		if(player.getPosX()+player.getWidth()/2>offX+limitR) {
			limitL=3*gc.getWidth()/10;
			limitR=2*gc.getWidth()/5;
			//A refaire
			offX-=dt*(offX-playerX-limitR)/1.5;
		}

		if(player.isGround()) {
			limitU=gc.getHeight()/4;
			limitD=3*gc.getHeight()/4;
			toY=playerY;
		}

		if(player.getPosY()+player.getHeight()/2<offY+limitU) {
			limitU=3*gc.getHeight()/4;
			limitD=7*gc.getHeight()/8;
			//A refaire
			offY-=dt*(offY-playerY+limitU);
		}else {//A refaire
			offY-=dt*(offY-toY)/1.5;}

		if(player.getPosY()+player.getHeight()/2>offY+limitD) {
			limitU=gc.getHeight()/8;
			limitD=gc.getHeight()/4;
			//A refaire
			offY-=dt*(offY-playerY-limitD);
		}else {//A refaire
			offY-=dt*(offY-toY)/1.5;}



		if(offX<0) {offX=0;limitR=2*gc.getWidth()/3;
		limitL=gc.getWidth()/3;}
		if(offX+gc.getWidth()>=GamePage.getLvlW()*GamePage.TS*GamePage.L) {
			offX=GamePage.getLvlW()*GamePage.TS*GamePage.L-gc.getWidth();
			limitR=2*gc.getWidth()/3;
			limitL=gc.getWidth()/3;
		}
		if(offY<0) {offY=0;}
		if(offY+gc.getHeight()>=GamePage.getLvlH()*GamePage.TS*GamePage.L) {
			offY=GamePage.getLvlH()*GamePage.TS*GamePage.L-gc.getHeight();
		}
		gc.getRenderer().setCamX((int)offX);
		gc.getRenderer().setCamY((int)offY);
	}

	public float getOffX() {
		return offX;
	}

	public void setOffX(float offX) {
		this.offX = offX;
	}

	public float getOffY() {
		return offY;
	}

	public void setOffY(float offY) {
		this.offY = offY;
	}

	public GameObject getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
