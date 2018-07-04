package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import java.net.URI;
import search.models.Webpage;


/**
 * @author mmb1995
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;
    
    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.
    private IDictionary<URI, Double> docDenomScore;
    
    // Constructor for TfIdfAnalyzer
    public TfIdfAnalyzer(ISet<Webpage> webpages) {
		if (webpages == null) {
			throw new NullPointerException();
		}
        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.docDenomScore = new ChainedHashDictionary<URI, Double>();
        for (Webpage page: webpages) {
        		this.docDenomScore.put(page.getUri(), 
        							  norm(documentTfIdfVectors.get(page.getUri())));
        }
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    /**
     * This method should return a dictionary mapping every single unique word found
     * in any documents to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
		idfScores = new ChainedHashDictionary<String, Double>();
		IDictionary<String, Double> docCount = new ChainedHashDictionary<String, Double>();
		
		// Iterates through every webpage and maps all of the unique word counts for that page
		for (Webpage page: pages) {
			IList<String> words = page.getWords();
			ISet<String> keySet = getKeySet(words);
			for (String word: keySet) {
				if (!docCount.containsKey(word)) {
					docCount.put(word, 1.0);
				} else {
					docCount.put(word, docCount.get(word) + 1.0);
				}
			}
		}
				
		Double totalDocuments = (double) pages.size();
		
		// Updates IDF scores
		for (KVPair<String, Double> pair: docCount) {
			idfScores.put(pair.getKey(), Math.log(totalDocuments / pair.getValue()));
		}
		return idfScores;
    }
    
    // Returns an ISet containing the unique values from the given list
    private ISet<String> getKeySet(IList<String> words) {
		ISet<String> keySet = new ChainedHashSet<String>();
		for (String word: words) {
			keySet.add(word);
		}
		return keySet;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * We are treating the list of words as if it were a document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Double> tfScores = new ChainedHashDictionary<String, Double>();
        IDictionary<String, Double> wordCounts = new ChainedHashDictionary<String, Double>();
        
        // maps each given word to their frequency
        for (String word: words) {
    		if (!wordCounts.containsKey(word)) {
    			wordCounts.put(word, 1.0);
    		} else {
    			Double count = wordCounts.get(word);
    			wordCounts.put(word, count + 1.0);
    		}      		
        }
        Double totalWords = (double) words.size();
        
        // Computes TF scores
        for (KVPair<String, Double> pair: wordCounts) {
    		tfScores.put(pair.getKey(), pair.getValue() / totalWords);
        }
        return tfScores;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
		documentTfIdfVectors = new ChainedHashDictionary<URI, IDictionary<String, Double>>();
		
		// Iterates through the given set of webpages
		for (Webpage page: pages) {
			IDictionary<String, Double> tfIdfScores = new ChainedHashDictionary<String, Double>();
			IDictionary<String, Double> tfScores = computeTfScores(page.getWords());
			
			// Computes TfIdf scores
			for (KVPair<String, Double> pair: tfScores)  {
				Double tfIdfScore = pair.getValue() * idfScores.get(pair.getKey());
				tfIdfScores.put(pair.getKey(), tfIdfScore);
			}
			documentTfIdfVectors.put(page.getUri(), tfIdfScores);
		}
		return documentTfIdfVectors;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     * Throws @NullPointerException if the query or pageUri are null              
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
	   if (query == null || pageUri == null) {
		   throw new NullPointerException();
	   }
	   
       IDictionary<String, Double> documentVector = documentTfIdfVectors.get(pageUri);
       IDictionary<String, Double> queryTfScores = computeTfScores(query);
       IDictionary<String, Double> queryVector = new ChainedHashDictionary<String, Double>();
       Double numerator = 0.0;
       
       for (KVPair<String, Double> pair: queryTfScores) {
	   		Double queryWordScore = 0.0;
	   		Double docWordScore = 0.0;
	   		if (idfScores.containsKey(pair.getKey())) {	
	   		    queryWordScore = queryTfScores.get(pair.getKey()) * idfScores.get(pair.getKey());
	   		}
   			queryVector.put(pair.getKey(), queryWordScore);
	   		if (documentVector.containsKey(pair.getKey())) {
	   			docWordScore = documentVector.get(pair.getKey());
	   		} 
	   		numerator += queryWordScore * docWordScore;
       } 
       
       Double denominator = docDenomScore.get(pageUri) * norm(queryVector);
       
       // A check to avoid division by zero
       if (denominator != 0.0) {
   		    return numerator / denominator;
       } else {
           return 0.0;
       }
    }
     
     // Calculates and returns a score from the given vector
	 private double norm(IDictionary<String, Double> vector) {
		 double output = 0.0;
		 for (KVPair<String, Double> pair : vector) {
			 double score = pair.getValue();
		     output = output + (score * score);
		 }
	     return Math.sqrt(output);	    
    }
}
