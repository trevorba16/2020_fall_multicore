package fft;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelCombination {

	public static Complex[][] Combine(Complex[][] subArrays, int numThreads) {
    	if (subArrays.length == 1)
    		return subArrays;
    	
    	try {
	    	int numArrays = subArrays.length;
	    	int numCombinations = numArrays / 2;
	    	
	    	Complex[][] comboResults = new Complex[numCombinations][];
	    	
	    	List<Future<Complex[]>> resultList = new ArrayList<Future<Complex[]>>();
	    	ExecutorService threadPool = Executors.newCachedThreadPool();
	    	
	    	int c = 0;
	    	for (int i = 0; i < numCombinations; i++) {
	    		
	    		resultList.add(threadPool.submit(new CombinationThread(subArrays[i], subArrays[i + (numCombinations / 2)])));
	    		
	    		if (i + 1 % numThreads == 0) {
	    			for (int j = 0; j < numThreads; i++)
	    	        {
	    	    		comboResults[c] = resultList.get(j).get();
	    	    		c++;
	    	        }
	    		}
	    	}
	    	return comboResults;
    	
    	} catch (Exception e)
        {
            System.err.println(e);
            return null;
        }
    }
	
}
