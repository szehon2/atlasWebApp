package com.szehon.historyatlas.persistent.model;

public class GeoName {
  
  String name;
  GeoTranslation translation;
  public GeoName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }
  
  public GeoName(String name, GeoTranslation translation) {
    super();
    this.name = name;
    this.translation = translation;
  }
  
  public void setTranslation(GeoTranslation translation) {
    this.translation = translation;
  }
}
