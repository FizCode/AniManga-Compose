package dev.fizcode.mediadetails

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Test

class MemoryBenchmark {

    private val iterations = 100_000

    @Test
    fun compareMemoryUsage() {
        println("=== Memory Benchmark (Iterations: $iterations) ===")
        
        // Measure Channel
        val channelMemory = measureMemory {
            List(iterations) { Channel<String>(capacity = 1) }
        }
        println("Channel: ${String.format("%.2f", channelMemory / iterations.toDouble())} bytes/object")

        // Measure SharedFlow
        val sharedFlowMemory = measureMemory {
            List(iterations) { MutableSharedFlow<String>() }
        }
        println("SharedFlow: ${String.format("%.2f", sharedFlowMemory / iterations.toDouble())} bytes/object")
        
        println("==============================================")
    }

    private fun measureMemory(block: () -> Any): Long {
        val runtime = Runtime.getRuntime()
        
        // Trigger GC to clear previous state
        repeat(3) { 
            System.gc()
            Thread.sleep(100)
        }
        
        val before = runtime.totalMemory() - runtime.freeMemory()
        val result = block() // Hold reference to prevent GC
        
        // Trigger GC again to ensure we are measuring current state
        System.gc()
        
        val after = runtime.totalMemory() - runtime.freeMemory()
        
        // Keep result alive during measurement
        val hash = result.hashCode()
        
        return after - before
    }
}
