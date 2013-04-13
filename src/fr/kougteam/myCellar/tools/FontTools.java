package fr.kougteam.myCellar.tools;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontTools {

	public static final String DEFAULT_FONT_NAME = "Delicious-Italic.otf";
	
	public static final Typeface getDefautTypeFace(AssetManager assets) {
		return Typeface.createFromAsset(assets, DEFAULT_FONT_NAME);
	}
	
	public static final void setDefaultAppFont(ViewGroup mContainer, AssetManager assets) {
		final Typeface mFont = getDefautTypeFace(assets);
		
	    if (mContainer == null || mFont == null) return;

	    final int mCount = mContainer.getChildCount();

	    // Loop through all of the children.
	    for (int i = 0; i < mCount; ++i) {
	        final View mChild = mContainer.getChildAt(i);
	        if (mChild instanceof TextView) {
	            // Set the font if it is a TextView.
	            ((TextView) mChild).setTypeface(mFont);
	        } else if (mChild instanceof ViewGroup) {
	            // Recursively attempt another ViewGroup.
	        	setDefaultAppFont((ViewGroup) mChild, assets);
	        }
	    }
	}
}
