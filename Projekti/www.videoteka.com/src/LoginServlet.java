import java.io.IOException;
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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

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
		String imeSesije = (String) request.getSession().getAttribute("ime");
				
		if(imeSesije != null) { // Korisnik je ve? prijavljen!
			response.sendRedirect("filmovi.html");
		}else if(ime == null || sifra == null) {// Neko od polja nije popunjeno!
			response.sendRedirect("/www.videoteka.com/login.html");
		}else {
			String url = "jdbc:sqlserver://localhost:1433;databaseName=Videoteka;user=sa;password=root";
			Connection con = null;
			boolean pronadjen = false;
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				con = DriverManager.getConnection(url);
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM Korisnici WHERE ime='" + ime + "' AND sifra='" + sifra + "'");
				rs.next();
				if(rs.getRow() == 1) {
					pronadjen = true;
				}
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}finally {
				if(con != null) {
					try {						
						con.close();
						if(pronadjen) {
							request.getSession().setAttribute("ime", ime);
							response.sendRedirect(request.getContextPath() + "/filmovi.html");							
						}else {
							response.sendRedirect("login.html");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
