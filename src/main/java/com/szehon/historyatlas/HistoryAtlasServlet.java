package com.szehon.historyatlas;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.szehon.historyatlas.persistent.model.GeoObjectType;

@SuppressWarnings("serial")
public class HistoryAtlasServlet extends HttpServlet {
  
  

  ContentProvider provider = new MemoryContentProvider2();

  @Override
  public void init(ServletConfig config) throws ServletException {
    provider.init(config);
  }

  
  
  @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
    String method = req.getParameter("method");
    String json = "";
    
    if (method.equals("getGeoTypes")) {
      List<GeoObjectType> geoObjectTypes = provider.getGeoObjectTypes();
      json = new Gson().toJson(geoObjectTypes);
    }
    
    
    

    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    resp.getWriter().write(json);
	  
	}
  

  

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    System.out.println("");
  }
}
