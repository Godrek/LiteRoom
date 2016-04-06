import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.activation.MimetypesFileTypeMap;
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
	private DropListener dropListener;
	private DropTarget dropTarget;
	
	public ImagePanel(LiteRoom app){
		this.setFocusable(false);
		undoStack = new Stack<BufferedImage>();
		redoStack = new Stack<BufferedImage>();
		icon = new JLabel();
	    dropListener = new DropListener();
	    dropTarget = new DropTarget(this, dropListener);
		this.add(icon);
		_app = app;
	}
	
	public void loadImage(String filepath){
		try{
			File f = new File(filepath);
	        String type= new MimetypesFileTypeMap().getContentType(f).split("/")[0];
	        if(type.equals("image")){ //check if file is an image
				_srcImg = ImageIO.read(f);
				//uncomment to resize image to window, does not maintain aspect ratio
				//_srcImg = resize(_srcImg,this.getWidth(), this.getHeight());
				_img = deepCopy(_srcImg);
				_app.calculateHistogram(0);
				icon.setIcon(new ImageIcon(_img));
				_app.repaint();
	        }
	        else{
	        	ErrorPopup.infoBox("Innapropriate file type, please select an image instead", "ERROR");
	        }
		}catch (IOException e){}
	}
	
	public void loadImage(BufferedImage img){
		_srcImg = img;
		_img = deepCopy(_srcImg);
		_app.calculateHistogram(0);
		icon.setIcon(new ImageIcon(_img));
		_app.repaint();
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
	public void repaint(){
		
	}
	public BufferedImage resize(BufferedImage src, int w, int h)
	{
	  BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	  int x, y;
	  int ww = src.getWidth();
	  int hh = src.getHeight();
	  for (x = 0; x < w; x++) {
	    for (y = 0; y < h; y++) {
	      int col = src.getRGB(x * ww / w, y * hh / h);
	      img.setRGB(x, y, col);
	    }
	  }
	  return img;
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
	private class DropListener implements DropTargetListener{
		@Override
		public void drop(DropTargetDropEvent event) {
	        event.acceptDrop(DnDConstants.ACTION_COPY);
	        // grab the transferable item
	        Transferable t = event.getTransferable();
	        
	        //check if item is a file
	        if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            	try {
                	@SuppressWarnings("unchecked")
					java.util.List<File> files = (java.util.List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    loadImage(files.get(0).getAbsolutePath());
                } catch (UnsupportedFlavorException fExcept) {
                    fExcept.printStackTrace();
                } catch (IOException ioExcept) {
                    ioExcept.printStackTrace();
                }
    		}
	        event.dropComplete(true);
		}
		@Override
		public void dragEnter(DropTargetDragEvent dtde) {}
		@Override
		public void dragExit(DropTargetEvent dte) {}
		@Override
		public void dragOver(DropTargetDragEvent dtde) {}
		@Override
		public void dropActionChanged(DropTargetDragEvent dtde) {}
	}
	
}

