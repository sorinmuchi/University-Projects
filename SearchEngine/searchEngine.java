import java.util.*;
import java.io.*;
/**
@author Bogdan Mazoure
GitHub	https://github.com/ArtificialBreeze
@see searchEngine
*/

public class searchEngine {

    public HashMap<String, LinkedList<String> > wordIndex;                 
    public directedGraph internet;   

	//Constructor
    searchEngine() 
	{
		htmlParsing.internetFilesLocation = "internetFiles";
		wordIndex = new HashMap<String, LinkedList<String> > ();		
		internet = new directedGraph();
    } 
	
    public String toString () {
	return "wordIndex:\n" + wordIndex + "\ninternet:\n" + internet;
    }
	
	//traverseInternet method - build the graph
    void traverseInternet(String url) throws Exception {
		if (!internet.getVisited(url))
		{
			wordIndex.put(url, htmlParsing.getContent(url));	//write the content of the url to the hashmap
			LinkedList<String> edges = htmlParsing.getLinks(url);	//get a list of edges
			internet.setVisited(url,true);
			Iterator<String> i = edges.iterator();
			internet.addVertex(url);	//add this page to the graph
			while (i.hasNext())	//iterate the neighbors and add them to the graph and connect them
			{
				String near=i.next();
				internet.addVertex(near);
				internet.addEdge(url,near);
				traverseInternet(near);
			}
		}
    } // end of traverseInternet
	
	//computePageRanks method - compute the page rank and write it to the graph
    void computePageRanks() {
		LinkedList<String> vertices = internet.getVertices();
		Iterator<String> i = vertices.iterator();
		while (i.hasNext())	//initialize the rank to 1
		{
			String page=i.next();
			internet.setPageRank(page, 1);
		}
		for (int z=0;z<100;z++)	//iterate 100 times and apply the formula to each page
		{
			Iterator<String> j = vertices.iterator();
			while (j.hasNext())
			{
				String page=j.next();
				internet.setPageRank(page, PR(page));
			}
		}
    } // end of computePageRanks
	
	//PR method - the rank formula implementation
    double PR(String v)
	{
		LinkedList<String> neighbors=internet.getEdgesInto(v);
		Iterator<String> i =neighbors.iterator();
		double rank=0.5;
		while (i.hasNext())
		{
			String near=i.next();
			rank+=0.5*internet.getPageRank(near) / internet.getOutDegree(near); 
		}
		return rank;
	}
	
	//getBestURL method - return the best suited result for the user query
    String getBestURL(String query) {
	double maxRank=0;	//default values, so if nothing is found, "No result" is printed
	String maxPage="No result";
	LinkedList<String> vertices=internet.getVertices();
	Iterator<String> i=vertices.iterator();	
			while (i.hasNext()) //iterate over the pages
			{
				String page=i.next();
				double rank=internet.getPageRank(page);
				LinkedList<String> results = wordIndex.get(page);
				Iterator<String> it=results.iterator();
				boolean flag=false;
				while (it.hasNext()) //iterate over the list of a page's keywords in the hashmap
				{
					String value=it.next();
					if (value.indexOf(query)!=-1)
					{
						flag=true; //if the value is present, switch the flag to true
					}
				}
				if ( rank >= maxRank && flag) //if both the page rank is the biggest one and the query word is present, then save this page and its rank
				{
					maxRank=rank;
					maxPage=page;
				}
			}
	return maxPage; //after iterating the whole graph, this is what is left - the best page
    } // end of getBestURL
    
	//print method - simple hack to save some typing time and space
    public static void print(Object o)
	{System.out.println(o);}
	
    public static void main(String args[]) throws Exception{		
		searchEngine mySearchEngine = new searchEngine();
		mySearchEngine.traverseInternet("http://www.cs.mcgill.ca/index.html");
		mySearchEngine.computePageRanks();
		BufferedReader stndin = new BufferedReader(new InputStreamReader(System.in));
		String query;
		do {
			print("Enter query: ");
			query = stndin.readLine();
			if ( query != null && query.length() > 0 ) {
			print("Best result : " + mySearchEngine.getBestURL(query));
			}
		} while (query!=null && query.length()>0);	
    } // end of main
}
