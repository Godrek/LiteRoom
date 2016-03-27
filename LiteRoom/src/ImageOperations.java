import java.awt.image.BufferedImage;

public class ImageOperations {
	private LiteRoom _app;
	
	public ImageOperations(LiteRoom app){
		_app = app;
	}
	
	
	public void applyFilter(Filter f){
		BufferedImage img = f.applyFilter(_app.getDCImage());
		_app.updateImage(img);
		_app.repaint();
	}
	
	public void convertColor(int choice){
		int x,y;
		BufferedImage img = _app.getDCImage();
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
		_app.updateImage(img);
		_app.repaint();
	}
	
	public void hHistogramEqualize(float alpha){
		int x,y;
		int[] histogram = _app.calculateHistogram(0);
		float[] cdfHistogram = new float[histogram.length];
		float rFactor = .299f;
		float gFactor = .587f;
		float bFactor = .114f;
		float rDiff,bDiff;
		BufferedImage img = _app.getDCImage();
		Pixel p = new Pixel();
		
		if(img != null){
			int width = img.getWidth();
			int height = img.getHeight();
			int numPixels = width*height;
			int sum = 0;
			float intensity;
			float newRed;
			float newGreen;
			float newBlue;
			
			for(int i=0;i<histogram.length;i++){
				sum += histogram[i];
				cdfHistogram[i] = (float)sum/(float)numPixels;
			}
			
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
					p.setPixel(img.getRGB(x, y));
					rDiff = (float)p.getRed()/(float)p.getGreen();
					bDiff = (float)p.getBlue()/(float)p.getGreen();
					intensity = cdfHistogram[(int)p.getIntensity()] * 255f;
					intensity = alpha*intensity + (1-alpha)*p.getIntensity();
					newGreen = intensity/((rDiff*rFactor) + gFactor + (bDiff * bFactor));
					newRed = newGreen * rDiff;
					newBlue = newGreen * bDiff;
					p.setRed((int)newRed);
					p.setGreen((int)newGreen);
					p.setBlue((int)newBlue);
					img.setRGB(x, y, p.getPixel());
				}
			}
		}	
		_app.updateImage(img);
		_app.calculateHistogram(0);
		_app.repaint();
	}
}
