import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class OperationsPanel extends JPanel{
	private LiteRoom _app;
	
	private JButton reload;
	private JButton equalize;
	
	public OperationsPanel(LiteRoom app){
		this.setLayout(new GridLayout(8,1));
		
		//Reload button doesn't need its own class because it is added
		reload = new JButton("RELOAD");
		reload.addActionListener(new ReloadImageButtonHandler(app));
		equalize = new JButton("EQUALIZE");
		equalize.addActionListener(new EqualizeHistogramButtonHandler(app));
		this.add(reload);
	}
	
	//this is sloppy, refactor, probably put in its own class
	public void addEqualize(){
		this.add(equalize);
	}
	
	private class ReloadImageButtonHandler implements ActionListener{
		private LiteRoom _app;
		public ReloadImageButtonHandler(LiteRoom app){
			_app = app;
		}
		public void actionPerformed(ActionEvent e){
			_app.reloadImage();
		}
	}
	
	private class EqualizeHistogramButtonHandler implements ActionListener{
		private LiteRoom _app;
		public EqualizeHistogramButtonHandler(LiteRoom app){
			_app = app;		
		}
		public void actionPerformed(ActionEvent e){
			_app.histogramEqualize();
		}
	}
}
