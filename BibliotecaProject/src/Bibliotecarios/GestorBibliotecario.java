package Bibliotecarios;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Scanner;

public class GestorBibliotecario {
	LocalDate fecha = LocalDate.now();

	/* Libro:
	 * isbn		int
	 * Titulo	String
	 * Autor	String
	 * Prestado	(true/false) boolean
	 * fecha	(null/fecha) String (dd-mm-yyyy)
	 */

	public boolean existeElLibro(BufferedReader in, String title) { // true si existe, false si no existe
		String c, titulo;
		boolean existe=false;
		
		try {
			in = new BufferedReader(new FileReader("libros"));
			
			while ((c = in.readLine()) != null && !existe) {
				String todo;						 					// isbn+titulo+autor+prestado+fecha
				
				todo = c.substring(c.indexOf(",") + 1); 				// titulo+autor+prestado+fecha
				titulo = todo.substring(0, todo.indexOf(",")); 			// titulo
				
				if(titulo.equals(title)) {
					existe=true;
				}
			}
			
		}catch (FileNotFoundException e) {
			System.err.println("Fichero no encontrado!!");
		}catch (IOException f) {
			System.out.println();
		}finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Error, " + e.getMessage());
				}
			}
		}
		return existe;
	}

	public int darDeAlta(BufferedReader in, PrintWriter out, String title, Scanner teclado) { // 1=Dado de alta, 2=No dado, 3=Terminar
		boolean existe=true;
		File f = new File("libros");
		File f2 = new File("IDlibros");
		PrintWriter out2 =null;
		
		try {		//Para que existan ambos
			if (!f.exists()) {
				if(!f2.exists()) {
					System.out.println("Los ficheros no existen, los creamos");
					f.createNewFile();
					f2.createNewFile();
					existe=false;
				}else {
					int eleccion=0;
					while(eleccion!=1 && eleccion!=2) {
						System.out.println("No puede existir el fichero libros sin existir el fichero IDlibros, puedes:");
						System.out.println("(1) Borrar ambos ficheros y crear de nuevo toda la informacion.");
						System.out.println("(2) Terminar el programa en ejecucion.");
						eleccion = teclado.nextInt();

						switch (eleccion) {
						case 1:
							f.delete();
							f2.delete();
							f.createNewFile();
							f2.createNewFile();
							break;
						case 2:
							return 3;
						}
						System.out.println("Numero incorrecto");
					}
				}
			}
			
			if(f.length()>0) {
				in = new BufferedReader(new FileReader("libros"));
				if (!existeElLibro(in, title)) {
					existe=false;
				}else {
					return 2;
				}
				if (in != null) {
					in.close();
				}
			}
			
			if(!existe) {
				String ultimoid = null;
				int isbn;
				if(f.length()==0) {
					out2 = new PrintWriter(new BufferedWriter(new FileWriter("IDlibros",true)));
					out2.println(10000+","+title);
					out = new PrintWriter(new BufferedWriter(new FileWriter("libros",true)));
					String autorEs=GestorBibliotecario.nombres()+" "+GestorBibliotecario.apellidos();
					boolean prestadoEs=false;
					String fechaEs = "NO";
					out.println(10000+","+title+","+autorEs+","+prestadoEs+","+fechaEs);
				}else {
					BufferedReader in2 = new BufferedReader(new FileReader("IDlibros"));
					String id;
					while((id = in2.readLine())!=null) {
						ultimoid=id;
					}
					in2.close();
						
					out2 = new PrintWriter(new BufferedWriter(new FileWriter("IDlibros",true)));
					isbn = Integer.parseInt(ultimoid.substring(0, ultimoid.indexOf(",")));
					isbn++;
					out2.println(isbn+","+title);
					
					out = new PrintWriter(new BufferedWriter(new FileWriter("libros",true)));
					String autorEs=GestorBibliotecario.nombres()+" "+GestorBibliotecario.apellidos();
					boolean prestadoEs=false;
					String fechaEs = "NO";
					
					out.println(isbn+","+title+","+autorEs+","+prestadoEs+","+fechaEs);
				}
			}
			
		} catch (IOException e) {
			System.err.println("Error");
		} finally {
			if (out != null) {
				out.close();
			}
			if(out2 != null) {
				out2.close();
			}
		}
		
		return 1;
	}
	
	
	public boolean darDeBaja(BufferedReader in, PrintWriter out, String title) {
		
		File f = new File("libros");
		File f2 = new File("IDlibros");
		if (!f.exists()) {
			System.out.println("El fichero libros no existe.");
			return false;
		}else if(f.length()==0){
			System.out.println("El fichero libros no existe.");
			return false;
		}else if(f2.length()==0){
			System.out.println("El fichero IDlibros no existe.");
			return false;
		}else {
			if (!f2.exists()) {
				System.out.println("El fichero IDlibros no existe");
				return false;
			}
		}
		
		if(!existeElLibro(in, title)) {
			System.out.println("El libro no existe.");
			return false;
		}
		
		int ejemplaresBorrados=0;
		try {
			in = new BufferedReader(new FileReader("libros"));
			out = new PrintWriter(new BufferedWriter(new FileWriter("librosTemporal")));
			String c, titulo;
			
			while ((c = in.readLine()) != null) {
				String todo = c;// isbn+titulo+autor+prestado+fecha
				
				todo = c.substring(c.indexOf(",") + 1); 				// titulo+autor+prestado+fecha
				titulo = todo.substring(0, todo.indexOf(",")); 			// titulo
				
				if(titulo.equals(title)) {
					ejemplaresBorrados++;
				}else {
					out.println(c);
				}
			}
			
		}catch (IOException e) {
			System.err.println("!!!");
		}finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				out.close();
			}
			GestorBibliotecario.renombrarlo();
		}
		
		if(ejemplaresBorrados==0) {
			System.out.println("Se ha borrado el ejemplar de "+title);
		}else {
			System.out.println("Se han borrado los "+ejemplaresBorrados+" ejemplares del libro "+title);
		}
		
		return true;
	}

	
	public boolean modificarTitulo(BufferedReader in, PrintWriter out, String tituloComparar, String tituloNuevo) {
		
		File f = new File("libros");
		File f2 = new File("IDlibros");
		if (!f.exists()) {
			System.out.println("El fichero libros no existe.");
			return false;
		}else if(f.length()==0){
			System.out.println("El fichero libros no existe.");
			return false;
		}else if (!f2.exists()) {
			System.out.println("El fichero IDlibros no existe");
			return false;
		}else if(f2.length()==0){
			System.out.println("El fichero IDlibros no existe.");
			return false;
		}
		
		if(!existeElLibro(in, tituloComparar)) {
			return false;
		}
		String c, titulo;
			
		try {
			in = new BufferedReader(new FileReader("libros"));		
			out = new PrintWriter(new BufferedWriter(new FileWriter("librosTemporal")));	
			
			while ((c = in.readLine()) != null) {

				String todo; 														// isbn+titulo+autor+prestado+fecha
				todo = c.substring(c.indexOf(",") + 1); 							// titulo+autor+prestado+fecha
				titulo = todo.substring(0, todo.indexOf(",")); 						//2 titulo

				if (titulo.equals(tituloComparar)) {
					String autorEs, prestadoEs, fechaEs, isbnEs;
					isbnEs = c.substring(0, c.indexOf(","));				 		//1 isbn
					autorEs = c.substring(c.indexOf(",") + 1); 						//  titulo+autor+prestado+fecha
					autorEs = autorEs.substring(autorEs.indexOf(",") + 1); 			//  autor+prestado+fecha
					autorEs = autorEs.substring(0, autorEs.indexOf(",")); 			//3 autor
					prestadoEs = c.substring(c.indexOf(",") + 1); 					//  titulo+autor+prestado+fecha
					prestadoEs = prestadoEs.substring(prestadoEs.indexOf(",") + 1); //  autor+prestado+fecha
					prestadoEs = prestadoEs.substring(prestadoEs.indexOf(",") + 1); //  prestado+fecha
					prestadoEs = prestadoEs.substring(0, autorEs.indexOf(",")); 	//4 prestado
					fechaEs = c.substring(c.lastIndexOf(",") + 1);					//5 fecha

					out.println(isbnEs + "," + tituloNuevo + "," + autorEs + "," + prestadoEs + "," + fechaEs);
				} else {
					out.println(c);
				}
			}

		} catch (FileNotFoundException e) {
			System.err.println("Fichero no encontrado!!");
		} catch (IOException j) {
			System.out.println();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Error, " + e.getMessage());
				}
			}
			if(out!=null) {
				out.close();
			}
		}
		GestorBibliotecario.renombrarlo();
		return true;
	}
	
	public static void renombrarlo(){
		Path partida = FileSystems.getDefault().getPath("librosTemporal");
		Path destino = FileSystems.getDefault().getPath("libros");
		 try {
			 Files.move(partida, destino, StandardCopyOption.REPLACE_EXISTING);
		 } catch (IOException e) {
			 System.out.println("Errrooooorrr"+e.getMessage());
		 }
	}
	
	private static String nombres() {
		String nombre[]=new String[36];
		nombre[0]="Alfonso";
		nombre[1]="Lucia";
		nombre[2]="Emilio";
		nombre[3]="Sofia";
		nombre[4]="Antonio";
		nombre[5]="Maria";
		nombre[6]="Carlos";
		nombre[7]="Paula";
		nombre[8]="Nicolas";
		nombre[9]="Julia";
		nombre[10]="Roberto";
		nombre[11]="Alba";
		nombre[12]="Daniel";
		return nombre[(int)(Math.random()*13)];
	}

	public static String apellidos() {
		String apellido[]=new String[36];
		apellido[0]="García";
		apellido[1]="Gonzalez";
		apellido[2]="Rodriguez";
		apellido[3]="Fernández";
		apellido[4]="Martinez";
		apellido[5]="Sanchez";
		apellido[6]="Perez";
		apellido[7]="Parra";
		apellido[8]="Jimenez";
		apellido[9]="Ferrer";
		apellido[10]="Pascual";
		apellido[11]="Gómez";
		apellido[12]="Megina";
		return apellido[(int)(Math.random()*13)];
	}
	
	
	public void aumentarEjemplares(BufferedReader in, PrintWriter out, String tituloPedido, int numAumento) {
		String c, linea, titulo;
		boolean encontrado=false;
		String principioLinea3, principioLinea4;
		
		int numEjemplares = contarEjemplares(in, out, tituloPedido);
		
		if (numEjemplares!=0) {
		
			try {
				in = new BufferedReader(new FileReader("libros"));
				out = new PrintWriter(new BufferedWriter(new FileWriter("libros")));
				
				while ((c = in.readLine())!=null) {						// c = 		isbn , titulo , autor , prestado , fecha
					while (encontrado) {
						linea = c.substring(c.charAt(',')+1);			// linea =	titulo , autor , prestado , fecha
						titulo = linea.substring(0, linea.charAt(','));	// titulo =	titulo
						principioLinea4 = c.substring(0,c.lastIndexOf(','));
						principioLinea3 = principioLinea4.substring(0,principioLinea4.lastIndexOf(','));
						if (titulo.equalsIgnoreCase(tituloPedido)) {
							for (int i=0;i<numAumento;i++) {
								out.println(principioLinea3+","+false+"NO");
								encontrado = true;
							}
						}
					}
				}
				
			} catch (FileNotFoundException e) {
				System.out.println("No existe ningun libro");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (in!=null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out!=null) {
					out.close();
				}
			}
		} else {
			System.out.println("No se ha encontrado el libro");
		}
	}
	
	
	public void disminuirEjemplares(BufferedReader in, PrintWriter out, String tituloPedido, int numDisminuciones) {
		
		int numEjemplares = contarEjemplares(in, out, tituloPedido);
		
		if (numEjemplares!=0) {
			
			String c, linea, titulo, principioLinea4, prestado;
			int contador = 0;
			
			try {
				in = new BufferedReader(new FileReader("libros"));
				out = new PrintWriter(new BufferedWriter(new FileWriter("librosTemporal")));
				while ((c = in.readLine())!=null) {					// c = 		isbn , titulo , autor , prestado , fecha
					linea = c.substring(c.charAt(',')+1);			// linea =	titulo , autor , prestado , fecha
					titulo = linea.substring(0, linea.charAt(','));	// titulo =	titulo
					principioLinea4 = c.substring(0,c.lastIndexOf(','));
					prestado = principioLinea4.substring(principioLinea4.lastIndexOf(',')+1);
					while (contador<numDisminuciones) {
						if (titulo.equalsIgnoreCase(tituloPedido)) {
							if (prestado.equalsIgnoreCase("true")) {
								out.println(c);
							} else {
								contador++;
							}
						} else {
							out.println(c);
						}
					}
					out.println();
				}
			} catch (FileNotFoundException e) {
				System.out.println("No existe ningun libro");
			} catch (IOException e) {
				System.out.println();
			} finally {
				if (in!=null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out!=null) {
					out.close();
				}
			}
			
			Path partida = FileSystems.getDefault().getPath("librosTemporal");
			Path destino = FileSystems.getDefault().getPath("libros");
			try {
				Files.move(partida, destino, StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				
			}
			
		} else {
			System.out.println("No se ha encontrado el libro");
		}
		
	}
	
	
	public int contarEjemplares(BufferedReader in, PrintWriter out, String tituloPedido) {
		int numEjemplares=0;
		String c, linea, titulo;
		try {
			while ((c = in.readLine())!=null) {				// c = 		isbn , titulo , autor , prestado , fecha
				linea = c.substring(c.charAt(',')+1);		// linea =	titulo , autor , prestado , fecha
				titulo = linea.substring(0, c.charAt(','));	// titulo =	titulo
				if (titulo.equalsIgnoreCase(tituloPedido)) {
					numEjemplares++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return numEjemplares;
	}

}

