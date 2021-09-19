package com.Michel.pages;

import com.Michel.engine.GameContainer;
import com.Michel.engine.Renderer;
import com.Michel.engine.gfx.Image;
import com.Michel.game.GameManager;

public abstract class Page {
	
	protected Image background;

	public abstract void update(GameContainer gc,GameManager gm,float dt);
	public abstract void render(GameContainer gc,Renderer r);
}
