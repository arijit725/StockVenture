package org.arijit.stock.analyze.parser;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PDFParserUtil {

    public static String parsePDF(File pdfFile) throws IOException {
        PDDocument pdDocument = null;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(pdfFile, "r");
            PDFParser parser = new PDFParser(randomAccessFile);
            parser.parse();
            pdDocument = parser.getPDDocument();

            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(pdDocument);
            return text;
        }finally {
            if(pdDocument!=null)
                pdDocument.close();
        }
    }

    public static List<String> processPDFContents(String text){
        String[] stringList = text.split("\n");
        List<String> list = Arrays.asList(stringList).stream().map(line->{
            StringBuilder builder = new StringBuilder();
            for(int i=0;i<line.length();i++)
            {
                int j = line.charAt(i);
                if(j<128){
                    builder.append(line.charAt(i));
                }
            }
            return builder.toString();
        }).collect(Collectors.toList());
        return list;
    }

}
