package com.db.awmd.challenge.service.impl.utility;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUtility {
  private AppUtility() {
  }

  public  static String getCurrentDate() {
    return  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
  }
}

// ~ Formatted by Jindent --- http://www.jindent.com
