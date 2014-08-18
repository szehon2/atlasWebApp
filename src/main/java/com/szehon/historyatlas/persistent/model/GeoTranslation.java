package com.szehon.historyatlas.persistent.model;

public class GeoTranslation {
  String translation;
  String language;
  String transliteration;
  boolean isNative;
  
  
  public GeoTranslation(String translation, String language, boolean isNative) {
   this(translation, null, language, isNative);
  }
  
  public GeoTranslation(String translation, String transliteration, String language, boolean isNative) {
    super();
    this.translation = translation;
    this.language = language;
    this.isNative = isNative;
  }
}
