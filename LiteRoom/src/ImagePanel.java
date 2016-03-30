import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * To-Do: limit depth of redo/undo stack
 */

public class ImagePanel extends JPanel {
	private JLabel icon;
	private BufferedImage _srcImg, _img, _prevImg;
	private Stack<BufferedImage> undoStack,redoStack;
	private LiteRoom _app;
	
	public ImagePanel(LiteRoom app){
		undoStack = new Stack<BufferedImage>();
		redoStack = new Stack<BufferedImage>();
		icon = new JLabel();
		this.add(icon);
		_app = app;
	}
	
	public void loadImage(String filepath){
		try{
			_srcImg = ImageIO.read(new File(filepath));
			_img = deepCopy(_srcImg);
			_app.calculateHistogram(0);
			icon.setIcon(new ImageIcon(_img));
			_app.repaint();
		}catch (IOException e){}
	}
	
	public void reloadImage(){
		undoStack.push(_img);
		while(redoStack.empty() == false){
			redoStack.pop();
		}
		_img = deepCopy(_srcImg);
		_app.calculateHistogram(0);
		icon.setIcon(new ImageIcon(_img));
		_app.repaint();
	}
	
	public void undoOperation(){
		if(undoStack.empty() == false){
			redoStack.push(_img);
			_img = deepCopy(undoStack.pop());
			_app.calculateHistogram(0);
			icon.setIcon(new ImageIcon(_img));
			_app.repaint();
		}
	}
	
	public void redoOperation(){
		if(redoStack.empty() == false){
			undoStack.push(_img);
			_img = deepCopy(redoStack.pop());
			_app.calculateHistogram(0);
			icon.setIcon(new ImageIcon(_img));
			_app.repaint();
		}
	}
	
	public BufferedImage getImage(){
		return _img;
	}
	
	public void updateImage(BufferedImage img){
		undoStack.push(deepCopy(_img));
		while(redoStack.empty() == false){
			redoStack.pop();
		}
		_img = deepCopy(img);
		icon.setIcon(new ImageIcon(_img));
		_app.repaint();
	}

	public BufferedImage deepCopy(BufferedImage bi){
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public BufferedImage getDCImage(){
		return deepCopy(_img);
	}
}
