import com.sun.tools.javac.util.ArrayUtils;
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

/**
 * This is the final project of INFO 201
 *
 * @author Jincheng He
 */
public class ProcessingTest extends PApplet {


  private final static int scene_width = 1280;
  private final static int scene_height = 720;

  VideoCapture cap;
  Mat fm;
  Ball ball = new Ball(700, 30, 70, 8, -2, 1);
  ArrayList<Face> faces_info = new ArrayList<>();

  CascadeClassifier face;
//  File faceFile = new File("haarcascade_frontalface_alt.xml");
//  ClassLoader classLoader = this.getClass().getClassLoader();

  public static String base = "/Users/jincheng/Desktop/INFO_PROJ/resources/";
  public static CascadeClassifier facebook;


  public void settings() {
    size(scene_width, scene_height);
  }


  public void setup() {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    facebook = new CascadeClassifier(base + "lbpcascade_frontalface_improved.xml");
    cap = new VideoCapture();
    cap.set(Videoio.CAP_PROP_FRAME_WIDTH, width);
    cap.set(Videoio.CAP_PROP_FRAME_HEIGHT, height);
    cap.open(Videoio.CAP_ANY);
    fm = new Mat();
    frameRate(60);
  }


  public void draw() {
    faces_info.clear();
    background(0);
    Mat tmp_mat = new Mat();
    cap.read(tmp_mat);

    Rect[] face_rec = getFace(tmp_mat);

    Imgproc.cvtColor(tmp_mat, fm, Imgproc.COLOR_BGR2RGBA);
    PImage img = matToImg(fm);
    image(img, 0, 0);

    // Draw rectangles for faces
    for (int i = 0; i < face_rec.length; i++) {
//      rect(face_rec[i].x, face_rec[i].y, face_rec[i].width, face_rec[i].height);
      faces_info.add(new Face(face_rec[i].x, face_rec[i].y, face_rec[i].width, face_rec[i].height));
    }

    drawFaceBalls(faces_info);

    tmp_mat.release();

    // Update Ball
    UpdateCollideWithFace(ball, faces_info);
    UpdateCollideWithWall(ball);
    ball.update();

    // Draw Ball
    fill(255);
    ellipse(ball.getXposition(), ball.getYposition(), ball.getRadius() * 2, ball.getRadius() * 2);
  }


  public static void main(String[] args) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    PApplet.main("ProcessingTest");
  }

  public void drawFaceBalls(ArrayList<Face> faces_info) {
    for (Face face : faces_info) {
      fill(255);
      ellipse(face.getXcenter(), face.getYcenter(), face.getRadius() * 2, face.getRadius() * 2);
    }
  }

  public static void UpdateCollideWithFace(Ball ball, ArrayList<Face> faces_info) {
    for (Face face : faces_info) {
      if (ball.CheckCollideWithFace(face)) {
        System.out.println("Collide");
        ball.changeXdirection();
        ball.changeYdirection();
      } else {
        System.out.println("Did not Collide");
      }
    }
  }

  public static void UpdateCollideWithWall(Ball ball) {
    if (!(ball.getRadius() < ball.getXposition() && ball.getXposition() < scene_width - ball
        .getRadius())) {
      ball.changeXdirection();
    }

    if (!(ball.getRadius() < ball.getYposition() && ball.getYposition() < scene_height - ball.getRadius())) {
      ball.changeYdirection();
    }
  }


  /**
   * Get Rects which store the information about the rectangle of detected faces
   *
   * @param image Mat from Opencv
   * @return return Rect[]
   */
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

  /**
   * Convert Mat from Opencv to BufferedImage
   *
   * @param m Mat from Opencv
   * @return return BufferedImage
   */
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


  /**
   * Display Image
   *
   * @param img2 Image which need to be displayed
   */
  public void displayImage(Image img2) {
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


  /**
   * Change Mat from Opencv to the PImage instance
   *
   * @param m Mat from Opencv
   * @return return PImage instance
   */
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