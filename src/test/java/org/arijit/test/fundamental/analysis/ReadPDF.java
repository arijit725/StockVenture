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
import org.arijit.stock.analyze.parser.ProfitAndLossPDFParser;
import org.arijit.stock.analyze.parser.YearlyReportParser;

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
        String pdfPath = "/home/arijit/Downloads/tcs_bl.pdf";
//        String pdfPath = "/home/arijit/Downloads/hcl_yearly.pdf";
        File file = new File(pdfPath);

        BalanceSheetPDFParser balanceSheetPDFParser = new BalanceSheetPDFParser(file);
        balanceSheetPDFParser.generateDto(FundamentalInfoDto.builder(5));
//        YearlyReportParser balanceSheetPDFParser = new YearlyReportParser(file);
//        balanceSheetPDFParser.generateDto(FundamentalInfoDto.builder(5));
    }
}
