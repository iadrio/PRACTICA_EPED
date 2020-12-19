package es.uned.lsi.eped.pract2017_2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.ListIF;
import es.uned.lsi.eped.DataStructures.*;

public class Main {
	
	
	
	public static void main(String [] args) throws IOException{
		
		//lectura de par�metros
		
		//estructura: L (lista) o T (�rbol general)
		String estructure = args[0]; 
		
		//fichero con el dep�sito de queries
		String pathDepot = args[1];
		//fichero de operaciones
		String pathOperations = args[2];

		//creaci�n del dep�sito de acuerdo a la estructura seleccionada
		QueryDepotIF QD = null;
		if(estructure.equalsIgnoreCase("L")){
			QD = new QueryDepotList();
		}
		if(estructure.equalsIgnoreCase("T")){
			QD = new QueryDepotTree();
		}
		//lectura de las queries del dep�sito
        FileReader fDepot = new FileReader(pathDepot);
        BufferedReader bDepot = new BufferedReader(fDepot);
        String lineDepot;
        while((lineDepot = bDepot.readLine())!=null) {
        	QD.incFreqQuery(lineDepot);
        }
        bDepot.close();
        System.out.println("Consultas almacenadaS: "+QD.numQueries());
		//lectura y ejecuci�n de las operaciones 
        FileReader fOperations = new FileReader(pathOperations);
        BufferedReader bOperations = new BufferedReader(fOperations);
        String lineOperation;
        ListIF<Double> listaFrecuencias = new List<Double>();
        ListIF<Double> listaSugerencias = new List<Double>();
        while((lineOperation = bOperations.readLine())!=null) {
        	Operation operation = new Operation(lineOperation);
        	String op = operation.getOperation();
        	String arg = operation.getArg();
        	if(op.equalsIgnoreCase("F")){
        		double t0 = System.nanoTime(); 
        		int f = QD.getFreqQuery(arg);
        		double t1 = System.nanoTime()-t0;
        		t1 = t1/1000000.0;
        		System.out.println("La frecuencia de \""+arg+"\" es "+f+".");
        		System.out.println("-Tiempo: "+t1);
        		listaFrecuencias.insert(t1, listaFrecuencias.size()+1);
        	}
        	if(op.equalsIgnoreCase("S")){
        		double t0 = System.nanoTime(); 
        		ListIF<Query> L = QD.listOfQueries(arg);
        		double t1 = System.nanoTime()-t0;
        		t1 = t1/1000000.0;
        		System.out.println("La lista de sugerencias para \""+arg+"\" es:");
        		IteratorIF<Query> I = L.iterator();
        		while(I.hasNext()){
        			Query query = I.getNext();
        			String textQuery = query.getText();
        			int frec = query.getFreq();
            		System.out.println("\t\""+textQuery+"\" con frecuencia "+frec+".");
        		}
        		System.out.println("-Tiempo: "+t1);
        		listaSugerencias.insert(t1, listaSugerencias.size()+1);

        	}
        }                
        bOperations.close();
	}


}
