import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.*;
import processing.video.*;
import org.opencv.videoio.*;
import org.opencv.core.*;
import org.opencv.imgproc.*;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import java.nio.ByteBuffer;
import sun.rmi.rmic.iiop.ClassPathLoader;

public class ProcessingTest extends PApplet {

  VideoCapture cap;
  Mat fm;

  CascadeClassifier face;
//  File faceFile = new File("haarcascade_frontalface_alt.xml");
//  ClassLoader classLoader = this.getClass().getClassLoader();

  public static String base = "/Users/jincheng/Desktop/INFO_PROJ/resources/";
  public static CascadeClassifier facebook;


  public void settings() {
    size(1280, 720);
  }


  public void setup() {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    facebook = new CascadeClassifier(base + "haarcascade_frontalface_default.xml");
    cap = new VideoCapture();
    cap.set(Videoio.CAP_PROP_FRAME_WIDTH, width);
    cap.set(Videoio.CAP_PROP_FRAME_HEIGHT, height);
    cap.open(Videoio.CAP_ANY);
    fm = new Mat();
    frameRate(30);
  }


  public void draw() {
    background(0);
    Mat tmp_mat = new Mat();
    cap.read(tmp_mat);

    Rect[] face_rec = getFace(tmp_mat);

    Imgproc.cvtColor(tmp_mat, fm, Imgproc.COLOR_BGR2RGBA);
    PImage img = matToImg(fm);
    image(img, 0, 0);

    // Draw rectangles for faces
    for (int i = 0; i < face_rec.length; i++) {
      rect(face_rec[i].x, face_rec[i].y, face_rec[i].width, face_rec[i].height);
    }

    tmp_mat.release();
  }


  public static void main(String[] args) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    PApplet.main("ProcessingTest");
  }

  public static Rect[] getFace(Mat image) {
    MatOfRect face = new MatOfRect();
    facebook.detectMultiScale(image, face);
    Rect[] rects = face.toArray();
//    for (int i = 0; i < rects.length; i++) {
//      Imgproc.rectangle(image, new Point(rects[i].x, rects[i].y),
//          new Point(rects[i].x + rects[i].width, rects[i].y + rects[i].height),
//          new Scalar(0, 255, 0));
//      Imgproc.putText(image, "Human", new Point(rects[i].x, rects[i].y),
//          Imgproc.COLOR_BayerBG2BGR_EA, 1.0,
//          new Scalar(0, 255, 0), 1, Imgproc.LINE_AA, false);
//    }
    return rects;
  }

  public BufferedImage Mat2BufferedImage(Mat m) {
    // Fastest code
    // output can be assigned either to a BufferedImage or to an Image

    int type = BufferedImage.TYPE_BYTE_GRAY;
    if (m.channels() > 1) {
      type = BufferedImage.TYPE_3BYTE_BGR;
    }
    int bufferSize = m.channels() * m.cols() * m.rows();
    byte[] b = new byte[bufferSize];
    m.get(0, 0, b); // get all the pixels
    BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
    final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
    System.arraycopy(b, 0, targetPixels, 0, b.length);
    return image;
  }


  public void displayImage(Image img2) {
    //BufferedImage img=ImageIO.read(new File("/HelloOpenCV/lena.png"));
    ImageIcon icon = new ImageIcon(img2);
    JFrame frame = new JFrame();
    frame.setLayout(new FlowLayout());
    frame.setSize(img2.getWidth(null) + 50, img2.getHeight(null) + 50);
    JLabel lbl = new JLabel();
    lbl.setIcon(icon);
    frame.add(lbl);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }


  public PImage matToImg(Mat m) {
    PImage im = createImage(m.cols(), m.rows(), ARGB);
    ByteBuffer b = ByteBuffer.allocate(m.rows() * m.cols() * m.channels());
    m.get(0, 0, b.array());
    b.rewind();
    b.asIntBuffer().get(im.pixels);
    im.updatePixels();
    return im;
  }
}