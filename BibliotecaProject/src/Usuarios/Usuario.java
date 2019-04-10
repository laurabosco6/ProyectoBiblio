package Usuarios;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Usuario {

	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		GestorUsuario user = new GestorUsuario();
		BufferedReader in = null;
		PrintWriter out = null;
		RandomAccessFile r = null;
		int opcion=0;
		boolean bFormado=true, inicio=false;
		String usuario, pasword, titulo, autor;

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
			System.out.print(" Contraseña: ");
			pasword = teclado.nextLine();
			if (pasword.length() > 15) {
				bFormado = false;
			}
		} while (!bFormado);

		while (!inicio) {
			if (user.iniciarSesion(r, usuario, pasword)) {
				inicio = true;
			}
		}
		
		System.out.println("(1) Cojer un libro en prestamo");
		System.out.println("(2) Devolver un libro prestado");
		System.out.println("(3) Listar libro por titulo");
		System.out.println("(4) Listar libros por autor");
		System.out.println("(5) Listar todos los libros");
		System.out.println("(6) Salir");
		opcion=teclado.nextInt();
		while (opcion != 6) {
			switch (opcion) {
			case 1:
				System.out.print("Titulo del libro: ");
				titulo=teclado.nextLine();
				titulo=teclado.nextLine();
				user.pedirLibro(in, out, titulo);
				break;
			case 2:
				System.out.print("Titulo del libro: ");
				titulo=teclado.nextLine();
				titulo=teclado.nextLine();
				if(user.devolverLibro(in, titulo)) {
					user.devolverLibro(in, titulo);
				}
				break;
			case 3:
				System.out.print("Titulo del libro: ");
				titulo=teclado.nextLine();
				titulo=teclado.nextLine();
				user.listarDisponibles(in, titulo);
				break;
			case 4:
				System.out.println("Autor: ");
				autor=teclado.nextLine();
				autor=teclado.nextLine();
				user.listarPorAutor(in, autor);
				break;
			case 5:
				user.listarTodos(in);
				break;
			}
		}
		teclado.close();
	}
}
