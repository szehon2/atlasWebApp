package com.szehon.historyatlas.persistent.model;




/**
 * Represents a relationship between two references.
 */
public class GeoReference {
  public String refClass;  //class of referrable.
  public String refType;  //type of relationship, a static constant.
  public String refId;   //unique id of referrable
  public Referrable referrable;  //hydrated reference (once looked up from refType, refId)
  public TimePeriod period;  //period that the reference holds.
  public GeoObject advancedDesc;

  public GeoReference(String refType, Referrable referrable, TimePeriod period) {
    super();
    this.refType = refType;
    this.referrable = referrable;
    this.period = period;
    this.refClass = referrable.getClassName();
  }
  
  public GeoReference(String refType, String refId, String refClass, TimePeriod period) {
    this.refType = refType;
    this.refId = refId;
    this.refClass = refClass;
    this.period = period;
  }
  
  void makeShallow() {
    this.refId = referrable.getUniqueId();
    referrable = null;
  }
  
  public Referrable getReferrable() {
    return referrable;
  }
  
  public String getRefClass() {
    return refClass;
  }
  
  public String getRefId() {
    return refId;
  }
  
  public void setAdvancedDesc(GeoObject advancedDesc) {
    this.advancedDesc = advancedDesc;
  }
  
}
