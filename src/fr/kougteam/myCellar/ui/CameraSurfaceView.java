package fr.kougteam.myCellar.ui;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
	private SurfaceHolder holder;
	private Camera camera;

	public CameraSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);

		//Initiate the Surface Holder properly
		this.holder = this.getHolder();
		this.holder.addCallback(this);
		this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			//Open the Camera in preview mode
			this.camera = Camera.open();
			this.camera.setPreviewDisplay(this.holder);
			

		} catch(IOException ioe) {
			ioe.printStackTrace(System.out);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		try {
			Camera.Parameters parameters = camera.getParameters();
			parameters.setPreviewSize(width, height);
			camera.setParameters(parameters);
			camera.setDisplayOrientation(90);
			camera.startPreview();
		} catch (Exception e) {
			Log.e("CameraPreview error in surfaceChanged", e.getMessage());
		}
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when replaced with a new screen
		//Always make sure to release the Camera instance
		try {
			camera.stopPreview();
			camera.release();
			camera = null;
		} catch (Exception e) {
			Log.e("CameraPreview error in surfaceDestroyed", e.getMessage());
		}
	}

	public Camera getCamera() {
		return this.camera;
	}
	
	public void setCameraDisplayOrientation(Activity activity, int cameraId) {
	     android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
	     android.hardware.Camera.getCameraInfo(cameraId, info);
	     int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
	     int degrees = 0;
	     switch (rotation) {
	         case Surface.ROTATION_0: degrees = 0; break;
	         case Surface.ROTATION_90: degrees = 90; break;
	         case Surface.ROTATION_180: degrees = 180; break;
	         case Surface.ROTATION_270: degrees = 270; break;
	     }

	     int result;
	     if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	         result = (info.orientation + degrees) % 360;
	         result = (360 - result) % 360;  // compensate the mirror
	     } else {  // back-facing
	         result = (info.orientation - degrees + 360) % 360;
	     }
	     camera.setDisplayOrientation(result);
	 }
}
