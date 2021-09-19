package com.Michel.pages;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.Michel.engine.GameContainer;
import com.Michel.engine.Renderer;
import com.Michel.engine.gfx.Image;
import com.Michel.game.Button;
import com.Michel.game.GameManager;

public class MenuPage extends Page {
	
	private ArrayList<Button> buttons=new ArrayList<Button>();
	
	public MenuPage() {
		background=new Image("/MenuBackground.png");
		buttons.add(new Button(100,50,100,100,"Start game","start"));
		buttons.add(new Button(100,50,100,175,"Start test","test"));
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
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.setCamX(0);
		r.setCamY(0);
		r.drawImage(background, 0, 0);
		for(Button button : buttons) {
			button.render(gc, r);
		}
	}

}
