package Bibliotecarios;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

	public boolean darDeAlta(BufferedReader in, PrintWriter out, String title) { // Devuelve true cuando se le da de alta
		
		if(!existeElLibro(in, title)) {
			return false;
		}
		
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter("libros",true)));
			int isbnEs=idISBN;
			String autorEs=GestorBibliotecario.nombres()+" "+GestorBibliotecario.apellidos();
			String tituloEs=title;
			boolean prestadoEs=false;
			String fechaEs = "NO";
			/*
			String fechaEs = fecha.toString();				// yyyy-mm-dd
			String dia, mes, anno;
			dia=fechaEs.substring(fechaEs.indexOf("-")+1);	// mm-dd
			dia=dia.substring(dia.indexOf("-")+1);				// dd
			mes=fechaEs.substring(fechaEs.indexOf("-")+1);	// mm-dd
			mes=mes.substring(0, mes.indexOf("-")+1);			// mm
			anno=fechaEs.substring(0, fechaEs.indexOf("-"));	// yyyy
			*/
			out.println(isbnEs+","+tituloEs+","+autorEs+","+prestadoEs+","+fechaEs);
			this.sumaidISBN();
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
		}
		return true;
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
		int numEjemplares = 0;
		
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter("libros",true)));
			in = new BufferedReader(new FileReader("libros"));
			numEjemplares = contarEjemplares(in, tituloPedido);
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		try {
			
			if (numEjemplares!=0) {
			
				out = new PrintWriter(new BufferedWriter(new FileWriter("libros",true)));
				in = new BufferedReader(new FileReader("libros"));
				
				while ((c = in.readLine())!=null && !encontrado) {		// c = 		isbn , titulo , autor , prestado , fecha
					linea = c.substring(c.indexOf(',')+1);				// linea =	titulo , autor , prestado , fecha
					titulo = linea.substring(0, linea.indexOf(','));	// titulo =	titulo
					if (titulo.equalsIgnoreCase(tituloPedido)) {
						principioLinea4 = c.substring(0,c.lastIndexOf(','));
						principioLinea3 = principioLinea4.substring(0,principioLinea4.lastIndexOf(','));
						for (int i=0;i<numAumento;i++) {
							out.println(principioLinea3+","+false+",NO");
							encontrado = true;
						}
					}
				}
				
			} else {
				System.out.println("No se ha encontrado el libro");
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
	
}
	
public void disminuirEjemplares(BufferedReader in, PrintWriter out, String tituloPedido, int numDisminuciones) {
	
	int numEjemplares = 0;
	
	try {
		in = new BufferedReader(new FileReader("libros"));
		numEjemplares = contarEjemplares(in, tituloPedido);
		
	} catch (FileNotFoundException e1) {
		e1.printStackTrace();
	}	
	
	try {
		
		if (numEjemplares!=0) {
		
			out = new PrintWriter(new BufferedWriter(new FileWriter("librosTemporal",true)));
			in = new BufferedReader(new FileReader("libros"));
			
			String c, linea, titulo, principioLinea4, prestado;
			int contador = 0;
			
			while ((c = in.readLine())!=null) {						// c = 		isbn , titulo , autor , prestado , fecha
				linea = c.substring(c.indexOf(',')+1);				// linea =	titulo , autor , prestado , fecha
				titulo = linea.substring(0, linea.indexOf(','));	// titulo =	titulo
				if (titulo.equalsIgnoreCase(tituloPedido) && (contador<numDisminuciones)) {
					principioLinea4 = c.substring(0,c.lastIndexOf(','));
					prestado = principioLinea4.substring(principioLinea4.lastIndexOf(',')+1);
					if (prestado.equalsIgnoreCase("true")) {
						out.println(c);
					} else {
						contador++;
					}
				} else {
					out.println(c);
				}
			}
			
		} else {
			System.out.println("No se ha encontrado el libro");
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
				} catch (IOException e) {
				System.out.println("Errrooooorrr"+e.getMessage());
			}
			
		}
	
	public int contarEjemplares(BufferedReader in, String tituloPedido) {
		int numEjemplares=0;
		String c, linea, titulo;
		try {
			while ((c = in.readLine())!=null) {				// c = 		isbn , titulo , autor , prestado , fecha
				linea = c.substring(c.indexOf(',')+1);		// linea =	titulo , autor , prestado , fecha
				titulo = linea.substring(0, linea.indexOf(','));	// titulo =	titulo
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
