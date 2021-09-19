package com.Michel.game;

import java.awt.event.KeyEvent;

import com.Michel.animation.Body;
import com.Michel.animation.BodyPartIn;
import com.Michel.animation.BodyPartOut;
import com.Michel.engine.GameContainer;
import com.Michel.engine.Renderer;
import com.Michel.pages.GamePage;

public class Player extends Entity {
	private float jump = -3.3f;
	private float attackDelay=0;
	private int speed=120;
	private boolean attacking=false;
	private int attack=1;
	private int knockBack=100;

	private Body body;
	private BodyPartIn BBrasAV,BBrasAR;
	private BodyPartOut ABrasAV,ABrasAR;
	private BodyPartOut JambeAR,JambeAV;
	private BodyPartIn Tete;
	private int i=0;

	private int [] course=new int [] {0,20,35,45,50,45,35,20,0,-20,-35,-45,-50,-45,-35,-20};

	public Player(int posX, int posY) {
		super(posX,posY);
		this.tag = "player";
		
		this.posX = posX;
		this.posY = posY;
		
		this.width = 15;
		this.height = 32;
		
		body=new Body("/Michel/Corps.png",7,11,5,posX,posY);
		BBrasAV=new BodyPartIn("/Michel/BBrasAV.png",2,2,7,1,4);
		ABrasAV=new BodyPartOut("/Michel/ABrasAV.png","/Michel/BLinkAV.png",2,0,7,2,6,2,0);
		BBrasAR=new BodyPartIn("/Michel/BBrasAR.png",2,2,4,7,4);
		ABrasAR=new BodyPartOut("/Michel/ABrasAR.png","/Michel/BLinkAR.png",2,0,2,2,6,2,0);
		
		Tete=new BodyPartIn("/Michel/Tete.png",3,12,6,2,0);
		JambeAV=new BodyPartOut("/Michel/JambeAV.png","/Michel/JLinkAV.png",2,0,4,3,16,2,0);
		JambeAR=new BodyPartOut("/Michel/JambeAR.png","/Michel/JLinkAR.png",2,0,3,7,16,2,0);

		body.addBodyPart(Tete);
		body.addBodyPart(JambeAV);
		body.addBodyPart(JambeAR);
		body.addBodyPart(BBrasAV);body.addBodyPart(BBrasAR);
		BBrasAV.addBodyPart(ABrasAV);BBrasAR.addBodyPart(ABrasAR);
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		vX=0;
		
		if(attacking) {
			if(attackDelay<2) {
				attackDelay+=dt;
			}else {
				attackDelay=0;
				attacking=false;
			}
		}
		
		if(gc.getInput().isButton(1)&&!attacking) {
			attack();
		}
		
		if(gc.getInput().isKey(KeyEvent.VK_D)) {
			reverse=false;vX=speed;
		}

		if(gc.getInput().isKey(KeyEvent.VK_Q)) {
			reverse=true;vX=-speed;
		}
		
		if(!gc.getInput().isKey(KeyEvent.VK_Q)&&!gc.getInput().isKey(KeyEvent.VK_D)) {i=0;}
		if(gc.getInput().isKey(KeyEvent.VK_Z)&&ground) {vY=jump;ground=false;}
		
		move(dt);

	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		body.setOffX((int)posX+8);body.setOffY((int)posY+23);body.setReverse(reverse);if(reverse) {body.setOffX(body.getOffX()-1);}
		ABrasAR.setAngle(-course[i]);
		ABrasAV.setAngle(course[i]);
		BBrasAV.setAngle(course[i]);
		BBrasAR.setAngle(-course[i]);
		JambeAR.setAngle(course[i]);
		JambeAV.setAngle(-course[i]);
		body.render(gc, r);
		
		if(attacking) {
			if(reverse) {
				r.drawRect((int)posX-GamePage.TS,(int) posY, GamePage.TS, GamePage.TS, 0Xffff0000);
			}else {
				r.drawRect((int)posX+width,(int) posY, GamePage.TS, GamePage.TS, 0Xffff0000);
			}
		}
		
	}

	public int getTileX() {
		return (int)posX / GamePage.TS;
	}

	public int getTileY() {
		return (int)posY / GamePage.TS;
	}

	public boolean isGround() {
		return ground;
	}

	
	public void attack() {
		attacking=true;
		if(reverse) {
			GamePage.attack((int)posX+width/2,(int)posY+height/2,(int)posX-GamePage.TS,(int) posY, GamePage.TS, GamePage.TS, attack,knockBack);
		}else {
			GamePage.attack((int)posX+width/2,(int)posY+height/2,(int)posX+width,(int) posY, GamePage.TS, GamePage.TS, attack,knockBack);
		}
	}

	@Override
	public void hurt(int fromX, int fromY, int damage,int knockBack) {
		
	}

}
