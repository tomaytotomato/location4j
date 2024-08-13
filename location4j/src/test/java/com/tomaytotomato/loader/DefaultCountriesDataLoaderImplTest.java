package com.tomaytotomato.loader;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultCountriesDataLoaderImplTest {

  /**
   * Verifies that the constructor throws a SecurityException if resource paths do not match.
   */
  @DisplayName("Constructor should throw SecurityException for mismatched resource paths")
  @Test
  void testSecurityExceptionThrownWhenFilePathIsInvalid() {

    // Given
    class TestCountriesDataLoader extends DefaultCountriesDataLoaderImpl {
      @Override
      protected URL getResource(String resource) {
        try {
          if (resource.equals("/location4j.bin")) {
            return new URI("file:///mock-path/location4j.bin").toURL();
          } else if (resource.equals("TestCountriesDataLoader.class")) {
            return new URI("file:///hacked/DefaultCountriesDataLoaderImpl.class").toURL();
          }
        } catch (MalformedURLException | URISyntaxException e) {
          throw new RuntimeException("Failed to create mock URL for resource: " + resource, e);
        }
        return null;
      }

      @Override
      protected InputStream getResourceAsStream(String resource) {
        return this.getClass().getResourceAsStream("/hacked-location4j.bin");
      }
    }

    // When Then
    assertThatThrownBy(TestCountriesDataLoader::new)
        .isInstanceOf(SecurityException.class)
        .hasMessageContaining("/location4j.bin is not in the same artifact as the loader: security issue");
  }
}
