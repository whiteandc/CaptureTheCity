package com.whiteandc.capture.opencv;

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
	public static boolean compare2Images(String capturedImgName, int imageResourceID, FragmentActivity fragmentActivity, StringBuilder debugInfo) throws IOException{
		//Load images to compare
		Mat img1 = Highgui.imread(capturedImgName, Highgui.CV_LOAD_IMAGE_COLOR);
		Mat img2=null;
		try {
			img2 = Utils.loadResource(fragmentActivity, imageResourceID, Highgui.CV_LOAD_IMAGE_COLOR);
		} catch (IOException e) {
			Log.e(CLASS, "Error: ", e);
			throw e;
		} 
	
		debugInfo.append( "Tamano de la imagen a reproducir = "+img2.size()+"\n");
		debugInfo.append("Tamano original de la imagen capturada = "+img1.size()+"\n");
		if(img2.size().width < img2.size().height){
			Imgproc.resize(img1, img1, new Size(img2.size().height, img2.size().width));
			Core.flip(img1.t(), img1, 1);
		}else{
			Imgproc.resize(img1, img1, img2.size());
		}
		debugInfo.append("Tamano tras resize() de la imagen capturada = "+img1.size()+"\n");
		
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
		detector.detect(img1, keypoints1);
		detector.detect(img2, keypoints2);  
		debugInfo.append("Tamano de los keypoints de la imagen capturada = "+keypoints1.size()+"\n");
		debugInfo.append("Tamano de los keypoints de la imagen a reproducir = "+keypoints2.size()+"\n");
		
		//Extract descriptors
		extractor.compute(img1, keypoints1, descriptors1);
		extractor.compute(img2, keypoints2, descriptors2);
		debugInfo.append("Tamano de los descriptores de la imagen capturada = "+descriptors1.size()+"\n");
		debugInfo.append("Tamano de los descriptores de la imagen a reproducir = "+descriptors2.size()+"\n");
		
		//Definition of descriptor matcher
		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
	
		//Match points of two images
		MatOfDMatch matches = new MatOfDMatch();
		matcher.match(descriptors1,descriptors2 ,matches);
		debugInfo.append("Tamano de los matches = "+matches.size()+"\n");

		MatOfDMatch good = new MatOfDMatch();
		good.fromList(getMatchesPercentage(matches, keypoints1, keypoints2, debugInfo));
		debugInfo.append("Tamano de los matches BUENOS = "+good.size()+"\n");
		
		storeMatchesPicture(img1, img2, keypoints1, keypoints2, good);
		
		return good.size().height >= MIN_GOOD_MATCHES;
	}

	private static void storeMatchesPicture(Mat img1, Mat img2,
			MatOfKeyPoint keypoints1, MatOfKeyPoint keypoints2, MatOfDMatch good) {
		Mat outImg = new Mat();
		Features2d.drawMatches(img1, keypoints1, img2, keypoints2, good, outImg);
		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+File.separator+"CaptureTheCity"+File.separator+"Debug");
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        
        
		if (! path.exists()){
            if (! path.mkdirs()){
                Log.d("Camera Guide", "Required media storage does not exist");
            }
        }
        File file = new File(path,"IMG_"+ timeStamp + ".jpg");

		boolean bool = false;
		bool = Highgui.imwrite(file.toString(), outImg);
		if (bool == true){
			Log.d(CLASS, "SUCCESS writing image to external storage");
		}else{
			Log.d(CLASS, "Fail writing image to external storage");
		}
	}
	
	private static LinkedList<DMatch> getMatchesPercentage(MatOfDMatch matches, MatOfKeyPoint keypoints1, MatOfKeyPoint keypoints2, StringBuilder debugInfo){
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

		debugInfo.append("Distancia m�xima calculada: "+max_dist+"\n");
		debugInfo.append("Distancia m�nima calculada: "+min_dist+"\n");
		
		LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
		for (int i = 0; i < matchesList.size(); i++)  {  
		    if (matchesList.get(i).distance <= MIN_DIST){
		    	good_matches.addLast(matchesList.get(i));
		    }
		}

		return good_matches;
	}

}
