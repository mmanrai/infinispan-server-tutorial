package org.infinispan.tutorial.db;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.tutorial.data.LocationWeather;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * This class connects to Infinispan and gets or creates two caches in the server
 * - Simple cache (String-Float)
 * - Queryable Cache (String, {@link LocationWeather}
 */
public class DataSourceConnector {
    private RemoteCacheManager remoteCacheManager;

    public DataSourceConnector() {

    }

    public DataSourceConnector(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }

    // STEP Implement the connection
    public void connect() {
        System.out.println("---- Connect to Infinispan ----");
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.uri("hotrod://admin:password@localhost:11222");

        URI temperatureCacheConfig;
        try {
            temperatureCacheConfig = getClass().getClassLoader().getResource("temperatureCacheConfig.xml").toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        builder.remoteCache("temperature").configurationURI(temperatureCacheConfig);
        remoteCacheManager = new RemoteCacheManager(builder.build());

    }

    public void health() {
        checkConnection();
        System.out.println("---- Connection count: " + remoteCacheManager.getConnectionCount() + " ----");
    }

    // STEP Creating the 'temperature' cache
    public RemoteCache<String, Float> getTemperatureCache() {
        System.out.println("---- Get or create the 'temperature' cache ----");
        checkConnection();

        return remoteCacheManager.getCache("temperature");
    }
    public RemoteCache<String, LocationWeather> getWeatherCache() {
        System.out.println("---- Get or create the 'weather' cache ----");
        checkConnection();

        // STEP Create the weather cache
        return null;
    }

    public void shutdown() {
        System.out.println("---- Shutdown ----");
        if(remoteCacheManager != null) {
            remoteCacheManager.stop();
        }
    }

    private void checkConnection() {
        Objects.requireNonNull(remoteCacheManager, "Implement 'connect' in the 'DataSourceConnector' class");
    }
}
