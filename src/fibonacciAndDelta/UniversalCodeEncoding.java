package fibonacciAndDelta;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.PriorityQueue;


public class UniversalCodeEncoding 
{
	private String inputPath;//the string of the path to the input file
	private String outputPath;//the string of the path to the output file
	private String encodingType;//the type of encoding to show the user in the GUI
	private String[] outputNamesCoded;//what we need to add to the name of the encoded output file
	private String[] outputNamesNew;//what we need to add to the name of the decoded output file
	private StringBuilder sortedCharacters;//the StringBuilder to represent the chars from the FrequencyArray in max sort
	private StringBuilder sortedCharactersInts;//the StringBuilder to represent sortedCharacters but in int format
	private int[] FrequencyArray;//the array which will be our frequency table
	private PriorityQueue<CharContainer> priorityCharContainer;//PriorityQueue for creating the sortedCharacters
	private byte[] inputToEncode;//the input from the original file
	private byte[] inputToDecode;//the input from the encoded file

	
	
	public UniversalCodeEncoding(String input_names)//a constructor
	{
		this.inputPath=input_names;
		this.outputPath=new String();
		this.encodingType=new String();
		this.sortedCharacters=new StringBuilder();//initialize the sortedCharacters
		this.sortedCharactersInts=new StringBuilder();//initialize the sortedCharactersInts
		FrequencyArray = new int[256];//initialize the array
		priorityCharContainer=new PriorityQueue<CharContainer>(256, (e1, e2) -> { return e1.compareTo(e2); });//initialize the PriorityQueue with the Comparable function from CharContainer class 
		outputNamesCoded=new String[]{"_CodedD", "_CodedF"};//the names of the encoded output file
		outputNamesNew=new String[]{"_NewD", "_NewF"};//the names of the decoded output file
	}
	public void startEncoding()//a method to make all the necessary parameters to the encoding process and then call for the right encoding to start 
	{
		File originalFileInput = new File(inputPath);//the path to the input file
		this.InputToEncodData(originalFileInput);//create the inputToEncode
		this.frequencyArrayBulid();//build the frequencyArray
		this.sortedCharactersBulid();//create the sortedCharacters from the frequencyArray
	}
	public void startDecoding()//a method to make all the necessary parameters to the decoding process and then call for the right decoding to start 
	{
		File compressedFileInput = new File(inputPath);//the path to the input file
		String fileName=compressedFileInput.getName();//the name of the file		
		char encodingId=fileName.charAt(fileName.lastIndexOf(".")-1);//get the char for knowing which encoding it is
		this.InputToDecodData(compressedFileInput);//create the inputToDecode, the sortedCharacters and sortedCharactersInts from the input
		//determine  the type of encoding and activate it
		if(encodingId=='F')
		{
			this.fibonacciDe();//Fibonacci decoding
			encodingType="Fibonacci";
		}
		else if(encodingId=='D')
		{
			this.eliasDeltaDe();//EliasDelta decoding
			encodingType="EliasDelta";
		}			
	}	
	public void frequencyArrayBulid()////a method make the FrequencyArray 
	{
    	for (byte b : inputToEncode)//for each byte in inputToEncode
    	{
    		FrequencyArray[b&0xFF]++;//add 1 to the index which byte0xFF(because we want 0-255 and not -127 to 128) represent each time we encounters the character(byte b)
        }	        		        	
	}
	public void sortedCharactersBulid()//a method to create the sortedCharacters from the frequencyArray
	{
		for(int i=0;i<FrequencyArray.length;i++) //with this loop we build our PriorityQueue
	      {
	        if(FrequencyArray[i]>0)//if its 0 then that's means that there wasn't an occurrence of that character 
	        	priorityCharContainer.add(new CharContainer(i,FrequencyArray[i]));//create a CharContainer with its constructor and add it to the PriorityQueue
	      } 
		while (priorityCharContainer.size() > 0)//while we sill have a CharContainer
	     {
			CharContainer temp=priorityCharContainer.poll();//extract and save the max
			sortedCharacters.append(temp.getChar());//get the char and append to sortedCharacters
			sortedCharactersInts.append((int)temp.getChar()+",");//get the char in int format and append to sortedCharactersInts with , between each number			
	     }
	}
	public void InputToEncodData(File originalFileInput )//a method to create the inputToEncode 
	{   	       	
    	Path path = Paths.get(originalFileInput.getAbsolutePath());//the path to the input file
    	try 
    	{
			this.inputToEncode = Files.readAllBytes(path);//read all the byte and save it
			
		}     	
    	catch (IOException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//reading the file as byte and saving it in inputToEncode		
	}	
	public void InputToDecodData(File compressedFileInput)//a method to create the inputToDecode, the sortedCharacters and sortedCharactersInts from the input
	{
		
    	Path path = Paths.get(compressedFileInput.getAbsolutePath());//the path to the input file
    	try 
    	{
			this.inputToDecode = Files.readAllBytes(path);//read all the byte and save it				
			ByteBuffer sortedCharactersSizeBytes=ByteBuffer.wrap(inputToDecode, 0, 2);//the first 2 bytes is the size of the sortedCharactersInts String
        	short sortedCharactersSize = sortedCharactersSizeBytes.getShort();//get the short from the byte to know the size of the sortedCharactersInts string		
        	byte[] header = Arrays.copyOfRange(inputToDecode, 2, 2+sortedCharactersSize);//get the sortedCharactersInts and save it..its in byte format
        	String content = new String(header, "UTF-8");//create a string from the header
        	sortedCharacters = new StringBuilder();//initialize the sortedCharacters  	
        	for(String str: content.split(","))//for each string between the "," char
        	{
        		int num=Integer.valueOf(str);//parse it to int
        		sortedCharacters.append((char)num);//parse it to char and append it to sortedCharacters
        	}        	
        	inputToDecode =Arrays.copyOfRange(inputToDecode, 2+sortedCharactersSize, inputToDecode.length);//the rest rest of the inputToDecode is the coded data so we save only it without the header and sent it to the next class
		}     	
    	catch (IOException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//reading the file as byte and saving it in inputToDecode	
	}
	public void outputFilePathCreate(boolean EnOrDe,int id)//a method to create the string that will represent the outputFile, if its true then is for encoding else is for decoding and id for the type
	{
		StringBuilder tempStr=new StringBuilder(this.inputPath);//a temporary StringBuilder for the outputFilePath		
		if(EnOrDe)//if true so encoding then
		{
			int index=tempStr.lastIndexOf(".");//the index for the last '.' because we don't want to change its type
			tempStr.insert(index, outputNamesCoded[id]);//insert before the last .
			this.outputPath=tempStr.toString();//put it to outputFilePath
			
		}
		else //so decoding then
		{
			int index=tempStr.indexOf(outputNamesCoded[id]);//the index that the coded name start in
			tempStr.replace(index,index+ 7, outputNamesNew[id]);//replace the coded name with the decoded name,its plus 7 because all the coded name are 7 long
			this.outputPath=tempStr.toString();//put it to outputFilePath
		}		
	}
	public void fibonacciEn()//a method to start the Fibonacci encoding process
	{
		this.outputFilePathCreate(true,1);//true for encoding and 1 for Fibonacci
		FibonacciEncoderDecoder fibEncod = new FibonacciEncoderDecoder(this.sortedCharacters.toString(),this.outputPath);//create a FibonacciEncoderDecoder
		fibEncod.setInputToEncodeF(inputToEncode);//set its input as the inputToEncode
		fibEncod.setToCodeInt(sortedCharactersInts.toString());//set its toCodeInt as sortedCharactersInts
		fibEncod.fibonacciEncoder();//start encoding
	}	
	public void fibonacciDe()//a method to start the Fibonacci decoding process
	{
		this.outputFilePathCreate(false,1);//false for decoding and 1 for Fibonacci
		FibonacciEncoderDecoder fibDecode = new FibonacciEncoderDecoder(this.sortedCharacters.toString(),this.outputPath);//create a FibonacciEncoderDecoder
		fibDecode.setInputToDecodeF(inputToDecode);//set its input as the inputToDecode
		fibDecode.fibonacciDecoder();//start Decoding
	}
	public void eliasDeltaEn()//a method to start the EliasDelta encoding process
	{
		this.outputFilePathCreate(true,0);//true for encoding and 0 for EliasDelta
		EliasDeltaEncoderDecoder elDeEncod = new EliasDeltaEncoderDecoder(this.sortedCharacters.toString(),this.outputPath);//create a EliasDeltaEncoderDecoder
		elDeEncod.setInputToEncodeED(inputToEncode);//set its input as the inputToEncode
		elDeEncod.setToCodeInt(sortedCharactersInts.toString());//set its toCodeInt as sortedCharactersInts
		elDeEncod.eliasDeltaEncoder();//start encoding
	}	
	public void eliasDeltaDe()//a method to start the EliasDelta decoding process
	{
		this.outputFilePathCreate(false,0);//false for decoding and 0 for EliasDelta
		EliasDeltaEncoderDecoder elDeDecode = new EliasDeltaEncoderDecoder(this.sortedCharacters.toString(),this.outputPath);//create a EliasDeltaEncoderDecoder
		elDeDecode.setInputToDecodeED(inputToDecode);//set its input as the inputToDecode
		elDeDecode.eliasDeltaDecoder();//start Decoding
	}		
	public String getOutputPath()//a getter 
	{
		return this.outputPath;
	}
	public String getEncodingType()//a getter 
	{
		return this.encodingType;
	}
	public void TestPrintsortedCharacters()//a test function for personal use
	{
		System.out.println(sortedCharacters);
	}

}
