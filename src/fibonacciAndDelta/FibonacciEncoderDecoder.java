package fibonacciAndDelta;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

public class FibonacciEncoderDecoder 
{
	private String toCode;//the string to represent the FrequencyString
	private String toCodeInt;////the string to represent the toCode string but in a integer format, to write in the file itself
	private String outputFilePath;////the string of the path to the output file
	private StringBuilder[] codeWords;//a string to represent the Fibonacci code for each char
	private HashMap<Character, String> charToFibonacciCodes;//HashMap for easy access to the char and its Fibonacci code(for encoding)
	private HashMap<String, Character> fibonacciToCharCodes;//HashMap for easy access to the Fibonacci code and its char(for decoding)
	private List<Integer> fibonacciArray;//a list of the Fibonacci
	private byte[] inputToEncodeF;//input from the UniversalCodeEncoding class from the original file
	private byte[] inputToDecodeF;//input from the UniversalCodeEncoding class from the encoded file
	
	public FibonacciEncoderDecoder(String toCode,String outputFilePath)//a constructor
	{
		this.toCode=toCode;
		this.outputFilePath=outputFilePath;
		fibonacciArray=new ArrayList<Integer>();//make a dynamic list because we still don't know how many Fibonacci number we need
		charToFibonacciCodes = new HashMap<Character, String>();//initialize the map
		fibonacciToCharCodes= new HashMap<String,Character >();//initialize the map
		codeWords=new StringBuilder[toCode.length()];//its length is equal to toCode.length because that how many characters we need to code
		for(int i=0;i<toCode.length();i++)//initialize the codeWords
			codeWords[i]=new StringBuilder();
		
	}
	public void fibonacciEncoder()//a method to encode
	{
		this.fibonacciArrayBulid();//create the fibonacciArray
		this.codeWordsBulid();//create the Fibonacci codes for the Fibonacci numbers
		this.fibonacciMapBulid(true);//create the HashMap (its true because its encoding)
		this.writeFibonacciCode();//write the Fibonacci Code to the file.
		
		
	}	
	public void fibonacciDecoder()//a method to decode
	{
		this.fibonacciArrayBulid();//create the fibonacciArray
		this.codeWordsBulid();//create the Fibonacci codes for the Fibonacci
		this.fibonacciMapBulid(false);//create the HashMap (its false because its decoding)		
		this.writeOriginalCode();//write the Original Code to the file.
		
	}		
	public void fibonacciArrayBulid()//a method to create the fibonacciArray
	{
		int fibMax=this.toCode.length();//the length will be the maximum Fibonacci number we need
		//we skip the 0 and first 1 of the standard Fibonacci here because we don't need 0 and we already have 1
		fibonacciArray.add(1);  // fibonacciArray[0] stores 2nd Fibonacci No.
		fibonacciArray.add(2);  // fibonacciArray[1] stores 3rd Fibonacci No.
	    for (int i=2; fibonacciArray.get(i-1)<=fibMax; i++)//until we reached the fibMax
	    	fibonacciArray.add(fibonacciArray.get(i-1)+fibonacciArray.get(i-2));//the new number id the sum of the last 2	
	}	
	public void codeWordsBulid()//a method to create the Fibonacci codes for the Fibonacci numbers
	// for example we want to code the number 4 and we have 4 Fibonacci numbers then the array is : 1 2 3 5
	// now we think about it like binary..if we take it then put 1 and if dont then put 0 
	// we start at the end so to create 4 we dont need 5 then 0, we need 3 so 1 and now we do 4-3=1 so we need to create 1 now  
	// we dont need 2 then 0 but we do need 1 then 1 so we have: 1 2 3 5 
	//															 1 0 1 0 the 3 is the msb so -> 101
	//and we add 1 to the end because that how Fibonacci coding works so-> 1011
	//
	{		
		for(int i=0;i<toCode.length();i++)//we do this for each index, we start at index 0 but we treat it as 1
		{
			int fibNumber=i+1;//the number to code will be i+1
			int fibIndex=fibonacciArray.size()-1;//we always start with he biggest Fibonacci number 
			while(!(fibIndex==-1))//while we still didn't reached the last Fibonacci number
			{
				if(fibonacciArray.get(fibIndex)>fibNumber)//if the Fibonacci number is bigger then our fibNumber then
				{
					codeWords[i].append('0');//put zero
					fibIndex--;//the next Fibonacci number
				}
				else//if its smaller or equal
				{
					codeWords[i].append('1');//put zero
					fibNumber=fibNumber-fibonacciArray.get(fibIndex);//subtract the Fibonacci number from our number 
					fibIndex--;//the next Fibonacci number
				}
			}			
		}
		for(int i=0;i<codeWords.length;i++)//for each code that we have created
		{
			codeWords[i].reverse();//reverse it(because we started at the end)
			int index=codeWords[i].lastIndexOf("1")+1;//its the msb 
			codeWords[i].delete(index, codeWords[i].length());//we dont need 0 pass the msb
			codeWords[i].append("1");//put 1 because that how Fibonacci coding works 			
		}

	}
	public void fibonacciMapBulid(boolean EnOrDe)//a method to create the HashMap if its true then is for encoding else is for decoding
	{
		if(EnOrDe)//if true so encoding then 
		{
			for(int i=0;i<codeWords.length;i++)
				charToFibonacciCodes.put(toCode.charAt(i), codeWords[i].toString());//put the char and its counterpart code
		}
		else//so decoding then
		{
			for(int i=0;i<codeWords.length;i++)
			{
				fibonacciToCharCodes.put(codeWords[i].toString(),toCode.charAt(i));//put the code and its counterpart char
			}
		}			
	}	
	public void setInputToEncodeF(byte[] inputToEncodeF)//a setter
	{
		this.inputToEncodeF=inputToEncodeF;
	}
	public void setInputToDecodeF(byte[] inputToDecodeF)//a setter
	{
		this.inputToDecodeF=inputToDecodeF;
	}
	public void setToCodeInt(String toCodeInt)//a setter
	{
		this.toCodeInt=toCodeInt;
	}	
	public void writeFibonacciCode()
	{		
		File fileout = new File(this.outputFilePath);//the output file
		try 
        {
			if(!fileout.exists())//if new file exits then replace it if not create it 
        		fileout.createNewFile();
        	else
        	{
        		fileout.delete();
        		fileout.createNewFile();
        	}
			FileOutputStream out = new FileOutputStream(fileout,true);//for the new file. true is because we write twice to the file
        	BufferedOutputStream bitout = new BufferedOutputStream(out);//for output in byte form        	       	
        	StringBuilder all = new StringBuilder();//a StringBuilder which represent the Fibonacci code of the file in binary String format 
        	short length=(short) (toCodeInt.length());//the length of the toCodeInt string in short format	
			byte[] size=ByteBuffer.allocate(2).putShort(length).array();//a byte[] that will have our size
			out.write(size);//write the size first
			out.write(toCodeInt.getBytes());//write toCodeInt in byte format
        	char currentchar;//a char to represent the characters in inputToEncodeF
        	for (byte b : inputToEncodeF)//for each byte in inputToEncodeF
        	{
        		currentchar=(char)(b&0xFF);//cast the byte to char and save the characters ( b&0xFF because we want 0-255 and not -127 to 128)
        		String fibonacciString=charToFibonacciCodes.get(currentchar);//get the code of that characters from the HashMap
				all=all.append(fibonacciString);//append all the binaryStrings together
        	}      	
        	BitSet fibonacciCodeBit = new BitSet(all.length());//BitSet for all the bits on the binary String	
			for (int i = 0; i < all.length(); i++) 
			{
			    if(all.charAt(i) == '1')//if the char at the string is '1' then put 1 on in the Bitset..all the other bits are '0' by default
			    	fibonacciCodeBit.set(i);
			}
			bitout.write(fibonacciCodeBit.toByteArray());//get the Bitset in byte array format and then write it	
			bitout.close();
			out.close();
		} 
        catch (IOException e) 
		{
				e.printStackTrace();
		}	
	}			
	public void writeOriginalCode()
	{
		File fileout = new File(this.outputFilePath);//the output file
		try {
			if(!fileout.exists())//if new file exits then replace it if not create it 
        		fileout.createNewFile();
        	else
        	{
        		fileout.delete();
        		fileout.createNewFile();
        	}
			FileOutputStream out = new FileOutputStream(fileout);//for the new file
			BitSet inputDataBitsF=BitSet.valueOf(inputToDecodeF);//the inputToDecodeF in bit format
        	char currentchar;//the char to write to the file
        	StringBuilder bits = new StringBuilder();//a string which will represent the Fibonacci code
        	for (int i=0;i<inputDataBitsF.length()+1;i++)//for each byte in inputDataBitsF
        	{
        		if(inputDataBitsF.get(i)==true)//if its true then append '1' to bits
        		{
        			bits.append('1');
        		}
        		else
        		{
        			bits.append('0');
        		}
        		if(fibonacciToCharCodes.containsKey(bits.toString()))//check if the bits we have are a Fibonacci code, if not Continue until it is
        		{
        			currentchar=fibonacciToCharCodes.get(bits.toString());//if it is then get the char that he represent
        			out.write(currentchar);//write it
        			bits.setLength(0);//clear bits for the new Fibonacci code
        		}
            }
        	out.close();
		} 	
		catch (IOException e) 
		{
				e.printStackTrace();
		}
	}	
	public void TestPrintCodesWords()//a test function for personal use
	{
		for(int i=0;i<codeWords.length;i++)
		{
			System.out.println(codeWords[i]);			
		}		
	}
	public void TestPrintMap()//a test function for personal use
	{
		for (char key : this.charToFibonacciCodes.keySet()) {
		    System.out.println(key + " " + this.charToFibonacciCodes.get(key));
		}
	}
}
