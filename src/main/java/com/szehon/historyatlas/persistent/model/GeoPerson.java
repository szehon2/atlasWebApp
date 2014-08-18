package com.szehon.historyatlas.persistent.model;

public class GeoPerson extends Referrable {
  GeoName name;
  
  public GeoPerson(String nameString, TimePeriod period) {
    super(period, 
        nameString + period.toString());  //id
    this.name = new GeoName(nameString);
  }
  
  public void addTranslation(String translation, String language, boolean isNative) {
    this.name.setTranslation(new GeoTranslation(translation, language, isNative));
  }

}
