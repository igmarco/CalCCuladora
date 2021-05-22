package Tools;

import org.apache.commons.math3.complex.Complex;

public class Complejo{
     private double real;
     private double imag;

  public Complejo() {
     real=0.0;
     imag=0.0;
  }
  public Complejo(double real, double imag){
     this.real=real;
     this.imag=imag;
  }
  public static Complejo conjugado(Complejo c){
     return new Complejo(c.real, -c.imag);
  }
  
  public double getReal() {
	  return this.real;
  }
  
  public double getImag() {
	  return this.imag;
  }
  
  public static Complejo opuesto(Complejo c){
     return new Complejo(-c.real, -c.imag);
  }
  public double modulo(){
     return Math.sqrt(real*real+imag*imag);
  }
//devuelve el �ngulo en grados
  public double argumento(){
     double angulo=Math.atan2(imag, real);     //devuelve el �ngulo entre -PI y +PI
     if(angulo<0)  angulo=2*Math.PI+angulo;
     return angulo*180/Math.PI;
  }
//suma de dos n�meros complejos
  public static Complejo suma(Complejo c1, Complejo c2){
     double x=c1.real+c2.real;
     double y=c1.imag+c2.imag;
     return new Complejo(x, y);
  }
//producto de dos n�meros complejos
 public static Complejo producto(Complejo c1, Complejo c2){
     double x=c1.real*c2.real-c1.imag*c2.imag;
     double y=c1.real*c2.imag+c1.imag*c2.real;
     return new Complejo(x, y);
  }
//producto de un complejo por un n�mero real
  public static Complejo producto(Complejo c, double d){
     double x=c.real*d;
     double y=c.imag*d;
     return new Complejo(x, y);
 }
//producto de un n�mero real  por un complejo
  public static Complejo producto(double d, Complejo c){
     double x=c.real*d;
     double y=c.imag*d;
     return new Complejo(x, y);
 }
//cociente de dos n�meros complejos
//excepci�n cuando el complejo denominador es cero
  public static Complejo cociente(Complejo c1, Complejo c2)throws ExcepcionDivideCero{
     double aux, x, y;
     if(c2.modulo()==0.0){
          throw new ExcepcionDivideCero("Divide entre cero");
     }else{
          aux=c2.real*c2.real+c2.imag*c2.imag;
          x=(c1.real*c2.real+c1.imag*c2.imag)/aux;
          y=(c1.imag*c2.real-c1.real*c2.imag)/aux;
     }
     return new Complejo(x, y);
  }
//cociente entre un n�mero complejo y un n�mero real
  public static Complejo cociente(Complejo c, double d)throws ExcepcionDivideCero{
    double x, y;
    if(d==0.0){
          throw new ExcepcionDivideCero("Divide entre cero");
    }else{
        x=c.real/d;
        y=c.imag/d;
    }
     return new Complejo(x, y);
  }
//el n�mero e elevado a un n�mero complejo
  public static Complejo exponencial(Complejo c){
     double x=Math.cos(c.imag)*Math.exp(c.real);
     double y=Math.sin(c.imag)*Math.exp(c.real);
     return new Complejo(x, y);
  }
//ra�z cuadrada de un n�mero positivo o negativo
  public static Complejo csqrt(double d){
     if(d>=0) return new Complejo(Math.sqrt(d), 0);
     return new Complejo(0, Math.sqrt(-d));
  }
//funci�n auxiliar  para la potencia de un n�mero complejo
  private static double potencia(double base, int exponente){
    double resultado=1.0;
    for(int i=0; i<exponente; i++){
        resultado*=base;
    }
    return resultado;
  }
//funci�n auxiliar para la potencia de un n�mero complejo
  private static double combinatorio(int m, int n){
    long num=1;
    long den=1;
    for(int i=m; i>m-n; i--){
        num*=i;
    }
    for(int i=2; i<=n; i++){
        den*=i;
    }
    return (double)num/den;
  }
//potencia de un n�mero complejo
  public static Complejo potencia(Complejo c, int exponente){
    double x=0.0, y=0.0;
    int signo;
    for(int i=0; i<=exponente; i++){
        signo=(i%2==0)?+1:-1;
//parte real
        x+=combinatorio(exponente, 2*i)*potencia(c.real, exponente-2*i)*potencia(c.imag, 2*i)*signo;
        if(exponente==2*i)  break;
//parte imaginaria
        y+=combinatorio(exponente, 2*i+1)*potencia(c.real, exponente-(2*i+1))*potencia(c.imag, 2*i+1)*signo;
    }
    return new Complejo(x, y);
  }
  
  public static Complejo potencia(Complejo base, Complejo exponente) {
	  Complex baseAux = new Complex(base.getReal(),base.getImag());
	  Complex exponenteAux = new Complex(exponente.getReal(),exponente.getImag());
	  
	  Complex resultado = baseAux.pow(exponenteAux);
	  
	  return new Complejo(resultado.getReal(),resultado.getImaginary());
  }
  
//traduce un n�mero complejo a un string
  public String toString(){
     if(imag>0 && real != 0) return new String((double)Math.round(1000*real)/1000+" + "+(double)Math.round(1000*imag)/1000+"*i");
     else if(imag < 0 && real != 0) return new String((double)Math.round(1000*real)/1000+" - "+(double)Math.round(-1000*imag)/1000+"*i");
     else if(imag == 0) return new String((double)Math.round(1000*real)/1000 + "");
     else return new String((double)Math.round(1000*imag)/1000+"*i");
  }
}

//class ExcepcionDivideCero extends Exception {
//
//  public ExcepcionDivideCero() {
//         super();
//  }
//  public ExcepcionDivideCero(String s) {
//         super(s);
//  }
//}
