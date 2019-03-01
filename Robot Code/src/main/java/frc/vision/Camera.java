package frc.vision;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class Camera implements Runnable {
	private Thread thread;
	private UsbCamera camera;
	private UsbCamera camera2;

	private boolean isAlive;

	public Camera() {
		this(640, 480);
	}

	public Camera(int xRes, int yRes) {
		camera = CameraServer.getInstance().startAutomaticCapture(0);
		camera.setResolution(xRes, yRes);
		camera2 = CameraServer.getInstance().startAutomaticCapture(1);
		camera2.setResolution(xRes, yRes);

		start();
	}

	public void start() {
		thread = new Thread(this, "Camera thread");
		thread.start();
		isAlive = true;
		//you might need a second thread, it think cam2 is just overriding the instance rn
	}

	@Override
	public void run() {
		Mat source = new Mat();
		Mat output = new Mat();

		CvSink cvSink = CameraServer.getInstance().getVideo();
		CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);

		while (!Thread.interrupted() && isAlive) {
			cvSink.grabFrame(source);
			Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
			outputStream.putFrame(output);
		}
	}

	public void stop() {
		isAlive = false;
	}
}
