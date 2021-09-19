package com.Michel.game;

import com.Michel.engine.GameContainer;
import com.Michel.engine.Renderer;
import com.Michel.engine.gfx.Image;
import com.Michel.pages.GamePage;

public class Chunk {
	
	private int[] blocks=new int[GamePage.L*GamePage.L];
	private int X,Y;
	
	public Chunk(String ref,int X,int Y) {
		this.X=X;this.Y=Y;
		Image levelImage = new Image(ref);
		for (int y = 0; y <GamePage.L; y++) {
			for (int x = 0; x <GamePage.L; x++) {
				blocks[x + y * GamePage.L]=levelImage.getP()[x + y * GamePage.L];
			}
		}
	}
	
	public Chunk(int[] p,int X,int Y) {
		this.X=X;this.Y=Y;
		for (int y = 0; y <GamePage.L; y++) {
			for (int x = 0; x <GamePage.L; x++) {
				blocks[x + y * GamePage.L]=p[x + y * GamePage.L];
			}
		}
	}
	
	public boolean getCollision(int x,int y) {
		return blocks[x + y * GamePage.L]>=0;
	}
	
	public int getBlock(int x,int y) {
		return blocks[x + y * GamePage.L];
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

}
