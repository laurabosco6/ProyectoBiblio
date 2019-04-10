package Administradores;

import java.util.Scanner;

public class Administrador {

	public static void main(String[] args) {
		
		Scanner teclado = new Scanner(System.in);
		GestorAdministrador g = new GestorAdministrador();
		
		int opcion;
		
		do {
			
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
