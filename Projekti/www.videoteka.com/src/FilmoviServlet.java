import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet({ "/FilmoviServlet", "/filmovi.html" })
@MultipartConfig
public class FilmoviServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// deo za proveru logovanja
		String imeSesije = (String) request.getSession().getAttribute("ime");

		if (imeSesije == null) {
			response.sendRedirect("login.html");
		} else {
			// ovo je kraj dela za proveru
			String id = request.getParameter("id");
			String slika = request.getParameter("slika");
			String naziv = request.getParameter("naziv");
			String zanr = request.getParameter("zanr");
			String godina = request.getParameter("godina");
			String opis = request.getParameter("opis");

			String out = "";

			out += "\r\n" + "<html>\r\n" + "	<head>\r\n"
					+ "		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n"
					+ "		<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
					+ " 		<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\r\n"
					+ " 		<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\r\n"
					+ "		 <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>\r\n"
					+ "		<title>Pregled filmova</title>\r\n" 
					+ "		<link rel=\"stylesheet\" href=\"./css/dodajFilm.css\" >"
					+ "	</head>\r\n" + "	\r\n" + "	<body>\r\n" 
					+ Util.getMenu(request) 
					+ "<div>\r\n "
					+ "		<h1 id=\"naslov\">Stranica za pregled filmova</h1>\r\n"
					+ "		<form id=\"izmjeni\" method=\"post\" ></form>\r\n"
					+ "		<form id=\"obrisi\" method=\"post\" ></form>\r\n" + "		<table class=\"center\">\r\n"
					+ "			<tr>\r\n" + "				<th hidden>ID</th>\r\n" + "				<th>Slika</th>\r\n"
					+ "				<th>Naziv</th>\r\n" + "				<th>?anr</th>\r\n"
					+ "				<th>Godina</th>\r\n" + "				<th>Re?iser</th>\r\n"
					+ "				<th></th>\r\n" + "			</tr>\r\n";

			String url = "jdbc:sqlserver://localhost:1433;databaseName=Videoteka;user=sa;password=root";
			Connection con = null;

			if (id == null) {
				try {
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
					con = DriverManager.getConnection(url);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT *, (select count(*) from Najam where Filmov.id = Najam.filmId and datumVracanja is null) as Iznajmljen  FROM Filmov");
					while (rs.next()) {
						out += "	<tr>\r\n" + "				<td hidden>" + rs.getInt("id") + "</td>\r\n"
								+ "				<td><img src=data:image/jpeg;base64," + new String(Base64.getEncoder().encode(rs.getBytes(2))) + " width=\"80px\"></td>\r\n"
								+ "				<td>" + rs.getString("naziv") + "</td>\r\n" + "				<td>"
								+ rs.getString("zanr") + "</td>\r\n" + "				<td>" + rs.getString("godina")
								+ "</td>\r\n" + "				<td>" + rs.getString("opis") + "</td>\r\n";
								if(imeSesije.equals("admin")) {
									out +=""
											+ "<td> <a class=\"glyphicon glyphicon-pencil\" href=\"izmjeni.html?id=" + rs.getInt("id")
											+ "\"><a/> &nbsp;&nbsp;&nbsp; <a class=\"glyphicon glyphicon-trash\" href=\"obrisi.html?id=" + rs.getInt("id")
											+ "\"><a/></td>\r\n";	

									if(rs.getInt("Iznajmljen") != 0 ) { 
										out +=""
												+ "<td> <a class=\"glyphicon glyphicon-refresh\" href=\"VracanjeActionServlet?id=" + rs.getInt("id")+ "\"><a/></td>\r\n"; 										
									}
								}else {
									if(rs.getInt("Iznajmljen") == 0 ) { 
										out +=""
											+ "<td> <a class=\"glyphicon glyphicon-plus\" href=\"NajamActionServlet?id=" + rs.getInt("id")+ "\"><a/></td>\r\n"; 
									}
								}
								out+= "	</tr>\r\n";
					}
				} catch (Exception e) {
					out += "	<tr>\r\n" + "				<td hidden> GRE?KA !!! </td>\r\n"
							+ "				<td> GRE?KA !!! </td>\r\n" + "				<td> GRE?KA !!! </td>\\r\\n\""
							+ "				<td> GRE?KA !!! </td>\\r\\n\""
							+ "				<td> GRE?KA !!! </td>\\r\\n\""
							+ "				<td> GRE?KA !!! </td>\\r\\n\""
							+ "				<td> GRE?KA !!! </td>\\r\\n\"" + "	</tr>\r\n";
					e.printStackTrace();
				} finally {
					if (con != null) {
						try {
							con.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}

			} else {
				// preusmereni sa izmjeni.html
				Part filePart = request.getPart("slika"); // Retrieves <input type="file" name="file">
			    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
			    InputStream fileContent = filePart.getInputStream();
			    
				try {
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
					con = DriverManager.getConnection(url);
					// Statement stmt = con.createStatement();
					// stmt.execute("UPDATE Filmov SET slika='" + slika + "', naziv='" + naziv + "', zanr='" + zanr
					//		+ "', godina='" + godina + "', opis='" + opis + "' WHERE id='" + id + "'");
					if(fileName.length()>0) {
						PreparedStatement pstmt = con.prepareStatement("UPDATE Filmov SET slika=?, naziv=?,zanr=?,godina=?,opis=? WHERE id=?");
	
						pstmt.setBinaryStream(1, fileContent);
						pstmt.setString(2, naziv);
						pstmt.setString(3, zanr);
						pstmt.setString(4, godina);
						pstmt.setString(5, opis);
						pstmt.setString(6, id);	
						
						pstmt.executeUpdate();
						fileContent.close();
					} else {
						PreparedStatement pstmt = con.prepareStatement("UPDATE Filmov SET naziv=?,zanr=?,godina=?,opis=? WHERE id=?");

						pstmt.setString(1, naziv);
						pstmt.setString(2, zanr);
						pstmt.setString(3, godina);
						pstmt.setString(4, opis);
						pstmt.setString(5, id);	
						
						pstmt.executeUpdate();
						fileContent.close();
					}
					
					// za ispisvanje na ekranu
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM Filmov");
					while (rs.next()) {
						out += "	<tr>\r\n" + "				<td hidden>" + rs.getInt("id") + "</td>\r\n"
								+ "				<td><img src=data:image/jpeg;base64," + new String(Base64.getEncoder().encode(rs.getBytes(2))) + " width=\"80px\"></td>\r\n" + "				<td>"
								+ rs.getString("naziv") + "</td>\r\n" + "				<td>" + rs.getString("zanr")
								+ "</td>\r\n" + "				<td>" + rs.getString("godina") + "</td>\r\n"
								+ "				<td>" + rs.getString("opis") + "</td>\r\n"
								+ "<td> <a class=\"glyphicon glyphicon-pencil\" href=\"izmjeni.html?id=" + rs.getInt("id")
								+ "\"><a/> &nbsp;&nbsp;&nbsp; <a class=\"glyphicon glyphicon-trash\" href=\"obrisi.html?id=" + rs.getInt("id")
								+ "\"><a/></td>\r\n" + "	</tr>\r\n";
					}
				} catch (Exception e) {
					out += "	<tr>\r\n" + "				<td hidden> GRE?KA !!! </td>\r\n"
							+ "				<td> GRE?KA !!! </td>\r\n" + "				<td> GRE?KA !!! </td>\\r\\n\""
							+ "				<td> GRE?KA !!! </td>\\r\\n\""
							+ "				<td> GRE?KA !!! </td>\\r\\n\""
							+ "				<td> GRE?KA !!! </td>\\r\\n\""
							+ "				<td> GRE?KA !!! </td>\\r\\n\"" + "	</tr>\r\n";
					e.printStackTrace();
				} finally {
					if (con != null) {
						try {
							con.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}

			out += "		</table>\r\n" + "		<br/>"
					+ "	</body>\r\n" + "</html>\r\n";

			PrintWriter pw = response.getWriter();
			pw.println(out);

		}

	}
}



