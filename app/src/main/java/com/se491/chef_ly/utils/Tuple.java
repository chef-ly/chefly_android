package com.se491.chef_ly.utils;

/**
 * Created by admin on 5/23/2017.
 */
public class Tuple<X, Y> {

    public final X x;
    public final Y y;
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "("+this.x + ", " + this.y+")";
    }

    public String getX(){
        return String.valueOf(x);
    }

    public int getXAsInt(){
        return Integer.parseInt(String.valueOf(x));
    }

    public String getY(){
        return String.valueOf(y);
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (!Tuple.class.isAssignableFrom(obj.getClass())){
            return false;
        }
        final Tuple other = (Tuple) obj;
        if ((this.x == other.x && this.y == other.y)){
            return true;
        }
        return false;
    }
}

