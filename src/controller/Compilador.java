package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import model.AFD;
import model.AlfabetoAltomato;
import model.Automato;
import model.Operandor;
import model.Pilha;
import model.TabelaTransicao;
import model.TransicaoAutomato;

@ManagedBean(name="compilador")
@ViewScoped
public class Compilador implements Serializable{

   private static final long serialVersionUID=1L;
   private String infixa;
   private String posfixa;
   private List<String> infixaConvertida;
   private List<String> posfixaConvertida;
   
   public List<String> getPosfixaConvertida() {
	  if(posfixaConvertida == null)
		 posfixaConvertida = new ArrayList<String>();
      return posfixaConvertida;
   }

   public void setPosfixaConvertida(List<String> posfixaConvertida) {
      this.posfixaConvertida=posfixaConvertida;
   }
   
   public List<String> getInfixaConvertida() {
      	if (infixaConvertida == null) {
      	   infixaConvertida = new ArrayList<String>();
      	}
	  return infixaConvertida;
   }

   public void setInfixaConvertida(List<String> infixaConvertida) {
      this.infixaConvertida=infixaConvertida;
   }

   public String getInfixa() {
      return infixa;
   }

   public void setInfixa(String infixa) {
      this.infixa=infixa;
   }
   
   public String getPosfixa() {
      return posfixa;
   }

   public void setPosfixa(String posfixa) {
      this.posfixa=posfixa;
   }

   public void acaoLimpar(){
	  setPosfixaConvertida(null);
	  setInfixaConvertida(null);
	  setInfixa(null);
	  setPosfixa(null);	  
	  setAutomato(null);
	  setAlfabeto(null);
	  setTransicoesTabela(null);
	  setTabelaTransicoes(null);
	  setTabelaAFN(null);
	  setDa(null);
	  setTransicoesTabelaAFD(null);
	  setAfd(null);
   }
   
   public void preConversaoInfixaPosfixa() {
	  
	  int cont = 0;
	  int contParenteses = 0;	
	  boolean temOperando = false;
	  
	  limpar();
	  
	  String[] infixaDividido=getInfixa().split("");
	  
	  String op2 = "";
	  if(infixaDividido.length > 1) {
		 op2 = infixaDividido[cont+1];
		 
		 for(String simbolo : infixaDividido) {
			
			if("(".equals(simbolo)){
			   if(")".equals(infixaDividido[cont+1])){
				  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					 FacesMessage.SEVERITY_ERROR, "Error!", "Operador binário necessida de dois operandos!"));
				  return;
			   }
			   getInfixaConvertida().add(simbolo);
			   contParenteses++;
			}
			
			else if(")".equals(simbolo)){
			   contParenteses--;
			   if (contParenteses < 0){
				  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					 FacesMessage.SEVERITY_ERROR, "Error!", "Necessário abrir parenteses!"));
				  return;
			   }
			   getInfixaConvertida().add(simbolo);

			   if("(".equals(op2) || "operando".equals(verificaSimbolo(op2))){
				  getInfixaConvertida().add(".");
			   }
			}
			
			else if("operando".equals(verificaSimbolo(simbolo))){
			   getInfixaConvertida().add(simbolo);
			   if("operando".equals(verificaSimbolo(op2))){
				  getInfixaConvertida().add(".");
			   }
			   else if("(".equals(op2)){
				  getInfixaConvertida().add(".");
			   }
			   temOperando = true;
			}
			
			else if("operador".equals(verificaSimbolo(simbolo)) && temOperando){ // para haver operador, obrigatoriamente deve haver operando
			   if(")".equals(infixaDividido[cont-1]) || "operando".equals(verificaSimbolo(infixaDividido[cont-1]))){
				  if("*".equals(simbolo)){
					 getInfixaConvertida().add(simbolo);
					 if("(".equals(infixaDividido[cont+1]) || "operando".equals(verificaSimbolo(infixaDividido[cont+1]))){
						getInfixaConvertida().add(".");
					 }
				  }
				  else {
					 if("(".equals(infixaDividido[cont+1]) || "operando".equals(verificaSimbolo(infixaDividido[cont+1]))){
						 getInfixaConvertida().add(simbolo);
					 }
					 else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
							 FacesMessage.SEVERITY_ERROR, "Error!", "TODO OPERADOR DEVE SER SEGUIDO DE OPERANDO OU PARENTESES."));
						return;
					 }
				  }
			   }
			   else if ("*".equals(infixaDividido[cont-1])){
				  getInfixaConvertida().add(simbolo);
			   }
			   else {
				  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					 FacesMessage.SEVERITY_ERROR, "Error!", "TODO OPERADOR DEVE SER SEGUIDO DE OPERANDO OU PARENTESES."));
				  return;
			   }
			}
		
			else {
			   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					 FacesMessage.SEVERITY_ERROR, "Error!", "TODO OPERADOR DEVE SER SEGUIDO DE OPERANDO OU PARENTESES."));
			   return;
			}
			
			cont++;
			if (!(infixaDividido.length == cont+1)){
			   op2 = infixaDividido[cont+1];
			}else {
			   if(op2.equals("(") || op2.equals("+") || op2.equals(".")){ //verifica ultimo simbolo
				  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "PALAVRA INVALIDA."));
				  System.out.println("palavra invalida");
				   return;
			   }
			   else{
				  getInfixaConvertida().add(op2);
				  if(op2.equals(")")){
					 contParenteses--;					 
				  }
				  if (contParenteses != 0){
					 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "PALAVRA INVALIDA."));
					 return;
				  }
				  break;
			   }
			}
		 }
	  }
	  if (temOperando == false){
		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Warning!", "EXPRESSÃO REGULAR INVÁLIDA"));
		 return;
	  }
	  else if(infixaDividido.length == 0) {
		 getInfixaConvertida().add(infixaDividido[cont]);
	  }
	  
	  System.out.println("INFIXA CONVERTIDA:" + infixaConvertida.toString());
	  System.out.println("===================================");
	  System.out.println();
	  converterInfixaPosFixa();
   }
   
   public void converterInfixaPosFixa() {
	  Pilha<String> pilha = new Pilha<String>();
	  
	  for(String aux : getInfixaConvertida()){
		 if("(".equals(aux)){
			pilha.insere(aux);
		 }
		 else if (")".equals(aux)){
			if("operador".equals(verificaSimbolo(pilha.topo()))){
			   while("operador".equals(verificaSimbolo(pilha.topo()))){
				   getPosfixaConvertida().add(pilha.topo());
				   pilha.remove();
				}
			} if (!pilha.vazia()){
			   while("(".equals(pilha.topo())){
				   pilha.remove();
				}
			}
			else {
			   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Erro!"));
			}
		 }
		 else if ("operando".equals(verificaSimbolo(aux))){
			getPosfixaConvertida().add(aux);
		 }
		 else if("operador".equals(verificaSimbolo(aux))){
			
			if(!pilha.vazia()){
			   if ("operador".equals(verificaSimbolo(pilha.topo()))){
				  int op1 = descobrirOperador(aux);
				  int op2 = descobrirOperador(pilha.topo());
				  
				  while (op1 <= op2){
					 getPosfixaConvertida().add(pilha.topo());
					 pilha.remove();
					 
					 if(!pilha.vazia()){
						if ("operador".equals(verificaSimbolo(pilha.topo()))){
						   op2 = descobrirOperador(pilha.topo());
						}else {
						   break;
						}
					 }else
						break;					 
				  }
			   }
			   pilha.insere(aux);
			}
			else{
			   pilha.insere(aux);
			}
		 }
	  }
	  
	  if(!pilha.vazia()) {
      	  while("operador".equals(verificaSimbolo(pilha.topo()))){
      		 getPosfixaConvertida().add(pilha.topo());
      		 pilha.remove();
      	  }
	  }
	  
	  String pos = getPosfixaConvertida().toString();
	  pos = pos.replaceAll(" ","");
	  pos = pos.replaceAll("\\[","");
	  pos = pos.replaceAll("\\]","");
	  pos = pos.replace(",","");
	  setPosfixa(pos);
   }
  
   public void chamaThompson(){
	  Trabalho2Controller t = new Trabalho2Controller();
	  
	  for (String temp : getPosfixaConvertida()){
		 if ("operando".equals(verificaSimbolo(temp)) && !getAlfabeto().getSimbolos().contains(temp))
			getAlfabeto().getSimbolos().add(temp);
	  }
	  
	  getAlfabeto().getSimbolos().add("&");
	  setAutomato(t.thompson(posfixaConvertida));
	  setTabelaAFN(t.criarTabela(getAutomato().getQtdEstados(),getAlfabeto().getSimbolos().size()+1,0));
	  
	  System.out.println("\n============= TABELA AFN-e ===============");
	  for (int i=0; i < getTabelaAFN().getColumnCount(); i++)
		 System.out.printf("%s\t", getTabelaAFN().getColumnName(i));
	  System.out.println();

	  for (int i=0; i < getTabelaAFN().getRowCount(); i++) {
		 Map<String,String> stats = new LinkedHashMap<String, String>();
		 for (int j=0; j < getTabelaAFN().getColumnCount(); j++){
			System.out.printf("%s\t", getTabelaAFN().getValueAt(i, j));
			if (j!=0){
			   String value = getTabelaAFN().getValueAt(i , j).toString();
			   stats.put(getAlfabeto().getSimbolos().get(j-1) , value);
			}
		 }
		 
		 System.out.println();
		 TabelaTransicao f=new TabelaTransicao(getAutomato().getEstado(i).getEtiqueta()
			+getAutomato().getEstado(i).getIdentificador()+"");
		 f.setTrans(stats);
		 getTabelaTransicoes().add(f);
	  }
	  getAutomato().setAlfabeto(getAlfabeto());
   }
   
   
   /*** CHAMADA AO METODO QUE CONVERTE O AFN -> AFD **/
   public void converteAFD(){
	  afd = Trabalho3Controller.getAFD(getAutomato(), getPosfixa());
      TabelaTransicao tabelaAfd = afd.getTabelaTransicao();
      
      System.out.println("============= TABELA AFD ===============");
      for (int i=0; i < tabelaAfd.getColumnCount(); i++)
          System.out.printf("%s\t\t", tabelaAfd.getColumnName(i));
      
      // DESENHA TABELA NO CONSOLE
      System.out.println();
      for (int i=0; i < tabelaAfd.getRowCount(); i++) {
    	 Map<String,String> stats = new LinkedHashMap<String, String>();
          for (int j=0; j < tabelaAfd.getColumnCount(); j++){
              System.out.printf("%s\t\t", tabelaAfd.getValueAt(i, j));
              
              if (j!=0){
            	 String value = tabelaAfd.getValueAt(i , j).toString();
            	 stats.put(tabelaAfd.getColumnName(j) , value);
              }
          }
          System.out.println();
          TabelaTransicao f=new TabelaTransicao(afd.getEstado(i).getEtiqueta()+afd.getEstado(i).getIdentificador()+"");
          f.setTrans(stats);
          getTransicoesTabelaAFD().add(f);
      }
      afd.getAlfabeto().getSimbolos().remove("&");
      setDa(afd.getFechos());
   }
   
   /* VERIFICA SE O SIMBOLO É OPERANDO OU OPERADOR**/
   public String verificaSimbolo(String s) {
	  String retorno="";
	  if (s.matches("[a-zA-Z]")){
		 retorno = "operando";
	  }
	  else if (s.equals("&")){
		 retorno = "operando";
	  }
	  else {
		 for (Operandor o : Operandor.values()){
			if(s.equals(o.getOperador())){
			   retorno = "operador";
			   break;
			}
		 }
	  }
	  return retorno;
   }
   
   /* RETORNA PRECEDENCIA DO OPERADOR */
   public int descobrirOperador(String s){
	  int operador = 0;
	  if (s.equals(Operandor.SOMA.getOperador())){
		 operador = 1;
	  }
	  else if (s.equals(Operandor.CONCATENACAO.getOperador())){
		 operador = 2;
	  }
	  else if (s.equals(Operandor.FECHO.getOperador())){
		 operador = 3;
	  }
	  return operador;
   }
   
   public void limpar(){
	  setPosfixa(null);
	  setPosfixaConvertida(null);
	  setInfixaConvertida(null);
   }
   
   private Automato automato;
   
   private AFD afd;
   
   private AlfabetoAltomato alfabeto;
   private List<TransicaoAutomato> transicoesTabela;

   public Automato getAutomato() {
	  if (automato == null)
		 automato = new Automato();
      return automato;
   }

   public void setAutomato(Automato automato) {
      this.automato=automato;
   }
   
   public AFD getAfd() {
	  if(afd == null)
		 afd = new AFD();
      return afd;
   }

   public void setAfd(AFD afd) {
      this.afd=afd;
   }

   public AlfabetoAltomato getAlfabeto() {
	  if (alfabeto == null)
		 alfabeto = new AlfabetoAltomato();
      return alfabeto;
   }

   public void setAlfabeto(AlfabetoAltomato alfabeto) {
      this.alfabeto=alfabeto;
   }
   
   public List<TransicaoAutomato> getTransicoesTabela() {
	  if (transicoesTabela == null)
		 transicoesTabela = new ArrayList<TransicaoAutomato>();
      return transicoesTabela;
   }

   public void setTransicoesTabela(List<TransicaoAutomato> transicoesTabela) {
      this.transicoesTabela=transicoesTabela;
   }
   
   private TabelaTransicao tabelaAFN = new TabelaTransicao(null, null);
   private List<TabelaTransicao> tabelaTransicoes;
   
   public TabelaTransicao getTabelaAFN() {
      return tabelaAFN;
   }

   public void setTabelaAFN(TabelaTransicao tabelaAFN) {
      this.tabelaAFN=tabelaAFN;
   }
   
   public void setTransicoesTabelaAFD(List<TabelaTransicao> transicoesTabelaAFD) {
      this.transicoesTabelaAFD=transicoesTabelaAFD;
   }

   public List<TabelaTransicao> getTabelaTransicoes() {
	  if (tabelaTransicoes == null)
		 tabelaTransicoes = new ArrayList<TabelaTransicao>();
      return tabelaTransicoes;
   }
   
   private TabelaTransicao tabelaAFD = new TabelaTransicao(null, null);
   private List<TabelaTransicao> transicoesTabelaAFD;
   
   public TabelaTransicao getTabelaAFD() {
      return tabelaAFD;
   }

   public void setTabelaAFD(TabelaTransicao tabelaAFD) {
      this.tabelaAFD=tabelaAFD;
   }
   
   public List<TabelaTransicao> getTransicoesTabelaAFD() {
	  if (transicoesTabelaAFD == null)
		 transicoesTabelaAFD = new ArrayList<TabelaTransicao>();
      return transicoesTabelaAFD;
   }

   public void setTabelaTransicoes(List<TabelaTransicao> tabelaTransicoes) {
      this.tabelaTransicoes=tabelaTransicoes;
   }
   
   String fechoCompleto;

   public String getDa() {
	  return fechoCompleto;
   }

   public void setDa(String da) {
	  this.fechoCompleto=da;
   }
}
