/* MIT License

Copyright (c) 2018 Neil Justice

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE. */

package com.github.neiljustice.louvain.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class FileLoader {

  private FileLoader() {
    // Disable instantiation of static helper class
  }

  public static List<String> readFile(String filename) {
    return readFile(filename, StandardCharsets.UTF_8);
  }

  public static List<String> readFile(String filename, Charset charset) {
    final List<String> list = new ArrayList<>();
    try (FileInputStream fis = new FileInputStream(filename);
         InputStreamReader isr = new InputStreamReader(fis, charset);
         BufferedReader reader = new BufferedReader(isr)) {
      String line;
      while ((line = reader.readLine()) != null) {
        list.add(line);
      }
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    return list;
  }

  public static void forEachLine(File file, Consumer<String> callback) {
    forEachLine(file, callback, StandardCharsets.UTF_8);
  }

  public static void forEachLine(String filename, Consumer<String> callback) {
    forEachLine(filename, callback, StandardCharsets.UTF_8);
  }

  public static void forEachLine(String filename, Consumer<String> callback, Charset charset) {
    forEachLine(new File(filename), callback, charset);
  }

  public static void forEachLine(File file, Consumer<String> callback, Charset charset) {
    try (FileInputStream fis = new FileInputStream(file);
         InputStreamReader isr = new InputStreamReader(fis, charset);
         BufferedReader reader = new BufferedReader(isr)) {
      String line;
      while ((line = reader.readLine()) != null) {
        callback.accept(line);
      }
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}