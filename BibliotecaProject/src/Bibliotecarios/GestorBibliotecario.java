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

public class GestorBibliotecario {
	LocalDate fecha = LocalDate.now();
	public static int idISBN=10000;

	/* Libro:
	 * isbn		int
	 * Titulo	String
	 * Autor	String
	 * Prestado	(true/false) boolean
	 * fecha	(null/fecha) String (dd-mm-yyyy)
	 */

	public boolean existeElLibro(BufferedReader in, String title) {
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

	public void sumaidISBN() {
		GestorBibliotecario.idISBN++;
	}

	public boolean darDeAlta(BufferedReader in, PrintWriter out, String title) { // Devuelve true cuando se le da de																		// alta
		PrintWriter out2 = null;
		boolean noExiste = false;

		try {	//Por si no existe	in
			File f = new File("libros");
			if (f.exists()) {
				in = new BufferedReader(new FileReader("libros"));
				if (!existeElLibro(in, title)) {
					noExiste = true;
				}
			} else {
				System.out.println("El fichero no existe, lo creamos");
				f.createNewFile();
				noExiste = true;
			}
		} catch (IOException e) {
			System.err.println("Error");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Error, " + e.getMessage());
				}
			}
		}
		if (!noExiste) { 		// Si existe
			return !noExiste; 	// Devuelve false
		}
		
		try{			//Comprueba que existe el isbn
			File f2 = new File("IDlibros");
			if(!f2.exists()) {
				f2.createNewFile();
				out2 = new PrintWriter(new BufferedWriter(new FileWriter("IDlibros",true)));
				out2.println(Integer.parseInt("10000")+","+title);
			out2.close();
			}
		}catch (Exception e) {
			System.err.println("Errrorr");
		}
		
		try {		//Imprime
			out = new PrintWriter(new BufferedWriter(new FileWriter("libros",true)));
			BufferedReader in2 = new BufferedReader(new FileReader("IDlibros"));
			
			String id,ultimo = null;
			while((id = in2.readLine())!=null) {
				ultimo=id;
			}
			
			in2.close();
			
			int isbnEs=Integer.parseInt(ultimo.substring(0, ultimo.indexOf(",")))+1;
			String autorEs=GestorBibliotecario.nombres()+" "+GestorBibliotecario.apellidos();
			String tituloEs=title;
			boolean prestadoEs=false;
			String fechaEs = "NO";
			
			out.println(isbnEs+","+tituloEs+","+autorEs+","+prestadoEs+","+fechaEs);
			
			
		} catch (FileNotFoundException e) {
			System.err.println("Fichero no encontrado!");
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
			if (out != null) {
				out.close();
			}
		}
		
		try{			// Para aumentar el isbn
			out2 = new PrintWriter(new BufferedWriter(new FileWriter("IDlibros",true)));
			out2.println();
		}catch (Exception e) {
			System.err.println("Errrorr");
		}finally {
			out2.close();
		}
		
		return noExiste;
	}
	
	public boolean darDeBaja(BufferedReader in, PrintWriter out, String title) {
		boolean encontrado=false;
		int  veces2=0;
		String c, titulo="sin titulo";
		
		if(existeElLibro(in, title)) {
			encontrado=true;
		}
		
		if(!encontrado) {
			return encontrado;
		}

		try {
			in = new BufferedReader(new FileReader("libros"));
			out = new PrintWriter(new BufferedWriter(new FileWriter("librosTemporal")));
			
			while ((c = in.readLine()) != null) {
				String todo;						 					// isbn+titulo+autor+prestado+fecha
				
				todo = c.substring(c.indexOf(",") + 1); 				// titulo+autor+prestado+fecha
				titulo = todo.substring(0, todo.indexOf(",")); 			// titulo
				
				if(titulo.equals(title)) {
					veces2++;
				}else {
					out.println(c);
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
			if (out != null) {
				out.close();
			}
			GestorBibliotecario.renombrarlo();
		}
		
		if(veces2==0) {
			System.out.println("Se ha borrado el ejemplar de "+title);
		}else {
			System.out.println("Se han borrado los ejemplares del libro"+title);
		}
		
		return encontrado;
	}

	public boolean modificarTitulo(BufferedReader in, PrintWriter out, String tituloComparar, String tituloNuevo) {
		
		if(!existeElLibro(in, tituloComparar)) {
			return false;
		}
		String c, titulo;
			
		try {
			in = new BufferedReader(new FileReader("libros"));		
			out = new PrintWriter(new BufferedWriter(new FileWriter("librosTemporal")));	
			
			while ((c = in.readLine()) != null) {

				String todo; 										// isbn+titulo+autor+prestado+fecha
				todo = c.substring(c.indexOf(",") + 1); 			// titulo+autor+prestado+fecha
				titulo = todo.substring(0, todo.indexOf(",")); 		// titulo

				if (titulo.equals(tituloComparar)) {
					String autorEs, prestadoEs, fechaEs, isbnEs;
					isbnEs = c.substring(0, c.indexOf(","));				 		// titulo
					autorEs = c.substring(c.indexOf(",") + 1); 						// titulo+autor+prestado+fecha
					autorEs = autorEs.substring(autorEs.indexOf(",") + 1); 			// autor+prestado+fecha
					autorEs = autorEs.substring(0, autorEs.indexOf(",")); 			// autor
					prestadoEs = c.substring(c.indexOf(",") + 1); 					// titulo+autor+prestado+fecha
					prestadoEs = prestadoEs.substring(prestadoEs.indexOf(",") + 1); // autor+prestado+fecha
					prestadoEs = prestadoEs.substring(prestadoEs.indexOf(",") + 1); // prestado+fecha
					prestadoEs = prestadoEs.substring(0, autorEs.indexOf(",")); 	// prestado
					fechaEs = c.substring(c.lastIndexOf(",") + 1);					// fecha

					out.println(isbnEs + "," + tituloNuevo + "," + autorEs + "," + prestadoEs + "," + fechaEs);
				} else {
					out.println(c);
				}
			}

		} catch (FileNotFoundException e) {
			System.err.println("Fichero no encontrado!!");
		} catch (IOException f) {
			System.out.println();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Error, " + e.getMessage());
				}
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

