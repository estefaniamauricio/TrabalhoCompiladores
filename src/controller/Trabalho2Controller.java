
package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import model.AlfabetoAltomato;
import model.Automato;
import model.EstadoAutomato;
import model.Operandor;
import model.Pilha;
import model.TabelaTransicao;
import model.TransicaoAutomato;

@ManagedBean(name="trabalhoDois")
@ViewScoped
public class Trabalho2Controller implements Serializable{
   private static final long serialVersionUID= -1107306893828397551L;
   
   private Automato automato;
   private AlfabetoAltomato alfabeto;
  
   public Automato getAutomato() {
	  if (automato == null)
		 automato = new Automato();
      return automato;
   }

   public void setAutomato(Automato automato) {
      this.automato=automato;
   }
   
   public AlfabetoAltomato getAlfabeto() {
	  if (alfabeto == null)
		 alfabeto = new AlfabetoAltomato();
      return alfabeto;
   }

   public void setAlfabeto(AlfabetoAltomato alfabeto) {
      this.alfabeto=alfabeto;
   }

   public Automato thompson(List<String> posfixa) {
	  Pilha<Automato> pilhaAutomato = new Pilha<Automato>();
	  int cont = 0;
	  Automato op1;
	  while (cont < posfixa.size()){
		 String simbolo = posfixa.get(cont);
		 if ("operando".equals(verificaSimbolo(simbolo))){
			getAlfabeto().getSimbolos().add(simbolo);
			pilhaAutomato.insere(basico(simbolo));
		 }
		 else {
			if ("operador".equals(verificaSimbolo(simbolo))){
			   if ("*".equals(simbolo)){
				  op1=pilhaAutomato.remove();
				  pilhaAutomato.insere(fechoKleene(op1));
			   }else {
				  Automato op2=pilhaAutomato.remove();
				  if (pilhaAutomato.vazia()){
					 System.out.println("Pilha Vazia");
				  }else {
					 op1 = pilhaAutomato.remove();
					 if (".".equals(simbolo)){
						pilhaAutomato.insere(concatenacao(op1 , op2));
					 }
					 else if ("+".equals(simbolo)){
						pilhaAutomato.insere(uniao(op1 , op2));
					 }
				  }
				  
			   }
			}
		 }cont++;
	  }
	  
	  Automato afd=pilhaAutomato.remove();
	  if (pilhaAutomato.vazia()){
		 setAutomato(afd);
	  }
	  organizaAlfabeto();
	  return afd;
   }
   
   
   public static Automato basico(String simbolo) {
	  Automato afn=new Automato();
     
      EstadoAutomato ini = new EstadoAutomato(0);
      EstadoAutomato fin = new EstadoAutomato(1, true);
      
      TransicaoAutomato tran = new TransicaoAutomato(fin, simbolo);
      ini.getTransicoes().add(tran);   
      afn.addEstado(ini);
      afn.addEstado(fin);
      afn.setQtdEstados(afn.getEstados().size());
      return afn;
  }

   public Automato concatenacao(Automato a, Automato b) {
	  Automato novo=new Automato();
	  
	  // copiar estados de 'a' para o novo automato;
	  copiarEstados(a , novo , novo.getQtdEstados());
	  EstadoAutomato ultimoAtual = novo.getEstado(novo.getQtdEstados()-1);
	  
	  // copiar estados de 'b' para o novo automato;
	  copiarEstados(b , novo , novo.getQtdEstados()-1, 1);
	  EstadoAutomato inicioB = b.getEstadoInicial();
	  
	  
	  copiarTransicoes(novo , inicioB.getTransicoes() , ultimoAtual , ultimoAtual.getIdentificador());
	  
	  novo.getEstado(novo.getQtdEstados()-1).setEsFinal(true);
	  
	  return novo;
   }

   public Automato uniao(Automato a, Automato b) {
	  Automato novo=new Automato();
	  TransicaoAutomato transicao;
	  
	  EstadoAutomato novoInicio = new EstadoAutomato(novo.getQtdEstados());
	  novo.addEstado(novoInicio);
	  
	  copiarEstados(a , novo , novo.getQtdEstados());
	  copiarEstados(b , novo , novo.getQtdEstados());
	  
	  EstadoAutomato novoFim = new EstadoAutomato(novo.getQtdEstados(), true);
	  novo.addEstado(novoFim);
	  
	  //add transicao vazia 'novoIncio' para o primeiro de 'a'
	  transicao = new TransicaoAutomato();
	  transicao.setEstado(novo.getEstado(1));
	  transicao.setSimbolo(AlfabetoAltomato.VAZIO);
	  novoInicio.getTransicoes().add(transicao);
	  
	  //add transicao vazia 'novoIncio' para o primeiro de 'b'
	  transicao = new TransicaoAutomato();
	  transicao.setEstado(novo.getEstado(a.getQtdEstados() + 1));
	  transicao.setSimbolo(AlfabetoAltomato.VAZIO);
	  novoInicio.getTransicoes().add(transicao);	  
	  
	  //cria transicao vazia para o ultimo estado (novoFim)
	  transicao = new TransicaoAutomato();
	  transicao.setEstado(novo.getEstado(novo.getQtdEstados() - 1)); //ultimo estado
	  transicao.setSimbolo(AlfabetoAltomato.VAZIO);
	  novo.getEstado(a.getQtdEstados()).getTransicoes().add(transicao);//ultimo estado do 'a' transicao para ultimo do 'novo'
	  
	  //cria transicao vazia para o ultimo estado (novoFim)
	  transicao = new TransicaoAutomato();
	  transicao.setEstado(novo.getEstado(novo.getQtdEstados() - 1)); //ultimo estado
	  transicao.setSimbolo(AlfabetoAltomato.VAZIO);
	  novo.getEstado(novo.getQtdEstados()-2).getTransicoes().add(transicao);//ultimo estado do 'b' transicao para ultimo do 'novo'
	  
	  return novo;
   }

   public Automato fechoKleene(Automato a) {
	  Automato novo=new Automato();
	  
	  EstadoAutomato novoInicio = new EstadoAutomato(novo.getQtdEstados());
	  novo.addEstado(novoInicio);
	  copiarEstados(a , novo , novo.getQtdEstados());
	  
	  EstadoAutomato novoFim = new EstadoAutomato(novo.getQtdEstados(), true);
	  novo.addEstado(novoFim);
	  
	  novoInicio.getTransicoes().add(new TransicaoAutomato(novo.getEstado(1), AlfabetoAltomato.VAZIO));
	  novoInicio.getTransicoes().add(new TransicaoAutomato(novoFim, AlfabetoAltomato.VAZIO));
	  
	  EstadoAutomato finalAnterior = novo.getEstado(novo.getQtdEstados()-2);
	  finalAnterior.getTransicoes().add(new TransicaoAutomato(novo.getEstado(1), AlfabetoAltomato.VAZIO));
	  finalAnterior.getTransicoes().add(new TransicaoAutomato(novoFim, AlfabetoAltomato.VAZIO));
	  
	  return novo;
   }
   
   public void copiarEstados(Automato origem, Automato destino, int incrementoTrans){
	  copiarEstados(origem , destino , incrementoTrans , 0);
   }
   
   public void copiarEstados(Automato origem, Automato destino, int incrementoTrans, int omitidos){
	  int incrementoEst = incrementoTrans;
	  for (int i=omitidos;i < origem.getQtdEstados(); i++){
		 destino.addEstado(new EstadoAutomato(destino.getQtdEstados()));
	  }
	  
	  /* Contador de omitidos */
      int contador = 0;
	  
	  for (EstadoAutomato temp : origem.getEstados()){
		 
		 if (omitidos > contador++)
            continue;
		 
		 EstadoAutomato objetivo = destino.getEstado(temp.getIdentificador()+incrementoEst);
		 copiarTransicoes(destino, temp.getTransicoes(), objetivo, incrementoTrans);
	  }
   }

   private void copiarTransicoes(Automato destino, List<TransicaoAutomato> transicoes, EstadoAutomato objetivo, int incrementoTrans) {
	  for (TransicaoAutomato trans : transicoes){
		 int idDestino = trans.getEstado().getIdentificador(); //para qual estado vai a trasicao
		 String simbolo = trans.getSimbolo(); //simbolo da transicao

		 EstadoAutomato estadoDestino = destino.getEstado(idDestino+incrementoTrans);

		 TransicaoAutomato novaTransicao = new TransicaoAutomato(estadoDestino, simbolo);
		 objetivo.getTransicoes().add(novaTransicao);
	  }
   }
   
   @SuppressWarnings("unchecked")
   public TabelaTransicao criarTabela(int qtdLinhas, int qtdColunas, int indiceZero) {
       String[] cabecalho = new String[qtdColunas];
      
       Object[][] transicoes = new Object[qtdLinhas][qtdColunas];
      
       cabecalho[indiceZero] = "Estados";
       
       //preenche o cabeçalho
       for (int i=indiceZero + 1; i < qtdColunas; i++)
           cabecalho[i] = getAlfabeto().getSimbolo(i - indiceZero - 1);
      
       //preenche primeira coluna com estados
       for (int i=0; i < qtdLinhas; i++) {
    	   if(i==0)
    		  transicoes[i][indiceZero] = "->"+getAutomato().getEstado(i).getIdentificador();
    	   else if (i==qtdLinhas-1)
    		  transicoes[i][indiceZero] = "*"+getAutomato().getEstado(i).getIdentificador();
    	   else
    		  transicoes[i][indiceZero] = getAutomato().getEstado(i).getIdentificador();
       }
       
       for (EstadoAutomato e : getAutomato().getEstados()) {
           int fil = e.getIdentificador();
          
           for (TransicaoAutomato t : e.getTransicoes()) {
               int col = getAlfabeto().obtenerPosicion(t.getSimbolo());
              
               if (transicoes[fil][col + indiceZero + 1] == null)
            	  transicoes[fil][col + indiceZero + 1] = new ArrayList<Integer>();
              
               int id = t.getEstado().getIdentificador();
               ((List<Integer>) transicoes[fil][col + indiceZero + 1]).add(id);
           }
       }
      
       String vazio = "";
       for (int i=0; i < qtdLinhas; i++) {
           for (int j=indiceZero + 1; j < qtdColunas; j++) {
               if (transicoes[i][j] == null)
                   transicoes[i][j] = vazio;
           }
       }
      
       return new TabelaTransicao(cabecalho, transicoes);
   }
   
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
   
   public void organizaAlfabeto(){
	  List<String> novoAlfabeto = new ArrayList<String>();
	  for(String s : getAlfabeto().getSimbolos()){
		if(!novoAlfabeto.contains(s))
		   novoAlfabeto.add(s);
	  }
	  getAlfabeto().setSimbolos(novoAlfabeto);
   }
}
