package com.Michel.game;

import com.Michel.engine.GameContainer;
import com.Michel.engine.Renderer;

public class Ennemy extends Entity {
	private int colorPicker=0;
	private int[] colors=new int[] {0xff0000ff,0xff00ff00,0xffffff00,0xffff7f00,0xffff0000,0xff7f00ff,0xff000000}; 
	
	public Ennemy(int posX,int posY,int w,int h) {
		super(posX,posY);this.width=w;this.height=h;
	}
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		if(ground)vX=0;
		move(dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawFilledRect((int)posX, (int)posY, width, height, colors[colorPicker]);
	}

	@Override
	public void hurt(int fromX, int fromY, int damage,int knockBack) {
		// TODO Auto-generated method stub
		colorPicker++;
		if(colorPicker==colors.length) {
			dead=true;colorPicker--;
		}
		if(fromX>(int)posX+width/2) {
			vX-=knockBack;
		}else {
			vX+=knockBack;
		}
		vY=-2;
		ground=false;
	}

}
