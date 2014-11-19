package test;

import java.util.HashSet;


public class FFTTest {

	public static void main(String[] args){
		HashSet<Pair> pairs = new HashSet<Pair>();
		int N =20;
		for(int i=0;i<N;i++){
			pairs.add(new Pair(5,5));
		}
		System.out.println(pairs.size());
	}
}
class Pair{
	int x;
	int y;
	public Pair(int x,int y){
		this.x=x;
		this.y=y;
	}
	@Override
	public int hashCode(){
		return (this.x+this.y);
	}
	@Override
	public boolean equals(Object p){
		if(p instanceof Pair){
			Pair o=(Pair)p;
			if(this.x==o.x && this.y==o.y){
				return true;
			}
		}
		return false;
	}
}