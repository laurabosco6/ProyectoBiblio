package Bibliotecarios;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class Bibliotecario {

	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		int opcion=0, ejemplares;
		String titulo, tituloNuevo;
		GestorBibliotecario gestionar = new GestorBibliotecario();
		BufferedReader in = null;
		PrintWriter out = null;
		
		while(opcion!=6) {
			System.out.println("(1) Dar de alta un libro");
			System.out.println("(2) Dar de baja un libro");
			System.out.println("(3) Modificar el titulo de un libro");
			System.out.println("(4) Aumentar el numero de ejemplares");
			System.out.println("(5) Disminuir el numero de ejemplares");
			opcion = Integer.parseInt(teclado.nextLine());
			switch (opcion) {
			case 1:
				System.out.println("Introduce el título del libro que quieras dar de alta");
				titulo=teclado.nextLine();
				if(gestionar.darDeAlta(in, out, titulo)) {
					System.out.println("El libro "+titulo+" ya se ha dado de alta");
				}else {
					System.out.println("El libro ya ha sido dado de alta antes o el fichero no esta creado");
				}
				break;
			case 2:
				System.out.println("Introduce el título del libro");
				titulo=teclado.nextLine();
				if(gestionar.darDeBaja(in, out, titulo)){
					System.out.println("El libro se ha dado de baja");
				}else {
					System.out.println("El libro no existe por lo que no se ha dado de baja");
				}
				break;
			case 3:
				System.out.println("Introduce el título del libro a cambiar");
				titulo=teclado.nextLine();
				System.out.println("Introduce el titulo por el que lo quieras cambiar");
				tituloNuevo=teclado.nextLine();
				if(gestionar.modificarTitulo(in, out, titulo, tituloNuevo)) {
					System.out.println("El titulo se ha modificado");
				}else {
					System.out.println("El titulo no se ha modificado");
					System.out.println("Comprueba si esta bien escrito");
				}
				break;
			case 4:
				System.out.println("Introduce el título del libro");
				titulo=teclado.nextLine();
				System.out.println("Introduce el número de ejemplares que quieras aumentar");
				ejemplares=Integer.parseInt(teclado.nextLine());
				gestionar.aumentarEjemplares(in,out,titulo,ejemplares);
				break;
			case 5:
				System.out.println("Introduce el título del libro");
				titulo=teclado.nextLine();
				System.out.println("Introduce el número de ejemplares que quieras disminuir");
				ejemplares=Integer.parseInt(teclado.nextLine());
				gestionar.disminuirEjemplares(in,out,titulo,ejemplares);
				break;
			}
		}
		
		teclado.close();
	}

}
