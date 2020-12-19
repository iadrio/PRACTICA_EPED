package es.uned.lsi.eped.pract2017_2018;

import es.uned.lsi.eped.DataStructures.*;
//esto es otra prueba
public class QueryDepotList implements QueryDepotIF {

	private List<Query> queryDepot;
	
	public QueryDepotList()
	{
		queryDepot = new List<Query>();
	}
	
	@Override
	public int numQueries() {
		
		return queryDepot.size();
	}

	@Override
	public int getFreqQuery(String q) {
		if(checkQueryExists(q))
		{
			return getQuery(q).getFreq();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public List<Query> listOfQueries(String prefix) {
		
		return sortQueries(queryFilter(prefix));
	}

	@Override
	public void incFreqQuery(String q) 
	{
		int i=1;
		int f = numQueries();
		int m;
		Query query = new Query(q);
		
		if(queryDepot.isEmpty())
		{
			queryDepot.insert(query, 1);
		}
		else
		{
			while(i<f)
			{			
				m=(f+i)/2;
				
				Query mQuery = queryDepot.get(m);
				if(mQuery.getText().equals(q))
				{
					mQuery.setFreq(mQuery.getFreq()+1);
					return;
				}
				else if(q.compareTo(mQuery.getText())<0)
				{
					f=m-1;
				}
				else
				{
					i=m+1;	
				}
			}
			Query iQuery = queryDepot.get(i);
			if(iQuery.getText().equals(q))
			{
				queryDepot.get(i).setFreq(queryDepot.get(i).getFreq()+1);
			}
			else if (q.compareTo(queryDepot.get(i).getText())>0)
			{	
				queryDepot.insert(query,i+1);
			}
			else
			{
				queryDepot.insert(query,i);
			}
		}
	}

		
	//busca una query en la lista y si no existe devuelve null
	public Query getQuery(String q)
	{
		int i=1;
		int f = numQueries();
		int m;
		
		while(i<=f)
		{
			m=(f+i)/2;
			Query mQuery = queryDepot.get(m);
			if(mQuery.getText().equals(q))
			{
				return mQuery;
			}
			else if(q.compareTo(mQuery.getText())<0)
			{
				f=m-1;
			}
			else
			{
				i=m+1;
			}
		}
		
		return null;
	}
	
	//devuelve true si la query existe en la lista y false si no es asi�
	public boolean checkQueryExists(String q)
	{
		if(!(getQuery(q)==null))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//nos devuelve la query de una determinada posicion
	public Query get(int i)
	{
		return queryDepot.get(i);
	}
		
	
	//dado un String filtra el deposito de queries que lo contienen 
	public List<Query> queryFilter(String q)
	{
		List<Query> filteredList = new List<Query>();
		Query query;
		IteratorIF<Query> iter = queryDepot.iterator();
		
		while(iter.hasNext())
		{
			query = iter.getNext();
			if(query.getText().startsWith(q))
			{
				filteredList.insert(query, filteredList.size()+1);
			}
		}
		return filteredList;
	}
	
	//ordena el deposito de queries por frecuencia descendente
	public List<Query> sortQueries(List<Query> list)
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

