package es.uned.lsi.eped.DataStructures;

public class Queue<E> extends Sequence<E> implements QueueIF<E> {

	private NodeSequence lastNode;

	/* Constructor por defecto: crea una cola vacía */
	public Queue(){
		super();         /* Construimos la secuencia vacía */
		lastNode = null; /* No hay último nodo */
	}
	
	/* Constructor por copia: delega en el constructor por copia *
	 * de la secuencia                                           */
    public Queue(Queue<E> s) {
    	super(s);
    }
    
	/* Devuelve el primer elemento de la cola */
	public E getFirst() {
		return this.firstNode.getValue();
	}

	/* Añade un nuevo elemento al final de la cola */
	public void enqueue(E elem) {
		NodeSequence newNode = new NodeSequence(elem);
		if(isEmpty()){
			this.firstNode = newNode;
		}
		else{
			this.lastNode.setNext(newNode);
		}
		this.lastNode = newNode;
		this.size++;
	}

	/* Elimina el primer elemento de la cola */
	public void dequeue() {
		this.firstNode = this.firstNode.getNext();
		this.size--;
		if(this.size == 0){
			this.lastNode = null;
		}
	}
	
	/* Vacía la cola */
	public void clear() {
		super.clear();   /* Vaciamos la secuencia */
		this.lastNode = null; /* No hay último nodo */
	}
}
