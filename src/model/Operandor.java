package model;

public enum Operandor {
  
   SOMA("+"), 
   CONCATENACAO("."), 
   FECHO("*"),
   VAZIA("&");
   
   private String operador;
   
   private Operandor(String operador) {
	  this.operador = operador;
   }

   public String getOperador() {
      return operador;
   }

   public void setValor(String operador) {
      this.operador=operador;
   }
   
}
