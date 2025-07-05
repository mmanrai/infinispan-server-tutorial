package org.infinispan.tutorial.services.temperature;

import org.infinispan.client.hotrod.annotation.ClientCacheEntryCreated;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryModified;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.event.ClientCacheEntryCreatedEvent;
import org.infinispan.client.hotrod.event.ClientCacheEntryModifiedEvent;

@ClientListener(includeCurrentState = true)
public class TempListener {

    @ClientCacheEntryCreated
    public void handleCreatedEvent(ClientCacheEntryCreatedEvent<String> e) {
        System.out.println("*** TempListener - Entry created: " + e.getKey() + " ***");
    }

    @ClientCacheEntryModified
    public void handleModifiedEvent(ClientCacheEntryModifiedEvent<String> e) {
        System.out.println("*** TempListener - Entry modified: " + e.getKey() + " ***");
    }
}
