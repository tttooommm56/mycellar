package fr.kougteam.myCellar.tools;

import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;

public class FileTools {
	public static boolean copyFile(Context context,String fileName) {
        boolean status = false;
        try { 
            FileOutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            InputStream in = context.getAssets().open(fileName);
            // Transfer bytes from the input file to the output file
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // Close the streams
            out.close();
            in.close();
            status = true;
        } catch (Exception e) {
            System.out.println("Exception in copyFile:: "+e.getMessage());
            status = false;
        }
        System.out.println("copyFile Status:: "+status);
        return status;
    }
}
