/*
 * Copyright (c) 2008 Standard Performance Evaluation Corporation (SPEC)
 *               All rights reserved.
 *
 * Copyright (c) 2007 Sun Microsystems, Inc. All rights reserved.
 *
 * This source code is provided as is, without any express or implied warranty.
 */

package spec.benchmarks.sunflow;

import edu.uchicago.cs.heprofiler.HEProfiler;
import edu.uchicago.cs.heprofiler.HEProfilerEvent;
import edu.uchicago.cs.heprofiler.HEProfilerEventFactory;

import org.sunflow.Benchmark;
import org.sunflow.system.UI;
import org.sunflow.system.UI.Module;
import org.sunflow.system.UI.PrintLevel;

import spec.harness.Constants;
import spec.harness.Context;
import spec.harness.Launch;
import spec.harness.SpecJVMBenchmarkBase;
import spec.harness.Util;
import spec.harness.results.BenchmarkResult;


public class Main extends SpecJVMBenchmarkBase {
    public static final int resolution = 128;
    private static Benchmark[] benchmarks;   
    
    /** Run this in single mode, multi threading is handled by the benchmark. */
    public static String testType() {
        return MULTI;
    }
    
    static class BenchmarkImpl extends Benchmark{
        public BenchmarkImpl(int resolution, boolean showOutput, boolean showBenchmarkOutput,
                boolean saveOutput, int threads) {
            super(resolution, showOutput, showBenchmarkOutput, saveOutput, threads);
        }
        
        public void print(Module m, PrintLevel level, String s) {
            if (!s.startsWith("Loading reference image from") && m == Module.BENCH) {
                Context.getOut().println(UI.formatOutput(m, level, s));
            }
            if (level == PrintLevel.ERROR)
                Context.getOut().println("ERROR...");
        }
    }
    
    Benchmark benchmark;
    
    public Main(BenchmarkResult bmResult, int threadId) {
        super(bmResult, threadId);        
        benchmark = benchmarks[threadId - 1];
    }
    
    public static void setupBenchmark() { 
        HEProfiler.init(Profiler.class, null, 20, Profiler.APPLICATION, 20, null);
    	int threads = Util.getIntProperty(Constants.SUNFLOW_THREADS, null);
    	int bmThreads = Launch.currentNumberBmThreads;
        benchmarks = new Benchmark[bmThreads];
        for (int i = 0; i < bmThreads; i ++) {        	
            benchmarks[i] = new BenchmarkImpl(resolution, false, true, false, threads);
            benchmarks[i].kernelBegin();
        }
    }

    public static void tearDownBenchmark() {
        HEProfiler.dispose();
    }
    
    public void harnessMain() {
        HEProfilerEvent event = HEProfilerEventFactory.createHEProfilerEvent(true);
        benchmark.kernelMain();
        event.eventEndBegin(Profiler.KERNEL_MAIN, 0);
        benchmark.kernelEnd();
        event.eventEnd(Profiler.KERNEL_END, 0, true);
    }
    
    public static void main(String[] args) throws Exception {
        runSimple(Main.class, args);
    }
}
