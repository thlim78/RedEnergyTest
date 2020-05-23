// Copyright Red Energy Limited 2017

package simplenem12;

import simplenem12.exception.InvalidDateFormatException;
import simplenem12.exception.InvalidFileContentException;
import simplenem12.exception.InvalidNMIException;
import simplenem12.model.*;
import simplenem12.model.type.EnergyUnit;
import simplenem12.model.type.Quality;
import simplenem12.model.type.RecordType;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class SimpleNem12ParserImpl implements SimpleNem12Parser {

  /**
   * Parses Simple NEM12 file.
   * 
   * @param simpleNem12File file in Simple NEM12 format
   * @return Collection of <code>MString line;eterRead</code> that represents the data in the given file.
   */
  private static final String commarDelimiter = ",";
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

  public Collection<MeterRead> parseSimpleNem12(File simpleNem12File) throws IOException {

    List<String> stringList = Files.lines(Paths.get(simpleNem12File.getAbsolutePath()))
    .collect(Collectors.toList());

    validateRecordType100FirstLineInFile(stringList.get(0));
    validateRecordType900LastLineInFile(stringList.get(stringList.size()-1));

    List<MeterRead> meterReads = new ArrayList<MeterRead>();

    stringList.stream()
          .filter(RecordType::isBody)
          .forEach(record -> {
            String[] values = record.split(commarDelimiter);
            if (RecordType.isMeterReadBlock(values[0])) {
                String NMI = values[1];
                validateNMILength(NMI);
                EnergyUnit energyUnit = EnergyUnit.lookup(values[2]);
                meterReads.add(new MeterRead(NMI, energyUnit));
            } else if (RecordType.isVolumeOfMeterRead(values[0])) {
                validateRecordType200AtTheStartOfMeterReadBlock(meterReads);
                LocalDate localDate = parseStringToDate(values[1]);
                Quality quality = Quality.lookup(values[3]);
                MeterVolume meterVolume = new MeterVolume(new BigDecimal(values[2]), quality);
                MeterRead meterRead = meterReads.get(meterReads.size()-1);
                meterRead.appendVolume(localDate, meterVolume);
            }
          });

    return meterReads;
  }

  private void validateRecordType100FirstLineInFile(String firstLine) {
    if (!RecordType.isHeader(firstLine)) {
        System.err.println("RecordType 100 must be the first line in the file");
        throw new InvalidFileContentException("RecordType 100 must be the first line in the file");
    }
  }

  private void validateRecordType900LastLineInFile(String lastLine) {
    if (!RecordType.isFooter(lastLine)) {
        System.err.println("RecordType 900 must be the last line in the file");
        throw new InvalidFileContentException("RecordType 900 must be the last line in the file");
    }
  }

  private void validateRecordType200AtTheStartOfMeterReadBlock(List<MeterRead> meterReads) {
    if (meterReads.isEmpty()) {
        System.err.println("RecordType 200 must be the start of meter read block");
        throw new InvalidFileContentException("RecordType 200 must be the start of meter read block");
    }
  }

  private void validateNMILength(String NMI) {
    if (NMI.length() != 10) {
        System.err.println("NMI should always be 10 characters long");
        throw new InvalidNMIException("NMI should always be 10 characters long");
    }
  }

  private LocalDate parseStringToDate(String date) {
      try {
          return LocalDate.parse(date, formatter);
      } catch (DateTimeParseException exception) {
          throw new InvalidDateFormatException("Date must be in yyyyMMdd - " + date);
      }
  }

}
