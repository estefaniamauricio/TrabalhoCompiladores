
package model;


public class TransicaoAutomato implements Comparable<TransicaoAutomato>{
 
   private EstadoAutomato estado;
   private String simbolo;
   
   public TransicaoAutomato(EstadoAutomato estado, String simbolo) {
	  this.estado=estado;
	  this.simbolo=simbolo;
   }
   
   public TransicaoAutomato() {
	  this(null, null);
   }
   
   public void addTransicaoAutomato(String simbolo) {
	  this.simbolo = simbolo;
   }
   
   public EstadoAutomato getEstado() {
	  return estado;
   }

   public void setEstado(EstadoAutomato estado) {
	  this.estado=estado;
   }

   public String getSimbolo() {
	  return simbolo;
   }

   public void setSimbolo(String simbolo) {
	  this.simbolo=simbolo;
   }

   @Override
   public String toString() {
	  return "(" + getEstado() + ", " + getSimbolo() + ")";
   }

   public int compareTo(TransicaoAutomato obj) {
	  EstadoAutomato e1=this.getEstado();
	  EstadoAutomato e2=obj.getEstado();
	  int diferencia=e1.getIdentificador() - e2.getIdentificador();
	  if(diferencia != 0)
		 return diferencia;
	  String s1=this.getSimbolo();
	  String s2=obj.getSimbolo();
	  return s1.compareTo(s2);
   }
}
