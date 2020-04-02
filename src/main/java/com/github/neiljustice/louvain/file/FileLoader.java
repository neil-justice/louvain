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

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileLoader {

  public static void loadList(String in, Collection<String> coll) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(new File(in)));
      String line;

      while ((line = reader.readLine()) != null) {
        coll.add(line.toLowerCase());
      }
    } catch (FileNotFoundException e) {
      throw new Error("input file not found at " + in);
    } catch (IOException e) {
      throw new Error("IO error");
    }
  }

  public static void processFile(String in, String out, LineOperator op) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(new File(in)));
      BufferedWriter writer = new BufferedWriter(new FileWriter(new File(out)));
      String line;
      int cnt = 0;

      while ((line = reader.readLine()) != null) {
        cnt++;
        String outString = op.operate(line);
        writer.write(outString);
        writer.newLine();
        if (cnt % 100 == 0) {
          writer.flush();
        }
      }
      writer.flush();
    } catch (FileNotFoundException e) {
      throw new Error("input file not found at " + in);
    } catch (IOException e) {
      throw new Error("IO error");
    }
  }

  public static List<String> readFile(String in) {
    return readFile(in, null);
  }

  public static List<String> readFile(String in, LineReader r) {
    List<String> list = new ArrayList<String>();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(new File(in)));
      String line;
      while ((line = reader.readLine()) != null) {
        if (r != null) {
          r.read(line);
        }
        list.add(line);
      }
    } catch (FileNotFoundException e) {
      System.out.println("No file called " + in);
    } catch (IOException e) {
      throw new Error("IO error");
    }
    return list;
  }

  public interface LineOperator {
    String operate(String in);
  }

  public interface LineReader {
    void read(String in);
  }
}