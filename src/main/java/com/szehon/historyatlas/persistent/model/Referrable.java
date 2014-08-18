package com.szehon.historyatlas.persistent.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.szehon.historyatlas.MemoryContentProvider2;

/**
 * Base class of a first-class object.
 * Contains generic forward and backward references to other first-class objects
 * and methods to auto-set and remove them.
 */
public class Referrable {
  
  TimePeriod period;
  String uniqueId;
  Map<String, List<GeoReference>> backRefs = new HashMap<String, List<GeoReference>>();
  Map<String, List<GeoReference>> forwardRefs = new HashMap<String, List<GeoReference>>();
  
  public Referrable(TimePeriod period, String uniqueId) {
    this.period = period;
    this.uniqueId = uniqueId;
  }

  /**
   * After de-serializing from file, data structs need initialization.
   */
  public void init() {
    if (backRefs == null) {
      backRefs = new HashMap<String, List<GeoReference>>();
    }
    if (forwardRefs == null) {
      forwardRefs = new HashMap<String, List<GeoReference>>();
    }
  }
  
  public String getClassName() {
    return this.getClass().getCanonicalName();
  }
  
  public String getUniqueId() {
    return uniqueId;
  }
  
  public List<GeoReference> getBackwardReference(String description) {
    return backRefs.get(description);
  }
  
  public List<GeoReference> getForwardReference(String description) {
    return forwardRefs.get(description);
  }
  
  public Map<String, List<GeoReference>> getForwardReferences() {
    return forwardRefs;
  }
  
  /**
   * Testing only.
   * @param desc
   * @param refId
   * @param refClass
   */
  public void setShallowRef(String desc, Referrable ref) {
    List<GeoReference> list = forwardRefs.get(desc);
    if (list == null) {
      list = new ArrayList<GeoReference>();
      forwardRefs.put(desc, list);
    }
    list.add(new GeoReference(desc, ref.getUniqueId(), ref.getClassName(), null));
  }
  
  void setRef(String desc, Referrable referrable) {
    setRef(desc, referrable, null);
  }

  void setRef(String desc, Referrable referrable, TimePeriod period) {
    validateChild(referrable, period);
    
    //forward ref
    GeoReference fowardRef = new GeoReference(desc, referrable, period);
    List<GeoReference> forwardList = forwardRefs.get(desc);
    if (forwardList == null) {
      forwardList = new ArrayList<GeoReference>();
    }
    forwardList.add(fowardRef);
    forwardRefs.put(desc, forwardList);
    
    //back ref
    GeoReference backRef = new GeoReference(desc, this, period);
    List<GeoReference> backList = referrable.backRefs.get(desc);
    if (backList == null) {
      backList = new ArrayList<GeoReference>();
    }
    backList.add(backRef);
    referrable.backRefs.put(desc, backList);
  }
  
  void setRef(String desc, Referrable referrable, TimePeriod period, GeoObject fwdAdvDesc, GeoObject backAdvDesc) {
    validateChild(referrable, period);
    
    //forward ref
    GeoReference forwardRef = new GeoReference(desc, referrable, period);
    forwardRef.setAdvancedDesc(fwdAdvDesc);
    List<GeoReference> forwardList = forwardRefs.get(desc);
    if (forwardList == null) {
      forwardList = new ArrayList<GeoReference>();
    }
    forwardList.add(forwardRef);
    forwardRefs.put(desc, forwardList);
    
    //back ref
    GeoReference backRef = new GeoReference(desc, this, period);
    backRef.setAdvancedDesc(backAdvDesc);
    List<GeoReference> backList = referrable.backRefs.get(desc);
    if (backList == null) {
      backList = new ArrayList<GeoReference>();
    }
    backList.add(backRef);
    referrable.backRefs.put(desc, backList);
  }

  private void validateChild(Referrable child, TimePeriod childPeriod) {
    if (childPeriod == null) {
      return;
    }
    if (childPeriod != null && !childPeriod.containedIn(period)) {
      throw new RuntimeException("Child period : " + childPeriod + 
        " not contained in " + toString() + " period " + period);
    }
    if (child.period != null && !childPeriod.containedIn(child.period)) {
      throw new RuntimeException("Child period : " + childPeriod + 
          " not contained in " + child + " period " + period);
    }
  }

  void removeRef(String desc, String refId) {
    //remove from forward
    List<GeoReference> forwardRefList = forwardRefs.get(desc);
    if (forwardRefs == null) {
      throw new RuntimeException(refId + " not found in forward ref of " + this.uniqueId);
    }
    GeoReference toRemove = null;
    for (GeoReference ref : forwardRefList) {
      if (ref.refId.equals(refId)) {
        toRemove = ref;
      }
    }
    if (toRemove == null) {
      throw new RuntimeException(refId + " not found in forward ref of " + this.uniqueId);
    }
    forwardRefList.remove(toRemove);
    
    //remove from backward
    removeBackRefFromChild(desc, toRemove);
  }

  private void removeBackRefFromChild(String desc, GeoReference fwdRef) {
    List<GeoReference> backRefList = fwdRef.referrable.backRefs.get(desc);
    if (backRefs == null) {
      throw new RuntimeException(this.uniqueId + " not found in backward ref of "+ 
        fwdRef.referrable.uniqueId);
    }
    GeoReference toRemove = null;
    for (GeoReference backRef : backRefList) {
      if (backRef.refId.equals(this.uniqueId)) {
         toRemove = backRef;
      }
    }
    if (toRemove == null) {
      throw new RuntimeException(this.uniqueId + " not found in backward ref of " + 
        fwdRef.referrable.uniqueId);
    }
    backRefList.remove(toRemove);
  }
  
  /**
   * Make all the fwd-references shallow, and eliminate back refs, in preparation for serialization.
   */
  public void makeShallow() {
    for (List<GeoReference> fwdRefList : forwardRefs.values()) {
      for (GeoReference fwdRef : fwdRefList) {
        fwdRef.makeShallow();
      }
    }
    backRefs = null;
  }
  
  
  /**
   * Hydrates this first-class object (fill in actual references by lookup from the existing data structures).
   * Pre-requisite is that they already exist.
   */
  public void hydrate() {
    Map<String, List<GeoReference>> temp = new HashMap<String, List<GeoReference>>();
    temp.putAll(forwardRefs);
    forwardRefs.clear();
    
    for (Entry<String, List<GeoReference>> fwdRefList : temp.entrySet()) {
      for (GeoReference fwdRef : fwdRefList.getValue()) {
        Referrable ref = MemoryContentProvider2.getInstance().getReferrable(
            fwdRef.refClass, fwdRef.refId);
        if (ref == null) {
          throw new RuntimeException("Corrupted map, no entry for: " + fwdRef.refId + 
            " of type: " + fwdRef.refClass);
        }
        setRef(fwdRefList.getKey(), ref);
      }
    }
  }
}
