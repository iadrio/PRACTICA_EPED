package es.uned.lsi.eped.pract2017_2018;

import es.uned.lsi.eped.DataStructures.*;

public class QueryDepotTree implements QueryDepotIF {
	
	private int size;
	private GTreeIF<String> queryTree;
	
	public QueryDepotTree()
	{
		queryTree = new GTree<String>();
		queryTree.setRoot("raiz");
		size=0;
	}

	
	@Override
	public int numQueries() {

		return size;
	}

	@Override
	public int getFreqQuery(String q) {
		GTreeIF<String> result = searchTree(queryTree,q);
		
		if(result!=null)
		{
			return Integer.parseInt(result.getChild(1).getRoot());
		}
		else
		{
			return 0;
		}
	}
	

	@Override
	public ListIF<Query> listOfQueries(String prefix) {
				
		ListIF<Query> lista = new List<Query>();
		Stack<String> stack = new Stack<String>();
		Query rootQuery= new Query(prefix);
		rootQuery.setFreq(getFreqQuery(prefix));
		GTreeIF<String> subTree = new GTree<String>();
		
		if(prefix.equals(""))
		{
			subTree = queryTree;
		}
		else
		{
			subTree = searchTree(queryTree, prefix);
		}
		
		if(!(subTree==null))
		{
			lista = fillQueryList(subTree,lista,stack,prefix);
			
			if(getFreqQuery(prefix)!=0)
			{
			lista.insert(rootQuery,lista.size()+1);
			}
		
		}
				
		return sortQueries(lista);
	}

	public void incFreqQuery(String q)
	{
		insertChild(q,queryTree);
	}
	
	public void insertChild(String q,GTreeIF<String> tree)
	{
		int l;
		l=q.length();
		String character = String.valueOf(q.charAt(0));
		if(l==1)
		{
			if(!tree.isLeaf())
			{		
				GTreeIF<String> child = searchChild(tree,character);
				if(child!=null)
				{
					increaseFrequency(child);
				}
				else
				{
					child = new GTree<String>();
					child.setRoot(character);
					GTreeIF<String> frequency = new GTree<String>();
					frequency.setRoot("1");
					child.addChild(1, frequency);
					tree.addChild(tree.getNumChildren()+1, child);
					size++;
				}
			}
			else
			{
				GTreeIF<String> child = new GTree<String>();
				child.setRoot(character);
				GTreeIF<String> frequency = new GTree<String>();
				frequency.setRoot("1");
				child.addChild(1, frequency);
				tree.addChild(tree.getNumChildren()+1, child);
				size++;
			}
		}
		else
		{
			if(!tree.isLeaf())
			{
				GTreeIF<String> child = searchChild(tree,character);
				if(child!=null)
				{
					insertChild(q.substring(1),child);
					
				}
				else
				{
					child = new GTree<String>();
					child.setRoot(character);
					GTreeIF<String> frequency = new GTree<String>();
					frequency.setRoot("0");
					child.addChild(1, frequency);
					tree.addChild(tree.getNumChildren()+1, child);
					insertChild(q.substring(1), child);
					
				}	
			}
			else
			{
				GTreeIF<String> child = new GTree<String>();
				child.setRoot(character);
				GTreeIF<String> frequency = new GTree<String>();
				frequency.setRoot("0");
				child.addChild(1, frequency);
				tree.addChild(tree.getNumChildren()+1, child);
				insertChild(q.substring(1), child);
				
			}
		}
		
	}
	
	
	public void increaseFrequency(GTreeIF<String> tree)
	{
		int frequency = Integer.parseInt(tree.getChild(1).getRoot());
		
		if(frequency==0)
		{
			size++;
		}
		String freq = Integer.toString(frequency+1);
		tree.getChild(1).setRoot(freq);
	}

	
	public GTreeIF<String> searchChild(GTreeIF<String> tree,String q)
	{
		GTreeIF<String> child=null;
		if(tree.getRoot().equals("raiz"))
		{
			boolean found = false;
			int i=1;
			while(!found&&i<=tree.getNumChildren())
			{
				if(tree.getChild(i).getRoot().equals(q))
				{
					child=tree.getChild(i);
					found=true;
				}
				
				i++;
			}
			return child;
		}
		else if(tree.getNumChildren()==1&&tree.getRoot().equals("raiz"))
		{
			return null;
		}
		else
		{
			boolean found = false;
			IteratorIF<GTreeIF<String>> iter = tree.getChildren().iterator();
			GTreeIF<String> auxChild = iter.getNext();
			while(!found&&iter.hasNext())
			{
				auxChild=iter.getNext();
				if(auxChild.getRoot().equals(q))
				{
					child=auxChild;
					found=true;
				}
				
			}
			return child;
		}
	}
	
	
	public GTreeIF<String> searchTree(GTreeIF<String> tree,String q)
	{
		GTreeIF<String>  result = new GTree<String>();				
		
		if(q.length()<2)
		{
			return searchChild(tree,q);
		}
		else
		{
			result = searchChild(tree,q.substring(0, 1));
			if(result==null)
			{
				return null;
			}
			else
			{
				return searchTree(result,q.substring(1));
			}
		}
		
		
	}

	
	public ListIF<Query> fillQueryList(GTreeIF<String> tree,ListIF<Query> queryList,Stack<String> stack,String prefix)
	{		
		
		GTreeIF<String> child;
		
		IteratorIF<GTreeIF<String>> iterator = tree.getChildren().iterator();
		while(iterator.hasNext())
		{
			child = iterator.getNext();
			
			if(!child.isLeaf())
			{
				stack.push(child.getRoot());
				queryList = fillQueryList(child,queryList,stack,prefix);	
			
			}
			else
			{
				if(!child.getRoot().equals("0"))
				{
					String text="";
					IteratorIF<String> iter = stack.iterator();
					StackIF<String> auxStack = new Stack<String>();
					
					if(!stack.isEmpty())
					{
						while(iter.hasNext())
						{
							auxStack.push(iter.getNext());
						}
						
						while(!auxStack.isEmpty())
						{
							text += auxStack.getTop();
							auxStack.pop();
						}
						int freq = getFreqQuery(prefix+text);
						Query query = new Query(prefix+text);
						query.setFreq(freq);
						queryList.insert(query,queryList.size()+1);		
					}
				}
			}
		}
		if(!stack.isEmpty())
		{
			stack.pop();
		}
		return queryList;
	}
		
	
	//ordena el deposito de queries por frecuencia descendente
	public List<Query> sortQueries(ListIF<Query> list)
	{
		List<Query> auxList =new List<Query>();
		int j;
		Query query,insertedQuery;
		IteratorIF<Query> iter = list.iterator();
		
		
		while(iter.hasNext())
		{
			IteratorIF<Query> auxIter = auxList.iterator();
			query = iter.getNext();
			if(auxList.isEmpty())
			{
				auxList.insert(query,1);
			}
			else
			{
				j=1;
				boolean inserted = false;
				while(!inserted)
				{
					if(auxIter.hasNext())
					{
						insertedQuery = auxIter.getNext();
						
						if(query.getFreq()>insertedQuery.getFreq())
						{
							auxList.insert(query,j);
							inserted = true;
						}
						else
						{
							if(query.getFreq()==insertedQuery.getFreq())
							{
								if(query.getText().compareTo(insertedQuery.getText())<0)
								{
									auxList.insert(query,j);
									inserted = true;
								}
								else
								{
									j++;
								}
							}
							else
							{
								j++;
							}
						}
					}
					else
					{
						auxList.insert(query,j);
						inserted = true;
					}
				}
			}
		}		
		return auxList;
	}

}
