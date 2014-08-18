package com.szehon.historyatlas.persistent.model;

public class GeoCouncilRef extends Referrable {
  
  public GeoCouncilRef(GeoPerson person, GeoUnit representation) {
    super(null, person.getUniqueId());
    setRef("Member", person);
    setRef("Representation", representation);
  }
}
