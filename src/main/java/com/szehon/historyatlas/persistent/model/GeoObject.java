package com.szehon.historyatlas.persistent.model;

import java.util.ArrayList;
import java.util.List;

public class GeoObject {
   public GeoName name;
   public String description;
   public List<GeoAlias> geoAliases = new ArrayList<GeoAlias>();
   
   public GeoObject(String nameString) {
     this.name = new GeoName(nameString);
   }
   
   public void setTranslation(String translation, String language, String transliteration, boolean isNative) {
     name.setTranslation(new GeoTranslation(translation, language, transliteration, isNative));
   }
   
   public void addAlias(GeoAlias alias) {
     geoAliases.add(alias);
   }
   
   public void setDescription(String description) {
     this.description = description;
   }
}
