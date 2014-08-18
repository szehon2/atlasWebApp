package com.szehon.historyatlas.persistent.model;

import java.util.ArrayList;
import java.util.List;

public class GeoUnit extends Referrable {

  public GeoName name;
  public GeoObject type;
  
  public static final String CHILDREN = "geo_unit_children";
  public static final String CAPITAL = "geo_unit_capitals";
  public String wikiLink;
  public List <GeoDescription> descriptions = new ArrayList<GeoDescription>();
  public List <GeoAlias> aliases = new ArrayList<GeoAlias>();
  public String location;
  public boolean isSoverign = false;


  public GeoUnit(String name, GeoObject type, TimePeriod period) {
    this(name, type, name + "," + type, null, period);
  }
  
  public GeoUnit(String name, GeoObject type, String location, TimePeriod period) {
    this(name, type, name + "," + type, location, period);
  }
  
  public GeoUnit(String nameString, GeoObject type, String uniqueId, String location, TimePeriod period) {
    super(period, uniqueId);
    this.name = new GeoName(nameString);
    this.type = type;
    this.location = location;
  }
  
  public void addChild(GeoUnit unit) {
    setRef(CHILDREN, unit);
  }
  
  public void setTranslation(GeoTranslation translation) {
    this.name.translation = translation;
  }
  
  /**
   * Can add again, if different periods.
   * @param unit
   * @param period
   */
  public void addChild(GeoUnit childUnit, TimePeriod childPeriod) {
    setRef(CHILDREN, childUnit, childPeriod);
  }
  
  public void addCapital(GeoUnit unit) {
    setRef(CAPITAL, unit);
  }
  
  /**
   * Can add again, if different periods.
   * @param unit
   * @param period
   */
  public void addCapital(GeoUnit childUnit, TimePeriod childPeriod) {
    setRef(CAPITAL, childUnit, childPeriod);
  }

  public void addAlias(GeoAlias alias) {
    if (alias.period != null && period!= null && !alias.period.containedIn(period)) {
      throw new RuntimeException("Alias period : " + alias.period + 
          " not contained in " + toString() + " period " + period);
    }
    aliases.add(alias);
  }
  

  public void addDesc(GeoDescription desc) {
    if (desc.period != null && period!= null && !desc.period.containedIn(period)) {
      throw new RuntimeException("Desc period : " + desc.period + 
          " not contained in " + toString() + " period " + period);
    }
    descriptions.add(desc);
  }
  
  public void setIsSoverign(boolean isSoverign) {
    this.isSoverign = isSoverign;
  }
  
  public void addRuler(GeoObject office, GeoPerson person, TimePeriod period) {
    setRef("ruler", person, period, office, office);
  }
  
  public void addGeoCouncil(GeoObject councilOffice, GeoPerson person, TimePeriod period, GeoUnit location) {
    GeoCouncilRef gcr = new GeoCouncilRef(person, location);
    setRef("council", gcr, period, councilOffice, councilOffice);
  }
  
  public void addSuccessor(GeoObject fwd, GeoObject bak, GeoUnit successor) {
    setRef("successor", successor, null, fwd, bak);
  }

}
