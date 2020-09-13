# Assignment 1

Problem 1

(a) 
- When the light is initially off, the prisoners select 1 prisoner to be the "counter"
- When other prisoners go in, if the light is off and that prisoner has never turned it on, the prisoner turns the switch on. If the switch is already on, the prisoners do nothing
- When the counter goes in, if the switch is on, they turn it off, and add 1 to their internal count. If the switch is off, the counter does nothing
- When the counter finally reaches 99, it means that all 99 of the other prisoners have entered the room to turn on the switch

(b)
- When the initial state of the switch is unknown, the prisoners again select 1 individual to be the "counter"
- When other prisoners go in, if the switch is off, they can turn it on their first 2 times.
- The counter again will be the only person to turn off the switch, and will add 1 to their counter each time they do so, except for the first time, when they will not add 1
- When the count reaches 197, the counter can declare that all prisoners have visited the room


Problem 2

Problem 3: l-exclusion problem

Modifed filter algorithm:

int N;  
int l; // l<N  
int [] gate;  
int [] last;  
int [] number;  

entry protocol for  thread i, loop over the threads  

for (int i = 1; i < N; i++)  
   number[i]=0;  
   for (int k = 1; k < N; k++)  
     gate[i]= k;  
     last[k]= i;  
     for (int j = 0; j<N; j++)  
       while ((j != i && (gate[j] >= k) && (last[k] = i))  
       {};  
     number[i]++    
     if (number[i] <= l) return;  

exit protocol  gate[i] = 0;  
               number[i] = 0;  



