package com.szehon.historyatlas;


import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.szehon.historyatlas.persistent.model.GeoObjectType;

public interface ContentProvider {
  
  public void init(ServletConfig config) throws ServletException;
  
  List<GeoObjectType> getGeoObjectTypes();

}
