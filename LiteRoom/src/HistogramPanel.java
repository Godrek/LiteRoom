import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HistogramPanel extends JPanel{
	private int[] histogram;
	private LiteRoom _app;
	private int maxValue;
	
	public HistogramPanel(LiteRoom app){
		_app = app;
	}
	
	public int[] calculateHistogram(int choice){
		BufferedImage img = _app.getImage();
		int x,y;
		Pixel p = new Pixel();
		histogram = new int[256];
		maxValue = 0;
		
		for(x=0;x<img.getWidth();x++){
			for(y=0;y<img.getHeight();y++){
				p.setPixel(img.getRGB(x, y));
				switch(choice){
					case 0: histogram[(int)p.getIntensity()]++;
							break;
					case 1: histogram[(int)p.getRed()]++;
							break;
					case 2: histogram[(int)p.getGreen()]++;
							break;
					case 3: histogram[(int)p.getBlue()]++;
							break;
				}
			}
		}
		
		for(int i=0;i<histogram.length;i++){
			maxValue = Math.max(maxValue, histogram[i]);
		}
		
		return histogram;
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(256,400);
	}
	
	@Override
	public void paintComponent(Graphics g){
		int height = getHeight();
		
		if(histogram != null){
			for(int i=0;i<histogram.length;i++){
				g.drawLine(i, height - ((int)((((float)histogram[i])/((float)maxValue))*((float)height))), i, height);
				System.out.println("i: " + i + " histogram[i]: " + histogram[i] + " max value: " + maxValue + " height: " + height);
				System.out.println(((int)((((float)histogram[i])/((float)maxValue))*((float)height))));
				System.out.println();
			}
		}
	}
}
