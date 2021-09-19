package com.Michel.engine.gfx;

public class ImageTile extends Image{
	
	private int tileW,tileH;
	
	public ImageTile(String path,int tileW,int tileH) {
		super(path);
		this.tileW=tileW;
		this.tileH=tileH;
	}
	
	public Image getTileImage(int tileX,int tileY) {
		int []q=new int [tileW*tileH];
		
		for(int y=0;y<tileH;y++) {
			for(int x=0;x<tileW;x++) {
				q[x+y*tileW]=p[(x+tileX*w)+(y+tileY*tileH)*w];
			}
		}
		
		return new Image(q,tileW,tileH);
	}

	public int getTileW() {
		return tileW;
	}

	public void setTileW(int tileW) {
		this.tileW = tileW;
	}

	public int getTileH() {
		return tileH;
	}

	public void setTileH(int tileH) {
		this.tileH = tileH;
	}
	
	public void setReverse(boolean reverse) {
		if(reverse^this.reverse) {
			int[] pMemory=new int[w*h];
			for(int tY=0;tY<h/tileH;tY++) {
				for(int tX=0;tX<w/tileW;tX++) {
					for(int y=0;y<tileH;y++) {
						for(int x=0;x<tileW;x++) {
							pMemory[x+tX*tileW+(y+tY*tileH)*w]=p[tX*tileW+(tileW-1-x)+(y+tY*tileH)*w];
						}
					}
				}
			}
			
			p=pMemory;
		}
		this.reverse = reverse;
	}
}
