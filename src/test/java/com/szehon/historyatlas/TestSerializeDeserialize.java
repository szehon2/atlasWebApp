package com.szehon.historyatlas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.szehon.historyatlas.persistent.model.GeoAlias;
import com.szehon.historyatlas.persistent.model.GeoDescription;
import com.szehon.historyatlas.persistent.model.GeoObject;
import com.szehon.historyatlas.persistent.model.GeoReference;
import com.szehon.historyatlas.persistent.model.GeoTranslation;
import com.szehon.historyatlas.persistent.model.GeoUnit;
import com.szehon.historyatlas.persistent.model.Referrable;
import com.szehon.historyatlas.persistent.model.TimePeriod;

public class TestSerializeDeserialize {
  
  @Test
  public void test() throws Exception {
    
    List<Referrable> testData = generateTestData();
    MemoryContentProvider2 mProvider = MemoryContentProvider2.getInstance();
    mProvider.updateData(testData);
    mProvider.serialize();
    mProvider.deserialize();
    
    validateTestData(generateTestData(), mProvider);

  }
  
  private static List<Referrable> generateTestData() {
    GeoObject city = new GeoObject("City");
    GeoObject department = new GeoObject("Department");
    GeoObject empire = new GeoObject("Empire");
    
    
    GeoUnit france = new GeoUnit("First French Empire", empire,
        new TimePeriod(1804, 5, 18, 1814, 4, 11));
    france.setIsSoverign(true);
    france.setTranslation(new GeoTranslation("Empire Français", "French", true));
    france.wikiLink = ("http://en.wikipedia.org/wiki/First_French_Empire");
    france.addDesc(new GeoDescription("This is the First French Empire of Napoleon I.  It was the"
        + "dominant power over most of contintental Europe during the Napoleonic wars"));
    
    GeoUnit ain = new GeoUnit("Ain", department, new TimePeriod(1790, 3, 4));
    ain.wikiLink = "http://en.wikipedia.org/wiki/Ain";
    ain.addDesc(new GeoDescription("In the 17th century sculpture, painting and literature prosper. "
        + "During the 18th century streets and small industries emerge. "));
    
    GeoUnit aisne = new GeoUnit("Aisne", department, new TimePeriod(1790, 3, 4));
    aisne.wikiLink = "http://en.wikipedia.org/wiki/Aisne";
    aisne.addDesc(new GeoDescription("It was created from parts of the former provinces of Île-de-France"
        + " (Laon, Soissons, Noyon,and Valois), Picardy (Thiérache Vermandois), and Champagne (Brie,and Omois)"));
  
    GeoUnit allier = new GeoUnit("Allier", department, new TimePeriod(1790, 3, 4));
    aisne.wikiLink = "http://en.wikipedia.org/wiki/Aisne";
    aisne.addDesc(new GeoDescription("It was created from parts of the former provinces of Auvergne and Bourbonnais."
        + "it is generally a sparsely populated department. Until the end of the 19th century."
        + "However, the population was increasing through the development of its cities accompanied "
        + "by the rural exodus."));
    
    france.setShallowRef(GeoUnit.CHILDREN, ain);
    france.setShallowRef(GeoUnit.CHILDREN, aisne);
    france.setShallowRef(GeoUnit.CHILDREN, allier);
    
    GeoUnit seine = new GeoUnit("Seine", department, new TimePeriod(1790, 3, 4, 1968, 1, 1));
    seine.wikiLink = "http://en.wikipedia.org/wiki/Seine_(department)";
    seine.addDesc(new GeoDescription("Seine was the official department number 75, seat of the French capital.  At the first French census in 1801, "
        + "the Seine department had 631,585 inhabitants (87% of them living in the city of Paris, "
        + "13% in the suburbs) and was the second most populous department of the vast Napoleonic Empire"
        + " (behind the Nord department), more populous than even the dense departments of what is now "
        + "Belgium and the Netherlands."));
    seine.addAlias(new GeoAlias("Paris", new TimePeriod(1790, 3, 4, 1795, 0, 0)));
    
    GeoUnit paris = new GeoUnit("Paris", city, "48.8567, 2.3508", new TimePeriod(-250, 0, 0));
    
    seine.setShallowRef(GeoUnit.CHILDREN, paris);
    seine.setShallowRef(GeoUnit.CAPITAL, paris);
    france.setShallowRef(GeoUnit.CAPITAL, paris);
    france.setShallowRef(GeoUnit.CHILDREN, seine);
    
    
    
    GeoUnit bourg = new GeoUnit("Bourg-en-Bresse", city, "46.2056, 5.2289", new TimePeriod(1250, 0, 0));

    GeoUnit oyonnax = new GeoUnit("Oyonnax", city, "46.2561, 5.6556", new TimePeriod(1601, 0, 0));

    GeoUnit amberiu = new GeoUnit("Ambérieu-en-Bugey", city, "45.9631, 5.3541", new TimePeriod(1250, 0, 0));
    
    ain.setShallowRef(GeoUnit.CAPITAL, bourg);
    ain.setShallowRef(GeoUnit.CHILDREN, bourg);
    ain.setShallowRef(GeoUnit.CHILDREN, oyonnax);
    ain.setShallowRef(GeoUnit.CHILDREN, amberiu);
    
    
    GeoUnit stquentin = new GeoUnit("Saint-Quentin", city, "49.8486, 3.2864", new TimePeriod(-30, 0, 0));
    
    GeoUnit laon = new GeoUnit("Laon", city, "49.564133, 3.61989", new TimePeriod(-30, 0, 0));
    laon.addAlias(new GeoAlias("Alaudanum", new TimePeriod(-30, 0, 0, 500, 0, 0)));
    
    GeoUnit soissons = new GeoUnit("Soissons", city, "49.3817, 3.3236", new TimePeriod(-30, 0, 0));
    
    GeoUnit chateau = new GeoUnit("Chateau-Thierry", city, "49.04, 3.4", new TimePeriod(-30, 0, 0));
    
    aisne.setShallowRef(GeoUnit.CHILDREN, stquentin);
    aisne.setShallowRef(GeoUnit.CHILDREN, laon);
    aisne.setShallowRef(GeoUnit.CAPITAL, laon);
    aisne.setShallowRef(GeoUnit.CHILDREN, soissons);
    aisne.setShallowRef(GeoUnit.CHILDREN, chateau);
    
    GeoUnit moulins = new GeoUnit("Moulins", city, "46.568059 , 3.334417", new TimePeriod(990, 0, 0));
    
    GeoUnit vichy = new GeoUnit("Vichy", city, "46.1278, 3.4267", new TimePeriod(-52, 0, 0));
    
    GeoUnit montlucon = new GeoUnit("Montluçon", city, "46.3408, 2.6033", new TimePeriod(1100, 0, 0));
    
    allier.setShallowRef(GeoUnit.CHILDREN, moulins);
    allier.setShallowRef(GeoUnit.CAPITAL, moulins);
    allier.setShallowRef(GeoUnit.CHILDREN, vichy);
    allier.setShallowRef(GeoUnit.CHILDREN, montlucon);

    
    List<Referrable> results = new ArrayList<Referrable>();
    results.add(france);
    results.add(ain);
    results.add(allier);
    results.add(aisne);
    results.add(seine);
    results.add(paris);
    results.add(stquentin);
    results.add(laon);
    results.add(soissons);
    results.add(chateau);
    results.add(bourg);
    results.add(amberiu);
    results.add(oyonnax);
    results.add(moulins);
    results.add(vichy);
    results.add(montlucon);
    return results;
  }
  
  
  public void validateTestData(Collection<Referrable> data, MemoryContentProvider2 provider) {
    
    for (Referrable geoUnit : data) {
      Referrable mapref = provider.getReferrable(geoUnit.getClassName(), geoUnit.getUniqueId());
      
      Map<String, List<GeoReference>> dataRefs = geoUnit.getForwardReferences();
      for (String dataRefDesc : dataRefs.keySet()) {
        List<GeoReference> dataRefList = dataRefs.get(dataRefDesc);
        List<GeoReference> mapRefList = mapref.getForwardReference(dataRefDesc);
        for (GeoReference dataRefObj : dataRefList) {
          boolean found = false;
          for (GeoReference mapRefObj : mapRefList) {
            Referrable finalMapRef = mapRefObj.getReferrable();
            if (dataRefObj.getRefClass().equals(finalMapRef.getClassName()) &&
                dataRefObj.getRefId().equals(finalMapRef.getUniqueId())) {
              found = true;
            }
          }
          if (!found) {
            Assert.fail("Not found");
          }
        } 
      }
    }
  }

}
