package Usuarios;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class GestorUsuario {
	LocalDate fecha = LocalDate.now();
	
	public boolean pedirLibro(BufferedReader in, PrintWriter out, String title) {
		boolean encontrado = false;
		String autor = "No tiene", titulo = "No existe", c, prestado = "NO", isbn = "No tiene", fetch="NO";

		try {
			in = new BufferedReader(new FileReader("libros"));
			out = new PrintWriter(new BufferedWriter(new FileWriter("librosTemporal")));
			while ((c = in.readLine()) != null) {
				in.readLine();
				String todo;											// isbn+titulo+autor+prestado+fecha

				todo = c.substring(c.indexOf(",") + 1); 				// titulo+autor+prestado+fecha
				titulo = todo.substring(0, todo.indexOf(",")); 			// titulo

				if (titulo.equals(title)) {
					todo = c.substring(c.indexOf(",") + 1); 			// titulo+autor+prestado+fecha
					todo = todo.substring(todo.indexOf(",") + 1); 		// autor+prestado+fecha
					todo = todo.substring(todo.indexOf(",") + 1); 		// prestado+fecha
					prestado = todo.substring(0, todo.indexOf(",")); 	// prestado

					if (prestado.equals("false")) {					
						todo = c.substring(c.indexOf(",") + 1); 		// titulo+autor+prestado+fecha
						todo = todo.substring(todo.indexOf(",") + 1); 	// autor+prestado+fecha
						autor = todo.substring(0, todo.indexOf(",")); 	// autor
						
						isbn=c.substring(0, c.indexOf(","));			// isbn
						
						fetch=fecha.toString();						// fecha	yyyy-mm-dd
						String dia, mes, anno;
						dia=fetch.substring(fetch.indexOf("-")+1);	// mm-dd
						dia=dia.substring(dia.indexOf("-")+1);				// dd
						mes=fetch.substring(fetch.indexOf("-")+1);	// mm-dd
						mes=mes.substring(0, mes.indexOf("-")+1);			// mm
						anno=fetch.substring(0, fetch.indexOf("-"));		// yyyy
						fetch=dia+","+mes+","+anno;
						
						encontrado = true;
					}
				}
				
				if(encontrado) {
					out.println(c);
				}else {
					out.println(isbn+","+titulo+","+autor+","+prestado+","+fetch);
				}
			}

		} catch (FileNotFoundException e) {
			System.err.println("No se ha encontrado el fichero");
		} catch (IOException f) {
			System.err.println("!!");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Error, " + e.getMessage());
				}
			}
			GestorUsuario.renombrarlo();
		}
		
		
		if (encontrado) {
			System.out.println("El libro " + titulo + " de " + autor + "con ISBN " + isbn + " ha sido pedido en prestamo el "+fetch);
		}
		return encontrado;
	}


	public boolean devolverLibro(BufferedReader in, String title) {	// Faltan las excepciones de segun los dias que haya tardado
		boolean devuelto=false;
		String autor = "No tiene", titulo = "No existe", c, prestado, isbn="No tiene", date="Ninguna";
		int diasDespues=0;
		
		try {
			in = new BufferedReader(new FileReader("libros"));
			while((c = in.readLine()) != null && !devuelto) {
				in.readLine();
				String todo;											// isbn+titulo+autor+prestado+fecha
				
				todo=c.substring(c.indexOf(",")+1);						//titulo+autor+prestado+fecha
				titulo=todo.substring(0, todo.indexOf(","));			//titulo
				
				if(titulo.equals(title)) {	
					todo = c.substring(c.indexOf(",") + 1); 			// titulo+autor+prestado+fecha
					todo = todo.substring(todo.indexOf(",") + 1); 		// autor+prestado+fecha
					todo = todo.substring(todo.indexOf(",") + 1); 		// prestado+fecha
					prestado = todo.substring(0, todo.indexOf(",")); 	// prestado
					
					if (prestado.equals("true")) {
						todo = c.substring(c.indexOf(",") + 1); 		// titulo+autor+prestado+fecha
						todo = todo.substring(todo.indexOf(",") + 1); 	// autor+prestado+fecha
						autor = todo.substring(0, todo.indexOf(",")); 	// autor
						
						isbn=c.substring(0, c.indexOf(","));			// isbn
						
						date=c.substring(c.lastIndexOf(","));			// fecha
						devuelto=true;
					}
				}
				//Comparo los dias
				if(devuelto) {
					int diasDif, mesDif, annoDif;
					String dia, mes, anno;						// dd-mm-yyyy
					anno=date.substring(date.indexOf("-")+1);	// mm-yyyy
					anno=anno.substring(anno.indexOf("-")+1);		// yyyy
					mes=date.substring(date.indexOf("-")+1);	// mm-yyyy
					mes=mes.substring(0, mes.indexOf("-")+1);		// mm
					dia=date.substring(0, date.indexOf("-"));		// dd
					diasDif=Integer.parseInt(dia);
					mesDif=Integer.parseInt(mes);
					annoDif=Integer.parseInt(anno);
					
					if(fecha.getYear()>annoDif) {
						diasDespues+=(fecha.getYear()-annoDif)*360;
					}
					
					if(fecha.getMonthValue()>mesDif) {
						diasDespues+=(fecha.getMonthValue()-mesDif)*30;
					}
					
					if(fecha.getDayOfMonth()>diasDif) {
						diasDespues+=(fecha.getDayOfMonth()-diasDif);
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			System.err.println("No se ha encontrado el fichero");
		} catch(IOException f){
			System.err.println("!!");
		}finally {
			if(in!=null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Error, "+e.getMessage());
				}
			}
		}
		if(devuelto) {
			System.out.println("El libro " + titulo + " de " + autor + "con ISBN " + isbn + " ha sido devuelto tras ser prestado en la fecha"+date+" "+diasDespues+" dias despues");
		}
		
		if(diasDespues>200) {
			//Si es un cursor, es uno implicito
			System.out.println("Este usuario ha sobrepasado la linea (se la ha jugao) asi que no podra volver a cojer libros en su vida");
		}else if(diasDespues>15) {
			System.out.println("Este usuario no va a poder tomar prestados mas libros hasta dentro de "+(diasDespues-15));
		}
		
		return devuelto;
	}
	

	public boolean iniciarSesion(RandomAccessFile r, String usuario, String contrasena) {
		boolean inicioSesion=false;
		
		while(usuario.length()<15) {
			usuario+="$";
		}
		while(contrasena.length()<15) {
			contrasena+="$";
		}

		try {
			r = new RandomAccessFile("usuarios", "r");
			String user, pass;
			
			for (int i = 0; i < r.length() && !inicioSesion; i++) {
				r.seek((i*38));
				if(r.readInt()==3) {								// Para saber si es usuario comun
					r.seek((i*38)+4);
					user=r.readUTF();
					user=user.substring(0, user.indexOf("$"));		// Quito los dolars
					if(usuario.equals(user)) {						// Leo el usuario y lo comparo
						r.seek((i*38)+21);
						pass=r.readUTF();
						pass=pass.substring(0, pass.indexOf("$"));	// Quito los dolars
						if(contrasena.equals(pass)) {				// Leo la contrasena y la comparo
							inicioSesion=true;
						}
					}
					
				}
				
			}
			
		}catch (FileNotFoundException e) {
			System.err.println("Este fichero no existe");
		}catch (IOException e) {
			System.out.println();
		}finally {
			if(r!=null) {
				try {
					r.close();
				} catch (IOException e) {
					System.out.println("Error, "+e.getMessage());
				}
			}
		}
		
		return inicioSesion;
	}

	
	public void listarDisponibles(BufferedReader in, String title) {
		String autor = "No tiene", titulo = "No existe", c, prestado, isbn, date;
		
		try {
			in = new BufferedReader(new FileReader("libros"));
			while ((c = in.readLine()) != null) {
				in.readLine();
				String todo;											// isbn+titulo+autor+prestado+fecha

				todo = c.substring(c.indexOf(",") + 1); 				// titulo+autor+prestado
				titulo = todo.substring(0, todo.indexOf(",")); 			// titulo

				if (titulo.equals(title)) {
					todo = c.substring(c.indexOf(",") + 1); 			// titulo+autor+prestado+fecha
					todo = todo.substring(todo.indexOf(",") + 1); 		// autor+prestado+fecha
					todo = todo.substring(todo.indexOf(",") + 1); 		// prestado+fecha
					prestado = todo.substring(0, todo.indexOf(",")); 	// prestado
					
					todo = c.substring(c.indexOf(",") + 1); 			// titulo+autor+prestado+fecha
					todo = todo.substring(todo.indexOf(",") + 1); 		// autor+prestado+fecha
					autor = todo.substring(0, todo.indexOf(",")); 		// autor
					
					isbn=c.substring(0, c.indexOf(","));				// isbn
					date=c.substring(c.lastIndexOf(","));				// fecha
					
					System.out.println("ISBN:    "+isbn);
					System.out.println("Titulo:  "+titulo);
					System.out.println("Autor:   "+autor);
					if(prestado.equals("true")) {
						System.out.println("Prestado: si");
						System.out.println("Prestado en: "+date);
					}else {
						System.out.println("Prestado: no");	
					}
				}
			}

		} catch (FileNotFoundException e) {
			System.err.println("No se ha encontrado el fichero");
		} catch (IOException f) {
			System.err.println("!!");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Error, " + e.getMessage());
				}
			}
		}
	}

	
	public boolean listarPorAutor(BufferedReader in, String author) {
		String autor = "No tiene", titulo = "No existe", c, prestado, isbn, date;
		boolean aut=false;
		
		try {
			in = new BufferedReader(new FileReader("libros"));
			while ((c = in.readLine()) != null) {
				in.readLine();
				String todo;											// isbn+titulo+autor+prestado+fecha

				todo = c.substring(c.indexOf(",") + 1); 			// titulo+autor+prestado+fecha
				todo = todo.substring(todo.indexOf(",") + 1); 		// autor+prestado+fecha
				autor = todo.substring(0, todo.indexOf(",")); 		// autor
				

				if (autor.equals(author)) {
					aut=true;
					todo = c.substring(c.indexOf(",") + 1); 			// titulo+autor+prestado+fecha
					todo = todo.substring(todo.indexOf(",") + 1); 		// autor+prestado+fecha
					todo = todo.substring(todo.indexOf(",") + 1); 		// prestado+fecha
					prestado = todo.substring(0, todo.indexOf(",")); 	// prestado
					
					
					isbn=c.substring(0, c.indexOf(","));				// isbn
					date=c.substring(c.lastIndexOf(","));				// fechatodo = c.substring(c.indexOf(",") + 1); 			// titulo+autor+prestado+fecha
					todo = todo.substring(todo.indexOf(",") + 1); 		// autor+prestado+fecha
					todo = todo.substring(todo.indexOf(",") + 1); 		// prestado+fecha
					prestado = todo.substring(0, todo.indexOf(",")); 	// prestado
					
					todo = c.substring(c.indexOf(",") + 1); 			// titulo+autor+prestado+fecha
					todo = todo.substring(todo.indexOf(",") + 1); 		// autor+prestado+fecha
					autor = todo.substring(0, todo.indexOf(",")); 		// autor
					
					isbn=c.substring(0, c.indexOf(","));				// isbn
					date=c.substring(c.lastIndexOf(","));				// fecha

					System.out.println("ISBN:    "+isbn);
					System.out.println("Titulo:  "+titulo);
					System.out.println("Autor:   "+autor);
					if(prestado.equals("true")) {
						System.out.println("Prestado: si");
						System.out.println("Fecha prestado: "+date);
					}else {
						System.out.println("Prestado: no");	
					}
				}
			}

		} catch (FileNotFoundException e) {
			System.err.println("No se ha encontrado el fichero");
		} catch (IOException f) {
			System.err.println("!!");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Error, " + e.getMessage());
				}
			}
		}
		return aut;
	}

	
	public void listarTodos(BufferedReader in) {
		String autor = "No tiene", titulo = "No existe", c, prestado, isbn, date;
		System.out.println("ISBN			Titulo			Autor			Prestado?");
		
		try {
			in = new BufferedReader(new FileReader("libros"));
			while ((c = in.readLine()) != null) {
				in.readLine();
				String todo;										// isbn+titulo+autor+prestado+fecha

				todo = c.substring(c.indexOf(",") + 1); 			// titulo+autor+prestado+fecha
				todo = todo.substring(todo.indexOf(",") + 1); 		// autor+prestado+fecha
				autor = todo.substring(0, todo.indexOf(",")); 		// autor
				
				todo = c.substring(c.indexOf(",") + 1); 			// titulo+autor+prestado+fecha
				todo = todo.substring(todo.indexOf(",") + 1); 		// autor+prestado+fecha
				todo = todo.substring(todo.indexOf(",") + 1); 		// prestado+fecha
				prestado = todo.substring(0, todo.indexOf(",")); 	// prestado
				
				todo = c.substring(c.indexOf(",") + 1); 			// titulo+autor+prestado+fecha
				todo = todo.substring(todo.indexOf(",") + 1); 		// autor+prestado+fecha
				autor = todo.substring(0, todo.indexOf(",")); 		// autor
				
				isbn=c.substring(0, c.indexOf(","));				// isbn
				date=c.substring(c.lastIndexOf(","));				// fecha

				System.out.print(isbn+"		"+titulo+"	"+autor+"	");
				if (prestado.equals("true")) {
					System.out.print("si "+date);
				} else {
					System.out.println("no");
				}
			}
			
		} catch (FileNotFoundException e) {
			System.err.println("No se ha encontrado el fichero");
		} catch (IOException f) {
			System.err.println("!!");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Error, " + e.getMessage());
				}
			}
		}
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
	
}
