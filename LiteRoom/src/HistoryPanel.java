import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HistoryPanel extends JPanel{
	private JButton reload, undo, redo;
	private JPanel historySubPanel;
	
	public HistoryPanel(LiteRoom app){
		this.setLayout(new GridLayout(2,1));
		
		reload = new JButton("RELOAD");
		reload.addActionListener(new ReloadImageButtonHandler(app));
		undo = new JButton("UNDO");
		undo.addActionListener(new UndoOperationButtonHandler(app));
		redo = new JButton("REDO");
		redo.addActionListener(new RedoOperationButtonHandler(app));
		
		historySubPanel = new JPanel();
		historySubPanel.setLayout(new GridLayout(1,2));
		historySubPanel.add(undo);
		historySubPanel.add(redo);
		this.add(reload);
		this.add(historySubPanel);
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
	
	private class UndoOperationButtonHandler implements ActionListener{
		private LiteRoom _app;
		public UndoOperationButtonHandler(LiteRoom app){
			_app = app;
		}
		public void actionPerformed(ActionEvent e){
			_app.undoOperation();
		}
	}
	
	private class RedoOperationButtonHandler implements ActionListener{
		private LiteRoom _app;
		public RedoOperationButtonHandler(LiteRoom app){
			_app = app;
		}
		public void actionPerformed(ActionEvent e){
			_app.redoOperation();
		}
	}
	
}
