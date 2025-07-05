package org.infinispan.tutorial.services.temperature;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryCreated;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryExpired;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.event.ClientCacheEntryCreatedEvent;
import org.infinispan.client.hotrod.event.ClientCacheEntryExpiredEvent;
import org.infinispan.tutorial.db.DataSourceConnector;

import java.util.Objects;

public class TemperatureMonitor {
   private final RemoteCache<String, Float> cache;

   public TemperatureMonitor(DataSourceConnector dataSourceConnector) {
      cache = dataSourceConnector.getTemperatureCache();
   }

   public Float getTemperatureForLocation(String location) {
      Objects.requireNonNull(cache, "'temperature' cache is not correctly initialized. "
            + "Check DataSourceConnector - getTemperatureCache method");
      return cache.get(location);
   }

   @ClientListener
   public class TemperatureChangesListener {
      private String location;

      TemperatureChangesListener(String location) {
         this.location = location;
      }

      // STEP Implement a Client Listener sub-steps 1-2-3
      @ClientCacheEntryCreated
      public void created(ClientCacheEntryCreatedEvent event) {
         System.out.println("inside listener : " + event.getKey());
         if (event.getKey().equals(location)) {
            cache.getAsync(location)
                    .whenComplete((temperature, ex) -> {
                       System.out.printf("location %s Temperature %s\n", location, temperature);
            });
         }
      }

      @ClientCacheEntryExpired
      public void expired(ClientCacheEntryExpiredEvent event) {
         System.out.println("inside listener : " + event.getKey());
         if (event.getKey().equals(location)) {
            cache.getAsync(location)
                    .whenComplete((temperature, ex) -> {
                       System.out.printf("location %s Temperature %s\n", location, temperature);
            });
         }
      }

   }

   /**
    * Monitor the given location
    *
    * @param location
    */
   public void monitorLocation(String location) {
      System.out.println("---- Start monitoring temperature changes for " + location + " ----\n");
      TemperatureChangesListener temperatureChangesListener = new TemperatureChangesListener(location);

      // STEP Implement a Client Listener - sub-step 4
      cache.addClientListener(temperatureChangesListener);
      cache.addClientListener(new TempListener());
   }

}
