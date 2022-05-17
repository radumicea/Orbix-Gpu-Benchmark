/**
 * Copyright (c) 2016 - 2018 Syncleus, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orbix.bench;

import com.aparapi.Kernel;

/**
 * Aparapi Fractals
 *
 * the kernel executes the math with complex numbers. Coordinates refer to
 * complex plane. result is a vector of number of iterations, It is transformed
 * in a color in the GUI, not here
 *
 * @author marco.stefanetti at gmail.com
 * @version $Id: $Id
 * @since 2.0.1
 */
public class MandelbrotKernel extends Kernel {

  /** the result of all calculations, the iterations for each pixel */
  private int[][] result;

  /** max iterations for pixel */
  private int max_iterations;

  /** starting point, x lower-left */
  private double cx1;
  /** starting point, y lower-left */
  private double cy1;

  /** max width */
  private int wmax;
  /** max height */
  private int hmax;

  /** one pixel width */
  private double wx;
  /** one pixel height */
  private double hy;

  public MandelbrotKernel(
    int max_iterations,
    double cx1,
    double cy1,
    double cx2,
    double cy2,
    int W,
    int H
  ) {
    result = new int[W][H];
    this.max_iterations = max_iterations;
    this.cx1 = cx1;
    this.cy1 = cy1;
    wmax = W;
    hmax = H;
    wx = (cx2 - cx1) / (double) wmax;
    hy = (cy2 - cy1) / (double) hmax;
  }

  /**
   * {@inheritDoc}
   *
   * just executes the "simple" math on a pixel
   */
  @Override
  public void run() {
    final int w = getGlobalId(0);
    final int h = getGlobalId(1);

    if ((w < wmax) && (h < hmax)) {
      /** from pixel to complex coordinates */
      final double cx = cx1 + w * wx;
      final double cy = cy1 + h * hy;

      double xn = cx;
      double yn = cy;

      double y2 = cy * cy;
      /** I don't save x2, x squared, It's slower **/

      int t = 0;

      /**
       * the original code gave a "goto" error in some platform while(
       * (++t<max_iterations) && (xn*xn+y2<4) )
       */

      for (t = 0; (t < max_iterations) && (xn * xn + y2 < 4); t++) {
        yn = 2d * xn * yn + cy;
        xn = xn * xn - y2 + cx;
        y2 = yn * yn;
      }

      result[w][h] = t;
    }
  }

  /**
   * <p>Getter for the field <code>result</code>.</p>
   *
   * @return an array of {@link int} objects.
   */
  public int[][] getResult() {
    return result;
  }
}
