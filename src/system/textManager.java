package system;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.apache.lucene.store.Directory;

import com.itextpdf.text.DocumentException;

import pack.infores1;
import resources.R;

public class textManager {

	String textFinal;

	public textManager(infores1 resources) throws IOException {

		String BookName = resources.NameOfTheBook;
		String AuthorName = resources.NameOfTheAuthor;

		String dr = R.PDFDirectory + "/" + resources.TRCODE;

		File theDir = new File(dr);
		theDir.mkdir();

		pdfCreator creator = new pdfCreator(AuthorName, BookName, dr + "/book.pdf");
		
//		new googler(resources.NameOfTheBook , resources.ChapitersName) ; 
		
		
		
		for (int xc = 0; xc < resources.NumberOfChapiters; xc++) {

			store(resources.ChapitersContent[xc], resources.TRCODE, xc + "");

			new googler(resources.TRCODE, xc + "", resources.ChapitersContent[xc]);

			pdfCreator chCreator = new pdfCreator(resources.NameOfTheAuthor, resources.NameOfTheBook,
					dr + "/" + xc + ".pdf");

			String[] ChapterContent = resolveLongText(resources.ChapitersContent[xc]);

			try {

				for (int n = 0; n < ChapterContent.length; n++) {
					creator.addArabicText(resources.ChapitersName[xc], ChapterContent[n], xc + 1, n);
					chCreator.addArabicText(resources.ChapitersName[xc], ChapterContent[n], xc + 1, n);
				}

			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			chCreator.end();

		}

		creator.end();

	}

	private void store(String TEXT, String TRCODE, String ChapiterAt) throws IOException {
		File drs = new File(R.FTPDirectory + "/" + TRCODE);
		drs.mkdir();

		PrintWriter writer = new PrintWriter(R.FTPDirectory + "/" + TRCODE + "/" + ChapiterAt + ".txt", "UTF-8");

		writer.write(TEXT);
		writer.close();
	}

	public static String[] resolveLongText(String longText) {

		int TextLimit = 3690;
		int CutNumber = 0;
		ArrayList<String> res = new ArrayList<String>();
		while (longText.length() > CutNumber * TextLimit) {

			int Start = TextLimit * CutNumber;
			int End = Start + TextLimit;
			if (End > longText.length())
				End = longText.length();

			res.add(longText.substring(Start, End));

			CutNumber++;

		}

		return res.toArray(new String[0]);
	}
}
