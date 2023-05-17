package com.journal.cqrses.producers;

import com.journal.cqrses.events.BaseEvent;

public interface EventProducer {
    void produce(String topic, BaseEvent event);
}
