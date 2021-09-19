package com.Michel.engine;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.Michel.engine.gfx.Font;
import com.Michel.engine.gfx.Image;
import com.Michel.engine.gfx.ImageRequest;
import com.Michel.engine.gfx.ImageTile;
import com.Michel.engine.gfx.Light;
import com.Michel.engine.gfx.LightRequest;

public class Renderer {

	private int pH,pW;
	private int [] p;
	private Font font= Font.STANDARD;
	private int[] zb;
	private int zDepth=0;
	private ArrayList<ImageRequest> imageRequests=new ArrayList<ImageRequest>();
	private ArrayList<LightRequest> lightRequests=new ArrayList<LightRequest>();
	private boolean processing=false;
	private int [] lm,lb;
	private int ambientColor=0xff999999;
	private static int camX,camY;

	public Renderer(GameContainer gc) {
		pW=gc.getWidth();
		pH=gc.getHeight();
		p=((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
		zb=new int [p.length];
		lm=new int [p.length];
		lb=new int [p.length];
	}

	public void clear() {
		for(int i=0;i<p.length;i++) {
			p[i]=0xffffffff;
			zb[i]=0;
			lm[i]=ambientColor;
			lb[i]=0;
		}
	}

	public void setPixel(int x,int y,int value) {
		int alpha=((value>>24)&0xff);
		x-=camX;y-=camY;
		if(x<0||x>=pW||y<0||y>=pH||alpha==0) {return;}
		int index =x+y*pW;
		if(zb[index]>zDepth) {return;}
		zb[index]=zDepth;
		if(alpha==255) {p[x+y*pW]=value;}
		else {
			int pixelColor=p[index];
			int newRed=((pixelColor>>16)&0xff) -(int) ((((pixelColor>>16)&0xff)-((value>>16)&0xff))*(alpha/255f)) ;
			int newGreen=((pixelColor>>8)&0xff) -(int) ((((pixelColor>>8)&0xff)-((value>>8)&0xff))*(alpha/255f)) ;
			int newBlue=((pixelColor)&0xff) -(int) (((pixelColor&0xff)-((value)&0xff))*(alpha/255f)) ;
			p[index]=(newRed<<16|newGreen<<8|newBlue);
		}
	}

	public void setLightMap(int x, int y,int value) {
		x-=camX;y-=camY;
		if(x<0||y<0||x>=pW||y>=pH) {return;}

		int baseColor = lm[x+y*pW];

		int maxRed=Math.max(((baseColor>>16)&0xff), ((value>>16)&0xff));
		int maxGreen=Math.max(((baseColor>>8)&0xff), ((value>>8)&0xff));
		int maxBlue=Math.max((baseColor&0xff), (value&0xff));

		lm[x+y*pW]=(maxRed<<16|maxGreen<<8|maxBlue);
	}

	public void setLightBlock(int x, int y,int value) {
		int alpha=((value>>24)&0xff);
		x-=camX;y-=camY;
		if(x<0||y<0||x>=pW||y>=pH||alpha==0) {return;}
		if(zb[x+y*pW]>zDepth) {return;}
		lb[x+y*pW]=1;
	}

	public void drawImage(Image image, int offX,int offY) {


		if(offX+image.getW()<camX) {return;}
		if(offY+image.getH()<camY) {return;}
		if(offX>=pW+camX) {return;}
		if(offY>=pH+camY) {return;}

		if(image.isAlpha()&&!processing) {
			imageRequests.add(new ImageRequest(image,zDepth,offX,offY));
			return;
		}

		int newX=0;
		int newY=0;
		int newW=image.getW();
		int newH=image.getH();

		if(offX<camX) {newX-=offX-camX;}
		if(offY<camY) {newY-=offY-camY;}
		if(offX+newW>=pW+camX) {newW=pW+camX-offX;}
		if(offY+newH>=pH+camY) {newH=pH+camY-offY;}

		for(int y=newY;y<newH;y++) {
			for(int x=newX;x<newW;x++) {
				setPixel(x+offX,y+offY,image.getP()[x+y*image.getW()]);
			}
		}
		if(image.getLightBlock()) {
			for(int y=newY;y<newH;y++) {
				for(int x=newX;x<newW;x++) {
					setLightBlock(x+offX,y+offY,image.getP()[x+y*image.getW()]);
				}}
		}
	}

	public void drawImageRotate(Image image, int offX,int offY,int cX,int cY,double angle) {
		if(image.isReverse()) {angle=-angle;cX=image.getW()-cX;}
		double maxX=Math.max(image.getW()-cX,cX);
		double maxY=Math.max(image.getH()-cY,cY);
		int radius=(int)Math.sqrt(maxX*maxX+maxY*maxY);
		radius++;
		int diameter=radius*2+1;
		if(offX<-radius+camX) {return;}
		if(offY<-radius+camY) {return;}
		if(offX>=pW+radius+camX) {return;}
		if(offY>=pH+radius+camY) {return;}
		if(image.isAlpha()&&!processing) {
			imageRequests.add(new ImageRequest(image,zDepth,offX,offY));
			return;
		}

		int newX=0;
		int newY=0;
		int newW=diameter;
		int newH=diameter;

		if(offX-radius<camX) {newX-=offX-camX;}
		if(offY-radius<camY) {newY-=offY-camY;}
		if(offX+radius>=pW+camX) {newW+=pW+camX-offX-radius;}
		if(offY+radius>=pH+camY) {newH+=pH+camY-offY-radius;}

		double radians=Math.toRadians(angle);
		double cosinus=Math.cos(radians);
		double sinus=Math.sin(radians);
		for(int y=newY;y<=newH;y++) {
			for(int x=newX;x<=newW;x++) {
				double iX =(x-radius)*cosinus-(y-radius)*sinus+cX;
				int imageX=(int)iX;
				if(iX%1>=0.5) {imageX++;}
				double iY =cosinus*(y-radius)+(x-radius)*sinus+cY;
				int imageY=(int)iY;
				if(iY%1>=0.5) {imageY++;}
				if(imageX==cX&&imageY==cY) {
				}
				if(imageY>=0&&imageX>=0&&imageY<image.getH()&&imageX<image.getW()) {
					setPixel( (x+offX-radius), (y+offY-radius),image.getP()[imageX+imageY*image.getW()]);
					if(image.getLightBlock()) {setLightBlock( (x+offX-radius), (y+offY-radius),image.getP()[imageX+(imageY)*image.getW()]);	
					}
				}
			}
		}
	}

	public void process() {
		processing=true;
		Collections.sort(imageRequests,new Comparator<ImageRequest>() {

			@Override
			public int compare(ImageRequest o1, ImageRequest o2) {
				if(o1.zDepth<o2.zDepth) {
					return -1;
				}
				if(o1.zDepth>o2.zDepth) {
					return 1;
				}
				return 0;
			}

		});

		for(int i=0;i<imageRequests.size();i++) {
			ImageRequest ir= imageRequests.get(i);
			setzDepth(ir.zDepth);
			drawImage(ir.image,ir.offX,ir.offY);
		}

		for(int i=0;i<lightRequests.size();i++) {
			LightRequest l= lightRequests.get(i);
			drawLightRequest(l.light,l.locX,l.locY);
		}

		for(int i=0;i<p.length;i++) {
			float r = ((lm[i]>>16)&0xff)/255f;
			float g = ((lm[i]>>8)&0xff)/255f;
			float b = (lm[i]&0xff)/255f;

			p[i]=((int)(((p[i]>>16)&0xff)*r)<<16|(int)(((p[i]>>8)&0xff)*g)<<8|(int)((p[i]&0xff)*b));
		}
		lightRequests.clear();
		imageRequests.clear();
		processing=false;
	}

	public void drawImageTile (ImageTile image,int offX,int offY,int tileX,int tileY) {
		if(offX+image.getTileW()<camX) {return;}
		if(offY+image.getTileH()<camY) {return;}
		if(offX>=pW+camX) {return;}
		if(offY>=pH+camY) {return;}

		if(image.isAlpha()&&!processing) {
			imageRequests.add(new ImageRequest(image.getTileImage(tileX, tileY),zDepth,offX,offY));
			return;
		}

		int newX=0;
		int newY=0;
		int newW=image.getTileW();
		int newH=image.getTileH();



		if(offX<camX) {newX-=offX-camX;}
		if(offY<camY) {newY-=offY-camY;}
		if(offX+newW>=pW+camX) {newW=pW+camX-offX;}
		if(offY+newH>=pH+camY) {newH=pH+camY-offY;}
		for(int y=newY;y<newH;y++) {
			for(int x=newX;x<newW;x++) {
				setPixel(x+offX,y+offY,image.getP()[(x+tileX*image.getTileW())+(y+tileY*image.getTileH())*image.getW()]);
			}
		}
		if(image.getLightBlock()) {
			for(int y=newY;y<newH;y++) {
				for(int x=newX;x<newW;x++) {
					setLightBlock(x+offX,y+offY,image.getP()[(x+tileX*image.getTileW())+(y+tileY*image.getTileH())*image.getW()]);
				}}}
		
		
	}
	
	public void drawImageTileRotate(ImageTile image, int offX,int offY,int tileX,int tileY,int cX,int cY,double angle) {
		if(image.isReverse()) {cX=image.getW()-cX;angle=-angle;}

		double maxX=Math.max(image.getTileW()-cX,cX);
		double maxY=Math.max(image.getTileH()-cY,cY);
		int radius=(int)Math.sqrt(maxX*maxX+maxY*maxY)+1;
		int diameter= ((radius)*2)+1;
		if(offX<-radius+camX) {return;}
		if(offY<-radius+camY) {return;}
		if(offX>=pW+radius+camX) {return;}
		if(offY>=pH+radius+camY) {return;}
		if(image.isAlpha()&&!processing) {
			imageRequests.add(new ImageRequest(image.getTileImage(tileX, tileY),zDepth,offX,offY));
			return;
		}

		int newX=0;
		int newY=0;
		int newW=diameter;
		int newH=diameter;

		if(offX-radius<camX) {newX-=offX-camX;}
		if(offY-radius<camY) {newY-=offY-camY;}
		if(offX+radius>=pW+camX) {newW+=pW+camX-offX-radius;}
		if(offY+radius>=pH+camY) {newH+=pH+camY-offY-radius;}

		double radians=Math.toRadians(angle);
		double cosinus=Math.cos(radians);
		double sinus=Math.sin(radians);
		for(int y=newY;y<=newH;y++) {
			for(int x=newX;x<=newW;x++) {
				double iX =(x-radius)*cosinus-(y-radius)*sinus+cX;
				int imageX = (int)iX;
				if(iX%1>=0.5) {imageX++;}
				double iY =cosinus*(y-radius)+(x-radius)*sinus+cY;
				int imageY=(int)iY;
				if(iY%1>=0.5) {imageY++;}
				if(imageY>=0&&imageX>=0&&imageY<image.getTileH()&&imageX<image.getTileW()) {
					setPixel( (x+offX-radius), (y+offY-radius),image.getP()[imageX+tileX*image.getTileW()+(imageY+tileY*image.getTileH())*image.getW()]);
					if(image.getLightBlock()) {setLightBlock(x+offX-radius,y+offY-radius,image.getP()[imageX+tileX*image.getTileW()+(imageY+tileY*image.getTileH())*image.getW()]);	
					}
				}
			}
		}
	}

	public void drawText (String text, int offX,int offY,int color) {
		offX+=camX;offY+=camY;
		int fontImageH=font.getFontImage().getH();
		int fontImageW=font.getFontImage().getW();
		text=text.toUpperCase();
		int offset=0;
		for(int i=0;i<text.length();i++) {
			int unicode=text.codePointAt(i)-32;
			for(int y=0;y<fontImageH;y++) {
				for(int x=0;x<font.getWidths()[unicode];x++) {
					if(font.getFontImage().getP()[(x+font.getOffsets()[unicode])+y*fontImageW]==0xffffffff) {
						setPixel(x + offset +offX,y+offY,color);
					}
				}
			}
			offset+=font.getWidths()[unicode];
		}
	}

	public void drawRect(int offX,int offY,int width,int height,int color) {
		if(offX+width<camX) {return;}
		if(offY+height<camY) {return;}
		if(offX>=pW+camX) {return;}
		if(offY>=pH+camY) {return;}

		int newX=0;
		int newY=0;
		int newW=width;
		int newH=height;

		if(offX<camX) {newX=offX-camX;}
		if(offY<camY) {newY=offY-camY;}
		if(offX+newW>=pW+camX) {newW=pW+camX-offX;}
		if(offY+newH>=pH+camY) {newH=pH+camY-offY;}
			
		for(int y=newY;y<=newH;y++) {
			setPixel(offX,y+offY,color);
			setPixel(offX+width,y+offY,color);
		}
		for(int x=newX;x<newW;x++) {
			setPixel(offX+x,offY,color);
			setPixel(offX+x,offY+height,color);
		}
	}

	public void drawFilledRect(int offX,int offY,int width,int height,int color) {
		
		if(offX+width<camX) {return;}
		if(offY+height<camY) {return;}
		if(offX>=pW+camX) {return;}
		if(offY>=pH+camY) {return;}

		int newX=0;
		int newY=0;
		int newW=width;
		int newH=height;

		if(offX<camX) {newX=offX-camX;}
		if(offY<camY) {newY=offY-camY;}
		if(offX+newW>=pW+camX) {newW=pW+camX-offX;}
		if(offY+newH>=pH+camY) {newH=pH+camY-offY;}
		
		for(int y=newY;y<newH;y++) {
			for(int x=newX;x<newW;x++) {
				setPixel(offX+x,offY+y,color);
			}
		}
	}

	public void drawLight(Light l,int offX,int offY) {
		lightRequests.add(new LightRequest(l,offX,offY));
	}

	private void drawLightRequest(Light l,int offX,int offY) {
		for(int i=0;i<=l.getDiameter();i++) {
			drawLightLine(l,l.getRadius(),l.getRadius(),i,0,offX,offY);
			drawLightLine(l,l.getRadius(),l.getRadius(),i,l.getDiameter(),offX,offY);
			drawLightLine(l,l.getRadius(),l.getRadius(),0,i,offX,offY);
			drawLightLine(l,l.getRadius(),l.getRadius(),l.getDiameter(),i,offX,offY);
		}
	}

	public void drawLightLine(Light l, int x0,int y0,int x1,int y1,int offX,int offY) {
		int dx=Math.abs(x0-x1);
		int dy=Math.abs(y0-y1);

		int sx=x0<x1?1:-1;
		int sy=y0<y1?1:-1;

		int err=dx-dy;
		int e2;

		while(true) {
			int screenX=x0-l.getRadius()+offX;
			int screenY=y0-l.getRadius()+offY;

			if(screenX<0||screenX>=pW||screenY<0||screenY>=pH) {
				return;
			}

			if(lb[screenX+screenY*pW]==Light.FULL)return;

			int lightColor=l.getLightValue(x0, y0);
			if(lightColor==0) {return;}



			setLightMap(screenX,screenY,l.getLm()[x0+y0*l.getDiameter()]);
			if(x0==x1&&y0==y1) {
				break;
			}
			e2=2*err;
			if(e2>-1*dy) {
				err-=dy;
				x0+=sx;
			}
			if(e2<dx) {
				err+=dx;
				y0+=sy;
			}
		}
	}

	public int getzDepth() {
		return zDepth;
	}

	public void setzDepth(int zDepth) {
		this.zDepth = zDepth;
	}

	public int getCamX() {
		return camX;
	}

	public void setCamX(int camX) {
		this.camX = camX;
	}

	public int getCamY() {
		return camY;
	}

	public void setCamY(int camY) {
		this.camY = camY;
	}
}
