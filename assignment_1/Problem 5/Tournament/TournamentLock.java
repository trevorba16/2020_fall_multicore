package q5.TournamentLock;

import java.util.Arrays;

public class TournamentLock implements Lock {
    /* number of nodes in the tree */
    private int n;

    /* points to first node containing a process */
    private int p;

    /* number of processes */
    private int z;

    /* used to store index of flag array. Used while unlocking */
    private int[][] process;

    /* flag to requestCS, the size is n + (n -1), where n is the number of processes */
    private boolean[] flag;

    /* to store the victim for each pair of process on each level. The size is (n -1) */
    private int[] victim;
    
    public Tournament(int numProc) {

        int i = numProc;
    /* here we extend the number of processes to the next exponent of 2 */  
        int q = (int) Math.pow(2, (int) Math.ceil(log2(i)));
    /* the number of nodes in the tree = number of leaves + (number of leaves -1) */  
        int n = q + (q -1);
        /* the number of points to the first leaf (process) */
        int p = q -1;
        int z = i;
        /* here we initialize a 2d array.[process and level/process] */       
        process = new int[i][z];
        for (int[] row: process) 
             Arrays.fill(row, -1);
        flag = new boolean[n];
        Arrays.fill(flag, false);
        victim = new int[q -1];
        Arrays.fill(victim, 0);

    }
      public void lock(int pid) {
          int ipid = p + pid -1; // pid is indexed from 1, ipid is the index of flag[]
          int k = (int) Math.ceil(log2(z)); // number of levels (height of the tree)
          for (int i = 0; i < k; i++) {
              flag[ipid] = true;
              victim[(ipid -1) / 2] = pid;
                if (ipid % 2 == 0) {
                   while (flag [ipid -1] && (victim[(ipid -1) / 2] == pid)) // if other process does not request CS
                     {};
                }
                else {               // move ahead by checking the flag
                   while (flag[ipid +1] && (victim[(ipid -1) / 2] == pid))  
                     {};
                }
              process[(pid -1)][z + i] = ipid; // store the index for unlocking
              ipid = (ipid -1) /2;
          }
      }
      public void unlock(int pid) {
           int k = (int) Math.ceil(log2(z)); // number of levels (height of the tree)
           for (int j = 0; j < k; j++) {
               flag[process[(pid -1)][z + j] = false;
           }
      }

     }
    }

}
