package com.szehon.historyatlas.persistent.model;

import java.util.ArrayList;
import java.util.List;

public class GeoObjectType extends Referrable {
  
  public static final String GEOUNIT_CHILD_TYPE = "Geounit_child_type";  
  public static final String GEOUNIT_TYPE = "Geounit_type";
  public static final String GEOUNIT_RULER_TYPE = "Geounit_ruler_type";
  
  
  List<GeoObject> geoObjects = new ArrayList<GeoObject>();
  public GeoObjectType(String typeName) {
    super(null, typeName);
  }
  
  public String getName() {
    return getUniqueId();
  }
  
  public void addGeoObject(GeoObject object) {
    geoObjects.add(object);
  }
  
  public List<GeoObject> getGeoObjects() {
    return geoObjects;
  }
}
