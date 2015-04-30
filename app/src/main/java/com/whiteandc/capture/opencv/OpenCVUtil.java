package com.whiteandc.capture.opencv;

import android.app.Activity;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class OpenCVUtil {

	private static final float MIN_DIST = 34;
	private static final float MIN_GOOD_MATCHES = 3;
	
	private static String CLASS = "OpenCVUtil"; 
	static {
	    if (!OpenCVLoader.initDebug()) {}
	}

    public static Mat resourceToMat(int imageResourceID, Activity activity)  {
        Mat resource = null;
        try {
            resource =  Utils.loadResource(activity, imageResourceID, Highgui.CV_LOAD_IMAGE_COLOR);
        } catch (IOException e) {
            Log.e(CLASS, "Error loading resource: " + imageResourceID, e);
        }
        return resource;
    }

	public static boolean compare2Images(Mat capturedImage, Mat referenceImage, boolean storeResults){

        // Adjust the orientation
        if(!sameOrientation(capturedImage, referenceImage)){
            Core.flip(referenceImage.t(), referenceImage, 1); // Rotate 90°
        }

        Log.i(CLASS, "Before scale adjust");
        Log.i(CLASS, "Captured image size: "  + capturedImage.size());
        Log.i(CLASS, "Reference image size: " + referenceImage.size());

        if(storeResults)
            storePicture(capturedImage);
        // Adjust the scale
        if(referenceImage.size().width > capturedImage.size().width){
            Size size = calculateSize(capturedImage, referenceImage);
            Imgproc.resize(referenceImage, referenceImage, size);
        }else{
            Size size = calculateSize(referenceImage, capturedImage);
            Imgproc.resize(capturedImage, capturedImage, size);
        }

        if(storeResults)
            storePicture(capturedImage);

        Log.i(CLASS, "After scale adjust");
        Log.i(CLASS, "Captured image size: "  + capturedImage.size());
        Log.i(CLASS, "Reference image size: " + referenceImage.size());

		//Key Points
		MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
		MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
		
		// Descriptors
		Mat descriptors1 = new Mat();
		Mat descriptors2 = new Mat();

		//Definition of ORB keypoint detector and descriptor extractors
		FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
		DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
	
		//Detect keypoints
		detector.detect(capturedImage, keypoints1);
		detector.detect(referenceImage, keypoints2);

		//Extract descriptors
		extractor.compute(capturedImage, keypoints1, descriptors1);
		extractor.compute(referenceImage, keypoints2, descriptors2);

		//Definition of descriptor matcher
		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
	
		//Match points of two images
		MatOfDMatch matches = new MatOfDMatch();
		matcher.match(descriptors1, descriptors2 ,matches);

		MatOfDMatch good = new MatOfDMatch();
		good.fromList(getMatchesPercentage(matches));

        final boolean match = good.size().height >= MIN_GOOD_MATCHES;
        if(match || storeResults) {
            storeMatchesPicture(capturedImage, referenceImage, keypoints1, keypoints2, good);
        }

        return match;
	}

    private static Size calculateSize(Mat mat1, Mat mat2) {
        double ratio = mat2.size().width / mat2.size().height;
        return new Size(mat1.size().width, mat1.size().width*ratio);
    }

    private static void storeMatchesPicture(Mat img1, Mat img2,
			MatOfKeyPoint keypoints1, MatOfKeyPoint keypoints2, MatOfDMatch good) {
		Mat outImg = new Mat();

        Features2d.drawMatches(img1, keypoints1, img2, keypoints2, good, outImg);

        storePicture(outImg);
	}

    private static void storePicture(Mat outImg) {
        Log.i(CLASS, "Storing picture");

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES + File.separator + "CaptureTheCity" + File.separator + "Debug");
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        if (! path.exists()){
            if (! path.mkdirs()){
                Log.i(CLASS, "Required media storage does not exist");
            }
        }
        File file = new File(path,"CAPTURE_"+ timeStamp + ".jpg");

        boolean bool = Highgui.imwrite(file.toString(), outImg);
        if (bool == true){
            Log.i(CLASS, "SUCCESS writing image to external storage");
        }else{
            Log.i(CLASS, "Fail writing image to external storage");
        }
    }

    private static LinkedList<DMatch> getMatchesPercentage(MatOfDMatch matches){
		List<DMatch> matchesList = matches.toList();
		Double max_dist = 0.0;
		Double min_dist = 100.0;

		for (int i = 0; i < matchesList.size(); i++) {
		    Double dist = (double) matchesList.get(i).distance;
		    if (dist < min_dist)
		        min_dist = dist;
		    if (dist > max_dist)
		    max_dist = dist;
		}
		
		LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
		for (int i = 0; i < matchesList.size(); i++)  {  
		    if (matchesList.get(i).distance <= MIN_DIST){
		    	good_matches.addLast(matchesList.get(i));
		    }
		}

		return good_matches;
	}


    private static boolean sameOrientation(Mat mat1, Mat mat2){
        return (mat1.size().width > mat1.size().height && mat2.size().width > mat2.size().height) ||
               (mat1.size().width < mat1.size().height && mat2.size().width < mat2.size().height);
    }
}
