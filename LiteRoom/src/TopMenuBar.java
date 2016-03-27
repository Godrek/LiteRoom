import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class TopMenuBar extends JMenuBar {
	private JMenu fileMenu;
	private JMenuItem loadImageMenuItem;
	private JMenuItem saveImageMenuItem;
	
	public TopMenuBar(LiteRoom app){
		
		fileMenu = new JMenu("File");
		this.add(fileMenu);
		
		loadImageMenuItem = new JMenuItem("Load");
		loadImageMenuItem.addActionListener(new LoadImageButtonHandler(app));
		saveImageMenuItem = new JMenuItem("Save");
		saveImageMenuItem.addActionListener(new SaveImageButtonHandler(app));
		fileMenu.add(loadImageMenuItem);
		fileMenu.add(saveImageMenuItem);
	}
	
	private class LoadImageButtonHandler implements ActionListener {
			private LiteRoom _app;
			
			public LoadImageButtonHandler(LiteRoom app){
				_app = app;
			}
			public void actionPerformed(ActionEvent e){
				String directory,filename,imagepath;
				JFileChooser c = new JFileChooser();
				int rVal = c.showOpenDialog(getParent());
				if (rVal == JFileChooser.APPROVE_OPTION){
					directory = c.getCurrentDirectory().toString();
					filename = c.getSelectedFile().getName();
					imagepath = directory + "/" + filename;
					_app.loadImage(imagepath);
				}
			}
	}
	
	private class SaveImageButtonHandler implements ActionListener {
		private LiteRoom _app;
		
		public SaveImageButtonHandler(LiteRoom app){
			_app = app;
		}
		public void actionPerformed(ActionEvent e){
			
		     String format = "png";
             File saveFile = new File("savedimage."+format);
             JFileChooser c = new JFileChooser();
             c.setSelectedFile(saveFile);
             int rval = c.showSaveDialog(getParent());
             if (rval == JFileChooser.APPROVE_OPTION) {
                 saveFile = c.getSelectedFile();
                 /* Write the filtered image in the selected format,
                  * to the file chosen by the user.
                  */
                 try {
                     ImageIO.write(_app.getImage(), format, saveFile);
                 } catch (IOException ex) {
                 }
             }
		}
	}
}
