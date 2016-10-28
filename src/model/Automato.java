
package model;

import java.util.ArrayList;
import java.util.List;

public class Automato{
   private List<EstadoAutomato> estados;

   private int qtdEstados;

   private int estadoIncial;

   private int qtdEstadosFinais;

   private AlfabetoAltomato alfabeto;

   public int getEstadoIncial() {
	  return estadoIncial;
   }

   public void setEstadoIncial(int estadoIncial) {
	  this.estadoIncial=estadoIncial;
   }

   public int getQtdEstados() {
	  qtdEstados=getEstados().size();
	  return qtdEstados;
   }

   public void setQtdEstados(int qtdEstados) {
	  this.qtdEstados=qtdEstados;
   }

   public int getQtdEstadosFinais() {
	  return qtdEstadosFinais;
   }

   public void setQtdEstadosFinais(int qtdEstadosFinais) {
	  this.qtdEstadosFinais=qtdEstadosFinais;
   }

   public List<EstadoAutomato> getEstados() {
	  if(estados == null)
		 estados=new ArrayList<EstadoAutomato>();
	  return estados;
   }

   public void setEstados(List<EstadoAutomato> estados) {
	  this.estados=estados;
   }

   public EstadoAutomato getEstadoInicial() {
	  return estados.get(0);
   }

   public EstadoAutomato getEstado(int pos) {
	  return estados.get(pos);
   }

   public void addEstado(EstadoAutomato estado) {
	  getEstados().add(estado);
   }

   public AlfabetoAltomato getAlfabeto() {
	  return alfabeto;
   }

   public void setAlfabeto(AlfabetoAltomato alfabeto) {
	  this.alfabeto=alfabeto;
   }
}
