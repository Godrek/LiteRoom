import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private JLabel icon;
	private BufferedImage _srcImg, _img;
	private LiteRoom _app;
	
	public ImagePanel(LiteRoom app){
		icon = new JLabel();
		this.add(icon);
		_app = app;
	}
	
	public void loadImage(String filepath){
		try{
			_img = ImageIO.read(new File(filepath));
			_srcImg = ImageIO.read(new File(filepath));
			_app.calculateHistogram(0);
			icon.setIcon(new ImageIcon(_img));
		}catch (IOException e){}
	}
	public void reloadImage(){
		_img = deepCopy(_srcImg);
		_app.calculateHistogram(0);
		icon.setIcon(new ImageIcon(_img));
		_app.repaint();
	}
	
	public BufferedImage getImage(){
		return _img;
	}
	

	public BufferedImage deepCopy(BufferedImage bi){
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

}
