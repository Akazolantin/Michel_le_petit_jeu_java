package com.Michel.engine.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {

	protected int w,h;
	protected int [] p;
	protected boolean alpha=false;
	protected boolean lightBlock=false;
	protected boolean reverse=false;

	public Image(String path) {

		BufferedImage image=null;
		try {
			image = ImageIO.read(Image.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		w=image.getWidth();
		h=image.getHeight();
		p=image.getRGB(0, 0, w, h, null, 0, w);

		image.flush();
	}
	
	public Image(int [] p ,int w,int h) {
		this.p=p;
		this.h=h;
		this.w=w;
	}


	public int getW() {
		return w;
	}


	public void setW(int w) {
		this.w = w;
	}


	public int getH() {
		return h;
	}


	public void setH(int h) {
		this.h = h;
	}


	public int[] getP() {
		return p;
	}


	public void setP(int[] p) {
		this.p = p;
	}


	public boolean isAlpha() {
		return alpha;
	}


	public void setAlpha(boolean alpha) {
		this.alpha = alpha;
	}

	public boolean getLightBlock() {
		return lightBlock;
	}

	public void setLightBlock(boolean lightBlock) {
		this.lightBlock = lightBlock;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		if(reverse^this.reverse) {
			int[] pMemory=new int[w*h];
			for(int y=0;y<h;y++) {
				for(int x=0;x<w;x++) {
					pMemory[x+y*w]=p[(w-1-x)+y*w];
				}
			}
			p=pMemory;
		}
		this.reverse = reverse;
	}
}
