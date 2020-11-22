package fft;

import static java.lang.Math.*;
import java.util.Objects;

import java.awt.Color;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class ParallelFFT {

    // compute the FFT of x[], assuming its length n is a power of 2
    public static Complex[] fft(Complex[] x) {
//    	System.out.println(set + " begin -------------------------");
//    	indent += "\t";
        int n = x.length;

        // base case
        if (n == 1) return new Complex[] { x[0] };

        // radix 2 Cooley-Tukey FFT
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }

        // compute FFT of even terms
        Complex[] even = new Complex[n/2];
        for (int k = 0; k < n/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] evenFFT = fft(even);

        // compute FFT of odd terms
        Complex[] odd  = even;  // reuse the array (to avoid n log n space)
        for (int k = 0; k < n/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] oddFFT = fft(odd);

        // combine
        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k++) {
//        	System.out.println(indent + "k: " + k);
//        	System.out.println(indent + "k + n/2: " + (k + n/2));
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            System.out.println("-----------------------------------------------------");
            System.out.println("k: " + k);
            System.out.println("evenFFT[k]: " + evenFFT[k]);
            System.out.println("oddFFT[k]: " + oddFFT[k]);
            y[k]       = evenFFT[k].plus (wk.times(oddFFT[k]));
            System.out.println("y[k]       = evenFFT[k].plus (wk.times(oddFFT[k]));");
            System.out.println("y[k]: " + y[k]);
            y[k + n/2] = evenFFT[k].minus(wk.times(oddFFT[k]));
            System.out.println("y[k + n/2] = evenFFT[k].minus(wk.times(oddFFT[k]));");
            System.out.println("y[k + n/2]: " + y[k + n/2]);
        }
//        System.out.println(set + " end -------------------------");
        return y;
    }

    // display an array of Complex numbers to standard output
    public static void show(Complex[] x, String title) {
        System.out.println(title);
        System.out.println("-------------------");
        for (int i = 0; i < x.length; i++) {
        	System.out.println(x[i]);
        }
        System.out.println();
    }
    
    public static void low_memory_fft(Complex[] x, int[] indices) {
    	int n = indices.length;

        // base case
        if (n == 1) { return; };

        // radix 2 Cooley-Tukey FFT
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }

        // compute FFT of even terms
        int[] evenIndexes = new int[n/2];
        for (int k = 0; k < n/2; k++) {
        	evenIndexes[k] = indices[2*k];
        }
        
        low_memory_fft(x, evenIndexes);

        // compute FFT of odd terms
        int[] oddIndexes = new int[n/2];;  // reuse the array (to avoid n log n space)
        for (int k = 0; k < n/2; k++) {
        	oddIndexes[k] = indices[2*k + 1];
        }
        low_memory_fft(x, oddIndexes);
        
        for (int k = 0; k < n/2; k++) {
        	
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            Complex temp1 = x[evenIndexes[k]];
            Complex temp2 = x[oddIndexes[k]];
            x[evenIndexes[k]]       = temp1.plus (wk.times(temp2));
            x[oddIndexes[k]] = temp1.minus (wk.times(temp2));
//            System.out.println("x[indices[k]]       = temp1.plus (wk.times(temp2));");
//            System.out.println(x[indices[k]]);
//            System.out.println("x[indices[k + n/2]] = temp1.minus (wk.times(temp2));");
//            System.out.println(x[indices[k + n/2]]);
        }
    }
    
    public static Complex[] generateSignal(int N)
	{
			Complex[] signal = new Complex[N];
			for (int i = 0; i < N; i++)
			{
//				signal[i] = new Complex(i, 0);
				signal[i] = new Complex(Math.cos((2 * i * 1 * Math.PI) / N) + 0.5
						* Math.cos(((2 * i * 2 * Math.PI) / N) + Math.PI / 4), 0);
			}
		return signal;
	}
    
    public static void CooleyTukeySeries() {
    	Runtime rt = Runtime.getRuntime();
        long prevTotal = 0;
        long prevFree = rt.freeMemory();
    	
    	for (int power = 2; power < 32; power++) {
    		long timeSum = 0;
    		int array_size = (int)Math.pow(2, power);
    		int[] indexes = new int[array_size];
    		for (int idx = 0; idx < array_size; idx++)
    			indexes[idx] = idx;
    		
//    		for (int i = 0; i<100; i++) {
    		
	    		Complex[] x = generateSignal(array_size);
	
	    		final long startTime = System.currentTimeMillis();
	            // FFT of original data
	            low_memory_fft(x, indexes);
	            final long executionTime = System.currentTimeMillis() - startTime;
	            timeSum += executionTime;
//    		}
    		long timeAvg = timeSum;// / 100;
            System.out.println(array_size + "," + timeAvg);
//            long total = rt.totalMemory();
//            long free = rt.freeMemory();
//            if (total != prevTotal || free != prevFree) {
//                long used = total - free;
//                long prevUsed = (prevTotal - prevFree);
//                System.out.println(
//                    "#" + power +
//                    ", Total: " + total +
//                    ", Used: " + used +
//                    ", ∆Used: " + (used - prevUsed) +
//                    ", Free: " + free +
//                    ", ∆Free: " + (free - prevFree));
//                prevTotal = total;
//                prevFree = free;
//            }
            
    	}
    }
//    
//    public static void CooleyTukeyParallel() {
//    	
//    	for (int power = 2; power < 32; power++) {
//    		long timeSum = 0;
//    		int array_size = (int)Math.pow(2, power);
//    		for (int i = 0; i<100; i++) {
//    		
//	    		Complex[] x = generateSignal(array_size);
//	
//	    		
//	    		
//	    		
//	    		
//	    		
//	    		
//	    		
//	    		
//	    		
//	    		final long startTime = System.currentTimeMillis();
//	            // FFT of original data
//	            Complex[] y = fft(x);
//	            final long executionTime = System.currentTimeMillis() - startTime;
//	            timeSum += executionTime;
//    		}
//    		long timeAvg = timeSum / 100;
//            System.out.println(array_size + "," + timeAvg);
//            
//    	}
//    }
    
    public static void main(String[] args) { 

//		int array_size = (int)Math.pow(2, 4);
//		int[] indices = new int[array_size];
//		Complex[] x = generateSignal(array_size);
//		for (int i = 0; i<array_size;i++)
//			indices[i] = i;
//    	
//    	Complex[] y = fft(x);
//    	
//    	System.out.println("======================================================================================"
//    			+ "\n======================================================================================"
//    			+ "\n======================================================================================"
//    			+ "\n======================================================================================");
//    	low_memory_fft(x, indices, "");
//
//    	show(y, "Original");
//    	
//    	show(x, "Modified");
    	CooleyTukeySeries();
//    	CooleyTukeyParallel();
    }

}
