# Orbix GPU Benchmark

Orbix GPU Benchmark is an application that evaluates the computational performance of a GPU. Unlike many other benchmarks which focus on rendering capabilities, Orbix targets raw computing power, providing a fast and efficient way to compare GPUs.

## Features

- **Data Transfer Benchmark**: Tests the GPU's ability to transfer large amounts of data from main memory to VRAM.
- **Trigonometry Benchmark**: Measures how many trigonometric functions the GPU can compute in a given period.
- **Matrix Multiplication Benchmark**: Assesses the parallel processing capability by multiplying increasingly large matrices.
- **Fractals Benchmark**: Tests how many iterations of Mandelbrot fractals the GPU can compute in a given period.
- **Standard Benchmark**: Combines the results of all the benchmarks to provide a standardized score.

## Motivation

Most existing GPU benchmarks focus on rendering, but Orbix was created to focus purely on computational performance. This benchmark gives a good approximation of a GPU's raw computing power and can serve as a fast alternative to lengthier benchmarks, like Unigine Heaven.

## Technologies Used

- **Java**: The entire project is written in Java, leveraging the *Aparapi* library to offload computations to the GPU.
- **OpenCL**: *Aparapi* converts Java bytecode into OpenCL, allowing Java code to run on the GPU.
- **MongoDB**: Stores the benchmarking results in a remote MongoDB cluster, with local fallback options.

## Installation

1. Ensure you have at least JDK 18 installed.

2. Download the .jar file corresponding to your OS.

3. Run the provided batch file (for Windows) or shell script (for Linux) to start the application.

## Usage

Once the application is started, you will be greeted by a user-friendly interface:

- **Start Benchmark**: Choose your GPU and benchmarking method, then click this button to run the test.
- **History**: View past benchmarking results either from the local CSV file or from the global history stored in MongoDB.
- **Visualize Fractals**: Enjoy exploring the colorful Mandelbrot fractals.

![UI](https://github.com/user-attachments/assets/23704339-c012-4957-a5be-c4c22c5aaf8f)

During the benchmark, the application will display progress and alerts, and once the benchmark is complete, results will be stored either locally or in the MongoDB database.

## Results

#### GPU Usage During Standard Benchmark (screenshot from Task Manager, running on an RTX 3060):

![GPU_usage](https://github.com/user-attachments/assets/6c4ffeb6-7732-4ced-94a2-e6254e865d1f)

We have benchmarked several GPUs, including:

- Nvidia Geforce RTX 3060 Laptop GPU
- Nvidia Geforce GTX 1650
- Radeon RX 6700 XT
- Radeon RX Vega 8
- Intel(R) HD Graphics 630
- Intel(R) UHD Graphics 620

The results provided by Orbix are consistent and comparable with other GPU benchmarks, such as Unigine Heaven, although Orbix completes its tests in significantly less time (~30 seconds).

#### Orbix Standard Benchmark Results:

![orbix_results](https://github.com/user-attachments/assets/f57aceb9-504d-4dc8-8aae-48a22782d1b6)

#### Unigine Heaven Benchmark Results:

![unigine_resutls](https://github.com/user-attachments/assets/eeada4e6-5481-4904-b145-ac779038a725)

### Interesting Findings

- **Integrated vs Dedicated GPUs**: Integrated GPUs outperform dedicated GPUs in the Data Transfer Benchmark due to their shared memory architecture.
- **Performance Scaling**: Faster GPUs require more runs to complete the benchmark, as the workload scales until 2 seconds of execution is reached.

## Limitations

- **Experimental Library**: The project relies on an experimental Java library (*Aparapi*) which may lead to unexpected errors on unsupported GPUs.
- **Biased Results**: Normalization is based on an RX Vega 8, which may introduce bias in the results.
## Acknowledgements

- [*Aparapi* Library](https://github.com/Syncleus/*Aparapi*-examples) for providing the framework to run Java code on GPUs.
