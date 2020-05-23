// Copyright Red Energy Limited 2017

package simplenem12.model.type;

import simplenem12.exception.InvalidQualityException;

/**
 * Represents meter read quality in SimpleNem12
 */
public enum Quality {

  A,
  E;

  public static Quality lookup(String value) {
    try {
      return Quality.valueOf(value);
    } catch (IllegalArgumentException exception) {
      System.err.println("Invalid quality value - " + value);
      throw new InvalidQualityException("Invalid quality value - " + value);
    }
  }
}
