package com.Michel.game;

import com.Michel.engine.AbstractGame;
import com.Michel.engine.GameContainer;
import com.Michel.engine.Renderer;
//import com.Michel.engine.gfx.Image;
import com.Michel.pages.MenuPage;
import com.Michel.pages.Page;

public class GameManager extends AbstractGame {
	
	private Page currentPage;
	private Page cachePage;
	
	public GameManager() {
		currentPage=new MenuPage();
	}

	@Override
	public void update(GameContainer gc, float dt) {
			currentPage.update(gc, this, dt);
		}

	@Override
	public void render(GameContainer gc, Renderer r) {
		currentPage.render(gc,r);
	}

	
	
	public static void main(String[] args) {
		GameContainer gc = new GameContainer(new GameManager());
		gc.start();
	}



	public Page getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Page currentPage) {
		this.currentPage = currentPage;
	}

	public Page getCachePage() {
		return cachePage;
	}

	public void setCachePage(Page cachePage) {
		this.cachePage = cachePage;
	}

}
