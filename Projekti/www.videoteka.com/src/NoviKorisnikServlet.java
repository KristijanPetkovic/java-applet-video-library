import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/NoviKorisnikServlet")
public class NoviKorisnikServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
		String ime = request.getParameter("ime");
		String sifra = request.getParameter("sifra");
				
		if(ime == null || sifra == null) { // Nije kliknuto dugme Sa?uvaj
			
		}else {
			String url = "jdbc:sqlserver://localhost:1433;databaseName=Videoteka;user=sa;password=root";
			Connection con = null;
			
			boolean sveOk = true;
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				con = DriverManager.getConnection(url);
				Statement stmt = con.createStatement();
				stmt.execute("INSERT INTO Korisnici VALUES ('" + ime + "','" + sifra + "')");
			} catch (ClassNotFoundException | SQLException e) {
				sveOk = false;
				response.sendRedirect("./noviKorisnik.html?greska=Dupli!");
				e.printStackTrace();
			}finally {
				if(con != null) {
					try {						
						con.close();
						if(sveOk) {
							response.sendRedirect("./login.html");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}

}
