package test;

import java.io.IOException;
import java.util.HashSet;


public class FFTTest {
	static int count=0;
	public static void main(String[] args){
		String filePath="test";
		String fileWav="/tmp/assignment7Sanguoyanyi" + (count++)+".wav";
		String cmd="./lame --decode "+filePath+" "+fileWav;
		Process p=null;
		try {
			p = java.lang.Runtime.getRuntime().exec("./lame sdfsdf sdfsd");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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