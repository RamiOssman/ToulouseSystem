package pack;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

public class infores1 {

	public String NameOfTheBook;
	public String NameOfTheAuthor;
	public String NationalityOfTheAuthor;
	public String NumberOfPages;
	public String Language;
	public String AddDate;
	public String AgentID;
	
	public int NumberOfChapiters;

	public String[] ChapitersName;
	public String[] ChapitersContent;
	public String[] ChapitersStartAt;
	public String[] ChapitersEndsAt;

	public String[] ChapitersContentsALFA;
	public String TRCODE;

	public String BookNameDB;
	public String AuthorNameDB;
	public String[] ChapiterNameDB;
	public String TLcrypt;

	public HttpServletRequest request;

	public infores1(HttpServletRequest requestR) {
		request = requestR;
		TRCODE = randGen(1, 99999999) + "";

	}

	public void input() throws UnsupportedEncodingException {

		NameOfTheBook = new String(request.getParameter("bookName").getBytes("ISO-8859-1"), "UTF-8");
		NameOfTheAuthor = new String(request.getParameter("authorName").getBytes("ISO-8859-1"), "UTF-8");
		NationalityOfTheAuthor = new String(request.getParameter("authorCountry").getBytes("ISO-8859-1"), "UTF-8");
		NumberOfPages = new String(request.getParameter("PagesCount").getBytes("ISO-8859-1"), "UTF-8");
		NumberOfChapiters = Integer.parseInt(new String(request.getParameter("ChapiterCount").getBytes("ISO-8859-1"), "UTF-8"));
		AddDate = new String(request.getParameter("PubDate").getBytes("ISO-8859-1"), "UTF-8");
		AgentID = new String(request.getParameter("AgentID").getBytes("ISO-8859-1"), "UTF-8");
		Language = new String(request.getParameter("lang").getBytes("ISO-8859-1"), "UTF-8");

		ChapitersName = new String[NumberOfChapiters];
		ChapitersContent = new String[NumberOfChapiters];
		ChapitersContentsALFA = new String[NumberOfChapiters];
		ChapitersStartAt = new String[NumberOfChapiters];
		ChapitersEndsAt = new String[NumberOfChapiters];

		for (int x = 0; x < NumberOfChapiters; x++) {
			int xp1 = x + 1;

			ChapitersName[x] = new String(request.getParameter("ch" + xp1 + "name").getBytes("ISO-8859-1"), "UTF-8");
			ChapitersContent[x] = new String(request.getParameter("ch" + xp1 + "content").getBytes("ISO-8859-1"),
					"UTF-8");
			ChapitersStartAt[x] = new String(request.getParameter("ch" + xp1 + "same").getBytes("ISO-8859-1"), "UTF-8");
			ChapitersEndsAt[x] = new String(request.getParameter("ch" + xp1 + "eame").getBytes("ISO-8859-1"), "UTF-8");
			ChapitersContentsALFA[x] = ChapitersContent[x];
			ChapitersContentsALFA[x].replaceAll("[^A-Za-z]+", "");

		}



	}

	public void encodeForDatabases() {
		ChapiterNameDB = new String[ChapitersName.length];
		BookNameDB = splitt(NameOfTheBook);
		AuthorNameDB = splitt(NameOfTheAuthor);

		for (int l = 0; l < ChapitersName.length; l++)
			ChapiterNameDB[l] = splitt(ChapitersName[l]);

		TLcrypt = crypter.getMD5(BookNameDB + AuthorNameDB);

	}

	public static String splitt(String st) {
		String[] arf = st.split(" ");
		String res = "";
		for (int x = 0; x < arf.length; x++)
			res += arf[x];
		return res;

	}

	public int randGen(int min, int max) {

		Random random = new Random();

		return random.nextInt(max - min + 1) + min;

	}

}
