package model;

import java.util.Map;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class TabelaTransicao implements TableModel {

   private Object[][] transicoes;
   private String[] header;
   private String estado;
   private Map<String,String> trans;

   public TabelaTransicao(String[] header, Object[][] transicoes) {
	  this.header = header;
      this.transicoes = transicoes;
   }
   
   public TabelaTransicao(String estado){
	  this.estado = estado;
   }
   
   public int getRowCount() {
      return transicoes.length;
  }

  public int getColumnCount() {
      return header.length;
  }

  public String getColumnName(int columnIndex) {
      return header[columnIndex];
  }

  public Class<?> getColumnClass(int columnIndex) {
      return getValueAt(0, columnIndex).getClass();
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
      return false;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
      return transicoes[rowIndex][columnIndex];
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      transicoes[rowIndex][columnIndex] = aValue;
  }
 
  public void setHeaderAt(String aValue, int columnIndex) {
      header[columnIndex] = aValue;
  }

  public void addTableModelListener(TableModelListener listener) {
      return;
  }

  public void removeTableModelListener(TableModelListener listener) {
      return;
  }

  public void setTrans(Map<String, String> trans) {
	 this.trans=trans;
  }

  public String getTrans(String simbolo) {
	 return trans.get(simbolo);
  }

  public String getEstado() {
	 return estado;
  }

  public void setEstado(String estado) {
	 this.estado=estado;
  }
  
}
