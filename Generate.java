package com.example.foodapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Generate extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            generatePDF();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generatePDF();
            } else {
                Toast.makeText(this, "Permission denied. Cannot generate PDF.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void generatePDF() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(600, 800, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        canvas.drawText("Day Quantity: 1", 100, 100, paint);
        canvas.drawText("Night Quantity: 1", 100, 200, paint);
        canvas.drawText("Total Cost for Day: 30", 100, 300, paint);
        canvas.drawText("Total Cost for Night: 30", 100, 400, paint);

        // Generate and include a token number in pdf
        String tokenNumber = generateTokenNumber();
        canvas.drawText("Token Number: " + tokenNumber, 100, 500, paint);
        document.finishPage(page);
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/food_order.pdf";
        File file = new File(filePath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            Toast.makeText(this, "PDF Generated Successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "PDF Generation Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateTokenNumber() {
        int min = 1000; // Minimum token number
        int max = 9999; // Maximum token number
        int token = min + new Random().nextInt(max - min + 1);
        return String.valueOf(token);
    }
}
