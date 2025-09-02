package Clases;

import java.util.ArrayList;
import java.util.List;

public class Niveles {
	
	private int numero;
	private int velo;
	private int colum;
	private int filas;
	
	public Niveles(int numero, int filas, int colum, int velo ) {
		this.numero = numero;
		this.colum = colum;
		this.filas = filas;
		this.velo = velo;
	}
	// Generar los nivles
	public List<Ene> generar(){
		List<Ene> enemigos = new ArrayList<>();
		int espacio = 40;
		for(int fila =0; fila < filas; fila++) {
			for(int col =0; col < colum; col++) {
				enemigos.add(new Ene(50+col*espacio,50+fila*espacio));
			}
		}
		return enemigos;
	}
	public int getvelo() {
		return velo;
	}
	public int getnumero() {
		return numero;
	}

}
