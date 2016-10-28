
package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import model.AFD;
import model.AlfabetoAltomato;
import model.Automato;
import model.EstadoAutomato;
import model.Fila;
import model.Pilha;
import model.TransicaoAutomato;

@ManagedBean(name="trabalho3")
@ViewScoped
public class Trabalho3Controller implements Serializable{
   private static final long serialVersionUID=1L;
   
   public static AFD getAFD(Automato afn, String exprReg) {
	  EstadoAutomato estadoOrigem, estadoDestino;
	  
	  AFD afd=new AFD(afn.getAlfabeto(), exprReg);
	  
	  List<List<EstadoAutomato>> estadosAFD = new ArrayList<List<EstadoAutomato>>();
	  Fila<List<EstadoAutomato>> colaTemp = new Fila<List<EstadoAutomato>>();
	  Fila<List<EstadoAutomato>> listaFechoE = new Fila<List<EstadoAutomato>>();
	  
	  int estadosProcessador = 0;
	  
	  List<EstadoAutomato> fechoEInicial = fechoE(afn.getEstadoInicial());
	  
	  estadosAFD.add(fechoEInicial);
	  colaTemp.insere(fechoEInicial);
	  
	  String fechoCompleto = "";
	  System.out.println("\n============= FECHOS EPSLON ===============");
	  for(int contEstados=0; contEstados < afn.getQtdEstados(); contEstados++){ //ESCREVE ARRUMADIN OS FECHOS DE CADO ESTADO KK
		   List<EstadoAutomato> fechos = fechoE(afn.getEstado(contEstados));
		   String s = trataString(fechos);
		   System.out.println("FECHO EPSLON(" + fechos.get(0).getIdentificador() + ") = " + "{" +s+"}");
		   fechoCompleto = fechoCompleto + "FECHO EPSLON(" + fechos.get(0).getIdentificador() + ") = " + "{" +s+"} \n";
		   listaFechoE.insere(fechos);
	  }
	  System.out.println("===========================================\n\n");
	  
	  while(!colaTemp.vazia()){
		 List<EstadoAutomato> T = colaTemp.remove();
		 
		 if(afd.getEstados().size() < estadosAFD.size()){
			afd.getEstados().add(new EstadoAutomato(afd.getEstados().size()));
		 }
		 
		 estadoOrigem = afd.getEstado(estadosProcessador);
		 estadosProcessador++;
		 
		 for (String simbolo : afn.getAlfabeto().getSimbolos()){
			
			List<EstadoAutomato> M = mover(T, simbolo); //RETORNA OS ESTADOS ALCANÇADOS PARA O SIMBOLO ESPECIFICADO
			List<EstadoAutomato> U = fechoE(M); //CALCULA OS FECHO-E DO CONJUNTO DE ESTADOS PARA FORMAR O NOVO ESTADO DO AFD
			
			if(estadosAFD.contains(U)){ //CASO ESSE ESTADO JA EXISTA NO AFD
			   int posicao = estadosAFD.indexOf(U);
			   estadoDestino = afd.getEstado(posicao);
			}
			
			else if(!U.isEmpty()){ //CRIA NOVO ESTADO EM AFD POIS NÃO EXISTIA AINDA
			   estadoDestino = new EstadoAutomato(afd.getQtdEstados());
			   afd.addEstado(estadoDestino);
			   
			   estadosAFD.add(U);
			   colaTemp.insere(U);
			}
			
			else{
			   continue;
			}
			
			TransicaoAutomato trans = new TransicaoAutomato(estadoDestino, simbolo);
			estadoOrigem.getTransicoes().add(trans);
		 }
	  }
	  
	  for (int i=0; i < estadosAFD.size(); i++){
		 EstadoAutomato estadoAFD = afd.getEstado(i);
		 for (EstadoAutomato e : estadosAFD.get(i)){
			if(e.getEsFinal()){
			   estadoAFD.setEsFinal(true);
			   break;
			}
		 }
	  }
	  
	  afd.setEstadosD(estadosAFD);
	  afd.setFechos(fechoCompleto);
	  return afd;
   }
   
   public static List<EstadoAutomato> fechoE(EstadoAutomato estado) {
	  List<EstadoAutomato> resultado = new ArrayList<EstadoAutomato>();
	  percorreAutomato(estado, resultado, AlfabetoAltomato.VAZIO);
	  resultado = organizar(resultado);
      return resultado;
  }
   
   public static List<EstadoAutomato> fechoE(List<EstadoAutomato> estados) {
	  List<EstadoAutomato> resultado = new ArrayList<EstadoAutomato>();
	  percorreAutomato(estados, resultado, AlfabetoAltomato.VAZIO);
	  resultado = organizar(resultado);
	  return resultado;
   }

   
   public static List<EstadoAutomato> mover(List<EstadoAutomato> estados, String simbolo) {
	  List<EstadoAutomato> resultado = new ArrayList<EstadoAutomato>();
	  percorreAutomato(estados, resultado, simbolo);
	  resultado = organizar(resultado);
      return resultado;
  }

   private static void percorreAutomato(EstadoAutomato atual, List<EstadoAutomato> alcancados, String simboloBuscado) {
	  Pilha<EstadoAutomato> pilha=new Pilha<EstadoAutomato>();
	 
	  if(simboloBuscado.equals(AlfabetoAltomato.VAZIO))
		 alcancados.add(atual);
	  
	  pilha.insere(atual);
	  
	  while ( !pilha.vazia()) {
		 atual=pilha.remove();
		 
		 for(TransicaoAutomato t : atual.getTransicoes()) {
			EstadoAutomato e=t.getEstado();
			String s=t.getSimbolo();
			
			if(s.equals(simboloBuscado) && !alcancados.contains(e)) {
			   alcancados.add(e);
			
			   if(simboloBuscado.equals(AlfabetoAltomato.VAZIO)) //insere na pilha se o estado alcaçado por um &
				  pilha.insere(e);
			}
		 }
	  }
   }
   
   private static void percorreAutomato(List<EstadoAutomato> inicios, List<EstadoAutomato> alcancados, String simboloBuscado) {
      for (EstadoAutomato e : inicios)
    	 percorreAutomato(e, alcancados, simboloBuscado);
   }
   
   public static List<EstadoAutomato> organizar(List<EstadoAutomato> resultado){
	  List<EstadoAutomato> ordenado = new ArrayList<EstadoAutomato>();
	  for (EstadoAutomato e : resultado){
		 if(!ordenado.contains(e))
			ordenado.add(e);
	  }
	  return ordenado;
   }
   
   public static String trataString(List<EstadoAutomato> fechos){
	  String s ="";
	  for(int i=0; i<fechos.size();i++){
		 if(fechos.size()>1 && !(i==fechos.size()-1)){
			s = s + fechos.get(i).getIdentificador() + ",";
			continue;
		 }
		 else
			s = s + fechos.get(i).getIdentificador();
		 
	  }
	  return  s;
   }
   
}
