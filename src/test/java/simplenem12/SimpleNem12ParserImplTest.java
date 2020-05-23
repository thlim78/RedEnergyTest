package simplenem12;

import org.junit.Test;
import simplenem12.exception.*;
import simplenem12.model.MeterRead;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Simple test harness for trying out SimpleNem12Parser implementation
 */
public class SimpleNem12ParserImplTest {

  @Test
  public void raiseInvalidFileContentExceptionIfRecordType100NotTheFirstLineInTheFile() {
    File fileRecordType100NotTheFirstLine = new File(getClass().getClassLoader().getResource("RecordType100NotTheFirstLine.csv").getFile());
    SimpleNem12Parser parser = new SimpleNem12ParserImpl();

    Exception exception = assertThrows(InvalidFileContentException.class, () -> {
      parser.parseSimpleNem12(fileRecordType100NotTheFirstLine);
    });

    String expectedMessage = "RecordType 100 must be the first line in the file";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void raiseInvalidFileContentExceptionIfRecordType900NotTheLastLineInTheFile() {
    File fileRecordType900NotTheLastLine = new File(getClass().getClassLoader().getResource("RecordType900NotTheLastLine.csv").getFile());
    SimpleNem12Parser parser = new SimpleNem12ParserImpl();

    Exception exception = assertThrows(InvalidFileContentException.class, () -> {
      parser.parseSimpleNem12(fileRecordType900NotTheLastLine);
    });

    String expectedMessage = "RecordType 900 must be the last line in the file";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void raiseInvalidFileContentExceptionIfRecordType200NotTheStartOfMeterReadBlock() {
    File fileRecordType200NotTheStartOfMeterReadBlock = new File(getClass().getClassLoader().getResource("RecordType200NotTheStartOfMeterReadBlock.csv").getFile());
    SimpleNem12Parser parser = new SimpleNem12ParserImpl();

    Exception exception = assertThrows(InvalidFileContentException.class, () -> {
      parser.parseSimpleNem12(fileRecordType200NotTheStartOfMeterReadBlock);
    });

    String expectedMessage = "RecordType 200 must be the start of meter read block";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void raiseInvalidNMIExceptionIfNMINot10CharactersLong() {
    File fileNMINot10CharactersLong = new File(getClass().getClassLoader().getResource("NMINot10CharactersLong.csv").getFile());
    SimpleNem12Parser parser = new SimpleNem12ParserImpl();

    Exception exception = assertThrows(InvalidNMIException.class, () -> {
      parser.parseSimpleNem12(fileNMINot10CharactersLong);
    });

    String expectedMessage = "NMI should always be 10 characters long";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void raiseInvalidEnergyUnitExceptionIfInvalidEnergyUnitEntered() {
      File fileInvalidEnergyUnitInput = new File(getClass().getClassLoader().getResource("InvalidEnergyUnitInput.csv").getFile());
      SimpleNem12Parser parser = new SimpleNem12ParserImpl();

      Exception exception = assertThrows(InvalidEnergyUnitException.class, () -> {
          parser.parseSimpleNem12(fileInvalidEnergyUnitInput);
      });

      String expectedMessage = "Invalid energy unit value - UNKNOWN";
      String actualMessage = exception.getMessage();

      assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void raiseInvalidDateFormatExceptionIfInvalidDateEntered() {
      File fileInvalidDateFormat = new File(getClass().getClassLoader().getResource("InvalidDateFormat.csv").getFile());
      SimpleNem12Parser parser = new SimpleNem12ParserImpl();

      Exception exception = assertThrows(InvalidDateFormatException.class, () -> {
          parser.parseSimpleNem12(fileInvalidDateFormat);
      });

      String expectedMessage = "Date must be in yyyyMMdd - 12212016";
      String actualMessage = exception.getMessage();

      assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void raiseInvalidQualityExceptionIfInvalidQualityEntered() {
      File fileInvalidQualityInput = new File(getClass().getClassLoader().getResource("InvalidQualityInput.csv").getFile());
      SimpleNem12Parser parser = new SimpleNem12ParserImpl();

      Exception exception = assertThrows(InvalidQualityException.class, () -> {
          parser.parseSimpleNem12(fileInvalidQualityInput);
      });

      String expectedMessage = "Invalid quality value - UNKNOWN";
      String actualMessage = exception.getMessage();

      assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void verifyTotalVolume() {
    File simpleNem12File = new File(getClass().getClassLoader().getResource("SimpleNem12.csv").getFile());

    try {
      // Uncomment below to try out test harness.
      Collection<MeterRead> meterReads = new SimpleNem12ParserImpl().parseSimpleNem12(simpleNem12File);

      BigDecimal expectedTotalVolume = new BigDecimal("-36.84");
      MeterRead read6123456789 = meterReads.stream().filter(mr -> mr.getNmi().equals("6123456789")).findFirst().get();
      assertEquals(expectedTotalVolume, read6123456789.getTotalVolume());

      expectedTotalVolume = new BigDecimal("14.33");
      MeterRead read6987654321 = meterReads.stream().filter(mr -> mr.getNmi().equals("6987654321")).findFirst().get();
      assertEquals(expectedTotalVolume, read6987654321.getTotalVolume());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
