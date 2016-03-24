import java.awt.image.BufferedImage;

public class ImageOperations {
	private LiteRoom _app;
	
	public ImageOperations(LiteRoom app){
		_app = app;
	}
	
	
	public void applyFilter(Filter f){
		f.applyFilter(_app.getImage());
		_app.repaint();
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
	
	public void hHistogramEqualize(float alpha){
		int x,y;
		int[] histogram = _app.calculateHistogram(0);
		float[] cdfHistogram = new float[histogram.length];
		float rFactor = .299f;
		float gFactor = .587f;
		float bFactor = .114f;
		float rDiff,bDiff;
		
		
		
		BufferedImage img = _app.getImage();
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
		
		_app.calculateHistogram(0);
		_app.repaint();
		
		
	}
	
	public void nHistogramEqualize(){
		int x,y;
		int[] histogram = _app.calculateHistogram(0);
		float[] cdfHistogram = new float[histogram.length];
		float rFactor = .299f;
		float gFactor = .587f;
		float bFactor = .114f;
		float rFrac,gFrac,bFrac,iFrac;
		
		BufferedImage img = _app.getImage();
		Pixel p = new Pixel();
		
		
		if(img != null){
			int width = img.getWidth();
			int height = img.getHeight();
			int numPixels = width*height;
			int sum = 0;
			float intensity,oldIntensity;
			float newRed;
			float newGreen;
			float newBlue;
			float rgbTriplet;
			
			for(int i=0;i<histogram.length;i++){
				sum += histogram[i];
				cdfHistogram[i] = (float)sum/(float)numPixels;
			}
			
			for(x=0;x<width;x++){
				for(y=0;y<height;y++){
					p.setPixel(img.getRGB(x, y));
					
					rgbTriplet = (float)p.getRed() + (float)p.getGreen() + (float)p.getBlue();
					//get color ratios
					rFrac = (float)p.getRed()/(rgbTriplet);
					gFrac = (float)p.getGreen()/(rgbTriplet);
					bFrac = (float)p.getBlue()/(rgbTriplet);
					
					//get old intensity/luma value
					oldIntensity = p.getIntensity();
					
					//get new intensity/luma value
					intensity = cdfHistogram[(int)p.getIntensity()] * 255f;
					
					iFrac = intensity/oldIntensity;
					
					newRed = iFrac * rFrac * rgbTriplet;
					newGreen = iFrac * gFrac * rgbTriplet;
					newBlue = iFrac * gFrac * rgbTriplet;
					
					p.setRed((int)newRed);
					p.setGreen((int)newGreen);
					p.setBlue((int)newBlue);
					img.setRGB(x, y, p.getPixel());
				}
			}
		}
		
		_app.calculateHistogram(0);
		_app.repaint();
		
		
	}
}
