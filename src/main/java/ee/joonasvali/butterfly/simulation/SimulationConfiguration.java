package ee.joonasvali.butterfly.simulation;

/**
 * @author Joonas Vali October 2016
 */
public class SimulationConfiguration {
  private volatile boolean paintVision = false;
  private volatile boolean paintButterflyEffect = false;

  public boolean isPaintVision() {
    return paintVision;
  }

  public void setPaintVision(boolean paintVision) {
    this.paintVision = paintVision;
  }

  public boolean isPaintButterflyEffect() {
    return paintButterflyEffect;
  }

  public void setPaintButterflyEffect(boolean paintButterflyEffect) {
    this.paintButterflyEffect = paintButterflyEffect;
  }
}
