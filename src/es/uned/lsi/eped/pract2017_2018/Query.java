package es.uned.lsi.eped.pract2017_2018;

public class Query {
	
	String text;
	int freq;
	
	/*Construye una nueva query con el texto pasado como parámetro*/
	public Query (String text)
	{
		this.text=text;
		this.freq=1;
	}
	
	/*Modifica la frecuencia de la query*/
	public void setFreq(int freq)
	{
		this.freq=freq;
	}
	
	/*Devuelve el texto de una query*/
	public String getText()
	{
		return text;
	}
	
	
	/*Devuelve la frecuencia de una query*/
	public int getFreq()
	{
		return freq;
	}
}
