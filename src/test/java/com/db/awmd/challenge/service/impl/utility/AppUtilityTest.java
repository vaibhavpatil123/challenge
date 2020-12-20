package com.db.awmd.challenge.service.impl.utility;

import org.junit.Test;

import static org.junit.Assert.*;

public class AppUtilityTest {
  @Test
  public void test_getCurrentDate() {
      String value=AppUtility.getCurrentDate();
      assertNotNull(value);
  }
}