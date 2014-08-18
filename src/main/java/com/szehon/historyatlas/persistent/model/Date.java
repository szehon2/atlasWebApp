package com.szehon.historyatlas.persistent.model;

import java.text.DateFormatSymbols;

public class Date implements Comparable<Date> {
  public static int BEFORE = -100;  //before 700AD represented by year=700,month=-100.
  public static int AFTER = 100;  //after 700AD represented by year=700,month=100.
  public static int APPROX = -1;  //approx 700AD represented by year=700,month=-1.
  
  public int year;
  public int month;
  public int day;
  public boolean approximate;
  
  public Date(int year, int month, int day) {
    if ((month < 0) && (month > 12)) {
      throw new IllegalArgumentException("Illegal month: " + month);
    }
    if ((day < 0) && (day > 31)) {
      throw new IllegalArgumentException("Illegal day: " + day);
    }
    this.year = year;
    this.month = month;
    this.day = day;
  }
  
  public int compareTo(Date other) {
    if (this.year > other.year) {
      return 1;
    } else if (this.year < other.year) {
      return -1;
    }
    
    if (this.month > other.month) {
      return 1;
    } else if (this.month < other.month) {
      return -1;
    }
    
    if (this.day > other.day) {
      return 1;
    } else if (this.day < other.day) {
      return -1;
    }
    return 0;
  }
  
  public String toString() {
    String monthStr = "";
    String dayStr = "";
    if (month == AFTER) {
      monthStr = "After";
    } else if (month == BEFORE) {
      monthStr = "Before";
    } else if (month == APPROX) {
      monthStr = "Approx";
    } else if (month != 0) {
       monthStr = new DateFormatSymbols().getMonths()[month-1] + (" ");
    }
    if (day != 0) {
       dayStr = String.valueOf(day) + " ";
    }
    String era = "";  //no need for AD.
    if (year < 0) {
      era = " BC";
    }
    return monthStr + dayStr + String.valueOf(Math.abs(year)) + era;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + day;
    result = prime * result + month;
    result = prime * result + year;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Date other = (Date) obj;
    if (day != other.day)
      return false;
    if (month != other.month)
      return false;
    if (year != other.year)
      return false;
    return true;
  }
  
  
}
