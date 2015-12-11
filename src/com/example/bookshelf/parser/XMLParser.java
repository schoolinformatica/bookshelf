package com.example.bookshelf.parser;

import com.example.bookshelf.model.Book;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jls on 5/21/15.
 */
public class XMLParser {

    // Used to parseBooks your xml
    public static List<Book> parse(String content) {

        try {
            // Working Objects and variables
            String tagName = ""; // Fill in your tagName
            boolean inDataItemTag = false;
            String currentTagName = "";
            Book book = null;
            List<Book> bookList = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); // Grab instace
            XmlPullParser parser = factory.newPullParser(); // init parser
            parser.setInput(new StringReader(content)); // Set the parser to parseBooks input param

            int eventType = parser.getEventType(); // Track Type of event

            // Move through the document
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        if (currentTagName.equals(tagName)) {
                            inDataItemTag = true;
                            book = new Book();
                            bookList.add(book);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(tagName)) {
                            inDataItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        if (inDataItemTag && book != null) {
                            // Create book Object
                            switch (currentTagName) {
                                case "pages":
                                    book.setPages(parser.getText());
                                    break;
                                case "title":
                                    book.setTitle(parser.getText());
                                    break;
                                case "author":
                                    book.setAuthor(parser.getText());
                                    break;
                                case "category":
                                    book.setCategory(parser.getText());
                                    break;
                                case "publisher":
                                    book.setPublisher(parser.getText());
                                    break;
                                case "summary":
                                    book.setSummary(parser.getText());
                                    break;
                                case "date":
                                    book.setPublicationDate(parser.getText());
                                    break;
                                case "isbn":
                                    book.setIsbn(parser.getText());
                                    break;
                                case "note":
                                    book.setNote(parser.getText());
                                    break;
                                case "read":
                                    book.setRead(Boolean.parseBoolean(parser.getText()));
                                    break;
                                case "language":
                                    book.setLanguage(parser.getText());
                                    break;
                                case "print":
                                    book.setPrint(parser.getText());
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                }
                eventType = parser.next(); // Trigger next evert for parser
            }

            return bookList; // Return a list of books
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
