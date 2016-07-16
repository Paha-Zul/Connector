package com.quickbite.connector2;

/**
 * Created by Paha on 7/11/2016.
 */
public class Pair<A, B>{
    private A first;
    private B second;

    public Pair(A first, B second){
        this.first = first;
        this.second = second;
    }

    public A getFirst(){
        return first;
    }

    public B getSecond(){
        return second;
    }

    @Override
    public String toString() {
        return "[First: "+first+", Second: "+second+"]";
    }
}
