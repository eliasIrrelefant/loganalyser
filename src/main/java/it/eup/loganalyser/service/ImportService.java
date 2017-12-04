package it.eup.loganalyser.service;

import it.eup.loganalyser.events.InterruptImportEvent;
import it.eup.loganalyser.exceptions.AuthenticationFailureException;
import it.eup.loganalyser.importfilter.Filterable;
import it.eup.loganalyser.logging.Logger;
import it.eup.loganalyser.model.StatisticsData;
import it.eup.loganalyser.reader.LogFileImporter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.zip.GZIPInputStream;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ImportService {

  @Autowired
  private LogFileImporter logFileImporter;

  @Autowired
  private Logger logger;

  public InputStream openUrlInputStream(final String urlString, final String username, final String password)
      throws FileNotFoundException, AuthenticationFailureException {
    try {
      CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

      if (StringUtils.isNotBlank(username) && StringUtils.isNoneBlank(password)) {
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
      }

      CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider)
          .disableAutomaticRetries().disableRedirectHandling().build();

      HttpGet request = new HttpGet(urlString);
      CloseableHttpResponse respone = httpClient.execute(request);

      int statusCode = respone.getStatusLine().getStatusCode();
      if (statusCode == 401) {
        throw new AuthenticationFailureException();
      }
      if (statusCode != 200) {
        throw new RuntimeException("Nicht erwarteter StatusCode. Erwartet: 200, Ist: " + statusCode);
      }

      InputStream inputStream = respone.getEntity().getContent();

      return inputStream;
    } catch (FileNotFoundException | AuthenticationFailureException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public InputStream openFileInputStream(String filename) {
    try {
      FileInputStream fileInputStream = new FileInputStream(filename);
      if (filename.endsWith(".gz")) {
        return new GZIPInputStream(fileInputStream);
      } else {
        return fileInputStream;
      }
    } catch (Exception e) {
      logger.log(e.getLocalizedMessage(), e);
      throw new RuntimeException(e);
    }
  }

  public StatisticsData importStream(final InputStream inputStream, final String origin, Filterable filter) {
    final LogFileImporter logFileImporter = this.logFileImporter;
    try {
      StatisticsData statistics = logFileImporter.process(inputStream, origin, filter);
      return statistics;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @EventListener
  public void handleInterruptEvent(
      InterruptImportEvent event) {
    logger.log("Breche Import ab...");
    logFileImporter.interrupt();
  }

  class InternalAuthenticator extends Authenticator {
    final String username;
    final String password;
    int count = 0;

    public InternalAuthenticator(String username, String password) {
      this.username = username;
      this.password = password;
    }

    @Override
    protected synchronized PasswordAuthentication getPasswordAuthentication() {
      if (count == 0) {
        count++;
        return new PasswordAuthentication(username, password.toCharArray());
      } else {
        throw new RuntimeException("Auth");
      }
    }
  }
}
