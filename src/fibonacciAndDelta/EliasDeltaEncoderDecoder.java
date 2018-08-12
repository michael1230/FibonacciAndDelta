package fibonacciAndDelta;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.HashMap;

public class EliasDeltaEncoderDecoder 
{
	private int N;//a parameter of the EliasDelta encoding
	private int L;//a second parameter of the EliasDelta encoding
	private String toCode;//the string to represent the FrequencyString
	private String toCodeInt;//the string to represent the toCode string but in a integer format, to write in the file itself
	private String outputFilePath;////the string of the path to the output file
	private StringBuilder[] codeWords;//a string to represent the EliasDelta code for each char
	private HashMap<Character, String> charToEliasDeltaCodes;//HashMap for easy access to the char and its EliasDelta code(for encoding)
	private HashMap<String, Character> eliasDeltaToCharCodes;//HashMap for easy access to the EliasDelta code and its char(for decoding)
	private byte[] inputToEncodeED;//input from the UniversalCodeEncoding class from the original file
	private byte[] inputToDecodeED;//input from the UniversalCodeEncoding class from the encoded file
		
	public EliasDeltaEncoderDecoder(String toCode,String outputFilePath)//a constructor
	{
		
		N=0;//initialize to 0
		L=0;//initialize to 0
		this.toCode=toCode;
		this.outputFilePath=outputFilePath;
		charToEliasDeltaCodes= new HashMap<Character, String>();//initialize the map
		eliasDeltaToCharCodes= new HashMap<String,Character >();//initialize the map
		codeWords=new StringBuilder[toCode.length()];//its length is equal to toCode.length because that how many characters we need to code
		for(int i=0;i<toCode.length();i++)//initialize the codeWords
			codeWords[i]=new StringBuilder();
	
	}
	public void codeWordsBulid()//a method to create the EliasDelta codes
	{
		for(int i=0;i<toCode.length();i++)//we do this for each index, we start at index 0 but we treat it as 1
			//for example we want to code the number 4 so N is 2, L is 1 so the code start with "0"
			//now the binary representation of N+1 is "11" so the code is "011"
			//now the binary representation of 4 is "100" and without the leading bit is "00"
			//so the EliasDelta code for 4 is "01100"
		{
			int elDeNumber=i+1;//the number to code will be i+1
			N=(int) Math.floor(Math.log10(elDeNumber)/Math.log10(2));//N is the log in base of 2 of the encoded number 
			L=(int) Math.floor(Math.log10(N+1)/Math.log10(2));//L is the log in base of 2 of N+1
			for(int j=0;j<L;j++)//we need to start with L zeros 
				codeWords[i].append("0");
			codeWords[i].append(Integer.toBinaryString(N+1));//then we put the binary representation of N+1 without the leading zeros
			StringBuilder str=new StringBuilder(Integer.toBinaryString(elDeNumber));//save the binary representation of our number
			str.deleteCharAt(0);//all but the leading bit  
			codeWords[i].append(str);//append it	
		}				
	}
	public void eliasDeltaMapBulid(boolean EnOrDe)//a method to create the HashMap if its true then is for encoding else is for decoding
	{
		if(EnOrDe)//if true so encoding then 
		{
			for(int i=0;i<codeWords.length;i++)
				charToEliasDeltaCodes.put(toCode.charAt(i), codeWords[i].toString());//put the char and its counterpart code
		}
		else//so decoding then
		{
			for(int i=0;i<codeWords.length;i++)
			{
				eliasDeltaToCharCodes.put(codeWords[i].toString(),toCode.charAt(i));//put the code and its counterpart char
			}
		}			
	}
	public void setInputToEncodeED(byte[] inputToEncodeED)//a setter
	{
		this.inputToEncodeED=inputToEncodeED;
	}
	public void setInputToDecodeED(byte[] inputToDecodeED)//a setter
	{
		this.inputToDecodeED=inputToDecodeED;
	}
	public void setToCodeInt(String toCodeInt)//a setter
	{
		this.toCodeInt=toCodeInt;
	}		
	public void eliasDeltaEncoder()//a method to encode
	{
		this.codeWordsBulid();//create the EliasDelta codes
		this.eliasDeltaMapBulid(true);//create the HashMap (its true because its encoding)
		this.writeEliasDeltaCode();//write the EliasDelta Code to the file.
	}
	public void eliasDeltaDecoder()//a method to decode
	{
		this.codeWordsBulid();//create the EliasDelta codes
		this.eliasDeltaMapBulid(false);//create the HashMap (its false because its decoding)		
		this.writeOriginalCode();//write the Original Code to the file.
		
	}
	public void writeEliasDeltaCode()
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
        	StringBuilder all = new StringBuilder();//a StringBuilder which represent the EliasDelta code of the file in binary String format 
        	short length=(short) (toCodeInt.length());//the length of the toCodeInt string in short format	
			byte[] size=ByteBuffer.allocate(2).putShort(length).array();//a byte[] that will have our size
			out.write(size);//write the size first
			out.write(toCodeInt.getBytes());//write toCodeInt in byte format
        	char currentchar;//a char to represent the characters in inputToEncodeED
        	for (byte b : inputToEncodeED)//for each byte in inputToEncodeED
        	{
        		currentchar=(char)(b&0xFF);//cast the byte to char and save the characters ( b&0xFF because we want 0-255 and not -127 to 128)
        		String eliasDeltaString=charToEliasDeltaCodes.get(currentchar);//get the code of that characters from the HashMap
				all=all.append(eliasDeltaString);//append all the binaryStrings together
        	}      	
        	BitSet eliasDeltaStringCodeBit = new BitSet(all.length());//BitSet for all the bits on the binary String	
			for (int i = 0; i < all.length(); i++) 
			{
			    if(all.charAt(i) == '1')//if the char at the string is '1' then put 1 on in the Bitset..all the other bits are '0' by default
			    	eliasDeltaStringCodeBit.set(i);
			}
			bitout.write(eliasDeltaStringCodeBit.toByteArray());//get the Bitset in byte array format and then write it	
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
			BitSet inputDataBitsED=BitSet.valueOf(inputToDecodeED);//the inputToDecodeED in bit format
        	char currentchar;//the char to write to the file
        	StringBuilder bits = new StringBuilder();//a string which will represent the EliasDelta code
        	for (int i=0;i<inputDataBitsED.length()+1;i++)//for each byte in inputDataBitsF
        	{
        		if(inputDataBitsED.get(i)==true)//if its true then append '1' to bits
        		{
        			bits.append('1');
        		}
        		else
        		{
        			bits.append('0');
        		}
        		if(eliasDeltaToCharCodes.containsKey(bits.toString()))//check if the bits we have are a EliasDelta code, if not Continue until it is
        		{
        			currentchar=eliasDeltaToCharCodes.get(bits.toString());//if it is then get the char that he represent
        			out.write(currentchar);//write it
        			bits.setLength(0);//clear bits for the new EliasDelta code
        		}
            }
        	out.close();
		} 	
		catch (IOException e) 
		{
				e.printStackTrace();
		}
	}	
}
