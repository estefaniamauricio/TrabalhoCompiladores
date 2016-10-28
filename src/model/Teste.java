package model;

public class Teste{
   public static void main(String[] args) {
	  int i =5654;
	  
	  int j =0;
	  
	  while(i!=j){
		 if(i>j) 
			i = i-j;
		 else
			j=j-i;
	  }
	  System.out.println(i);
	  
	  
	  while(i!=j){
		 if(i>j) 
			i = i%j;
		 else
			j=j%i;
	  }
	  System.out.println(i);
   }
}
