package test;

import java.io.IOException;

import info.bliki.wiki.model.WikiModel;

public class WikiTest {

	public static void main(String[] args) throws IOException {
		final String wikiContentPlain = "[[File:Hausziege.jpg]]";
		
		StringBuilder sb = new StringBuilder();
		WikiModel.toHtml(wikiContentPlain, sb, "http://152.96.56.36/mediawiki/index.php/${image}", "http://152.96.56.36/mediawiki/index.php/${title}");
		System.out.println(sb.toString());
		
	}

}
