package ee.joonasvali.butterfly.simulation;

import java.util.HashMap;

/**
 * Constant UID for physical things, to associate them with their own identities through time and space
 */
public class PhysicalUID {
  private final HashMap<Integer, PhysicalUID> derivatives = new HashMap<>();

  /**
   * Predictable way to get clones with identical PhysicalUIDs between tracks
   * @param seed abstract identifier for the clone. Will return existing, if seed was used before.
   * @return the PhysicalUID corresponding to the seed.
   */
  public PhysicalUID getDerivativeUID(int seed) {
    PhysicalUID child = derivatives.get(seed);
    if (child == null) {
      child = new PhysicalUID();
      derivatives.put(seed, child);
    }
    return child;
  }
}
