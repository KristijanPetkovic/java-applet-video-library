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

@WebServlet({ "/PocetnaServlet", "/pocetna.html" })
@MultipartConfig
public class PocetnaServlet extends HttpServlet {

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
					+ "	<nav class=\"navbar navbar-inverse\">\r\n" + "  <div class=\"container-fluid\">\r\n"
					+ "    <div class=\"navbar-header\">\r\n"
					+ "      <a class=\"navbar-brand\" href=\"#\">Videoteka</a>\r\n" + "    </div>\r\n"
					+ "    <ul class=\"nav navbar-nav navbar-left\">\r\n"
					+ "		 <li><a href=\"/www.videoteka.com/pravila.html\">Pravila</a></li></ul>\r\n"
					+ "    <ul class=\"nav navbar-nav navbar-right\">\r\n"
					+ "      <li><a href=\"/www.videoteka.com/noviKorisnik.html\"><span class=\"glyphicon glyphicon-new-window\"></span> Registruj se</a></li>\r\n"
					+ "      <li><a href=\"/www.videoteka.com/login.html\"><span class=\"glyphicon glyphicon-log-in\"></span> Prijavi se</a></li>\r\n"
					+ "    </ul>\r\n" + "  </div>\r\n" + "</nav>"
					+ "<div>\r\n "
					+ "		<h1 id=\"naslov\">Stranica za pregled filmova</h1>\r\n"
					+ "		<form id=\"izmjeni\" method=\"post\" ></form>\r\n"
					+ "		<form id=\"obrisi\" method=\"post\" ></form>\r\n" + "		<table class=\"center\">\r\n"
					+ "			<tr>\r\n" + "				<th hidden>ID</th>\r\n" + "				<th>Slika</th>\r\n"
					+ "				<th>Naziv</th>\r\n" + "				<th>?anr</th>\r\n"
					+ "				<th>Godina</th>\r\n" + "				<th>Re?iser</th>\r\n";

			String url = "jdbc:sqlserver://localhost:1433;databaseName=Videoteka;user=sa;password=root";
			Connection con = null;

			if (id == null) {
				try {
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
					con = DriverManager.getConnection(url);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM Filmov");
					while (rs.next()) {
						out += "	<tr>\r\n" + "				<td hidden>" + rs.getInt("id") + "</td>\r\n"
								+ "				<td><img src=data:image/jpeg;base64," + new String(Base64.getEncoder().encode(rs.getBytes(2))) + " width=\"80px\"></td>\r\n"
								+ "				<td>" + rs.getString("naziv") + "</td>\r\n" + "				<td>"
								+ rs.getString("zanr") + "</td>\r\n" + "				<td>" + rs.getString("godina")
								+ "</td>\r\n" + "				<td>" + rs.getString("opis") + "</td>\r\n"
								+ "	</tr>\r\n";
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


