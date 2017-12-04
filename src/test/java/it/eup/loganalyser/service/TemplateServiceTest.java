package it.eup.loganalyser.service;

import org.junit.Assert;
import org.junit.Test;

public class TemplateServiceTest {

  TemplateService templateService = new TemplateService();

  @Test
  public void merge() {
    String templateString = "<#assign x = 1>${x}";
    Assert.assertEquals("1", templateService.merge(templateString));

  }
}
