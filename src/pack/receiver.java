package pack;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

import system.sqlStore;
import system.textManager;

/**
 * Servlet implementation class receiver
 */
@WebServlet("/receiver")
@MultipartConfig
public class receiver extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public receiver() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

			PrintWriter out = response.getWriter();


			response.setContentType("text/html; UTF-8");

		infores1 resA = new infores1(request);
			resA.input();
			resA.encodeForDatabases();
			
			new textManager(resA);

		
			sqlStore store = new sqlStore(resA);

		    store.store();

			if (!store.connection_ready)
			out.println("\n connection is not established ! " + "\n " + store.ERROR_CODE);

		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}


}
