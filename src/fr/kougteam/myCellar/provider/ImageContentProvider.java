package fr.kougteam.myCellar.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;
import fr.kougteam.myCellar.helper.FileHelper;

public class ImageContentProvider extends ContentProvider {

	public static final Uri CONTENT_URI = Uri.parse("content://fr.kougteam.myCellar/");
	public static final String IMAGE_DIRECTORY = FileHelper.SD_DIRECTORY+File.separator+"img";
	public static final String IMAGE_NAME = "myCellarNewImage.jpg";

	private static final HashMap<String, String> MIME_TYPES = new HashMap<String, String>();

	static {
		MIME_TYPES.put(".jpg", "image/jpeg");
		MIME_TYPES.put(".jpeg", "image/jpeg");
	}

	@Override
	public boolean onCreate() {
		try {
			File mFile = new File(getContext().getFilesDir(), IMAGE_NAME);
			if (!mFile.exists()) {
				mFile.createNewFile();
			}
			getContext().getContentResolver().notifyChange(CONTENT_URI, null);
			return (true);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String getType(Uri uri) {
		String path = uri.toString();
		for (String extension : MIME_TYPES.keySet()) {
			if (path.endsWith(extension)) {
				return (MIME_TYPES.get(extension));
			}
		}
		return (null);
	}

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
		File f = new File(getContext().getFilesDir(), IMAGE_NAME);
		if (f.exists()) {
			return (ParcelFileDescriptor.open(f,
			ParcelFileDescriptor.MODE_READ_WRITE));
		}
		throw new FileNotFoundException(uri.getPath());
	}

	@Override
	public Cursor query(Uri url, String[] projection, String selection,	String[] selectionArgs, String sort) {
		throw new RuntimeException("Operation not supported");
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		throw new RuntimeException("Operation not supported");
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		throw new RuntimeException("Operation not supported");
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		throw new RuntimeException("Operation not supported");
	}
	
	public static Bitmap fixOrientation(Bitmap bmp) {
		//System.out.println("Bitmap width="+bmp.getWidth()+ "height="+bmp.getHeight());
		try {
		    if (bmp!=null && bmp.getWidth() > bmp.getHeight()) {
		        Matrix matrix = new Matrix();
		        matrix.postRotate(90);
		        Bitmap newBmp = Bitmap.createBitmap(bmp , 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		        bmp.recycle();
		        return newBmp;
		    }
		} catch (Exception e) {
			Log.e("ImageContentProvider.fixOrientation", "Unable to rotate Bitmap !", e);
		}
	    return bmp;
	}
	
	public static void fillImageViewWithFile(ImageView imageView, File imageFile, int reqWidth, int reqHeight) {
		//System.out.println("Screen width="+reqWidth+ "height="+reqHeight);
		Bitmap mBitmap = decodeSampledBitmapFromResource(imageFile, reqWidth, reqHeight);
		mBitmap = ImageContentProvider.fixOrientation(mBitmap);
		imageView.setImageBitmap(mBitmap);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    int height = options.outHeight;
	    int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (!(width>height && reqWidth<reqHeight)) {
	    	height = height / 2;
	    	width = width / 2;
	    }

        while ((height / inSampleSize) > reqHeight
                && (width / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
        //System.out.println("2. inSampleSize="+inSampleSize);

	
	    return inSampleSize;
	}
	
	public static Bitmap decodeSampledBitmapFromResource(File imageFile, int reqWidth, int reqHeight) {
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    options.inPurgeable = true; 
	    
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
	}
	
}
