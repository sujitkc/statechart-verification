package testcases;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

import org.junit.*;

import simulator2.tree.*;

public class TestTree {

  @Test
  public void testTree1() {
    try {
      Tree<Integer> tree = new Tree<>(1);
      tree.addChild(1, 2);
      tree.addChild(1, 3);
      tree.addChild(3, 4);
      System.out.println(tree);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testAddPath1() {
  // Adding a path whose nodes are some there, but the path is longer than the existing path.
    try {
      Tree<Integer> tree = new Tree<>(1);
      tree.addChild(1, 2);
      tree.addChild(1, 3);
      tree.addChild(3, 4);
      List<Integer> path = new ArrayList<>();
      path.add(1); path.add(2); path.add(5); path.add(6);
      tree.addPath(path);
      System.out.println(tree);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testAddPath2() {
  // Adding a path whose nodes are all there, but the path is size 1.
    try {
      Tree<Integer> tree = new Tree<>(1);
      tree.addChild(1, 2);
      tree.addChild(1, 3);
      tree.addChild(3, 4);
      List<Integer> path = new ArrayList<>();
      path.add(1);
      tree.addPath(path);
      System.out.println(tree);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testAddPath3() {
  // Adding a path whose nodes are all there, but the path is longer than 1.
    try {
      Tree<Integer> tree = new Tree<>(1);
      tree.addChild(1, 2);
      tree.addChild(1, 3);
      tree.addChild(3, 4);
      List<Integer> path = new ArrayList<>();
      path.add(1); path.add(2);
      tree.addPath(path);
      System.out.println(tree);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testlub1() {
  // Adding a path whose nodes are all there, but the path is longer than 1.
    try {
      Tree<Integer> tree = new Tree<>(1);
      tree.addChild(1, 2);
      tree.addChild(1, 3);
      tree.addChild(3, 4);
      List<Integer> path = new ArrayList<>();
      path.add(1); path.add(2); path.add(5); path.add(6);
      tree.addPath(path);
      tree.addChild(2, 7);
      System.out.println(tree);
      System.out.println("lub(6, 7) = " + tree.lub(6, 7));
      System.out.println("lub(5, 7) = " + tree.lub(5, 7));
      System.out.println("lub(3, 4) = " + tree.lub(3, 4));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testlub2() {
  // Adding a path whose nodes are all there, but the path is longer than 1.
    try {
      Tree<Integer> tree = new Tree<>(1);
      tree.addChild(1, 2);
      tree.addChild(1, 3);
      tree.addChild(3, 4);
      List<Integer> path = new ArrayList<>();
      path.add(1); path.add(2); path.add(5); path.add(6);
      tree.addPath(path);
      tree.addChild(2, 7);
      System.out.println(tree);
      Integer[] a1 = {5, 6, 7};
      System.out.println("lub(6, 7, 5) = " + tree.lub(new HashSet<Integer>(Arrays.asList(a1))));
      Integer[] a2 = {3, 6, 7};
      System.out.println("lub(6, 7, 3) = " + tree.lub(new HashSet<Integer>(Arrays.asList(a2))));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void test_getSubTree_1() {
  // Adding a path whose nodes are all there, but the path is longer than 1.
    try {
      Tree<Integer> tree = new Tree<>(1);
      tree.addChild(1, 2);
      tree.addChild(1, 3);
      tree.addChild(3, 4);
      List<Integer> path = new ArrayList<>();
      path.add(1); path.add(2); path.add(5); path.add(6);
      tree.addPath(path);
      tree.addChild(2, 7);
      System.out.println(tree);
      
      System.out.println("subtree(2) = " + tree.getSubtree(2));
      System.out.println("subtree(1) = " + tree.getSubtree(1));
      System.out.println("subtree(6) = " + tree.getSubtree(6));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

}
