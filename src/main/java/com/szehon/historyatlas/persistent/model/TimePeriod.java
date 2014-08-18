package com.szehon.historyatlas.persistent.model;



public class TimePeriod {
   public Date start; //never null
   public Date end;  //maybe null
   
   public TimePeriod(Date start, Date end) {
    this.start = start;
    this.end = end;
   }

   public TimePeriod(int beginY, int beginM, int beginD, int endY, int endM, int endD) {
     start = new Date(beginY, beginM, beginD);
     end = new Date(endY, endM, endD);
   }
   
   public TimePeriod(int beginY, int beginM, int beginD) {
     start = new Date(beginY, beginM, beginD);
   }
   
   public boolean containedIn(TimePeriod other) {
     int startComparison = start.compareTo(other.start);
     if ((end == null) && (other.end == null)) {
       return startComparison >= 0;
     }
     
     if ((end == null) && (other.end != null)) {
       //this is in the future.
       return false;
     }
     if ((end != null) && (other.end == null)) {
       return true;
     }
     
     
     int endComparison = end.compareTo(other.end);
     //if this period starts later, ends earlier than other.
     if ((startComparison >= 0) && (endComparison <= 0)) {
       return true;
     }
     return false;
   }
   
   public String toString() {
     if (end == null) {
       return start.toString();
     } else {
       return start.toString() + " to " + end.toString();
     }
   }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((end == null) ? 0 : end.hashCode());
    result = prime * result + ((start == null) ? 0 : start.hashCode());
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
    TimePeriod other = (TimePeriod) obj;
    if (end == null) {
      if (other.end != null)
        return false;
    } else if (!end.equals(other.end))
      return false;
    if (start == null) {
      if (other.start != null)
        return false;
    } else if (!start.equals(other.start))
      return false;
    return true;
  }   
}
