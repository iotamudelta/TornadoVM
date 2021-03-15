/*
 * This file is part of Tornado: A heterogeneous programming framework:
 * https://github.com/beehive-lab/tornadovm
 *
 * Copyright (c) 2020-2021, APT Group, Department of Computer Science,
 * The University of Manchester. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * GNU Classpath is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GNU Classpath; see the file COPYING.  If not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obligated to do so.  If you do not wish to do so, delete this
 * exception statement from your version.
 *
 */
package uk.ac.manchester.tornado.api;

/**
 * Context of TornadoVM execution to exploit kernel-parallel applications, in
 * which the parallelism is implicit.
 * 
 * The application can access thread-id for 1D, 2D and 3D dimensions.
 * Additionally, the application can access local memory (OpenCL terminology),
 * or shared memory (CUDA terminology) as well as synchronization primitives
 * such as barriers.
 * 
 * <p>
 * <ul>
 * <li>{@link TornadoVMContext} is an object exposed by the TornadoVM API in
 * order to leverage low-level programming features provided by heterogeneous
 * frameworks (e.g. OpenCL, CUDA) to the developers, such as thread-id, access
 * to local/shared memory and barriers.</li>
 * <li>{@link TornadoVMContext} provides a Java API that is transparently
 * translated to both OpenCL and PTX by the TornadoVM JIT compiler. The main
 * difference with the {@link TaskSchedule} API is that the tasks within a
 * {@link TaskSchedule} that use {@link TornadoVMContext} must be
 * {@link GridTask}.</li>
 * </ul>
 * </p>
 */
public class TornadoVMContext implements ExecutionContext {

    public final Integer threadIdx = 0;
    public final Integer threadIdy = 0;
    public final Integer threadIdz = 0;
    public final Integer groupIdx = 0;
    public final Integer groupIdy = 0;
    public final Integer groupIdz = 0;

    public final Integer localIdx = 0;
    public final Integer localIdy = 0;
    public final Integer localIdz = 0;

    private WorkerGrid grid;

    /**
     * Class constructor specifying a particular {@link WorkerGrid} object.
     */
    public TornadoVMContext(WorkerGrid grid) {
        this.grid = grid;
    }

    /**
     * Obtain the {@link WorkerGrid} that is used to indicate scheduling information
     * regarding the execution of the tasks withing a TaskSchedule. For example, the
     * dimensions, the global work size, the local work size.
     * 
     * @return {@link WorkerGrid} that is associated to TornadoVMContext
     */
    @Override
    public WorkerGrid getGrid() {
        return this.grid;
    }

    /**
     * Method used as a barrier to synchronize the order of memory operations to the
     * local memory (known as shared memory in PTX).
     * 
     * OpenCL equivalent: barrier(CLK_LOCAL_MEM_FENCE);
     * 
     * PTX equivalent: barrier.sync;
     */
    @Override
    public void localBarrier() {
    }

    /**
     * Method used as a barrier to synchronize the order of memory operations to the
     * global memory.
     * 
     * OpenCL equivalent: barrier(CLK_GLOBAL_MEM_FENCE);
     *
     * PTX equivalent: barrier.sync;
     */
    @Override
    public void globalBarrier() {

    }

    /**
     * It allocates a single dimensional array in local memory (known as shared
     * memory in PTX).
     *
     * @param size
     *            the size of the array
     * @return int[]: reference to the int array
     */
    @Override
    public int[] allocateIntLocalArray(int size) {
        return new int[size];
    }

    /**
     * It allocates a single dimensional array in local memory (known as shared
     * memory in PTX).
     *
     * @param size
     *            the size of the array
     * @return long[]: reference to the int array
     */
    @Override
    public long[] allocateLongLocalArray(int size) {
        return new long[size];
    }

    /**
     * It allocates a single dimensional array in local memory (known as shared
     * memory in PTX).
     *
     * @param size
     *            the size of the array
     * @return float[]: reference to the int array
     */
    @Override
    public float[] allocateFloatLocalArray(int size) {
        return new float[size];
    }

    /**
     * It allocates a single dimensional array in local memory (known as shared
     * memory in PTX).
     *
     * @param size
     *            the size of the array
     * @return double[]: reference to the int array
     */
    @Override
    public double[] allocateDoubleLocalArray(int size) {
        return new double[size];
    }

    /**
     * Dynamic dispatch - Not supported yet
     * 
     * @param f:
     *            {@link FunctionalInterface}
     * @param grid:
     *            {@WorkerGrid}
     */
    @Override
    public void launch(FunctionalInterface f, WorkerGrid grid) {
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * It returns the thread identifier for the first dimension.
     * 
     * OpenCL equivalent: get_global_id(0);
     * 
     * PTX equivalent: blockIdx * blockDim + threadIdx
     * 
     */
    public int getX() {
        return threadIdx;
    }

    /**
     * It returns the thread identifier for the second dimension.
     * 
     * OpenCL equivalent: get_global_id(1);
     *
     * PTX equivalent: blockIdy * blockDim + threadIdy
     *
     */
    public int getY() {
        return threadIdy;
    }

    /**
     * It returns the thread identifier for the third dimension.
     * 
     * OpenCL equivalent: get_global_id(2);
     *
     * PTX equivalent: blockIdz * blockDim + threadIdz
     *
     */
    public int getZ() {
        return threadIdz;
    }

    /**
     * It returns the local group size of the associated WorkerGrid for a particular
     * dimension.
     * 
     * OpenCL equivalent: get_local_size();
     *
     * PTX equivalent: blockDim
     * 
     */
    public int getLocalGroupSize(int dim) {
        return (int) grid.getLocalWork()[dim];
    }

    /**
     * It returns the global group size of the associated WorkerGrid for a
     * particular dimension.
     * 
     * OpenCL equivalent: get_global_size();
     *
     * PTX equivalent: gridDim * blockDim
     * 
     */
    public int getGlobalGroupSize(int dim) {
        return (int) grid.getGlobalWork()[dim];
    }
}