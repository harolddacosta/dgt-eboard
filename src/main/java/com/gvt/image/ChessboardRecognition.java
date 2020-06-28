package com.gvt.image;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gvt.windows.MainWindows;

/**
 * Simple class that takes some input from the command line and in some cases will do some action against the DLL (show, hide, quit).
 * 
 * @author Jan
 */
public class ChessboardRecognition extends Thread {

	private static Logger logger = LoggerFactory.getLogger(ChessboardRecognition.class);

	// Reference to the DLL containing class. This should be done through an observer/observable
//	private DgtEBoard dgtEBoard;
//	private SnipIt imagePortionCoordinates;

	/**
	 * Default constructor
	 */
	public ChessboardRecognition() {
	}

//	public ChessboardRecognition(DgtEBoard dgtEBoard, SnipIt snipIt) {
//		this.dgtEBoard = dgtEBoard;
//		this.imagePortionCoordinates = snipIt;
//	}

	/**
	 * The thread run method.
	 */
	@Override
	public void run() {
		// shows the DLL window
		logger.debug("Hilo del reconocedor iniciado");

		startRecognition();
	}

	private void startRecognition() {
		try {
			Robot robot = new Robot();
			BufferedImage screenFullImage = robot.createScreenCapture(MainWindows.snipIt.getSelectedBounds());

			Mat imageSourceMat = BufferedImage2Mat(screenFullImage);
			Mat imageDestMat = new Mat();
			Mat imageBluredMat = new Mat();

			Imgproc.cvtColor(imageSourceMat, imageDestMat, Imgproc.COLOR_BGR2GRAY);
//			Imgproc.blur(imageDestMat, imageBluredMat, new Size(3, 3));
			Imgproc.Canny(imageDestMat, imageDestMat, 10, 100);

//			Mat dest = new Mat();
//			Core.add(dest, Scalar.all(0), dest);
//
//			imageSourceMat.copyTo(dest, imageDestMat);

			saveImage(imageDestMat);
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveImage(Mat imageBluredMat) throws IOException {
		File file = new File("C:\\Users\\hdacosta\\Development\\eclipse-workspace-jsf2\\dgt-protocol\\src\\test\\resources\\output.png");

		ImageIO.write(Mat2BufferedImage(imageBluredMat), "png", file);
	}

	private Mat BufferedImage2Mat(BufferedImage image) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "png", byteArrayOutputStream);
		byteArrayOutputStream.flush();

		return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
	}

	private BufferedImage Mat2BufferedImage(Mat matrix) {
		MatOfByte mob = new MatOfByte();

		// convert the matrix into a matrix of bytes appropriate for
		// this file extension
		Imgcodecs.imencode(".png", matrix, mob);

		// convert the "matrix of bytes" into a byte array
		byte[] byteArray = mob.toArray();
		BufferedImage bufImage = null;

		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bufImage;
	}
}
