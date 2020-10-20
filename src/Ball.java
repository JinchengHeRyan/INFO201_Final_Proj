public class Ball {

  private int Xposition;
  private int Yposition;
  private int Xspeed;
  private int Yspeed;
  private int gravity;

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

  public void setGravity(int gravity) {
    this.gravity = gravity;
  }

  public void updateGravity() {
    this.Yspeed += this.gravity;
  }
}
