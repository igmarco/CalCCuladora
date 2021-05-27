package ServidorRaicesPolinomios;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class CalcularRaiz extends Thread{
	
	private ArrayList<Double> polinomio;
	private double puntoInicio;
	private double error;
	private double resultado;
	private Semaphore sem;
	private CountDownLatch cl;
	private Socket s;
	
	public CalcularRaiz(ArrayList<Double> pol, double puntoInicio,double error,Semaphore sem,CountDownLatch cl,Socket s) {
		this.polinomio=pol;
		this.puntoInicio=puntoInicio;
		this.error=error;
		this.sem=sem;
		this.s = s;
		this.cl=cl;
	}
	
	
	public void run() {
	
		//Aplicaremos el metodo iterativo de Newton-Raphson para calcular la raiz en cuestion:
		double aproximacionAnterior=this.puntoInicio;
		ArrayList<Double> derivada = CalcularRaices.Derivar(this.polinomio);
		double evaDerivada = evaluacion(derivada,this.puntoInicio);
		if(evaDerivada==0)
			this.puntoInicio*=2;
		double aproximacion = this.puntoInicio-evaluacion(polinomio,this.puntoInicio)/evaluacion(derivada,this.puntoInicio);
		while(Math.abs(aproximacion-aproximacionAnterior)>error) {
			aproximacionAnterior=aproximacion;
			aproximacion = aproximacion - (evaluacion(polinomio,aproximacion)/evaluacion(derivada,aproximacion));
		}
		try {
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			this.resultado=aproximacionAnterior;
			System.out.println(this.resultado);
			sem.acquire();
			this.Escribir(out);
			out.flush();
			sem.release();
			cl.countDown();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
//		}catch(BrokenBarrierException e) {
//			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public double evaluacion(ArrayList<Double> polinomio, double x) {
		double resultado=0;
		for(int i=0;i<polinomio.size();i++) {
			resultado+=polinomio.get(i)*Math.pow(x, i);
		}
		return resultado;
	}
	
	public double getResultado() {
		return this.resultado;
	}
	
	public synchronized void Escribir(DataOutputStream out) {
		try {
			if(Math.abs(this.resultado)<Math.pow(10, -9)) {
				out.writeBytes(String.valueOf(resultado)+" Aprox: 0"+"\r\n");
			}else
				out.writeBytes(String.valueOf(resultado)+"\r\n");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
