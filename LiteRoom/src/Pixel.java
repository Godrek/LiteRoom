
public class Pixel {
	private int _p;
	
	public Pixel(){
		_p = 0;
	}
	public Pixel(int p){
		_p = p;
	}
	
	public int getAlpha(){
		return ((_p>>24)&0xff);
	}
	public int getRed(){
		return ((_p>>16)&0xff);
	}
	public int getGreen(){
		return ((_p>>8)&0xff);
	}
	public int getBlue(){
		return _p&0xff;
	}
	public int getPixel(){
		return _p;
	}
	
	//Add checks to these to avoid spills when input values are > 255
	public void setAlpha(int a){
		if(a>255){
			a=255;
		} else if (a<0){
			a=0;
		}
		_p &= 0x00FFFFFF; _p += (a<<24);
	}
	public void setRed(int r){
		if(r>255){
			r=255;
		} else if (r<0){
			r=0;
		}
		_p &= 0xFF00FFFF; _p += (r<<16);
	}
	public void setGreen(int g){
		if(g>255){
			g=255;
		} else if (g<0){
			g=0;
		}
		_p &= 0xFFFF00FF; _p += (g<<8);
	}
	public void setBlue(int b){
		if(b>255){
			b=255;
		} else if (b<0){
			b=0;
		}
		_p &= 0xFFFFFF00; _p += b;
	}
	public void setPixel(int p){
		_p = p;
	}
	public int setGrayscale(int i){
		i += (i<<16);
		i += (i<<8);
		_p &= 0xFF000000; _p += i;
		//setRed(i);
		//setGreen(i);
		//setBlue(i);
		return _p;
	}
	
	public float getIntensity(){
		return 0.299f*((float)getRed())+0.587f*((float)getGreen())+0.114f*((float)getBlue());
	}
	
}
