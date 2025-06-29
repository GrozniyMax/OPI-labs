package com.max.backend.metrics

import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Metrics
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicReference
import java.util.function.ToDoubleFunction
import kotlin.math.PI

@Component
class AreaMetric(){

    private val area: AtomicReference<Double> = AtomicReference(0.0)

    init {
        Metrics.globalRegistry.gauge("area", area, { it.get() })
    }


    private var r: Double = 1.0

    fun updateR(r: Double){
        this.r = r
        calculateArea()
    }


    fun calculateArea() {
        area.set(r*r + r * r /2  + PI*r*r/4)
    }

}
