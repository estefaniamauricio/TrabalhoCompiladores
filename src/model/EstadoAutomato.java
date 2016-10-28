package model;

import java.util.ArrayList;
import java.util.List;

public class EstadoAutomato{
   
   private int identificador;
   List<TransicaoAutomato> transicoes;
   private boolean esFinal;
   private String etiqueta;
   
   public EstadoAutomato(int identificador) {
	  this(identificador, false);
   }
   
   public EstadoAutomato(int identificador, boolean esFinal) {
	  setIdentificador(identificador);
	  setEsFinal(esFinal);
   }
   
   public void setIdentificador(int identificador) {
	  this.identificador=identificador;
   }

   public int getIdentificador() {
	  if (etiqueta == null){
		 if (identificador == 0)
			setEtiqueta("->");
      	 if (esFinal)
      		setEtiqueta("*");
      	 if (identificador != 0 && !esFinal)
      		etiqueta = "";
	  }
	  return identificador;
   }
   
   public boolean getEsFinal() {
	  return esFinal;
   }

   public void setEsFinal(boolean esFinal) {
	  this.esFinal=esFinal;
   }
   
   public List<TransicaoAutomato> getTransicoes() {
	  if (transicoes == null) 
		 transicoes = new ArrayList<TransicaoAutomato>();
      return transicoes;
   }

   public void setTransicoes(List<TransicaoAutomato> transicoes) {
      this.transicoes=transicoes;
   }

   public String getEtiqueta() {
      return etiqueta;
   }

   public void setEtiqueta(String etiqueta) {
      this.etiqueta=etiqueta;
   }
   
   private boolean estaOrdenado;
   public boolean getEstaOrdenado() {
      return estaOrdenado;
  }
   
}
