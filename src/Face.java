public class Face {

  private int Width;
  private int Height;
  private int Xcenter;
  private int Ycenter;
  private int radius;

  public Face(int X_start, int Y_start, int width, int height) {
    this.Xcenter = X_start + width / 2;
    this.Ycenter = Y_start + height / 2;
    this.radius = (width + height) / 2;
    this.Width = width;
    this.Height = height;
  }

  public int getXcenter() {
    return Xcenter;
  }

  public int getYcenter() {
    return Ycenter;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  public int getWidth() {
    return Width;
  }

  public int getHeight() {
    return Height;
  }
}
