import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class topMenuBar extends JMenuBar {
	private JMenu fileMenu;
	private JMenuItem loadImageMenuItem;

	
	public topMenuBar(LiteRoom app){
		
		fileMenu = new JMenu("File");
		this.add(fileMenu);
		
		loadImageMenuItem = new JMenuItem("Load");
		loadImageMenuItem.addActionListener(new LoadImageButtonHandler(app));
		fileMenu.add(loadImageMenuItem);
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
}
