package fibonacciAndDelta;


public class CharContainer implements Comparable<CharContainer> {
	private char c;//the character 
	private int occurrence;//the character occurrence number 

	public CharContainer(int i,int occurrence)//a constructor
	{
		this.c=(char)i;//the char from the index of the FrequencyArray
		this.occurrence=occurrence;
	}
	public char getChar()//a getter for the character
	{
		return this.c;
	}
	public int getOccurrence()//a getter for the occurrence
	{
	    return this.occurrence;
	}
	@Override
	public int compareTo(CharContainer o) //Comparable method to compare according to the occurrence
	{
		// TODO Auto-generated method stub
		//return this.occurrence-o.occurrence;
		return o.occurrence-this.occurrence;
	}	
}
