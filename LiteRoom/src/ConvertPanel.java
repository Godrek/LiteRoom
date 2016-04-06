import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConvertPanel extends JPanel{
	
	private JPanel convertSubPanel;
	private JLabel convert;
	private JButton convertToRedB;
	private JButton convertToGreenB;
	private JButton convertToBlueB;
	
	public ConvertPanel(LiteRoom app){
		this.setLayout(new GridLayout(2,1));
		this.setFocusable(false);
		
		convertSubPanel = new JPanel();
		convertSubPanel.setLayout(new GridLayout(1,3));
		
		convert = new JLabel("CONVERT");
		this.add(convert);
		
		convertToRedB = new JButton("R");
		convertToGreenB = new JButton("G");
		convertToBlueB = new JButton("B");
		
		convertToRedB.addActionListener(new ConvertRedButtonHandler(app));
		convertToGreenB.addActionListener(new ConvertGreenButtonHandler(app));
		convertToBlueB.addActionListener(new ConvertBlueButtonHandler(app));
	
		convertSubPanel.add(convertToRedB);
		convertSubPanel.add(convertToGreenB);
		convertSubPanel.add(convertToBlueB);
		
		this.add(convertSubPanel);
	
	}
	
	public void convertColor(){
		
	}
	
	
	//can simplify into 1 handler with getSource?
	private class ConvertRedButtonHandler implements ActionListener{
		private LiteRoom _app;
		public ConvertRedButtonHandler(LiteRoom app){
			_app = app;
		}
		
		public void actionPerformed(ActionEvent e){
			_app.convertColor(0);
		}
	}
	private class ConvertGreenButtonHandler implements ActionListener{
		private LiteRoom _app;
		public ConvertGreenButtonHandler(LiteRoom app){
			_app = app;
		}
		public void actionPerformed(ActionEvent e){
			_app.convertColor(1);
		}
	}
	private class ConvertBlueButtonHandler implements ActionListener{
		private LiteRoom _app;
		public ConvertBlueButtonHandler(LiteRoom app){
			_app = app;
		}
		public void actionPerformed(ActionEvent e){
			_app.convertColor(2);
		}
	}
}
