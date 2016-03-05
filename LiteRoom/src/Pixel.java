
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
		_p &= 0x00FFFFFF; _p += (a<<24);
	}
	public void setRed(int r){
		_p &= 0xFF00FFFF; _p += (r<<16);
	}
	public void setGreen(int g){
		_p &= 0xFFFF00FF; _p += (g<<8);
	}
	public void setBlue(int b){
		_p &= 0xFFFFFF00; _p += b;
	}
	public void setPixel(int p){
		_p = p;
	}
	
	public float getIntensity(){
		return 0.299f*((float)getRed())+0.587f*((float)getGreen())+0.114f*((float)getBlue());
	}
	
}
