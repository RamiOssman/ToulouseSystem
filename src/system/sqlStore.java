package system;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import Administrative.As;
import pack.infores1;

import resources.*;

public class sqlStore {

	infores1 resource;
	public boolean connection_ready;
	public String ERROR_CODE;

	public sqlStore(infores1 resourcesR) {
		resource = resourcesR;
		connection_ready = true;
		ERROR_CODE = "";
	}

	public void store() {
		jdbc_connectors connectors = new jdbc_connectors(R.MYSQL_DefaultDatabase);
		if (!connectors.connectionIsReady) {
			connection_ready = false;
			ERROR_CODE = connectors.EXP;
			return;
		}

		try {
			As admin = new As(resource.AgentID, connectors);

			if (!admin.ALLOWTOUSE) {

			}

			ResultSet sF = connectors.SelectField("authors", new String[] { "AuthorName", "AuthorNat" },

					new String[] { resource.NameOfTheAuthor, resource.NationalityOfTheAuthor });
			boolean AuthorExists = sF.next();
			int AuthorId = 0;

			if (!AuthorExists)
				AuthorId = connectors.insertRecord("INSERT INTO `authors` (`AuthorName`,`AuthorNat`) VALUES ('"
						+ resource.NameOfTheAuthor + "' , '" + resource.NationalityOfTheAuthor + "')")[0];
			else
				AuthorId = sF.getInt("AuthorId");

			String BookPath = R.PDFDirectory + "/" + resource.TRCODE + "/book.pdf";

			Date date1 = new Date(); // your date
			Calendar cal = Calendar.getInstance();
			cal.setTime(date1);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);

			String date = day + "/" + month + "/" + year;
			
			int BookId = connectors.insertRecord(
					"INSERT INTO `books` (`BookTitle` , `BookLang` , `BookPath` , `BookPubYear` , `BookCreatedBy` , `BookPublishState` , `TRCODE` , `BookPages` , `BookAdd` , `BookChaptersNB`) VALUES ('"
							+ resource.NameOfTheBook + "','" + resource.Language + "','" + BookPath + "','"
							+ resource.AddDate + "','" + resource.AgentID + "' , 'NOTSET' , '" + resource.TRCODE + "' , '"+resource.NumberOfPages+"' , '"+date+"' , '"+resource.NumberOfChapiters+"')")[0];

			connectors.insertData("bookauthor", new String[] { "BookId", "AuthorId" },
					new String[] { BookId + "", AuthorId + "" });

			for (int xCh = 0; xCh < resource.NumberOfChapiters; xCh++) {

				connectors.insertRecord(
						"INSERT INTO `chapters` (`ChapterName` , `ChapterPath` , `ChapterOfBookId` , `ChapterStartAt` , `ChapterEndsAt`) VALUES ('"
								+ resource.ChapitersName[xCh] + "' , '" + xCh + "' , '" + BookId + "' , '"+resource.ChapitersStartAt[xCh]+"' , '"+resource.ChapitersEndsAt[xCh]+"') ");

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
