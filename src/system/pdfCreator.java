package system;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;



import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;

import resources.R;

public class pdfCreator {
	

	  
	//  private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
//	      Font.BOLD);
	  private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
	      Font.NORMAL, BaseColor.RED);
//	  private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
	//      Font.BOLD);
	//  private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
	//      Font.BOLD);
	  

	  
	  Font font =null ; 
	  public Document document ; 
	  PdfWriter writer ; 
	public pdfCreator(String AuthorName, String BookName , String Location ) 
	{

		  BaseFont base = null;
			try {
				try {
					base = BaseFont.createFont(R.FontDirectory, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
					BaseFont.createFont() ; 
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   font = new Font(base, 11f, Font.BOLD);

		
	    try {
	        document = new Document();
	        OutputStream out =  new FileOutputStream(Location);
	           
	        
            writer =    PdfWriter.getInstance(document,out);
	        document.open();
	        addMetaData(BookName , AuthorName);
	        addTitlePage(BookName ,AuthorName ,  "");
	        
	      } catch (Exception e) {
	        e.printStackTrace();
	     	
	}
	    
	    
	}
	
	  public void addMetaData(String BookName , String author) {
		    document.addTitle(BookName);
		    document.addSubject("");
		    document.addKeywords("BN , pdf , book");
		    document.addAuthor(author);
		    document.addCreator("BN site");
		  }
	  
	 public void addTitlePage(String Title , String AUTHOR , String Description ) throws DocumentException {
		 
		 
		    document.newPage();
		    
	        Phrase p = new Phrase(Title + " Écrit par: "+AUTHOR , font);
	        

	        addEmptyLine(p, 2);
	       // p.add(new Phrase(dest));
	        
	 
	        ColumnText canvas = new ColumnText(writer.getDirectContent());
	        canvas.setSimpleColumn(72, 72, 612-72, 792-72);
	        canvas.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
	        canvas.addElement(p );
	        canvas.addElement(new Paragraph(" "));
	        canvas.addElement(new Paragraph("ALL RIGHTS RECEIVED" , redFont));
	        
	       
	        canvas.go();
	        addEmptyLine(p, 4);
	        document.newPage();
		  }


		public void addArabicText(String Title, String dest, int ChapiterNb, int x) throws IOException, DocumentException {

			
			// p.add(new Phrase(dest));

			ColumnText canvas = new ColumnText(writer.getDirectContent());
			canvas.setAlignment(PdfWriter.RUN_DIRECTION_RTL);
			canvas.setSimpleColumn(72, 72, 612 - 72, 792 - 72);
			canvas.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			if (x == 0) {
				Phrase p = new Phrase("Chapitre " + ChapiterNb + ": " + Title, font);
				addEmptyLine(p, 2);
				canvas.addElement(p);
			}
			canvas.addElement(new Paragraph(" "));
			canvas.addElement(new Paragraph(dest, font));

			canvas.go();

			document.newPage();
		}

	
		 public void addEmptyLine(Paragraph paragraph, int number) {
			    for (int i = 0; i < number; i++) {
			      paragraph.add(new Paragraph(" "));
			    }
			  }
		 public void addEmptyLine(Phrase paragraph, int number) {
			    for (int i = 0; i < number; i++) {
			      paragraph.add(new Paragraph(" "));
			    }
			  }
		 public void nextPage()
		 {
			 document.newPage();	 
		 
		 }
	 public void end()
	 {
		 document.close();
		 
	 }
public String cutText(String Text , int part)
{
	int start = (part-1)*4100 ; 
	int end = (part)*4100 ; ;
	String result = ""; 
	
	if(Text.length() < end)
	{
		if(Text.length() < start)
			return null ; 
		end = Text.length() ; 
					
		
	}
	
	try
	{
	result = Text.substring(start, end) ; 
	}
	catch(IndexOutOfBoundsException x)
	{
		
		
		return null ; 
	}
	
	
	return result ; 
}
}
