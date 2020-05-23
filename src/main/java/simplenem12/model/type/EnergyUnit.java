// Copyright Red Energy Limited 2017
package simplenem12.model.type;

import simplenem12.exception.InvalidEnergyUnitException;

/**
 * Represents the KWH energy unit in SimpleNem12
 */
public enum EnergyUnit {
  KWH;

  public static EnergyUnit lookup(String value) {
    try {
      return EnergyUnit.valueOf(value);
    } catch (IllegalArgumentException exception) {
      System.err.println("Invalid energy unit value - " + value);
      throw new InvalidEnergyUnitException("Invalid energy unit value - " + value);
    }
  }
}
