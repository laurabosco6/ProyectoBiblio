package Bibliotecarios;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Bibliotecario {

	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		int opcion=0, ejemplares;
		String usuario, password, titulo, tituloNuevo;
		GestorBibliotecario gestionar = new GestorBibliotecario();
		BufferedReader in = null;
		PrintWriter out = null;
		RandomAccessFile r = null;
		boolean bFormado=true, inicio=false;
		
		System.out.println(" _________________");
		System.out.println(" |                |");
		System.out.println(" | INICIAR SESION |");
		System.out.println(" |________________|");
		System.out.println();

		do {
			System.out.print(" Usuario: ");
			usuario = teclado.nextLine();
			if (usuario.length() > 15) {
				bFormado = false;
			}
			System.out.print(" Contrasenya: ");
			password = teclado.nextLine();
			if (password.length() > 15) {
				bFormado = false;
			}
			inicio = gestionar.iniciarSesion(r, usuario, password);
			
			if (!inicio) {
				System.err.println("Error de autenticacion");
				System.out.println("");
			}
		} while (!bFormado || !inicio);
		
		while(opcion!=6) {
			System.out.println("(1) Dar de alta un libro");
			System.out.println("(2) Dar de baja un libro");
			System.out.println("(3) Modificar el titulo de un libro");
			System.out.println("(4) Aumentar el numero de ejemplares");
			System.out.println("(5) Disminuir el numero de ejemplares");
			System.out.println("(6) Terminar el programa");
			opcion = Integer.parseInt(teclado.nextLine());
			switch (opcion) {
			case 1:
				System.out.println("Introduce el título del libro que quieras dar de alta");
				titulo=teclado.nextLine();
				int deAlta=gestionar.darDeAlta(in, out, titulo, teclado);
				if(deAlta==1) {
					System.out.println("El libro "+titulo+" se ha dado de alta");
				}else if(deAlta==2) {
					System.out.println("El libro ya ha sido dado de alta antes");
				}else {
					opcion=6;
				}
				System.out.println();
				break;
			case 2:
				System.out.println("Introduce el título del libro");
				titulo=teclado.nextLine();
				if(gestionar.darDeBaja(in, out, titulo)){
					System.out.println("El libro se ha dado de baja");
				}else {
					System.out.println("El libro no se ha dado de baja");
				}
				System.out.println();
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
				System.out.println();
				break;
			case 4:
				System.out.println("Introduce el título del libro");
				titulo=teclado.nextLine();
				System.out.println("Introduce el número de ejemplares que quieras aumentar");
				ejemplares=Integer.parseInt(teclado.nextLine());
				gestionar.aumentarEjemplares(in,out,titulo,ejemplares);
				System.out.println();
				break;
			case 5:
				System.out.println("Introduce el título del libro");
				titulo=teclado.nextLine();
				System.out.println("Introduce el número de ejemplares que quieras disminuir");
				ejemplares=Integer.parseInt(teclado.nextLine());
				gestionar.disminuirEjemplares(in,out,titulo,ejemplares);
				System.out.println();
				break;
			case 6:
			}
		}
		
		teclado.close();
	}

}
