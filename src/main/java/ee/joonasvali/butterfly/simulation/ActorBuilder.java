package ee.joonasvali.butterfly.simulation;

import ee.joonasvali.butterfly.simulation.actor.Actor;

/**
 * @author Joonas Vali July 2016
 */
public class ActorBuilder implements Physical {

  private double x;
  private double y;
  private double rotation;
  private double rotationImpulse;
  private double xImpulse;
  private double yImpulse;
  private int diameter;
  private int health;
  private double speed;
  private String id;

  public ActorBuilder(Actor actor) {
    this.x = actor.getX();
    this.y = actor.getY();
    this.rotation = actor.getRotation();
    this.rotationImpulse = actor.getRotationImpulse();
    this.xImpulse = actor.getXImpulse();
    this.yImpulse = actor.getYImpulse();
    this.diameter = actor.getDiameter();
    this.health = actor.getHealth();
    this.speed = actor.getSpeed();
    this.id = actor.getId();
  }

  public Actor build() {
    return new Actor(id, x, y, diameter, rotation, xImpulse, yImpulse, rotationImpulse, health, speed);
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getRotation() {
    return rotation;
  }

  public void setRotation(double rotation) {
    this.rotation = rotation;
  }

  public double getRotationImpulse() {
    return rotationImpulse;
  }

  public void setRotationImpulse(double rotationImpulse) {
    this.rotationImpulse = rotationImpulse;
  }

  public double getXImpulse() {
    return xImpulse;
  }

  public void setXImpulse(double xImpulse) {
    this.xImpulse = xImpulse;
  }

  public double getYImpulse() {
    return yImpulse;
  }

  public void setYImpulse(double yImpulse) {
    this.yImpulse = yImpulse;
  }

  public int getDiameter() {
    return diameter;
  }

  public void setDiameter(int diameter) {
    this.diameter = diameter;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public double getSpeed() {
    return speed;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
