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
    System.out.println("testTree1 ...");
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
  public void test_copy_1() {
    System.out.println("test_copy_1 ...");
    try {
      Tree<Integer> tree = new Tree<>(1);
      tree.addChild(1, 2);
      tree.addChild(1, 3);
      tree.addChild(3, 4);
      System.out.println("tree = " + tree);
      System.out.println("tree.copy = " + tree.copy());
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  @Test
  public void testAddPath1() {
  // Adding a path whose nodes are some there, but the path is longer than the existing path.
    System.out.println("testAddPath1 ...");
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
    System.out.println("testAddPath2 ...");
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
    System.out.println("testAddPath3 ...");
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
    System.out.println("test_lub1 ...");
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
    System.out.println("test_lub2 ...");
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
    System.out.println("test_getSubTree_1 ...");
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

  @Test
  public void test_getSlicedSubTree_1() {
    System.out.println("test_getSlicedSubTree_1 ...");
    try {
      Tree<Integer> tree = new Tree<>(1);
      tree.addChild(1, 2);
      tree.addChild(1, 3);
      tree.addChild(3, 4);
      tree.addChild(2, 5);
      tree.addChild(2, 7);
      tree.addChild(5, 6);
      tree.addChild(7, 8);
      tree.addChild(7, 9);
      tree.addChild(6, 10);
      tree.addChild(6, 11);
      tree.addChild(6, 12);
      System.out.println(tree);
      Integer[] a = {11, 12, 9};
      Set<Integer> leaves = new HashSet<Integer>(Arrays.asList(a));
      System.out.println("sliced tree (2, [11, 12, 9]) = " + tree.getSlicedSubtree(2, leaves));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void test_addSubTree_1() {
    System.out.println("test_addSubTree_1 ...");
    try {
      Tree<Integer> tree1 = new Tree<>(1);
      tree1.addChild(1, 2);
      tree1.addChild(1, 3);
      tree1.addChild(3, 4);
      tree1.addChild(2, 7);
      tree1.addChild(7, 8);
      tree1.addChild(7, 9);
      System.out.println("Tree1 = " + tree1);

      Tree<Integer> tree2 = new Tree<>(5);
      tree2.addChild(5, 6);
      tree2.addChild(6, 10);
      tree2.addChild(6, 11);
      tree2.addChild(6, 12);
      System.out.println("Tree2 = " + tree2);

      tree1.addSubtree(2, tree2);
      System.out.println("Modified tree2 = " + tree1);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


}
