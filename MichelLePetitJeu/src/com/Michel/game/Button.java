package com.Michel.game;

import com.Michel.engine.GameContainer;
import com.Michel.engine.Renderer;
import com.Michel.pages.GamePage;
import com.Michel.pages.MenuPage;
import com.Michel.pages.TestPage;

public class Button extends GameObject{
	
	private String action;
	
	public Button(int width,int height,float posX,float posY,String action,String tag) {
		this.width=width;
		this.height=height;
		this.posX=posX;
		this.posY=posY;
		this.action=action;
		this.tag=tag;
	}
	
	public void update(GameContainer gc,GameManager gm,float dt) {
		
		int mouseX=gc.getInput().getMouseX();
		int mouseY=gc.getInput().getMouseY();
		
		if(posX+width<mouseX||mouseX<posX||posY+height<mouseY||mouseY<posY) {
			return;
		}
		
		doAction(gc,gm);
	}
	
	public void render(GameContainer gc,Renderer r) {
		r.drawFilledRect((int)posX+r.getCamX(), (int)posY+r.getCamY(), width, height, 0xff888888);
		r.drawRect((int)posX+r.getCamX(), (int)posY+r.getCamY(), width, height, 0xff000000);
	}
	
	private void doAction(GameContainer gc,GameManager gm) {
		switch(action) {
		case "Start game":
			if(gm.getCachePage()!=null) {
				gm.setCurrentPage(gm.getCachePage());
				gm.setCachePage(null);
			}else {
				gm.setCurrentPage(new GamePage());
			}
		    break;
		case "Move":
			posX=(float) (Math.random()*gc.getWidth()-width);
			posY=(float) (Math.random()*gc.getHeight()-height);
			break;
		case "Menu":
			if(gm.getCurrentPage().getClass()==GamePage.class) {
				gm.setCachePage(gm.getCurrentPage());
			}
			gm.setCurrentPage(new MenuPage());
			break;
		case "Start test":
			gm.setCurrentPage(new TestPage());
			break;
		}
	}
}
