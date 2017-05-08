#ifndef IMAGEPROCESS_H
#define IMAGEPROCESS_H

#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"

#include <iostream>

using namespace std;

void sub(cv::Mat im1, cv::Mat im2, cv::Mat diff){
	for (int i = 0; i<im1.rows; i++){
		for (int j = 0; j<im1.cols; j++){
			cv::Vec3b a = im1.at<cv::Vec3b>(i, j);
			cv::Vec3b b = im2.at<cv::Vec3b>(i, j);
			int difference1 = abs(a[0] - b[0]);
			int difference2 = abs(a[1] - b[1]);
			int difference3 = abs(a[2] - b[2]);
			cv::Vec3b temp(difference1, difference2, difference3);
			diff.at<cv::Vec3b>(i, j) = temp;
		}
	}
}

int imageprocess()
{

		/* VideoCapture cap(0); // open the default camera
    if(!cap.isOpened())  // check if we succeeded
        return -1;
        char ch;
         
		 int N1,N2;
		 Mat frame1,frame2,frame3;
		 
		 while(ch)
         {
         
		 while(N1==0)
		 {
	}
        cap >> frame1;
		while(N2==0)
		{
	}
		cap>>frame2;
		sub(frame1, frame2, diff);
		*/
		cv::Mat channel[3];
		int x1 = 120, x2 = 977, y1 = 46, y2 = 959, c1 = 0, c2 = 0, c3 = 0, c4 = 0, x, y, i, j, k, px1 = 0, py1 = 0, px2 = 0, py2 = 0, qx1 = 0, qx2 = 0, qy1 = 0, qy2 = 0;
		cv::Mat img2 = cv::imread("i1.JPG", CV_LOAD_IMAGE_COLOR);
		cv::Mat img1 = cv::imread("i2.JPG", CV_LOAD_IMAGE_COLOR);//read the image data in the file "MyPic.JPG" and store it in 'img'
		cv::Mat diff_im(img1.rows, img1.cols, CV_8UC1);
		cv::Mat diff = cv::Mat::zeros(img1.rows, img1.cols, CV_8UC3);
		sub(img1, img2, diff);

		cv::namedWindow("MyWindow4", CV_WINDOW_NORMAL);
		cv::imshow("MyWindow4", diff);
		subtract(img1, img2, diff_im);
		/*cv::namedWindow("MyWindow1", CV_WINDOW_NORMAL);
		cv::imshow("MyWindow1", img1);
		cv::namedWindow("MyWindow3", CV_WINDOW_NORMAL);
		cv::imshow("MyWindow3", img2);
		cv::namedWindow("MyWindow5", CV_WINDOW_NORMAL);
		cv::imshow("MyWindow5", diff_im);*/
		//wait infinite time for a keypress

		cv::split(diff, channel);
		cv::threshold(channel[0], diff, 70, 255, cv::THRESH_BINARY);
		vector<vector<cv::Point>> contours;
		vector<cv::Vec4i> hierarchy;
		cv::Point centroid1, centroid2;
		long int large1 = 0, large2 = 0, area;
		int n1, n2;

		cv::namedWindow("MyWindow1", CV_WINDOW_NORMAL);
		cv::imshow("MyWindow1", diff);
		cv::findContours(diff, contours, hierarchy, CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE, cv::Point(0, 0));
		for (i = 0; i < contours.size(); i++)
		{
			area = cv::contourArea(contours[i]);
			if (large1 < area)
			{
				large1 = area;
				n1 = i;
			}
		}
		for (i = 0; i < contours.size(); i++)
		{
			area = cv::contourArea(contours[i]);
			if (large2 < area && area != large1)
			{
				large2 = area;
				n2 = i;
			}
		}
		cout << large1 << "   " << large2;
		cv::Moments M1 = cv::moments(contours[n1]);
		centroid1 = cv::Point(M1.m10 / M1.m00, M1.m01 / M1.m00);
		cv::Moments M2 = cv::moments(contours[n2]);
		centroid2 = cv::Point(M2.m10 / M2.m00, M2.m01 / M2.m00);
		cout << "\n" << centroid1.x << "  " << centroid1.y;
		cout << "\n" << centroid2.x << "  " << centroid2.y;

		//wait infinite time for a keypress
		x = x2 - x1;
		y = y2 - y1;

		c1 = centroid1.x - x1;
		while (c1 > 0)
		{
			c1 -= x / 8;
			px1++;
		}
		c2 = centroid1.y - y1;
		while (c2 > 0)
		{
			c2 -= y / 8;
			py1++;
		}
		cout << "\n" << px1 << py1;
		c3 = centroid2.x - x1;
		while (c3 > 0)
		{
			c3 -= x / 8;
			px2++;
		}
		c4 = centroid2.y - y1;
		while (c4 > 0)
		{
			c4 -= y / 8;
			py2++;
		}
		cout << "\n" << px2 << py2;


		i = centroid1.x;
		j = centroid1.y;
		if (img1.at<cv::Vec3b>(i, j)[0] < 200 && img1.at<cv::Vec3b>(i, j)[1] >50 && img1.at<cv::Vec3b>(i, j)[2] > 50)
		{
			cout << "\n" << "1";
			return 1000 * px1 + 100 * py1 + 10 * px2 + py2;

		}
		i = centroid2.x;
		j = centroid2.y;
		if (img1.at<cv::Vec3b>(i, j)[0] < 200 && img1.at<cv::Vec3b>(i, j)[1] >50 && img1.at<cv::Vec3b>(i, j)[2] > 50)
		{
			cout << "\n" << "2";
			return 1000 * px2 + 100 * py2 + 10 * px1 + py1;

		}
		cv::waitKey(0);
		
}
}
#endif
