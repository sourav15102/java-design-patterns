/*
 * This project is licensed under the MIT license. Module model-view-viewmodel is using ZK framework licensed under LGPL (see lgpl-3.0.txt).
 *
 * The MIT License
 * Copyright © 2014-2022 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.iluwatar.intercepting.filter;

/**
 * Base class for order processing filters. Handles chain management.
 */
public abstract class AbstractFilter implements Filter {

  private Filter next;

  public AbstractFilter() {
  }

  public AbstractFilter(Filter next) {
    this.next = next;
  }

  @Override
  public void setNext(Filter filter) {
    this.next = filter;
  }

  @Override
  public Filter getNext() {
    return next;
  }

  @Override
  public Filter getLast() {
    Filter last = this;
    while (last.getNext() != null) {
      last = last.getNext();
    }
    return last;
  }


  /**
   * Helper method for execute.
   * @param order Order instance.
   * @return result string for execute.
   */
  public String getResult(Order order) {
    if (getNext() != null) {
      return getNext().execute(order);
    } else {
      return "";
    }
  }

  /**
   * Created a template 'execute method for all child classes'.
   * @param order Order instance.
   * @return String
   */
  @Override
  public String execute(Order order) {
    var result = this.getResult(order);
    var orderElement = this.getElement(order);
    if (this.getConditonal(orderElement)) {
      return result + this.getInvalidString(orderElement);
    } else {
      return result;
    }
  }

  /**
   * Helper functions for the template execute method.
   * @param order order instance.
   * @return String.
   */
  public abstract String getElement(Order order);

  /**
   * Helper functions for the template execute method.
   * @param element Element returned by execute method.
   * @return Boolean.
   */
  public abstract Boolean getConditonal(String element);

  /**
   * Helper functions for the template execute method.
   * @param element Element returned by execute method.
   * @return string.
   */
  public abstract String getInvalidString(String element);
}
