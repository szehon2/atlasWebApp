package com.szehon.historyatlas.persistent.model;


public class GeoAlias {
  public GeoName otherName;
  public TimePeriod period;
  public boolean isVisible = false;
  
  public GeoAlias(String otherNameString, TimePeriod period) {
    this.otherName = new GeoName(otherNameString);
  }
  
  public GeoAlias(GeoName otherName, TimePeriod period) {
    this.period = period;
    this.otherName = otherName;
  }
  
  public GeoAlias(String otherNameString) {
    this.otherName = new GeoName(otherNameString);
  }
  
  public GeoAlias(GeoName otherName) {
    this.otherName = otherName;
  }
  
  public void setTranslation(String translation, String transliteration, String language, boolean isNative) {
    otherName.setTranslation(new GeoTranslation(translation, transliteration, language, isNative));
  }
  
  public boolean isVisible() {
    return false;
  }
  
  public void setVisible(boolean isVisible) {
    this.isVisible = isVisible;
  }
}
