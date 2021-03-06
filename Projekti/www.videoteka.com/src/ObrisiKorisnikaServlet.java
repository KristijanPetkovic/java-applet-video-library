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

@WebServlet("/obrisiKorisnika.html")
public class ObrisiKorisnikaServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String imeSesije = (String) request.getSession().getAttribute("ime");
		
		if(imeSesije == null) {
			response.sendRedirect("login.html");
		}else {		
			String id = request.getParameter("id");
			
			if(id == null && imeSesije != null) {
				response.sendRedirect("korisnici.html");
			}else {
				String url = "jdbc:sqlserver://localhost:1433;databaseName=Videoteka;user=sa;password=root";
				Connection con = null;				
				try {
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
					con = DriverManager.getConnection(url);
					Statement stmt = con.createStatement();
					stmt.execute("DELETE FROM Korisnici WHERE id='" + id + "'");					
				}catch(Exception e) {
					e.printStackTrace();
				}finally {
					if(con != null) {
						try {						
							con.close();
							response.sendRedirect("korisnici.html");
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}					
			}			
		}
	}

}
