package Administradores;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class GestorAdministrador {
	
	/* 1. Dar de alta usuario
	* 2. Dar de baja usuario
	*/
	
	
	public void darAltaUsuario(Scanner teclado) {
		RandomAccessFile r = null;
		int id;
		String usuario, pass, usuarioEncontrado;
		boolean encontrado=false;
		try {
			r = new RandomAccessFile("usuarios", "rw");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		do{
			System.out.println("Nombre de usuario nuevo:");
			usuario = teclado.nextLine();
			System.out.println("");
			int posicionUsuario=4;
			encontrado = false;
			try {
				while (!encontrado) {
					r.seek(posicionUsuario);
					usuarioEncontrado="";
					for (int i = 0; i < 15; i++) {
						usuarioEncontrado += r.readChar();
					}
					try {
						usuarioEncontrado = usuarioEncontrado.substring(0, usuarioEncontrado.indexOf('$'));
					} catch (Exception e) {
						System.out.println();
					}
					if (usuarioEncontrado.equals(usuario)) {
						encontrado = true;
						System.out.println("Este nombre de usuario ya existe, por favor, introduzca otro");
						System.out.println("");
					}
					posicionUsuario+=64;
				}
			} catch (IOException e) {
				System.out.println();
			}
		} while (encontrado);
		
		System.out.println("Introduce contrasenya");
		pass = teclado.nextLine();
		do {
			System.out.println("Que tipo de usuario sera");
			System.out.println("1.	Administrador");
			System.out.println("2.	Bibliotecario");
			System.out.println("3.	Usuario");
			id = Integer.parseInt(teclado.nextLine());
			System.out.println("");
			if (id<1 || id>3) {
				System.out.println("Respuesta invalida");
			}
		} while (id<1 || id>3);
		while (usuario.length()!=15) {
			usuario += '$';
		}
		while (pass.length()!=15) {
			pass += '$';
		}
		try {
			r = new RandomAccessFile("usuarios", "rw");
			r.seek(r.length());
			r.writeInt(id);
			r.writeChars(usuario);
			r.writeChars(pass);
		} catch (IOException e) {
			System.out.println();
		} finally {
			if (r!=null) {
				try {
					r.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void darBajaUsuario(Scanner teclado) {
		
		RandomAccessFile r = null;
		String usuario,usuarioEncontrado="";
		int posicionUsuario=4;
		boolean encontrado = false;
		System.out.println("Introduce el nombre de usuario");
		usuario = teclado.nextLine();
		System.out.println("");
		try {
			r = new RandomAccessFile("usuarios", "rw");
			while (!encontrado) {
				r.seek(posicionUsuario);
				usuarioEncontrado="";
				for (int i = 0; i < 15; i++) {
					usuarioEncontrado += r.readChar();
				}
				try {
					usuarioEncontrado = usuarioEncontrado.substring(0, usuarioEncontrado.indexOf('$'));
				} catch (Exception e) {
					System.out.println();
				}
				if (usuarioEncontrado.equals(usuario)) {
					
					RandomAccessFile rCopia = new RandomAccessFile("usuarios2", "rw");
					int id;
					String pass;
					
					try {
						int posicionUsuario2=4;
						String usuarioEncontrado2;
						while (true) {
							usuarioEncontrado2="";
							r.seek(posicionUsuario2);
							for (int i = 0; i < 15; i++) {
								usuarioEncontrado2 += r.readChar();
							}
							try {
								usuarioEncontrado2 = usuarioEncontrado2.substring(0, usuarioEncontrado2.indexOf('$'));
							} catch (Exception e) {
								System.out.println();
							}
							if (!usuarioEncontrado2.equals(usuario)) {
								r.seek(posicionUsuario2-4);
								id = r.readInt();
								r.seek(posicionUsuario2+30);
								pass="";
								for (int i = 0; i < 15; i++) {
									pass += r.readChar();
								}
								while (usuarioEncontrado2.length()!=15) {
									usuarioEncontrado2 += '$';
								}
								rCopia.writeInt(id);
								rCopia.writeChars(usuarioEncontrado2);
								rCopia.writeChars(pass);
							}
							posicionUsuario2+=64;
						}
					} catch (Exception e) {
						
					} finally {
						if (rCopia!=null) {
							rCopia.close();
						}
					}
					encontrado = true;
				}
				posicionUsuario+=64;
			}
		} catch (IOException e) {
			System.out.println();
		} finally {
			if (!encontrado) {
				System.out.println("No existe ese usuario");
				System.out.println("");
			}
			if (r!=null) {
				try {
					r.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		Path partida = FileSystems.getDefault().getPath("usuarios2");
		Path destino = FileSystems.getDefault().getPath("usuarios");
		try {
			Files.move(partida, destino, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			
		}
	}
	
	public void listarUsuarios() {
		int id;
		String usuario="", pass="";
		RandomAccessFile r = null;
		try {
			r = new RandomAccessFile("usuarios", "rw");
			r.seek(0);
			while (true) {
				usuario="";
				pass="";
				id = r.readInt();
				for (int i=0;i<15;i++) {
					usuario += r.readChar();
				}
				for (int i=0;i<15;i++) {
					pass += r.readChar();
				}
				try {
					usuario = usuario.substring(0, usuario.indexOf('$'));
				} catch (Exception e) {
					System.out.println();
				}
				try {
					pass = pass.substring(0, pass.indexOf('$'));
				} catch (Exception e) {
					System.out.println();
				}
				System.out.print("Usuario:		");
				System.out.println(usuario);
				System.out.print("Contrasenya:		");
				System.out.println(pass);
				System.out.print("Tipo de usuario:	");
				if (id==1) {
					System.out.println("administrador");
				} else {
					if (id==2) {
						System.out.println("bibliotecario");
					} else {
						System.out.println("usuario");
					}
				}
				System.out.println("");
			}
		} catch (IOException e) {
			System.out.println();
		} catch (Exception e) {
			System.out.println();
		} finally {
			if (r!=null) {
				try {
					r.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
