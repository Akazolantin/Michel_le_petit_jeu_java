package com.Michel.pages;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import com.Michel.engine.GameContainer;
import com.Michel.engine.OpenSimplexNoise;
import com.Michel.engine.Renderer;
import com.Michel.engine.gfx.Image;
import com.Michel.game.Button;
import com.Michel.game.GameManager;

public class TestPage extends Page {
	
	private static int w;
	private static int h;
	private static int z=0;
	private static int increment=24;
	private static int [] p;
	private static OpenSimplexNoise noise;
	private static ArrayList<Button> buttons=new ArrayList<Button>();
	
	public TestPage() {
		w=GameContainer.width;
		h=GameContainer.height;
		p=new int[w*h];
		Arrays.fill(p, 0);
		background=new Image(p,w,h);
		noise = new OpenSimplexNoise();
		buttons.add(new Button(20,20,280,20,"Menu","menu"));
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
		
		for (int y = 0; y < h; y++){
			for (int x = 0; x < w; x++){
				double value = noise.eval((float)x/increment, (float)y/increment,(float)z/(increment*5));
				int rgb=0xff010101*(int)(255*(1+value)/2);
				
				p[x+ y*w]= rgb;
			}
		}
		background.setP(p);
		z++;
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
