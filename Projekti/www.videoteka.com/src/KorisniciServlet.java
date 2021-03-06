import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/KorisniciServlet","/korisnici.html"})
public class KorisniciServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    //deo za proveru logovanja
		String imeSesije = (String) request.getSession().getAttribute("ime");
		
		if(!imeSesije.equals("admin")) {
			response.sendRedirect("filmovi.html");
		}else {
			//ovo je kraj dela za proveru
			String id = request.getParameter("id");
			String ime = request.getParameter("ime");
			String sifra = request.getParameter("sifra");
						
			String out = "";
			
			out += "\r\n" + 
					"<html>\r\n" + 
					"	<head>\r\n" + 
					"		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n" + 
					"		<title>Pregled korisnika</title>\r\n" + 
					"       <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"+
					" 		<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\r\n"+
					" 		<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\r\n"+
					"		 <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>\r\n"+
					"		<link rel=\"stylesheet\" href=\"./css/dodajFilm.css\" >" +
					"	</head>\r\n" + 
					"	\r\n" + 
					"	<body>\r\n" +
					Util.getMenu(request) +
					"		<h1>Stranica za pregled korisnika</h1>\r\n" +
					"		<form id=\"izmjeni\" method=\"post\" ></form>\r\n" +
					"		<form id=\"obrisi\" method=\"post\" ></form>\r\n" +
					"		<table class=\"center\">\r\n" + 
					"			<tr>\r\n" + 
					"				<th hidden>ID</th>\r\n" + 
					"				<th>Korisni?ko ime</th>\r\n" + 
					"				<th>?ifra</th>\r\n" + 
					"				<th></th>\r\n" + 
					"			</tr>\r\n";
			
			String url = "jdbc:sqlserver://localhost:1433;databaseName=Videoteka;user=sa;password=root";
			Connection con = null;		
			
			if(id == null) {
				try {
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
					con = DriverManager.getConnection(url);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM Korisnici");
					while(rs.next()) {
						out += "	<tr>\r\n" + 
								"				<td hidden>" + rs.getInt("id") + "</td>\r\n" + 
								"				<td>" + rs.getString("ime") + "</td>\r\n" + 
								"				<td>" + rs.getString("sifra") + "</td>\r\n" + 
								 "				<td> <a class=\"glyphicon glyphicon-pencil\" href=\"izmjeniKorisnika.html?id=" + rs.getInt("id")+
								 "\"><a/> &nbsp;&nbsp;&nbsp; <a class=\"glyphicon glyphicon-trash\" href=\"obrisiKorisnika.html?id=" + rs.getInt("id")+
								 "\"><a/></td>\r\n" + 
								"	</tr>\r\n";
					}
				}catch(Exception e) {
					out += "	<tr>\r\n" + 
							"				<td hidden> GRE?KA !!! </td>\r\n" + 
							"				<td> GRE?KA !!! </td>\r\n" + 
							"				<td> GRE?KA !!! </td>\\r\\n\"" +  
							"				<td> GRE?KA !!! </td>\\r\\n\"" +  
							"	</tr>\r\n";
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
				
			}else {
				// preusmereni sa izmjeni.html
				try {
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
					con = DriverManager.getConnection(url);
					Statement stmt = con.createStatement();
					stmt.execute("UPDATE Korisnici SET ime='"+ ime + "', sifra='"+ sifra + "' WHERE id='"+ id +"'");
					ResultSet rs = stmt.executeQuery("SELECT * FROM Korisnici");
					while(rs.next()) {
						out += "	<tr>\r\n" + 
								"				<td hidden>" + rs.getInt("id") + "</td>\r\n" + 
								"				<td>" + rs.getString("ime") + "</td>\r\n" + 
								"				<td>" + rs.getString("sifra") + "</td>\r\n" + 
								"<td> <a class=\"glyphicon glyphicon-pencil\" href=\"izmjeniKorisnika.html?id=" + rs.getInt("id")+
								 "\"><a/> &nbsp;&nbsp;&nbsp; <a class=\"glyphicon glyphicon-trash\" href=\"obrisiKorisnika.html?id=" + rs.getInt("id")+
								 "\"><a/></td>\r\n" + 
								"	</tr>\r\n";
					}
				}catch(Exception e) {
					out += "	<tr>\r\n" + 
							"				<td hidden> GRE?KA !!! </td>\r\n" + 
							"				<td> GRE?KA !!! </td>\r\n" + 
							"				<td> GRE?KA !!! </td>\\r\\n\"" +  
							"				<td> GRE?KA !!! </td>\\r\\n\"" +  
							"	</tr>\r\n";
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
			}
							
			out +=  "		</table>\r\n" +
					"	</body>\r\n" + 
					"</html>\r\n";

			PrintWriter pw = response.getWriter();
			pw.println(out);
			
		}
		
	}

}
