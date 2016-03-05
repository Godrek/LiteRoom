import java.awt.image.BufferedImage;

public class ImageOperations {
	private LiteRoom _app;
	
	public ImageOperations(LiteRoom app){
		_app = app;
	}
	
	public void convertColor(int choice){
		int x,y;
		BufferedImage img = _app.getImage();
		Pixel p = new Pixel();
		
		for(x=0;x<img.getWidth();x++){
			for(y=0;y<img.getHeight();y++){
				p.setPixel(img.getRGB(x, y));
				switch(choice){
				case 0: p.setGreen(0);
						p.setBlue(0);;
						break;
				case 1: p.setRed(0);
						p.setBlue(0);
						break;
				case 2: p.setRed(0);
						p.setGreen(0);
						break;
				}
				img.setRGB(x, y, p.getPixel());
			}
		}
		_app.repaint();
	}
	
	public void histogramEqualize(){
		int x,y;
		int[] redHistogram = _app.calculateHistogram(1);
		int[] greenHistogram = _app.calculateHistogram(2);
		int[] blueHistogram = _app.calculateHistogram(3);
		float[] cdfRedHistogram = new float[redHistogram.length];
		float[] cdfGreenHistogram = new float[greenHistogram.length];
		float[] cdfBlueHistogram = new float[blueHistogram.length];
		
		
		BufferedImage img = _app.getImage();
		Pixel p = new Pixel();
		
		if(img != null){
			int width = img.getWidth();
			int height = img.getHeight();
			int numpixels = width * height;
			int sum = 0;	
			int max = 255;
			
			for(int i=0;i<redHistogram.length;i++){
				sum += redHistogram[i];
				cdfRedHistogram[i] = (float)sum/(float)numpixels;
			} sum = 0;
			for(int i=0;i<greenHistogram.length;i++){
				sum += greenHistogram[i];
				cdfGreenHistogram[i] = (float)sum/(float)numpixels;
			} sum = 0;
			for(int i=0;i<blueHistogram.length;i++){
				sum += blueHistogram[i];
				cdfBlueHistogram[i] = (float)sum/(float)numpixels;
			}
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
					p.setPixel(img.getRGB(x, y));
					p.setRed((int)(cdfRedHistogram[p.getRed()] * max));
					p.setGreen((int)(cdfGreenHistogram[p.getGreen()] * max));
					p.setBlue((int)(cdfBlueHistogram[p.getBlue()] * max));
					img.setRGB(x, y, p.getPixel());
				}
			}
		}
		_app.calculateHistogram(0);
		_app.repaint();
	}
	
}
