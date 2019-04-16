package Administradores;

import java.io.RandomAccessFile;
import java.util.Scanner;

public class Administrador {

	public static void main(String[] args) {
		
		Scanner teclado = new Scanner(System.in);
		GestorAdministrador g = new GestorAdministrador();
		String usuario, password;
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
			inicio = g.iniciarSesion(r, usuario, password);
			
			if (!inicio) {
				System.err.println("Error de autenticacion");
				System.out.println("");
			}
		} while (!bFormado || !inicio);
		
		int opcion;
		
		do {
			
			System.out.println("");
			System.out.println("ADMINISTRADOR");
			System.out.println("1.	Dar de alta un usuario");
			System.out.println("2.	Dar de baja un usuario");
			System.out.println("3.	Listar usuarios");
			opcion = Integer.parseInt(teclado.nextLine());
			System.out.println("");
			
			switch (opcion) {
			case 1:
				g.darAltaUsuario(teclado);
				break;
			case 2:
				g.darBajaUsuario(teclado);
				break;
			case 3:
				g.listarUsuarios();
				break;
			default:
				if (opcion!=0) {
					System.out.println("Respuesta invalida");
				}
				break;
			}
			
		} while (opcion!=0);
		
		teclado.close();

		System.out.println("Adios");
		
	}

}
