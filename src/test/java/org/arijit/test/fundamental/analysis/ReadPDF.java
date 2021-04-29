package org.arijit.test.fundamental.analysis;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.parser.BalanceSheetPDFParser;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

//triptesh
public class ReadPDF {

    public static void main(String args[]) throws Exception {
        String pdfPath = "/home/arijit/Downloads/Moneycontrol.com __ Company Info __ Print Financials.pdf";
        File file = new File(pdfPath);

        BalanceSheetPDFParser balanceSheetPDFParser = new BalanceSheetPDFParser(file);
        balanceSheetPDFParser.generateDto(FundamentalInfoDto.builder(5));
    }

//        InputStream inn = new FileInputStream(file);
//        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r");
////        PDFTextParser pdf = new PDFTextParser(pdfPath);
////        PDDocument document = PDDocument.load(file);
//        PDFParser parser = new PDFParser(randomAccessFile);
//        parser.parse();
//        PDDocument pdDocument= parser.getPDDocument();
//        PDPageTree pdPageTree = pdDocument.getPages();
//        Iterator<PDPage> it = pdPageTree.iterator();
//
//        PDFTextStripper pdfStripper = new PDFTextStripper();
//        String text = pdfStripper.getText(pdDocument);
//        String[] stringList = text.split("\n");
//        List<String> list = Arrays.asList(stringList).stream().map(line->{
//            System.out.println(line);
//            StringBuilder builder = new StringBuilder();
//            for(int i=0;i<line.length();i++)
//            {
//                int j = line.charAt(i);
//                System.out.print(j+" ");
//                if(j<128){
//                    builder.append(line.charAt(i));
//                }
//            }
//            System.out.println();
//            return builder.toString();
//        }).collect(Collectors.toList());
//        list.stream().forEach(System.out::println);
//
//        pdDocument.close();
//
//        String dateLine = "Mar'20Mar'19Mar'18Mar'17Mar'16";
//        List<String> startingList = new ArrayList<>();
//        startingList.add("Total Share Capital");
//        startingList.add("Equity Share Capital");
//        startingList.add("Reserves");
//        startingList.add("Total Debt");
//
//        filterELements(list,dateLine,startingList);
//    }
    // Mar'20Mar'19Mar'18Mar'17Mar'16
    // Mar'20Mar'19Mar'18Mar'17Mar'16
//    private static void filterELements(List<String> stringList,String dateLine,List<String> staringList){
//        System.out.println("==================================================================================");
//        Iterator<String> it = staringList.iterator();
//        while(it.hasNext()){
//            String datapoint = it.next();
//            Iterator<String> itT = stringList.iterator();
//            while(itT.hasNext()){
//                String line = itT.next().trim();
//                String compressedLine = new String(line.replaceAll(" ","").getBytes(), Charset.defaultCharset());
//                if(compressedLine.equals(dateLine))
//                    System.out.println("Dates: "+line);
//                else if(line.startsWith(datapoint))
//                    System.out.println(line);
//            }
//        }
//    }
}
