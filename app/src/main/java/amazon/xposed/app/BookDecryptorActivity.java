package amazon.xposed.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.mobi.mobi.MobiBook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BookDecryptorActivity extends Activity {

    private String TAG = "BookDecryptor";

    public void listf(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);

        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                listf(file.getAbsolutePath(), files);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<File> prcFiles = new ArrayList<File>();
        this.listf(Environment.getExternalStorageDirectory() + "/Android/data/com.amazon.kindle/files/", prcFiles);
        File[] kindleFiles = prcFiles.toArray(new File[prcFiles.size()]);

        List<MobiBook> mobiFiles = new ArrayList<MobiBook>();

        // kindleFiles.filter(_.getName.endsWith(".prc")).flatMap(MobiBook(_)) .... just sayin' ...

        for (File f : kindleFiles) {
            if (f.getName().endsWith(".prc")) {
                try {
                    Log.d(TAG, "File: " + f.getName());
                    mobiFiles.add(MobiBook.parse(f));
                } catch (Exception e) {
                    Log.e(TAG, "Failed to parse: " + f.getName() + " \t " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        StringBuilder sb = new StringBuilder();

        for (MobiBook mb : mobiFiles) {
            sb.append(mb.getName()).append("\n");
        }

        Log.d(TAG, sb.toString());
    }

}
