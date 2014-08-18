package com.szehon.historyatlas;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.google.gson.reflect.TypeToken;
import com.szehon.historyatlas.persistent.model.GeoAlias;
import com.szehon.historyatlas.persistent.model.GeoObject;
import com.szehon.historyatlas.persistent.model.GeoObjectType;
import com.szehon.historyatlas.persistent.model.GeoReference;
import com.szehon.historyatlas.persistent.model.GeoUnit;
import com.szehon.historyatlas.persistent.model.Referrable;

/**
 * This class provides an in-memory data structure for First-class objects.
 *
 */
public class MemoryContentProvider2 implements ContentProvider {
  
  
  static MemoryContentProvider2 INSTANCE = new MemoryContentProvider2();
  
  /**
   * @return singleton instance.
   */
  public static MemoryContentProvider2 getInstance() {
    return INSTANCE;
  }
  
  
  Map<String, Map<String, Referrable>> classToMaps = new HashMap<String, Map<String, Referrable>>();
  
  
  

  
  @Override
  public List<GeoObjectType> getGeoObjectTypes() {
    List<GeoObjectType> result = new ArrayList<GeoObjectType>();
    Collection<Referrable> refResult = classToMaps.get(GeoObjectType.class.getName()).values();
    for (Referrable ref : refResult) {
      result.add((GeoObjectType) ref);
    }
    return result;
  }

  /**
   * Fetch a first-class object.
   * @param className object's class
   * @param id object's unique id
   * @return matching first-class object.
   */
  public Referrable getReferrable(String className, String id) {
    return getReferrable(className, id, classToMaps);
  }
  
  Referrable getReferrable(String className, String id, Map<String, Map<String, Referrable>> classToRefIdMap) {
    Map<String, Referrable> refMap = classToRefIdMap.get(className);
    if (refMap == null) {
      return null;
    }
    
    return refMap.get(id);
  }
  
  
  /**
   * Serializes the data structures to disk.
   * @throws Exception
   */
  public void serialize() throws Exception {
    //prepping, making them shallow for easier serialization.
//    for (String className : classToMaps.keySet()) {
//      Map<String, Referrable> refMap = classToMaps.get(className);
//      Collection<Referrable> refList = refMap.values();
//      for (Referrable ref : refList) {
//        ref.makeShallow();
//      }
//    }
    FileSerializer.writeToDisk(classToMaps);
  }

  
  public void deserialize() throws Exception {
    classToMaps.clear();
    
    //fill the maps in the first round.
    List<Referrable> result = FileSerializer.readFromDisk(stringToTypes);
    for (Referrable ref : result) {
      ref.init();
    }
    registerReferrables(result);
    
    //hydrate references in the second round.
    for (Referrable ref : result) {
      ref.hydrate();
    }
  }
  
  public void clear() {
    classToMaps.clear();
  }
  
  /**
   * Have to add the types manually.
   */
  Map<String, Type> stringToTypes = new HashMap<String, Type>();
  
  public MemoryContentProvider2() {

    
    
    stringToTypes.put(GeoUnit.class.getName(), 
        new TypeToken<ArrayList<GeoUnit>>() {}.getType());
  }

  public void updateData(List<Referrable> referrables) {
    validateData(referrables);
    registerReferrables(referrables);
    for (Referrable referrable : referrables) {
      referrable.hydrate();
    }
  }
  
  public void validateData(List<Referrable> referrables) {
    Map<String, Map<String, Referrable>> tempMap = new HashMap<String, Map<String, Referrable>>();
    for (Referrable ref : referrables) {
      String className = ref.getClassName();
      Map<String, Referrable> map = tempMap.get(className);
      if (map == null) {
        map = new HashMap<String, Referrable>();
      }
      map.put(ref.getUniqueId(), ref);
      tempMap.put(className, map);
    }
    
    for (Referrable ref : referrables) {
      Map<String, List<GeoReference>> forwardReferences = ref.getForwardReferences();
      for (List<GeoReference> refListOfRef : forwardReferences.values()) {
        for (GeoReference refOfRef : refListOfRef) {
          Referrable referrable1 = getReferrable(refOfRef.refClass, refOfRef.refId, tempMap);
          Referrable referrable2 = getReferrable(refOfRef.refClass, refOfRef.refId);
          if (referrable1 == null && referrable2 == null) {
            throw new RuntimeException("Referrable cannot be found : " + refOfRef.refClass + 
               ", " + refOfRef.refId);
          }
        }
      }
      
    }
  }
  
  public List<GeoObject> getGeoObjects(String type) {
    Map<String, Referrable> geoObjectTypes = classToMaps.get(GeoObjectType.class.getName());
    List<GeoObjectType> list = (List<GeoObjectType>) geoObjectTypes.get(type);
    if (list == null) {
      return null;
    }
    if (list.size() != 1) {
      throw new RuntimeException("Non 1 list size");
    }
    return list.get(0).getGeoObjects();
  }
  
  /**
   * Call after deserialization.
   */
  private void initGeoObjectTypes() {
    //types of GeoUnit children.
    Map<String, Referrable> geoObjectTypes = new HashMap<String, Referrable>();
    GeoObjectType geoUnitChildType = new GeoObjectType(GeoObjectType.GEOUNIT_CHILD_TYPE);
    geoUnitChildType.addGeoObject(new GeoObject("Ecclesiastical"));
    geoUnitChildType.addGeoObject(new GeoObject("Political"));
    geoUnitChildType.addGeoObject(new GeoObject("Corporate"));
    geoObjectTypes.put(geoUnitChildType.getName(), geoUnitChildType);
    
    GeoObjectType geoUnitType = new GeoObjectType(GeoObjectType.GEOUNIT_TYPE);
    geoUnitType.addGeoObject(new GeoObject("Kingdom"));
    geoObjectTypes.put(geoUnitType.getName(), geoUnitType);
    
    GeoObjectType geoUnitRulerType = new GeoObjectType(GeoObjectType.GEOUNIT_RULER_TYPE);
    GeoObject japanEmperor = new GeoObject("Emperor");
    japanEmperor.setTranslation("天皇", "Tenno", "Japanese", true);
    
    GeoAlias mikado = new GeoAlias("Mikado");
    mikado.setTranslation("帝", null, "Japanese", true);
    japanEmperor.addAlias(mikado);
    japanEmperor.setDescription("Emperor of Japan");
    geoUnitRulerType.addGeoObject(japanEmperor);
    
    geoObjectTypes.put(geoUnitRulerType.getName(), geoUnitRulerType);
    
    classToMaps.put(GeoObjectType.class.getName(), geoObjectTypes);
  }


  @Override
  public void init(ServletConfig config) throws ServletException {
    
    //TODO- deserialize
    initGeoObjectTypes();
  }

  /**
   * Put the referrables in the map.
   */
  private void registerReferrables(List referrables) {
    for (Object obj : referrables) {
      String className = obj.getClass().getCanonicalName();
      Map<String, Referrable> map = classToMaps.get(className);
      if (map == null) {
        map = new HashMap<String, Referrable>();
        classToMaps.put(className, map);
      }
      Referrable ref = (Referrable) obj;
      map.put(ref.getUniqueId(), ref);
    }
  }
}
