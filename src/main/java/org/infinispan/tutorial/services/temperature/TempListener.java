package org.infinispan.tutorial.services.temperature;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryCreated;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryModified;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.event.ClientCacheEntryCreatedEvent;
import org.infinispan.client.hotrod.event.ClientCacheEntryModifiedEvent;

@ClientListener(includeCurrentState = true)
public class TempListener {

    private static final Logger logger = LogManager.getLogger(TempListener.class);

    @ClientCacheEntryCreated
    public void handleCreatedEvent(ClientCacheEntryCreatedEvent<String> e) {
        logger.info("Entry created: {}", e.getKey());
    }

    @ClientCacheEntryModified
    public void handleModifiedEvent(ClientCacheEntryModifiedEvent<String> e) {
        logger.info("Entry modified: {}", e.getKey());
    }
}
