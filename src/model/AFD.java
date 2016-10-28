package model;

import java.util.ArrayList;
import java.util.List;

public class AFD extends Automato{
   
   private List<List<EstadoAutomato>> estadosD;
   private String exprReg;
   private AlfabetoAltomato alfabeto;
   private String fechos;
   
   public AFD() {
	  this(null, "");
   }
   
   public AFD(AlfabetoAltomato alfabeto, String exprReg) {
	  this.alfabeto = alfabeto;
	  this.exprReg = exprReg;
	  estadosD = null;
   }
   
   public List<List<EstadoAutomato>> getEstadosD() {
	  if (estadosD == null) {
		 estadosD = new ArrayList<List<EstadoAutomato>>();
	  }
      return estadosD;
   }

   public void setEstadosD(List<List<EstadoAutomato>> estadosD) {
      this.estadosD=estadosD;
   }

   public String getExprReg() {
      return exprReg;
   }

   public void setExprReg(String exprReg) {
      this.exprReg=exprReg;
   }

   public AlfabetoAltomato getAlfabeto() {
      return alfabeto;
   }

   public void setAlfabeto(AlfabetoAltomato alfabeto) {
      this.alfabeto=alfabeto;
   }
   
   public String getFechos() {
      return fechos;
   }

   public void setFechos(String fechos) {
      this.fechos=fechos;
   }

   public TabelaTransicao getTabelaTransicao() {
	  TabelaTransicao tabela;
     
      if (getEstadosD() != null) {
          int qtdLinhas = getEstados().size();
          int qtdColunas = getAlfabeto().getSimbolos().size();

          tabela = criarTabelaAfd(qtdLinhas, qtdColunas, 0);
      }
      else {
          int qtdLinhas = getEstados().size();
          int qtdColunas = getAlfabeto().getSimbolos().size() + 1;
          tabela = criarTabelaAfd(qtdLinhas, qtdColunas, 0);
      }
      return tabela;
  }
   
   @SuppressWarnings("unchecked")
	public TabelaTransicao criarTabelaAfd(int linhas, int colunas, int indice0 ) {

	  String[] cabecalho = new String[colunas];
	  Object[][] transicoes = new Object[linhas][colunas];

	  cabecalho[indice0 ] = "Estados";
	  
	  //preenche header
	  for (int i=indice0  + 1; i < colunas; i++)
		 cabecalho[i] = getAlfabeto().getSimbolo(i - indice0  - 1);
	  
	  //preenche estados
	  for (int i=0; i < linhas; i++) {
		 if(getEstado(i).getIdentificador() == 0){
			getEstado(i).setEtiqueta("->");
		 }
		 if(getEstado(i).getEsFinal() && !"*".equals(getEstado(i).getEtiqueta()))
			getEstado(i).setEtiqueta(getEstado(i).getEtiqueta()+"*");
			transicoes[i][indice0 ] = getEstado(i).getEtiqueta()+getEstado(i).getIdentificador();
	  }

	  for (EstadoAutomato e : getEstados()) {
		 int fil = e.getIdentificador();

		 for (TransicaoAutomato t : e.getTransicoes()) {
			if(!"&".equals(t.getSimbolo())){
      			int col = getAlfabeto().obtenerPosicion(t.getSimbolo());
      
      			if (transicoes[fil][col + indice0 + 1] == null)
      			   transicoes[fil][col + indice0 + 1] = new ArrayList<Integer>();
      
      			int id = t.getEstado().getIdentificador();
      			((List<Integer>) transicoes[fil][col + indice0 + 1]).add(id);
			}
		 }
	  }

	  String vazio = "";
	  for (int i=0; i < linhas; i++) {
		 for (int j=indice0  + 1; j < colunas; j++) {
			if (transicoes[i][j] == null)
			   transicoes[i][j] = vazio;
		 }
	  }

	  return new TabelaTransicao(cabecalho, transicoes);
   }
}
