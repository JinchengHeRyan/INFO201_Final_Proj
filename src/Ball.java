public class Ball {

  private int Xposition;
  private int Yposition;
  private int radius;
  private int Xspeed;
  private int Yspeed;
  private int gravity;

  public Ball(int Xposition, int Yposition, int radius, int Xspeed, int Yspeed, int gravity) {
    this.Xposition = Xposition;
    this.Yposition = Yposition;
    this.radius = radius;
    this.Xspeed = Xspeed;
    this.Yspeed = Yspeed;
    this.gravity = gravity;
  }

  public int getXposition() {
    return Xposition;
  }

  public int getYposition() {
    return Yposition;
  }

  public void setXposition(int xposition) {
    Xposition = xposition;
  }

  public void setYposition(int yposition) {
    Yposition = yposition;
  }

  public int getXspeed() {
    return Xspeed;
  }

  public int getYspeed() {
    return Yspeed;
  }

  public void setXspeed(int xspeed) {
    Xspeed = xspeed;
  }

  public void setYspeed(int yspeed) {
    Yspeed = yspeed;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  public int getRadius() {
    return radius;
  }

  public void setGravity(int gravity) {
    this.gravity = gravity;
  }

  private void updateGravity() {
    this.Yspeed += this.gravity;
  }

  public void changeXdirection() {
    this.Xspeed *= -1;
  }

  public void changeYdirection() {
    this.Yspeed *= -1;
  }

  public void update() {
    this.updateGravity();
    this.setXposition(this.Xposition + this.Xspeed);
    this.setYposition(this.Yposition + this.Yspeed);
  }

  public boolean CheckCollideWithFace(Face face) {
    double distance = Math.sqrt(Math.pow(this.getXposition() - face.getXcenter(), 2) + Math
        .pow(this.getYposition() - face.getYcenter(), 2));
    return distance < this.getRadius() + face.getRadius();
  }
}
