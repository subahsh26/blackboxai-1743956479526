package com.ats.resumanager;

import android.content.Context;
import android.net.Uri;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import java.io.InputStream;

public class ResumeParser {
    public static String parseDocument(Context context, Uri uri) throws Exception {
        String mimeType = context.getContentResolver().getType(uri);
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        
        if (mimeType == null) {
            // Fallback to file extension
            String path = uri.getPath();
            if (path != null) {
                if (path.endsWith(".pdf")) {
                    return parsePdf(inputStream);
                } else if (path.endsWith(".doc")) {
                    return parseDoc(inputStream);
                } else if (path.endsWith(".docx")) {
                    return parseDocx(inputStream);
                }
            }
            throw new Exception("Unsupported file type");
        }

        switch (mimeType) {
            case "application/pdf":
                return parsePdf(inputStream);
            case "application/msword":
                return parseDoc(inputStream);
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return parseDocx(inputStream);
            default:
                throw new Exception("Unsupported file type: " + mimeType);
        }
    }

    private static String parsePdf(InputStream inputStream) throws Exception {
        PDDocument document = PDDocument.load(inputStream);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();
        return text;
    }

    private static String parseDoc(InputStream inputStream) throws Exception {
        HWPFDocument document = new HWPFDocument(inputStream);
        WordExtractor extractor = new WordExtractor(document);
        String text = extractor.getText();
        document.close();
        return text;
    }

    private static String parseDocx(InputStream inputStream) throws Exception {
        XWPFDocument document = new XWPFDocument(inputStream);
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        String text = extractor.getText();
        document.close();
        return text;
    }
}