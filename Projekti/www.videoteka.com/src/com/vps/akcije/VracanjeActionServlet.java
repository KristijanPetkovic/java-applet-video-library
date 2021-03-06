package com.vps.akcije;

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

/**
 * Servlet implementation class NajamActionServlet
 */
@WebServlet("/VracanjeActionServlet")
public class VracanjeActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public VracanjeActionServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String imeSesije = (String) request.getSession().getAttribute("ime");

		if (imeSesije == null) {
			response.sendRedirect("login.html");
		} else {
			// ovo je kraj dela za proveru
			String filmId = request.getParameter("id");

			String url = "jdbc:sqlserver://localhost:1433;databaseName=Videoteka;user=sa;password=root";
			Connection con = null;

			boolean sveOk = true;
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				con = DriverManager.getConnection(url);
				Statement stmt = con.createStatement();
				stmt.execute("UPDATE Najam SET datumVracanja = getdate() where datumVracanja is null and filmId=" + filmId);
			} catch (ClassNotFoundException | SQLException e) {
				sveOk = false;
				response.sendRedirect("./login.html");
				e.printStackTrace();
			} finally {
				if (con != null) {
					try {
						con.close();
						if (sveOk) {
							response.sendRedirect("filmovi.html");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
}
