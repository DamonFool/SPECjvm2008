/*
 * Copyright (c) 2008 Standard Performance Evaluation Corporation (SPEC)
 *               All rights reserved.
 *
 * This source code is provided as is, without any express or implied warranty.
 */

package spec.benchmarks.scimark.fft;

import edu.uchicago.cs.heprofiler.HEProfiler;
import edu.uchicago.cs.heprofiler.HEProfilerEvent;
import edu.uchicago.cs.heprofiler.HEProfilerEventFactory;

import spec.benchmarks.scimark.utils.kernel;
import spec.harness.SpecJVMBenchmarkBase;
import spec.harness.results.BenchmarkResult;

public class Main extends SpecJVMBenchmarkBase {
    
    /**
     * Run this in multi mode, next to each other.
     */
    public static String testType() {
        return MULTI;
    }
    
    static void runBenchmark() {
        //return new FFT().inst_main(args);
        // Loop a few times, to create some more work in each ops.
        HEProfilerEvent event = HEProfilerEventFactory.createHEProfilerEvent(true);
        for (int i = kernel.FFT_LOOPS; i > 0; i --) {
            FFT.main(i);
            event.eventEndBegin(Profiler.FFT, i);
        }
        event.dispose();
    }
    
    public static void Main() {
        runBenchmark();
    }
    
    public void harnessMain() {
        runBenchmark();
    }
    
    public Main(BenchmarkResult bmResult, int threadId) {
        super(bmResult, threadId);
    }
    
    public static void setupBenchmark() {
        HEProfiler.init(Profiler.class, null, 20, Profiler.APPLICATION, 20, null);
        kernel.init();
    }

    public static void tearDownBenchmark() {
        HEProfiler.dispose();
    }
    
    public static void main(String[] args) throws Exception {
        runSimple( Main.class, args );
    }
}
