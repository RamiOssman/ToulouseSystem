package system;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import resources.R;

public class googler {

	public googler(String BookName, String ChapiterName, String Content) throws   IOException {

		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		
		File fl = new File(R.LuceneDirectory + "/" + BookName) ; 
		fl.mkdir() ; 

		Directory index = FSDirectory.open(fl);

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);

		IndexWriter indexWriter = new IndexWriter(index, config);

		addDoc(indexWriter, Content, ChapiterName);
		indexWriter.close();

	}
	
	public googler(String BookName , String[] chapterList) throws IOException 
	{
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		
		File fl = new File(R.LuceneDirectory + "/" + BookName) ; 
		fl.mkdir() ; 
		
		Directory index = FSDirectory.open(fl);

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);

		IndexWriter indexWriter = new IndexWriter(index, config);

		addDoc(indexWriter, BookName, "PARATEXT");
		
		for(String ChapterTitle : chapterList)
			addDoc(indexWriter, ChapterTitle, "PARATEXT");



		indexWriter.close();

		
		
		
	}

	public void addDoc(IndexWriter w, String v, String where) throws CorruptIndexException, IOException {

		Document doc = new Document();
		doc.add(new Field(where, v, Field.Store.YES, Field.Index.ANALYZED));
		w.addDocument(doc);

	}

}
