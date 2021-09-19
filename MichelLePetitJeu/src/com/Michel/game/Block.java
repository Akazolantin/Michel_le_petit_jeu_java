package com.Michel.game;

public class Block {
	
	private int left=0,right=0,top=0,bottom=0;
	private boolean cS=true,cT=true,cB=true;
	private boolean transparent=false;
	
	public Block(int L,int R,int T,int B,boolean cS,boolean cT,boolean cB,boolean transparent) {
		this.left=L;this.right=R;this.top=T;this.bottom=B;
		this.cS=cS;this.cT=cT;this.cB=cB;
		this.transparent=transparent;
	}
	
	public Block(int L,int R,int T,int B,boolean transparent) {
		this.left=L;this.right=R;this.top=T;this.bottom=B;
		this.transparent=transparent;
	}
	
	public Block(int W,int H,boolean transparent) {
		left=right=W/2;
		top=bottom=H/2;
		this.transparent=transparent;
	}
	
	public Block(boolean solid,boolean transparent) {
		cS=cT=cB=solid;
		this.transparent=transparent;
	}
	public Block() {
		
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public boolean iscS() {
		return cS;
	}

	public void setcS(boolean cS) {
		this.cS = cS;
	}

	public boolean iscT() {
		return cT;
	}

	public void setcT(boolean cT) {
		this.cT = cT;
	}

	public boolean iscB() {
		return cB;
	}

	public void setcB(boolean cB) {
		this.cB = cB;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}
}
