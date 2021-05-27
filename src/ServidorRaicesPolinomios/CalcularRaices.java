package ServidorRaicesPolinomios;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class CalcularRaices extends Thread{
	
	private Socket s;
	private ArrayList<Double> polinomio;
	
	public CalcularRaices(Socket s,ArrayList<Double> polinomio) {
		this.s = s;
		this.polinomio = polinomio;
	}
	
	public void run() {
		//En este hilo que será unico para cada cliente, 1º con la regla de signos de Descartes
		//tratar de ver más o menos cuantas raices positivas y negativas reales.
		//2º acotación de las raices con MacLaurin
		//3º (puramente educativo) buscamos posibles raíces enteras usando la regla de Horner.
		//4º aplicamos sturm para obtener la secuencia del mismo {P(x),P1(x),...,Pn(x)} donde P es nuestro polinomio, P1 su derivada y 
		//Pk(x) es el resto cambiado de signo de Pk-2(x)/Pk-1(x) para 2<=k<=n.
		int NumCambiosDeSignoRNeg=0,NumCambiosDeSignoRPos=0;
		
		//Aplicamos la regla de signos de Descartes
		ArrayList<Double> Descartes = new ArrayList();
		ArrayList<Double> DescartesNeg = new ArrayList();
		for(int i=0; i<this.polinomio.size(); i++) {
			if(this.polinomio.get(i)!=0) {
				Descartes.add(this.polinomio.get(i));
				if(i%2==0) {
					DescartesNeg.add(this.polinomio.get(i));
				}else
					DescartesNeg.add(this.polinomio.get(i)*-1);
			}
		}
		for(int i=0;i<Descartes.size()-1;i++) {	
			if(Descartes.get(i)*Descartes.get(i+1)<0) {
				NumCambiosDeSignoRPos++;
			}
			if(DescartesNeg.get(i)*DescartesNeg.get(i+1)<0)
				NumCambiosDeSignoRNeg++;
		}
		
		System.out.println("Positivas: "+NumCambiosDeSignoRPos+" Negativas: "+NumCambiosDeSignoRNeg);
		
		//Ahora vamos a calcular las cotas con el Método de MacLaurin -> (1/(1+μ) < |ζi| < (1+λ) donde {ζ1,...,ζn} C Complejos las raices n raices. 
		//Primero calculamos μ, con μ = máximo de {|ak/a0|} donde 1<=k<=n y a es el coeficiente de x.
		double mu;
		double cotaMinima;
		if(this.polinomio.get(0)==0||this.polinomio.size()==1) {
			mu=0;
			cotaMinima=0;
		}else {
			mu=(this.polinomio.get(1)/this.polinomio.get(0));
			for(int i=1;i<this.polinomio.size();++i) {
				if(mu<Math.abs(this.polinomio.get(i)/this.polinomio.get(0)))
					mu=Math.abs(this.polinomio.get(i)/this.polinomio.get(0));
			}
			cotaMinima = 1/(1+mu);
		}
		System.out.println("Cota Minima: ±"+cotaMinima);
		//Ahora calculamos λ, con λ = máximo de {|ak/an|} donde 0<=k<=n-1 y a es el coeficiente de x.
		Double lambda=Math.abs((this.polinomio.get(0)/this.polinomio.get(this.polinomio.size()-1)));
		for(int i=0;i<this.polinomio.size()-1;++i) {
			if(lambda<Math.abs(this.polinomio.get(i)/this.polinomio.get(this.polinomio.size()-1)))
				lambda=Math.abs(this.polinomio.get(i)/this.polinomio.get(this.polinomio.size()-1));
		}
		Double cotaMaxima = 1+lambda;
		System.out.println("Cota Máxima: ±"+cotaMaxima);
		
		//Usando la regla de Horner calculamos las posibles raices enteras, este paso es totalmente opcional, es solo puramente lúdico.
		ArrayList<Double> PosiblesRaicesEnteras = new ArrayList();
		Double a0 = this.polinomio.get(0);
		for(int i=1;i<=a0;i++) {
			if(a0%i==0) {
				PosiblesRaicesEnteras.add((double)i);
				PosiblesRaicesEnteras.add((double)-i);
			}
		}
		
		System.out.println(PosiblesRaicesEnteras.toString());
		
		//aplicamos sturm:
		
		//Primero calculamos la derivada:
		ArrayList<Double> p1 = this.Derivar(this.polinomio);
		System.out.println(p1.toString());
		
		//Calculamos la secuencia:
		ArrayList<ArrayList<Double>> listaDePolinomios = new ArrayList();
		listaDePolinomios.add(this.polinomio);
		listaDePolinomios.add(p1);
		boolean RaicesSimples = SecuenciaSturm(p1,this.polinomio,listaDePolinomios,this.polinomio.size()-1,2);
		
		for(int i=0; i<listaDePolinomios.size();i++) {
			System.out.println(listaDePolinomios.get(i).toString());
		}
		
		//Teniendo la secuencia de sturm aplicamos el teorema sirviendonos del metodo de la bisección hasta encontrar 1 solo cambio de signo:
		System.out.println("Nº cambios de signo en la cota superior negativa: "+TeoremaSturm(listaDePolinomios,cotaMaxima*-1));
		System.out.println("Nº cambios de signo en cero: "+TeoremaSturm(listaDePolinomios,(double)0));
		System.out.println("Nº cambios de signo en la cota superior positiva: "+TeoremaSturm(listaDePolinomios,cotaMaxima));

		try{
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			out.writeBytes(p1.toString()+"\r\n");
			out.writeBytes(NumCambiosDeSignoRPos+" "+NumCambiosDeSignoRNeg+"\r\n");
			//Preuba pra formatear bien las cotas: DecimalFormat df = new DecimalFormat("#.000000");
			out.writeBytes("±"+String.format("%.5f", cotaMinima)+" ±"+String.format("%.5f", cotaMaxima)+"\r\n");
			out.writeBytes(listaDePolinomios.toString()+"\r\n");
			out.writeBytes(TeoremaSturm(listaDePolinomios,cotaMaxima*-1)+" "+TeoremaSturm(listaDePolinomios,(double)0)+" "+TeoremaSturm(listaDePolinomios,cotaMaxima)+"\r\n");
			out.flush();
			if(Math.abs(TeoremaSturm(listaDePolinomios,cotaMaxima*-1)-TeoremaSturm(listaDePolinomios,cotaMaxima))==0) {
				if(this.polinomio.size()==1&&this.polinomio.get(0)==0) {
					out.writeBytes("La recta real son las raíces de la función"+"\r\n");
					out.flush();
				}else {
					out.writeBytes("No existen raices reales"+"\r\n");
					out.flush();
				}
			}else {
				int numeroRaices = Math.abs(TeoremaSturm(listaDePolinomios,cotaMaxima*-1)-TeoremaSturm(listaDePolinomios,cotaMaxima));
				if(numeroRaices == 1) {
//					out.writeBytes("\r\n"+"Hay "+numeroRaices+" raiz real, que es: "+"\r\n");
				}else {
//					out.writeBytes("\r\n"+"Hay "+numeroRaices+" raíces reales distintas, que son: "+"\r\n");
				}
				out.writeBytes(numeroRaices+"\r\n");
				out.flush();
				ExecutorService pool = Executors.newFixedThreadPool(numeroRaices);
				//CyclicBarrier barrera = new CyclicBarrier(Math.abs(TeoremaSturm(listaDePolinomios,cotaMaxima*-1)-TeoremaSturm(listaDePolinomios,cotaMaxima))+1);
				Semaphore sem = new Semaphore(1);
				CountDownLatch count = new CountDownLatch(Math.abs(TeoremaSturm(listaDePolinomios,cotaMaxima*-1)-TeoremaSturm(listaDePolinomios,cotaMaxima)));
				LocalizarRaices(pool,listaDePolinomios,cotaMaxima*-1,cotaMaxima,sem,count,this.s);
				//barrera.await();
				count.await();
				out.close();
			}
//		}catch(BrokenBarrierException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	public static ArrayList<Double> Derivar(ArrayList<Double> polinomio){
		ArrayList<Double> derivada = new ArrayList();
		for(int i=1; i<polinomio.size();i++) {
			derivada.add(polinomio.get(i)*i);
		}
		return derivada;
	}
	
	public boolean SecuenciaSturm(ArrayList<Double> imenos2,ArrayList<Double> imenos1, ArrayList<ArrayList<Double>> listaDePolinomios, int grado, int numPaso){
		if(numPaso<=grado) {
			Double ultimoCoefi1 = imenos1.get(imenos1.size()-1);
			int gradoDeCoefi1 = imenos1.size()-1;
			Double ultimoCoefi2 = imenos2.get(imenos2.size()-1);
			int gradoDeCoefi2 = imenos2.size()-1;
			ArrayList<Double> aux = new ArrayList();
			ArrayList<Double> copia = (ArrayList<Double>)imenos1.clone();
			while(gradoDeCoefi2<=gradoDeCoefi1) {
				Double coeficoci = ultimoCoefi1/ultimoCoefi2;
				int gradococi = gradoDeCoefi1-gradoDeCoefi2;
				for(int i=0;i<gradoDeCoefi1;i++) {
					aux.add(i, null);
				}
				for(int i=0;i<=gradoDeCoefi2-1;i++) {
					Double resta = imenos2.get(i)*coeficoci;
					aux.set(i+gradococi,copia.get(i+gradococi)-resta);
				}
				for(int i=0;i<gradoDeCoefi1;i++) {
					if(aux.get(i)==null) {
						aux.set(i, copia.get(i));
					}
				}
				int cont = aux.size()-1;
				while(cont>=0&&aux.get(cont)==0) {
					aux.remove(cont);
					cont--;
				}
				copia = (ArrayList<Double>)aux.clone();
				aux.clear();
				gradoDeCoefi1 = copia.size()-1;
				if(copia.size()==0) {
					gradoDeCoefi1=0;
				}else
					ultimoCoefi1 = copia.get(copia.size()-1);
			}
			if(copia.size()==0) {
				return false;
			}
			if(copia.size()==1) {
				if(copia.get(copia.size()-1)==0) {
					return false;
				}else {
					for(int i=0; i<copia.size();i++) {
						copia.set(i,copia.get(i)*-1);
					}
					listaDePolinomios.add(copia);
					return true;
				}
			}else {
				for(int i=0; i<copia.size();i++) {
					copia.set(i,copia.get(i)*-1);
				}
				listaDePolinomios.add(copia);
				return SecuenciaSturm(copia,imenos2,listaDePolinomios,grado,(numPaso+1));
			}
		}else {
			return false;
		}
	}
	
	public int TeoremaSturm(ArrayList<ArrayList<Double>> listaDePolinomios,Double x) {
		ArrayList<String> signos = new ArrayList();
		for(int i=0;i<listaDePolinomios.size();i++) {
			double total=0;
			for(int j=0;j<listaDePolinomios.get(i).size();j++) {
				total += listaDePolinomios.get(i).get(j)*Math.pow(x, j);
			}
			if(total<0) {
				signos.add("-");
			}else {
				signos.add("+");
			}
		}
		int cambios=0;
		for(int i=0;i<signos.size();i++) {
			if(i<signos.size()-1&&!(signos.get(i).equals(signos.get(i+1)))) {
				cambios++;
			}
		}
		return cambios;
	}
	
	public void LocalizarRaices(ExecutorService pool,ArrayList<ArrayList<Double>> listaDePolinomios,Double cotaMaximaIz,Double cotaMaximaDer, Semaphore sem,CountDownLatch count,Socket s) {
		CalcularRaiz a;
		CalcularRaiz b;
		if(Math.abs(TeoremaSturm(listaDePolinomios,cotaMaximaIz)-TeoremaSturm(listaDePolinomios,cotaMaximaDer))==1) {
			if(TeoremaSturm(listaDePolinomios,cotaMaximaIz)-TeoremaSturm(listaDePolinomios,(cotaMaximaDer+cotaMaximaIz)/2)==1) {
				a = new CalcularRaiz(this.polinomio,(cotaMaximaIz+(cotaMaximaDer+cotaMaximaIz)/2)/2,(Double)Math.pow(10, -15),sem,count,s);
				pool.execute(a);
			}else {
				a = new CalcularRaiz(this.polinomio,(cotaMaximaDer+(cotaMaximaDer+cotaMaximaIz)/2)/2,(Double)Math.pow(10, -15),sem,count,s);
				pool.execute(a);
			}
		}else {
			Double c = (cotaMaximaDer+cotaMaximaIz)/2;			
			if(Math.abs(TeoremaSturm(listaDePolinomios,cotaMaximaIz)-TeoremaSturm(listaDePolinomios,c))==1){
				a = new CalcularRaiz(this.polinomio,(cotaMaximaIz+c)/2,(Double)Math.pow(10, -15),sem,count,s);
				pool.execute(a);
			}
			if(Math.abs(TeoremaSturm(listaDePolinomios,c)-TeoremaSturm(listaDePolinomios,cotaMaximaDer))==1){
				b = new CalcularRaiz(this.polinomio,(cotaMaximaDer+c)/2,(Double)Math.pow(10, -15),sem,count,s);
				pool.execute(b);
			}
			if(Math.abs(TeoremaSturm(listaDePolinomios,cotaMaximaIz)-TeoremaSturm(listaDePolinomios,c))>1){				
				LocalizarRaices(pool,listaDePolinomios,cotaMaximaIz,c,sem,count,s);
			}
			if(Math.abs(TeoremaSturm(listaDePolinomios,c)-TeoremaSturm(listaDePolinomios,cotaMaximaDer))>1) {
				LocalizarRaices(pool,listaDePolinomios,c,cotaMaximaDer,sem,count,s);
			}			
		}
	}

}
