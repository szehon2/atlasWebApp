package com.szehon.historyatlas.persistent.model;

public class GeoDescription {

  public TimePeriod period;
  public String desc;
  
  public GeoDescription(String desc, TimePeriod period) {
    this.desc = desc;
    this.period = period;
  }
  public GeoDescription(String desc) {
    this(desc, null);
  }
}
