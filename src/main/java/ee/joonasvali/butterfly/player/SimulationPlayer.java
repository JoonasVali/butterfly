package ee.joonasvali.butterfly.player;

import ee.joonasvali.butterfly.simulation.SimulationContainer;
import ee.joonasvali.butterfly.simulation.SimulationState;
import ee.joonasvali.butterfly.ui.MouseListener;

/**
 * @author Joonas Vali July 2016
 */
public class SimulationPlayer {

  private final SimulationContainer[] tracks;
  private final int totalFrames;
  private final Clock clock;
  private int track;

  public SimulationPlayer(SimulationContainer[] containers, int totalFrames, Clock clock) {
    this.totalFrames = totalFrames;
    this.tracks = containers;
    this.clock = clock;
    this.track = 0;
  }

  public void setTrackPlayed(int track) {
    this.track = track;
  }

  public int getTrackPlayed() {
    return track;
  }

  public void calculateSimulation() {
    for (SimulationContainer track : tracks) {
      track.reset();
      for (int j = 0; j < totalFrames - 1; j++) {
        track.nextState();
      }
    }
  }

  public SimulationState getState() {
    return getContainer().getState(clock.getFrameIndex());
  }

  public SimulationState getState(int frame) {
    return getContainer().getState(frame);
  }

  public int getCurrentFrame() {
    return clock.getFrameIndex();
  }

  public void passTime(int timeMs) {
    clock.passTime(timeMs);
  }

  public int getTotalFrames() {
    return totalFrames;
  }

  public void setFrameIndex(int frameIndex) {
    clock.setFrameIndex(frameIndex);
  }

  private SimulationContainer getContainer() {
    return getContainer(track);
  }

  public SimulationContainer getContainer(int index) {
    return tracks[index];
  }

  public Clock getClock() {
    return clock;
  }
}
