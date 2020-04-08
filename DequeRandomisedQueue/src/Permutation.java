import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        Deque<String> dQ = new Deque<>();
        while (!StdIn.isEmpty()) {
            String inputStr = StdIn.readString();
            int num = StdRandom.uniform(2);
            if (dQ.size() == k) {
                if (num == 0)
                    dQ.removeFirst();
                else
                    dQ.removeLast();
            }
            if (num == 0)
                dQ.addLast(inputStr);
            else
                dQ.addFirst(inputStr);
        }
        Iterator<String> randomIterator = dQ.iterator();
        for (int i = 0; i < k; i++) {
            StdOut.println(randomIterator.next());
        }
        /*
        RandomizedQueue<String> RandomQ = new RandomizedQueue<String>();
        System.out.println("Please enter ### string to finish input");
        String inputStr=StdIn.readString();
        while (!inputStr.equals("###")){
            RandomQ.enqueue(inputStr);
            inputStr=StdIn.readString();
        }
        Iterator<String> randomIterator = RandomQ.iterator();
        for(int i=0;i<k;i++){
            System.out.println(randomIterator.next());
        }
         */
    }
}
