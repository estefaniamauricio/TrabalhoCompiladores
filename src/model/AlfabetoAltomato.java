package model;

import java.util.ArrayList;
import java.util.List;

public class AlfabetoAltomato{
   
   public static final String VAZIO = "&";
   
   private List<String> simbolos;
   
   public String getSimbolo(int pos) {
      if (pos == getSimbolos().size())
          return AlfabetoAltomato.VAZIO;
      else
          return simbolos.get(pos);
  }

   public List<String> getSimbolos() {
	  if (simbolos == null)
		 simbolos = new ArrayList<String>();
      return simbolos;
   }

   public void setSimbolos(List<String> simbolos) {
      this.simbolos=simbolos;
   }
   
   public int obtenerPosicion(String caracter) {
      if (caracter.equals(AlfabetoAltomato.VAZIO))
          return getSimbolos().size();
      else
          return simbolos.indexOf(caracter);
  }
 
}
