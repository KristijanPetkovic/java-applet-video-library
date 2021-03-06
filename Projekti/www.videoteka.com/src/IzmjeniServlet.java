import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/izmjeni.html")
public class IzmjeniServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String imeSesije = (String) request.getSession().getAttribute("ime");
		
		if(!imeSesije.equals("admin")) {
			response.sendRedirect("filmovi.html");
		}else {		
			String id = request.getParameter("id");
			if(id == null && imeSesije != null) {
				response.sendRedirect("filmovi.html");
			}else {
				String url = "jdbc:sqlserver://localhost:1433;databaseName=Videoteka;user=sa;password=root";
				Connection con = null;
		
				String out = "";		
				out +=  "<html>\r\n" + 
						"	<head>\r\n" + 
						"		<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">\r\n" + 
						"		<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"+
								" 		<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\r\n"+
								" 		<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\r\n"+
								"		 <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>\r\n"+
						"		<title>Ažuriranje filma</title>\r\n" + 
						"		<link rel=\"stylesheet\" href=\"./css/dodajFilm.css\" >\r\n" +
						"	</head>\r\n" + 
						"	\r\n" + 
						"	<body>\r\n" +  
						Util.getMenu(request) +
						"		<h1 id=\"naslov\">Stranica za ažuriranje filma</h1>\r\n" +
						"		<form action=\"filmovi.html\" method=\"post\" enctype=\"multipart/form-data\">\r\n" +
						"			<table class=\"center\">\r\n";
				
				try {
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
					con = DriverManager.getConnection(url);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM Filmov WHERE id='" + id + "'");
								
					while(rs.next()) {				
						out +=  "			<tr>\r\n" + 
								"				<td hidden align=\"right\">Id: </th>\r\n" +
								"				<td hidden> <input enable=\"disable\" name=\"id\" type=\"text\" value=\"" + rs.getString("id") + "\"> </th>\r\n" +
								"			</tr>\r\n" +
								"			<tr>\r\n" + 
								"				<td align=\"right\">Slika: </th>\r\n" +
								"				<td> <img src=data:image/jpeg;base64," + new String(Base64.getEncoder().encode(rs.getBytes(2))) + " width=\"80px\">"
										+ "<input name=\"slika\" type=\"file\" value=\"" + rs.getString("slika") + "\"> </th>\r\n" +
								"			</tr>\r\n" +
								"			<tr>\r\n" + 
								"				<td align=\"right\">Naziv: </th>\r\n" +
								"				<td> <input name=\"naziv\" type=\"text\" value=\"" + rs.getString("naziv") + "\"> </th>\r\n" +
								"			</tr>\r\n" +
								"			<tr>\r\n" + 
								"				<td align=\"right\">Žanr: </th>\r\n" +
								"				<td> <input name=\"zanr\" type=\"text\" value=\"" + rs.getString("zanr") + "\"> </th>\r\n" +
								"			</tr>\r\n" +
								"			<tr>\r\n" + 
								"				<td align=\"right\">Godina: </th>\r\n" +
								"				<td> <input name=\"godina\" type=\"text\" value=\"" + rs.getString("godina") + "\"> </th>\r\n" +
								"			</tr>\r\n" +
								"			<tr>\r\n" + 
								"				<td align=\"right\">Režiser: </th>\r\n" +
								"				<td> <input name=\"opis\" type=\"text\" value=\"" + rs.getString("opis") + "\"> </th>\r\n" +
								"			</tr>\r\n";
						
					}
				}catch(Exception e) {
					out +=  "			<tr>\r\n" + 
							"				<td> GREŠKA !!! </td>" +
							"			</tr>\r\n";
					e.printStackTrace();
				}finally {
					if(con != null) {
						try {						
							con.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
										
				out +=  "			</table>\r\n" +
				
						"	<input class=\"color-change\" type=\"button\" onclick=\"location.href='filmovi.html';\" value=\"Nazad\" />"+
						" 		<input class=\"color-change\" type=\"submit\" value=\"Ažuriraj\">" +
						"		</form>\r\n" + 
						"		&nbsp;&nbsp;&nbsp;" +
						"	</body>\r\n" + 
						"</html>\r\n";
		
				PrintWriter pw = response.getWriter();
				pw.println(out);				
			}			
		}
	}

}
