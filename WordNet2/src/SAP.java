import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

import java.util.*;

public class SAP {
    Digraph dg;
    static final int max = Integer.MAX_VALUE;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        dg = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if(v<0 || v >= dg.V())
            return -1;
        //throw new IllegalArgumentException(v + "V not in graph");
        if(w<0 || w >= dg.V())
            return -1;
        //throw new IllegalArgumentException(w + "W not in graph");
        if (v == w)
            return 0;
        int[] path = ancestralPath(v, w);
        if (path == null)
            return -1;
        return path.length-2;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        if(v<0 || v >= dg.V())
            return -1;
        //throw new IllegalArgumentException(v + "V not in graph");
        if(w<0 || w >= dg.V())
            return -1;
        //throw new IllegalArgumentException(w + "W not in graph");
        if (v == w)
            return v;
        int[] path = ancestralPath(v, w);
        if(path == null)
            return -1;
        return path[0];
    }

    private int[] ancestralPath(int v, int w) {
        int len = dg.V();
        boolean[] markedV = new boolean[len];
        boolean[] markedW = new boolean[len];
        PriorityQueue<Integer> qV = new PriorityQueue<>();
        PriorityQueue<Integer> qW = new PriorityQueue<>();
        qV.add(v);
        qW.add(w);
        int[] edgeToV = new int[len];
        int[] edgeToW = new int[len];
        markedV[v] = true;
        markedW[w] = true;
        int commonAncestor = -1;
        while(!qV.isEmpty() || !qW.isEmpty()) {
            //System.out.println(qV);
            //System.out.println(qW);
            if(!qV.isEmpty()) {
            int i = qV.remove();
                for (int k : dg.adj(i)) {
                    if(markedW[k]) {
                        commonAncestor = k;
                        markedV[k] = true;
                        edgeToV[k] = i;
                        break;
                    }
                    if(!markedV[k]){
                        qV.add(k);
                        markedV[k] = true;
                        edgeToV[k] = i;
                    }
                }
            }
            if(!qW.isEmpty()) {
                int j = qW.remove();
                for (int k : dg.adj(j)) {
                    if (markedV[k]) {
                        commonAncestor = k;
                        markedW[k] = true;
                        edgeToW[k] = j;
                        break;
                    }
                    if (!markedW[k]) {
                        qW.add(k);
                        markedW[k] = true;
                        edgeToW[k] = j;
                    }
                }
            }
        }
        //System.out.println("Common ancestor =" + commonAncestor);
        //System.out.println("edgeToV" + Arrays.toString(edgeToV));
        //System.out.println("edgeToW" + Arrays.toString(edgeToW));
        //System.out.println("markedV" + Arrays.toString(markedV));
        //System.out.println("markedW" + Arrays.toString(markedW));

        if (commonAncestor == -1)
            return null;
        ArrayList<Integer> vPath = new ArrayList<>();
        ArrayList<Integer> wPath = new ArrayList<>();
        int currAncestor = commonAncestor;
        while (currAncestor != v) {
            vPath.add(currAncestor);
            //System.out.println("currAncestorV " + currAncestor);
            currAncestor = edgeToV[currAncestor];
        }
        vPath.add(currAncestor);
        //StdIn.readInt();
        currAncestor = commonAncestor;
        while (currAncestor != w) {
            wPath.add(currAncestor);
            currAncestor = edgeToW[currAncestor];
            //System.out.println("currAncestorW " + currAncestor);
            //StdIn.readInt();
        }
        wPath.add(currAncestor);

        int vsize = vPath.size(), wsize = wPath.size();
        int[] path = new int[vsize + wsize];
        path[0] = wPath.get(0);
        for(int i = 1; i < vsize+1; i++) {
            path[i] = vPath.get(vsize - i);
            //System.out.println("i" + i + "pathi" + path[i]);
        }

        for(int j = 1; j < wsize; j++) {
            path[j + vsize] = wPath.get(j);
            //System.out.println("j" + j + "pathj+vsize" + path[j + vsize]);
        }

        //System.out.println(Arrays.toString(path));
        return path;

    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null)
            throw new IllegalArgumentException("v is null");
        if (w == null)
            throw new IllegalArgumentException("w is null");
        int len = max, currlen;
        int[] currpath;
        for(int vV : v) {
            for(int wV: w) {
                if(vV<0 || vV >= dg.V())
                    throw new IllegalArgumentException(v + "V not in graph");
                if(wV<0 || wV >= dg.V())
                    throw new IllegalArgumentException(w + "W not in graph");
                if (vV == wV)
                    return 0;
                currpath = ancestralPath(vV, wV);
                if (currpath == null)
                    continue;
                currlen = currpath.length - 1;
                if (currlen < len)
                    len=currlen;
            }
        }
        if (len == max)
            return -1;
        return len;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null)
            throw new IllegalArgumentException("v is null");
        if (w == null)
            throw new IllegalArgumentException("w is null");
        int len = max, currlen, ancestor = -1;
        int[] currpath;
        for(int vV : v) {
            for(int wV: w) {
                if(vV<0 || vV >= dg.V())
                    throw new IllegalArgumentException(v + "V not in graph");
                if(wV<0 || wV >= dg.V())
                    throw new IllegalArgumentException(w + "W not in graph");
                if (vV == wV)
                    return vV;
                currpath = ancestralPath(vV, wV);
                if (currpath == null)
                    continue;
                currlen = currpath.length - 1;
                if (currlen < len) {
                    len = currlen;
                    ancestor = currpath[0];
                }
            }
        }
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args){

    }
}


