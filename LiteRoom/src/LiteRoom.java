import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class LiteRoom {
	private ImagePanel imgPanel;
	private HistoryPanel historyPanel;
	private ConvertPanel convertPanel;
	private ImageOperations imgOperations;
	private FilterPanel filterPanel;
	private JPanel opPanel;
	private HistogramPanel hgPanel;
	private KeysListener keyListener;
	
	JFrame frame;
	
	public LiteRoom()
	{
		frame = new JFrame("LiteRoom");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		keyListener = new KeysListener();
		frame.addKeyListener(keyListener);
		
		//get screen dimensions and scale for half width and 2/3 height
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)(screenSize.getWidth()/2);
		int height = (int)(screenSize.getHeight()*2)/3;
		frame.setSize(width,height);
		
		//initialize operations class
		imgOperations = new ImageOperations(this);

		
		//initialize panels
		imgPanel = new ImagePanel(this);
		opPanel = new JPanel();
		opPanel.setLayout(new GridLayout(8,1));

		
		historyPanel = new HistoryPanel(this);
		convertPanel = new ConvertPanel(this);
		filterPanel = new FilterPanel(this);
		hgPanel = new HistogramPanel(this, new Histogram(this));
		
		//add operation panels to opPanel
		opPanel.add(historyPanel);
		opPanel.add(convertPanel);
		opPanel.add(hgPanel);
		opPanel.add(filterPanel);
		
		//add frame elements
		frame.getContentPane().setLayout(new BorderLayout());
		
		frame.setJMenuBar(new TopMenuBar(this));
		frame.add(imgPanel,BorderLayout.CENTER);
		frame.add(opPanel, BorderLayout.EAST);
		//frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
	}
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				@SuppressWarnings("unused")
				LiteRoom app = new LiteRoom();
			}
		});
	}
	
	public void reloadImage(){
		imgPanel.reloadImage();
	}
	
	public void loadImage(String filepath){
		imgPanel.loadImage(filepath);
	}
	
	public void convertColor(int choice){
		imgOperations.convertColor(choice);
		repaint();
	}
	
	public BufferedImage getImage(){
		return imgPanel.getImage();
	}
	
	public BufferedImage getDCImage(){
		return imgPanel.getDCImage();
	}
	
	public void updateImage(BufferedImage img){
		imgPanel.updateImage(img);
	}
	
	public void repaint(){
		frame.repaint();
		frame.requestFocus();
	}
	
	public int[] calculateHistogram(int choice){
		return hgPanel.calculateHistogram(choice);
	}
	
	public void histogramEqualize(float alpha){
		imgOperations.hHistogramEqualize(alpha);
	}
	
	public void undoOperation(){
		imgPanel.undoOperation();
	}
	
	public void redoOperation(){
		imgPanel.redoOperation();
	}
	
	public void applyFilter(Filter f){
		imgOperations.applyFilter(f);
	}
	
	private class KeysListener implements KeyListener{
		@Override
		public void keyPressed(KeyEvent e) {
			if (!e.isShiftDown() && e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
				imgPanel.undoOperation();
			}
			else if(e.isShiftDown() && e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z){
				imgPanel.redoOperation();
			}
		}
		@Override
		public void keyReleased(KeyEvent arg0) {}
		@Override
		public void keyTyped(KeyEvent arg0) {}
	}
}
