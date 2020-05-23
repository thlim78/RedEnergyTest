// Copyright Red Energy Limited 2017

package simplenem12.model.type;

import java.util.Arrays;
import java.util.List;

public enum RecordType {
  HEADER(Arrays.asList("100")),
  BODY(Arrays.asList("200","300")),
  FOOTER(Arrays.asList("900"));

  private List<String> values;

  RecordType (List<String> values) {
    this.values = values;
  }
  public static Boolean isHeader(String value) {
    return HEADER.values.contains(value);
  }
  public static Boolean isBody(String value) {
    return BODY.values.stream().anyMatch(type -> value.startsWith(type));
  }
  public static Boolean isMeterReadBlock(String value) {return BODY.values.get(0).equalsIgnoreCase(value); }
  public static Boolean isVolumeOfMeterRead(String value) {return BODY.values.get(1).equalsIgnoreCase(value); }
  public static Boolean isFooter(String value) {
    return FOOTER.values.contains(value);
  }
}
