import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/DodajFilmServlet")
@MultipartConfig
public class DodajFilmServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String imeSesije = (String) request.getSession().getAttribute("ime");
		String out="";
	
		if(!imeSesije.equals("admin")) {
			response.sendRedirect("filmovi.html");
		} 
		String slika = request.getParameter("slika");
		String naziv = request.getParameter("naziv");
		String zanr = request.getParameter("zanr");
		String godina = request.getParameter("godina");
		String opis = request.getParameter("opis");
		
	
		if(naziv == null || godina == null) { // Nije kliknuto dugme Sa?uvaj
			out+="<html>\r\n" + 
					"<head>\r\n" + 
					"<link rel=\"stylesheet\" type=\"text/css\" href=\"./css/dodajFilm.css\" >\r\n" + 
					"<meta http-equiv=\"content-type\" content=\"text/html\">\r\n" + 
					"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n" + 
					"<link rel=\"stylesheet\"\r\n" + 
					"	href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\r\n" + 
					"<script\r\n" + 
					"	src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\r\n" + 
					"<script\r\n" + 
					"	src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>\r\n" + 
					"<title>Dodaj novi film</title>\r\n" + 
					"\r\n" + 
					"</head>\r\n" + 
					"<body>\r\n" + 
					 Util.getMenu(request) +
					"	<h1 id=\"naslov\">Unos novog filma</h1>\r\n" + 
					"	<form action=\"/www.videoteka.com/DodajFilmServlet\" method=\"post\"  enctype=\"multipart/form-data\">\r\n" + 
					"		<table class=\"center\">\r\n" + 
					"			<tr>\r\n" + 
					"				<td>Slika:</td>\r\n" + 
					"				<td><input name=\"slika\" type=\"file\"></td>\r\n" + 
					"			</tr>\r\n" + 
					"			<tr>\r\n" + 
					"				<td>Naziv:</td>\r\n" + 
					"				<td><input name=\"naziv\" type=\"text\"></td>\r\n" + 
					"			</tr>\r\n" + 
					"			<tr>\r\n" + 
					"				<td>?anr:</td>\r\n" + 
					"				<td><input name=\"zanr\" type=\"text\"></td>\r\n" + 
					"			</tr>\r\n" + 
					"			<tr>\r\n" + 
					"				<td>Godina:</td>\r\n" + 
					"				<td><input name=\"godina\" type=\"text\"></td>\r\n" + 
					"			</tr>\r\n" + 
					"			<tr>\r\n" + 
					"				<td>Re?iser:</td>\r\n" + 
					"				<td><input name=\"opis\" type=\"text\"></td>\r\n" + 
					"			</tr>\r\n" + 
					"		</table>\r\n" + 
					"		<input class=\"color-change\" type=\"button\"\r\n" + 
					"			onclick=\"location.href='/www.videoteka.com/filmovi.html';\" value=\"Nazad\" /> <input\r\n" + 
					"			class=\"color-change\" name=\"sa?uvaj\" type=\"submit\" value=\"Sa?uvaj\">\r\n" + 
					"	</form>\r\n" + 
					"</body>\r\n" + 
					"</html>\r\n";
			PrintWriter pw = response.getWriter();
			pw.println(out);		
					
		}else {
			Part filePart = request.getPart("slika"); // Retrieves <input type="file" name="file">
		    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
		    InputStream fileContent = filePart.getInputStream();
		    
		    
			String url = "jdbc:sqlserver://localhost:1433;databaseName=Videoteka;user=sa;password=root";
			Connection con = null;
			
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				con = DriverManager.getConnection(url);
				// Statement stmt = con.createStatement();
				// stmt.execute("INSERT INTO Filmov VALUES ('" + slika + "','" + naziv + "','" + zanr + "','" + godina + "','" + opis + "')");
				
				PreparedStatement pstmt = con.prepareStatement("INSERT INTO Filmov (slika,naziv,zanr,godina,opis) VALUES(?,?,?,?,?)");

				pstmt.setBinaryStream(1, fileContent);
				pstmt.setString(2, naziv);
				pstmt.setString(3, zanr);
				pstmt.setString(4, godina);
				pstmt.setString(5, opis);
				
				pstmt.executeUpdate();
				fileContent.close();
				
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}finally {
				if(con != null) {
					try {						
						con.close();
						response.sendRedirect("filmovi.html");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}

}
