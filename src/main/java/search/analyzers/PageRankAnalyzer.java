package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * @author mmb1995
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.
		if (webpages == null) {
			throw new NullPointerException();
		} else if (decay < 0.0 || decay > 1.0 || limit < 0) {
			throw new IllegalArgumentException();
		}
        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
		IDictionary<URI, ISet<URI>> graph = new ChainedHashDictionary<URI, ISet<URI>>();
		ISet<URI> webpageURIs = new ChainedHashSet<URI>();
		
		// obtains all the web uris from the given set of webpages
		for (Webpage page : webpages) {			
		    webpageURIs.add(page.getUri());
		}
		
		// creates an unweighted directed graph of all the webpages
		for (Webpage page: webpages) {
			ISet<URI> links = new ChainedHashSet<URI>();
			for (URI link: page.getLinks()) {
				if (webpageURIs.contains(link) && page.getUri() != link) {
					links.add(link);
				}
			}
			graph.put(page.getUri(), links);
		}
		return graph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
        
        // Step 1: The initialize step should go here
	    pageRanks = new ChainedHashDictionary<URI, Double>();
		Double totalPages = (double) graph.size();
		for (KVPair<URI, ISet<URI>> pair: graph) {
			pageRanks.put(pair.getKey(), 1 / totalPages);
		}
    		
		// Step 2: Updates page rank scores
		for (int i = 0; i < limit; i++) {
		    IDictionary<URI, Double> newRanks = new ChainedHashDictionary<URI, Double>();
		    
		    // Initializes new page rank scores
		    for (KVPair<URI, Double> pair: pageRanks) {
		        newRanks.put(pair.getKey(), 0.0);
		    }
		    
		    double size = (double) graph.size();
		    
		    // flag to identify if scores have converged to given amount
		    boolean end = true;
		    
		    // Performs the bulk of the update
		    for (KVPair<URI, ISet<URI>> pair: graph) {
		        double oldRank = pageRanks.get(pair.getKey());
		        ISet<URI> links = graph.get(pair.getKey());
			
    		    // increases page rank of all webpages when a page with no outgoing links is found
		        if (links.size() == 0) {
		            for (KVPair<URI, Double> values: pageRanks) {
		                double newRank = newRanks.get(values.getKey());
		                newRank += decay * (oldRank / size);
		                newRanks.put(values.getKey(), newRank);
		            }
	            
	            // gets new page rank scores
		        } else {
		            for (URI link: links) {
		                double newRank = 0.0;
		                if (newRanks.containsKey(link)) {
		                    // uses existing rank if one is present
		                    newRank = newRanks.get(link);
		                }
		                newRank += decay * (oldRank / (double) links.size());
		                newRanks.put(link, newRank);
		            }
		        }
		    }
		    
		    // Uses given decay rate to adjust scores
		    for (KVPair<URI, Double> value: newRanks) {
		        newRanks.put(value.getKey(), newRanks.get(value.getKey()) + ((1-decay) / size));
		    }
		    
		    // Checks for score convergence
		    for (KVPair<URI, Double> value: newRanks) {
		        if (Math.abs(pageRanks.get(value.getKey()) - newRanks.get(value.getKey())) > epsilon) {
		            pageRanks = newRanks;
		            end = false;
		            break;
		        }
		    }
		    
		    // stops iterating if scores have converged to given amount
		    if (end) {
		        return pageRanks;
		    }
		}
		
	   return pageRanks;
    }


    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        // TODO: Add working code here
    	return pageRanks.get(pageUri);
    }
}
