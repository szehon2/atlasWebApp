package com.szehon.historyatlas;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.szehon.historyatlas.persistent.model.Referrable;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class FileSerializer {
  //file-path variables.
  static final String filePath = "./geoUnits/";
  
  public static void writeToDisk(Map<String, Map<String, Referrable>> classToRefMap) throws Exception {
    String dirPath = filePath + getTimestamp();
    File dir = mkdir(dirPath);
    
    
//    for (String className : classToRefMap.keySet()) {
//      Map<String, Referrable> refMap = classToRefMap.get(className);
//      Collection<Referrable> refList = refMap.values();
//      
//      //write to disk.
//      Gson gson = new GsonBuilder().setPrettyPrinting().create();
//      String jsonOutput = gson.toJson(refList);
//      System.out.println(jsonOutput);
//      
//      File dest = getClassFile(dir, className);
//      dest.createNewFile();
//      writeToFile(dest, jsonOutput);
//    }
    
    for (String className : classToRefMap.keySet()) {
      Map<String, Referrable> refMap = classToRefMap.get(className);
      XStream xStream = new XStream(new DomDriver());
      xStream.setMode(XStream.ID_REFERENCES);
      String xml = xStream.toXML(refMap);
      System.out.println(xml);
    }
  }
  
  public static List<Referrable> readFromDisk(Map<String, Type> stringToTypes) throws Exception {
    
    List<Referrable> result = new ArrayList<Referrable>();
    verifyFile(filePath);
    
    File allDir = new File(filePath);
    File[] allFiles = allDir.listFiles();
    Arrays.sort(allFiles);
    File dir = allFiles[allFiles.length - 1];
    
    System.out.println("Reading from dir " + dir.getAbsolutePath());
    for (String className : stringToTypes.keySet()) {
      File f = getClassFile(dir, className);
      String contents = readFromFile(f);
      Type listType = stringToTypes.get(className);
      List referrables = new Gson().fromJson(contents, listType);
      result.addAll(referrables);
    }
    return result;    
  }
  
  private static String readFromFile(File file) throws Exception {
    byte[] encoded = Files.readAllBytes(Paths.get(file.getPath()));
    return new String(encoded, "UTF-8");
  }
    
    
  private static void writeToFile(File dest, String jsonString) throws Exception {
    try (PrintWriter writer = new PrintWriter(dest.getPath(), "UTF-8")) {
      writer.print(jsonString);
    }
  }

  private static String getTimestamp() {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ssSSS");
    return format1.format(cal.getTime());
  }
  
  public static void verifyFile(String dirPath) throws IOException {
    File dir = new File(dirPath);
    if (!dir.exists() || !dir.isDirectory()) {
      throw new IOException(dir.getAbsolutePath() + " doesn't exist");
    }
  }
  
  public static File mkdir(String dirPath) throws IOException {
    File dir = new File(dirPath);
    dir.mkdir();
    return dir;
  }
  
  private static File getClassFile(File dir, String className) {
    return new File(dir, className + ".json");
  }
}
