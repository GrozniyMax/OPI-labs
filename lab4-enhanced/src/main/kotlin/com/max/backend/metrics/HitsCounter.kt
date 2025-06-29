package com.max.backend.metrics

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
class HitsCounter(meterRegistry: MeterRegistry) {

    private val hitsCounter: Counter = Counter.builder("hitsCount")
        .description("Counter only for hits")
        .tags("category", "lab")
        .register(meterRegistry)

    private val totalCounter = Counter.builder("totalCount")
        .description("Counter for all")
        .tags("category", "lab")
        .register(meterRegistry)

    private val misses: AtomicInteger = AtomicInteger()

    private val missesCounter: Gauge = Gauge.builder("misses", misses, {ref -> ref.get().toDouble() })
        .description("Counts misses one by one")
        .tags("category", "lab")
        .register(meterRegistry)


    fun Counter.inc(): Counter {
        this.increment()
        return this
    }

    fun update(hit: Boolean) {
        totalCounter.inc()
        if (hit) {
            hitsCounter.inc()
            misses.set(0)
        } else {
            misses.incrementAndGet()
        }
    }

    fun dropMissesCounter() {
        misses.set(0)
    }
}
