package com.orbix.bench;

import com.aparapi.device.OpenCLDevice;
import com.aparapi.device.Device;
import com.aparapi.device.Device.TYPE;

public abstract class AbstractGPUBenchmark implements IBenchmark
{
    protected static final Device getGPU(String GPUName)
    {
        return OpenCLDevice.listDevices(TYPE.GPU).stream()
                           .filter(d -> d.getName().equals(GPUName))
                           .findFirst().get();
    }
}
