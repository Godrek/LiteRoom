import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class LiteRoom {
	private ImagePanel imgPanel;
	private ConvertPanel convertPanel;
	private ImageOperations imgOperations;
	private OperationsPanel opPanel;
	private HistogramPanel hgPanel;
	
	JFrame frame;
	
	public LiteRoom()
	{
		frame = new JFrame("LiteRoom");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800,900);
		
		//initialize operations class
		imgOperations = new ImageOperations(this);

		
		//initialize panels
		imgPanel = new ImagePanel(this);
		convertPanel = new ConvertPanel(this);
		imgOperations = new ImageOperations(this);
		opPanel = new OperationsPanel(this);
		hgPanel = new HistogramPanel(this);
		
		//add operation panels to opPanel
		opPanel.add(convertPanel);
		opPanel.add(hgPanel);
		opPanel.addEqualize();
		
		//add frame elements
		frame.getContentPane().setLayout(new BorderLayout());
		
		frame.setJMenuBar(new topMenuBar(this));
		frame.add(imgPanel,BorderLayout.CENTER);
		frame.add(opPanel, BorderLayout.EAST);
		
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
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
	
	public void repaint(){
		frame.repaint();
	}
	
	public int[] calculateHistogram(int choice){
		return hgPanel.calculateHistogram(choice);
	}
	
	public void histogramEqualize(){
		imgOperations.histogramEqualize();
	}
}
