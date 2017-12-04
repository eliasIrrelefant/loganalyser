package it.eup.loganalyser.service;

import static org.junit.Assert.assertNotNull;

import it.eup.loganalyser.model.ConfigModel;
import it.eup.loganalyser.model.QueryModel;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.yaml.snakeyaml.Yaml;

@RunWith(MockitoJUnitRunner.class)
public class ConfigServiceTest {

  @InjectMocks
  ConfigService configService;

  @Test
  public void testSimpleYml() throws Exception {
    String yml = "---\n\nlogformat: \"%l %u %t %r\"\n";

    ByteArrayInputStream inputStream = new ByteArrayInputStream(yml.getBytes("UTF-8"));

    ConfigModel configModel = configService.readYml(inputStream);

    assertNotNull(configModel);
    Assert.assertEquals("%l %u %t %r", configModel.getLogformat());
  }

  @Test
  public void dumpConfigYml() throws Exception {
    QueryModel queryModel = new QueryModel();
    queryModel.setName("Test Query");
    queryModel.setQuery("Select *\n\tfrom dummy");

    ConfigModel model = new ConfigModel();
    model.setLogformat("%l %s \"%r\"");
    model.getQueries().add(queryModel);
    Yaml yaml = configService.getYaml();

    String dump = yaml.dumpAsMap(model);

    Assert.assertFalse(dump.contains("comment: null"));
  }

  @Test
  public void deploymentConfigCanBeParsed() throws Exception {
    InputStream inputStream = getClass().getResourceAsStream("/config/config.yml");

    ConfigModel configModel = configService.readYml(inputStream);

    assertNotNull(configModel);
    Assert.assertTrue(configModel.getLogformat().startsWith("%{X-Forwarded-For}i %l %u %t \"%r\" %>s"));
    Assert.assertEquals(4, configModel.getQueries().size());
  }

}
