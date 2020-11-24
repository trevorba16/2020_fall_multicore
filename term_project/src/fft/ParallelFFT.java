package fft;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.Objects;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;


public class ParallelFFT {

    // display an array of Complex numbers to standard output
    public static void show(Complex[] x, String title) {
        System.out.println(title);
        System.out.println("-------------------");
        for (int i = 0; i < x.length; i++) {
        	System.out.println(x[i]);
        }
        System.out.println();
    }
    
    public static Complex[] generateSignal(int N)
	{
			Complex[] signal = new Complex[N];
			for (int i = 0; i < N; i++)
			{
				signal[i] = new Complex(i, 0);
//				signal[i] = new Complex(Math.cos((2 * i * 1 * Math.PI) / N) + 0.5
//						* Math.cos(((2 * i * 2 * Math.PI) / N) + Math.PI / 4), 0);
			}
		return signal;
	}
    
    public static int bitReverse(int n, int bits) {
        int reversedN = n;
        int count = bits - 1;
 
        n >>= 1;
        while (n > 0) {
            reversedN = (reversedN << 1) | (n & 1);
            count--;
            n >>= 1;
        }
 
        return ((reversedN << count) & ((1 << bits) - 1));
    }
 
    static void fft_iterative(Complex[] buffer) {
 
        int bits = (int) (log(buffer.length) / log(2));
        for (int j = 1; j < buffer.length / 2; j++) {
 
            int swapPos = bitReverse(j, bits);
            Complex temp = buffer[j];
            buffer[j] = buffer[swapPos];
            buffer[swapPos] = temp;
        }
 
        for (int N = 2; N <= buffer.length; N <<= 1) {
            for (int i = 0; i < buffer.length; i += N) {
                for (int k = 0; k < N / 2; k++) {
 
                    int evenIndex = i + k;
                    int oddIndex = i + k + (N / 2);
                    Complex even = buffer[evenIndex];
                    Complex odd = buffer[oddIndex];
 
                    double term = (-2 * PI * k) / (double) N;
                    Complex exp = (new Complex(cos(term), sin(term)).times(odd));
 
                    buffer[evenIndex] = even.plus(exp);
                    buffer[oddIndex] = even.minus(exp);
                }
            }
        }
    }
    
    static void fft_iterative_parallel(Complex[] buffer, int numThreads) {
    	int itemCount = buffer.length / (numThreads * 2);
    	
    	int run = 1;
    	int[] evenIndexes = new int[itemCount];
    	int[] oddIndexes = new int[itemCount];
    	int[] kValues = new int[itemCount];
    	
    	for (int N = 2; N <= buffer.length; N <<= 1) {
    		ExecutorService executor = Executors.newFixedThreadPool(4);
            for (int i = 0; i < buffer.length; i += N) {
                for (int k = 0; k < N / 2; k++) {
                    int evenIndex = i + k;
                    int oddIndex = i + k + (N / 2);
                    
                    evenIndexes[run - 1] = evenIndex;
                    oddIndexes[run - 1] = oddIndex;
                    kValues[run - 1] = k;
                    
                    if (run % itemCount == 0) {
                    	run = 0;
                    	CalcThread c = new CalcThread(buffer, evenIndexes, oddIndexes, kValues, N);
                    	executor.execute(c);
                    	evenIndexes = new int[itemCount];
                    	oddIndexes = new int[itemCount];
                    	kValues = new int[itemCount];
                    }
                    run++;
                }
            }
            executor.shutdown();
            while (!executor.isTerminated()) {   }  
    	}
    }
    
    static void fft_recursive(Complex[] x) {
    	int n = x.length;

        if (n == 1) return;
        // radix 2 Cooley-Tukey FFT
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }
        // compute FFT of even terms
        Complex[] even = new Complex[n/2];
        Complex[] odd = new Complex[n/2];
        for (int k = 0; k < n/2; k++) {
            even[k] = x[2*k];
            odd[k] = x[2*k + 1];
        }
        fft_recursive(even);
        fft_recursive(odd);
        // combine
        for (int k = 0; k < n/2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            x[k]       = even[k].plus (wk.times(odd[k]));
            x[k + n/2] = even[k].minus(wk.times(odd[k]));
        }
    }
    
    static void fft_recursive_parallel(Complex[] buffer) {
    	ExecutorService threadPool = Executors.newCachedThreadPool();
    	
    	Complex[][] allResults = new Complex[4][];
    	List<Future<Complex[]>> resultList = new ArrayList<Future<Complex[]>>();
    	
    	try {
    		Complex[] buffer_0 = new Complex[buffer.length / 4];
    		Complex[] buffer_1 = new Complex[buffer.length / 4];
    		Complex[] buffer_2 = new Complex[buffer.length / 4];
    		Complex[] buffer_3 = new Complex[buffer.length / 4];
    		
    		for (int i = 0; i < buffer.length / 4; i++) {
    			int idx = i * 4;
    			buffer_0[i] = buffer[idx];
    			buffer_1[i] = buffer[idx + 1];
    			buffer_2[i] = buffer[idx + 2];
    			buffer_3[i] = buffer[idx + 3];
    		}
	    	resultList.add(threadPool.submit(new CalcThreadRecursive(buffer_0)));
	    	resultList.add(threadPool.submit(new CalcThreadRecursive(buffer_1)));
	    	resultList.add(threadPool.submit(new CalcThreadRecursive(buffer_2)));
	    	resultList.add(threadPool.submit(new CalcThreadRecursive(buffer_3)));
	    	
	    	for (int i = 0; i < 4; i++)
	        {
	    		allResults[i] = resultList.get(i).get();
	        }
	    	int n = buffer.length / 2;
	    	Complex[] a = new Complex[n];
	    	Complex[] b = new Complex[n];
	        for (int k = 0; k < n/2; k++) {
	            double kth = -2 * k * Math.PI / n;
	            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
	            a[k]       = allResults[0][k].plus (wk.times(allResults[2][k]));
	            a[k + n/2] = allResults[0][k].minus(wk.times(allResults[2][k]));

	            b[k]       = allResults[1][k].plus (wk.times(allResults[3][k]));
	            b[k + n/2] = allResults[1][k].minus(wk.times(allResults[3][k]));
	        }
	        
	        n = buffer.length;
	        for (int k = 0; k < n/2; k++) {
	            double kth = -2 * k * Math.PI / n;
	            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
	            buffer[k]       = a[k].plus (wk.times(b[k]));
	            buffer[k + n/2] = a[k].minus(wk.times(b[k]));
	        }
    	
    	
    	} catch (Exception e)
        {
            System.err.println(e);
        }

    }
    
    public static void main(String[] args) { 
//    	System.out.println("Iterative");
//    	for (int power = 2; power < 32; power++) {
//    		long timeSum = 0;
//    		int array_size = (int)Math.pow(2, power);
//    		for (int i = 0; i<10; i++) {
//    			Complex[] x = generateSignal(array_size);
//	    		long startTime = System.currentTimeMillis();
//	    		fft_iterative(x);
//	            long executionTime = System.currentTimeMillis() - startTime;
//	            timeSum += executionTime;
//    		}
//    		long timeAvg = timeSum / 10;
//            System.out.println(array_size + "," + timeAvg);
//    	}
    	System.out.println("Iterative in parallel");
    	for (int power = 3; power < 32; power++) {
    		long timeSum = 0;
    		int array_size = (int)Math.pow(2, power);
    		for (int i = 0; i<10; i++) {
    			Complex[] x = generateSignal(array_size);
	    		long startTime = System.currentTimeMillis();
	    		fft_iterative_parallel(x, 4);
	            long executionTime = System.currentTimeMillis() - startTime;
	            timeSum += executionTime;
    		}
    		long timeAvg = timeSum / 10;
            System.out.println(array_size + "," + timeAvg);
    	}
//    	for (int power = 2; power < 32; power++) {
//    		long timeSum = 0;
//    		int array_size = (int)Math.pow(2, power);
//    		for (int i = 0; i<10; i++) {
//    			Complex[] x = generateSignal(array_size);
//	    		long startTime = System.currentTimeMillis();
//	    		fft_recursive(x);
//	            long executionTime = System.currentTimeMillis() - startTime;
//	            timeSum += executionTime;
//    		}
//    		long timeAvg = timeSum / 10;
//            System.out.println(array_size + "," + timeAvg);
//    	}
    	System.out.println("Recursive in Parallel");
    	for (int power = 2; power < 32; power++) {
    		long timeSum = 0;
    		int array_size = (int)Math.pow(2, power);
    		for (int i = 0; i<10; i++) {
    			Complex[] x = generateSignal(array_size);
	    		long startTime = System.currentTimeMillis();
	    		fft_recursive_parallel(x);
	            long executionTime = System.currentTimeMillis() - startTime;
	            timeSum += executionTime;
    		}
    		long timeAvg = timeSum / 10;
            System.out.println(array_size + "," + timeAvg);
    	}
    }

}
