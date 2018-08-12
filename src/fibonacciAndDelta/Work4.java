package fibonacciAndDelta;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class Work4 {

	private JFrame frame;
	private JTextField txtEncoderdecoder;
	private JTextField txtByMichaelBabaive;
	private JTextField txtEncodedMenu;
	private JFileChooser fileToEncodeChooser;
	private JFileChooser fileToDecodeChooser;
	private JPanel start;
	private JPanel encodMenu;
	private JPanel decodMenu;
	private UniversalCodeEncoding toEncode;
	private UniversalCodeEncoding toDecode;
	private File outputFile;
	private File fileToDecode;
	private File fileToEncode;
	private long originalLength;
	private JTextField txtDecodedMenu;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Work4 window = new Work4();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Work4() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		fileToEncodeChooser	= new JFileChooser(new File(System.getProperty("user.dir")));//////////////
		fileToDecodeChooser= new JFileChooser(new File(System.getProperty("user.dir")));///////////////
		frame = new JFrame();
		frame.setBounds(100, 100, 733, 435);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		frame.setTitle("MIchael and Alex Universal Code Encoding/Decodong");
		
		start = new JPanel();
		encodMenu = new JPanel();
		decodMenu = new JPanel();
		decodMenu.setBackground(Color.GRAY);
		frame.getContentPane().add(start, "name_5743502822073");
		start.setLayout(null);
		
		txtEncoderdecoder = new JTextField();
		txtEncoderdecoder.setEditable(false);
		txtEncoderdecoder.setFont(new Font("Tahoma", Font.PLAIN, 30));
		txtEncoderdecoder.setBounds(185, 48, 316, 51);
		txtEncoderdecoder.setText("Encoder/decoder");
		txtEncoderdecoder.setHorizontalAlignment(SwingConstants.CENTER);
		txtEncoderdecoder.setColumns(10);
		start.add(txtEncoderdecoder);
		
		txtByMichaelBabaive = new JTextField();
		txtByMichaelBabaive.setEditable(false);
		txtByMichaelBabaive.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtByMichaelBabaive.setBounds(212, 116, 261, 33);
		txtByMichaelBabaive.setText("by michael babaive");
		txtByMichaelBabaive.setHorizontalAlignment(SwingConstants.CENTER);
		txtByMichaelBabaive.setColumns(10);
		start.add(txtByMichaelBabaive);
		
		JLabel lbaleForEncode = new JLabel("");
		lbaleForEncode.setBounds(12, 214, 574, 16);
		start.add(lbaleForEncode);
		
		JLabel lbaleForDecoded = new JLabel("");
		lbaleForDecoded.setBounds(12, 307, 564, 16);
		start.add(lbaleForDecoded);
		start.setVisible(true);
		encodMenu.setVisible(false);
		decodMenu.setVisible(false);
		
		
		
		JLabel fileName = new JLabel();
		JLabel fibonacciSize = new JLabel();
		JLabel fileOriSize = new JLabel();
		JLabel EliasDeltaSize = new JLabel();
		JLabel encodingType = new JLabel();
		encodingType.setFont(new Font("Tahoma", Font.PLAIN, 20));
		encodingType.setForeground(new Color(255, 255, 255));
		encodingType.setBackground(new Color(255, 255, 255));
		JLabel fibSpaceSaved = new JLabel();
		JLabel DelSpaceSaved = new JLabel();
		
		
		JButton btnChooseFileToDecode = new JButton("Choose file to decode");
		btnChooseFileToDecode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnValue=fileToDecodeChooser.showOpenDialog(null);
				if(returnValue==JFileChooser.APPROVE_OPTION)
				{
					fileToDecode = fileToDecodeChooser.getSelectedFile();
					String pathToDecode=fileToDecode.getAbsolutePath();
					if(pathToDecode.contains("_CodedD")||pathToDecode.contains("_CodedF")||pathToDecode.contains("_CodedO")||pathToDecode.contains("_CodedG"))
					{
						
						lbaleForDecoded.setText("the name of the file is: "+fileToDecode.getName());
						toDecode = new UniversalCodeEncoding(pathToDecode);
						toDecode.startDecoding();					
						
						encodingType.setText("The encoding is: "+toDecode.getEncodingType());
						start.setVisible(false);
						encodMenu.setVisible(false);
						decodMenu.setVisible(true);
					}
					else
					{
						lbaleForDecoded.setText("this file is not encoded! please choose another one");
					}					
				}
				else
				{
					lbaleForDecoded.setText("no file choosen");
				}
				
			}
		});
		btnChooseFileToDecode.setBounds(12, 257, 179, 25);
		start.add(btnChooseFileToDecode);
		
		JButton btnChooseFileToEncode = new JButton("Choose file to encode");
		btnChooseFileToEncode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnValue=fileToEncodeChooser.showOpenDialog(null);
				if(returnValue==JFileChooser.APPROVE_OPTION)
				{
					 fileToEncode = fileToEncodeChooser.getSelectedFile();
					 String pathToEncode=fileToEncode.getAbsolutePath();					 
					 fileName.setText("File name: "+fileToEncode.getName());
					 if(pathToEncode.contains("_CodedD")||pathToEncode.contains("_CodedF")||pathToEncode.contains("_CodedO")||pathToEncode.contains("_CodedG"))
					 {
						 lbaleForEncode.setText("this file is already encoded! please choose another one"); 
					 }
					 else
					 {
						 toEncode= new UniversalCodeEncoding(pathToEncode);
						 toEncode.startEncoding();
						 lbaleForEncode.setText("the name of the file is: "+fileToEncode.getName());
						 originalLength=fileToEncode.length()/1024;
						 fileOriSize.setText("File original size: "+originalLength);
						 start.setVisible(false);
						 encodMenu.setVisible(true);
						 decodMenu.setVisible(false); 
					 }
					 
				}
				else
				{
					lbaleForEncode.setText("no file choosen");
				}
				
			}
		});
		btnChooseFileToEncode.setBounds(12, 174, 179, 25);
		start.add(btnChooseFileToEncode);
		
		
		
		frame.getContentPane().add(encodMenu, "name_5747107751268");
		encodMenu.setLayout(null);
		
		txtEncodedMenu = new JTextField();
		txtEncodedMenu.setEditable(false);
		txtEncodedMenu.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtEncodedMenu.setText("Encoded menu");
		txtEncodedMenu.setColumns(10);
		txtEncodedMenu.setBackground(Color.WHITE);
		txtEncodedMenu.setBounds(226, 13, 154, 36);
		encodMenu.add(txtEncodedMenu);
		
		fileName.setBounds(35, 71, 265, 16);
		encodMenu.add(fileName);
		
		fileOriSize.setBounds(35, 108, 265, 16);
		encodMenu.add(fileOriSize);
		
		JButton fibonacciBtn = new JButton("Fibonacci Encode");
		fibonacciBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toEncode.fibonacciEn();
				outputFile= new File(toEncode.getOutputPath());
				long compresedlLength=outputFile.length()/1024;
				fibonacciSize.setText("Encoded size: "+compresedlLength);
				int CompFibSize =(int) ((1-(compresedlLength/(double)originalLength))*100);
				fibSpaceSaved.setText("Space saved "+CompFibSize +"%" );
				
			}
		});
		fibonacciBtn.setHorizontalAlignment(SwingConstants.LEFT);
		fibonacciBtn.setBounds(12, 176, 142, 25);
		encodMenu.add(fibonacciBtn);
		
		JButton EliasDeltaBtn = new JButton("EliasDelta Encode");
		EliasDeltaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toEncode.eliasDeltaEn();
				outputFile= new File(toEncode.getOutputPath());
				long compresedlLength=outputFile.length()/1024;
				EliasDeltaSize.setText("Encoded size: "+compresedlLength);
				int CompFibSize =(int) ((1-(compresedlLength/(double)originalLength))*100);
				DelSpaceSaved.setText("Space saved "+CompFibSize +"%" );
			}
		});
		EliasDeltaBtn.setHorizontalAlignment(SwingConstants.LEFT);
		EliasDeltaBtn.setBounds(12, 224, 142, 25);
		encodMenu.add(EliasDeltaBtn);
		
		fibonacciSize.setBounds(213, 180, 229, 16);
		encodMenu.add(fibonacciSize);
		
		EliasDeltaSize.setBounds(213, 228, 229, 16);
		encodMenu.add(EliasDeltaSize);
		
		JButton toStartBtn1 = new JButton("back");
		toStartBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				start.setVisible(true);
				encodMenu.setVisible(false);
				decodMenu.setVisible(false);
			}
		});
		toStartBtn1.setBounds(489, 118, 97, 25);
		encodMenu.add(toStartBtn1);
		
		
		fibSpaceSaved.setBounds(489, 180, 186, 16);
		encodMenu.add(fibSpaceSaved);
		

		DelSpaceSaved.setBounds(489, 228, 186, 16);
		encodMenu.add(DelSpaceSaved);
		
		frame.getContentPane().add(decodMenu, "name_5752454053476");
		decodMenu.setLayout(null);
		
		encodingType.setBounds(45, 81, 422, 25);
		decodMenu.add(encodingType);
		
		JButton toStartBtn2 = new JButton("back");
		toStartBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				start.setVisible(true);
				encodMenu.setVisible(false);
				decodMenu.setVisible(false);
			}
		});
		toStartBtn2.setBounds(467, 176, 97, 25);
		decodMenu.add(toStartBtn2);
		
		txtDecodedMenu = new JTextField();
		txtDecodedMenu.setEditable(false);
		txtDecodedMenu.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtDecodedMenu.setText("Decoded menu");
		txtDecodedMenu.setColumns(10);
		txtDecodedMenu.setBackground(Color.WHITE);
		txtDecodedMenu.setBounds(222, 32, 151, 31);
		decodMenu.add(txtDecodedMenu);
	}
}
