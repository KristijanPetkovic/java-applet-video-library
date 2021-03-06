import javax.servlet.http.HttpServletRequest;

public class Util {
	public static String getMenu(HttpServletRequest request)
	{
		String imeSesije = (String) request.getSession().getAttribute("ime");
		if(imeSesije.equals("admin")) {
		return"	<nav class=\"navbar navbar-inverse\">\r\n" + "  <div class=\"container-fluid\">\r\n"
				+ "    <div class=\"navbar-header\">\r\n"
				+ "      <a class=\"navbar-brand\" href=\"filmovi.html\">Videoteka</a>\r\n" + "    </div>\r\n"		
				+ "    <ul class=\"nav navbar-nav\">\r\n"
				+ "      <li class=\"dropdown\"><a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"filmovi.html\">Filmovi <span class=\"caret\"></span></a>\r\n"
				+ "        <ul class=\"dropdown-menu\">\r\n"
				+ "          <li><a href=\"filmovi.html\">Pregled filmova</a></li>\r\n"
				+ "          <li><a href=\"DodajFilmServlet\">Dodavanje filmova</a></li>\r\n" + "        </ul>\r\n"
				+ "      </li>\r\n"
				+ "      <li class=\"dropdown\"><a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"korisnici.html\">Korisnici <span class=\"caret\"></span></a>\r\n"
				+ "        <ul class=\"dropdown-menu\">\r\n"
				+ "          <li><a href=\"korisnici.html\">Pregled korisnika</a></li>\r\n" + "        </ul>\r\n"
				+ "      </li>\r\n" + "    </ul>\r\n"
				+ "    <ul class=\"nav navbar-nav navbar-right\">\r\n"
				+ "      <li><a href=\"" + request.getContextPath() + "/odjava.html\"><span class=\"glyphicon glyphicon-log-out\"></span> Odjavi se " + (String) request.getSession().getAttribute("ime") + "</a></li>\r\n"
				+ "    </ul>\r\n" + "  </div>\r\n" + "</nav>";
		}else {
		return "	<nav class=\"navbar navbar-inverse\">\r\n" + "  <div class=\"container-fluid\">\r\n"
				+ "    <div class=\"navbar-header\">\r\n"
				+ "      <a class=\"navbar-brand\" href=\"filmovi.html\">Videoteka</a>\r\n" + "    </div>\r\n"		
				+ "    <ul class=\"nav navbar-nav navbar-right\">\r\n"
				+ "      <li><a href=\"" + request.getContextPath() + "/odjava.html\"><span class=\"glyphicon glyphicon-log-out\"></span> Odjavi se " + (String) request.getSession().getAttribute("ime") + "</a></li>\r\n"
				+ "    </ul>\r\n" + "  </div>\r\n" + "</nav>";
	}
		}
}
