package simulator2.tree;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

public class Tree<T>  {
  public final T root;
  private Map <T, Set<T>> map = new HashMap<>();

  public Tree(T root) {
    this.root = root;
    this.map.put(root, new HashSet<T>());
  }

  public void addChild(T parent, T child) throws Exception {
    if(this.map.keySet().contains(child) == true) {
      throw new Exception("Tree.addChild: child already exists - " + child.toString());
    }
    if(this.map.keySet().contains(parent) == false) {
      this.map.put(parent, new HashSet<T>());
    }
    this.map.get(parent).add(child);
    this.map.put(child, new HashSet<T>());
  }

  public void addPath(List<T> path) throws Exception {
    if(this.root != path.get(0)) {
      throw new Exception("Tree.addPath: invalid path - incompatible with root.");
    }
    if(path.size() == 1) {
      return;
    }
    for(int i = 1; i < path.size(); i++) {
      if(this.hasNode(path.get(i)) == false) {
        for(int j = i; j < path.size(); j++) {
          this.addChild(path.get(j - 1), path.get(j));
        }
        return;
      }
      else {
      }
     }
  }

  public Set<T> getAllNodes() {
    return this.map.keySet();
  }

  public boolean hasNode(T node) {
    return this.getAllNodes().contains(node);
  }

  public Set<T> getChildren(T node) {
    return this.map.get(node);
  }

  public T getParent(T node) throws Exception {
    if(this.map.keySet().contains(node) == false) {
      throw new Exception("Tree.getParent: child doesn't exist - " + node.toString());
    }
    for(T parent : this.map.keySet()) {
      if(this.map.get(parent).contains(node)) {
        return parent;
      }
    }
    throw new Exception("Tree.getParent: unknown error; parent not found - " + node.toString());
  }

  public List<T> getAllAncestors(T node) throws Exception {
    if(node.equals(this.root)) {
      List<T> ancestors = new ArrayList<T>();
      ancestors.add(node);
      return ancestors;
    }
    T parent = this.getParent(node);
    List<T> ancestors = this.getAllAncestors(parent);
    ancestors.add(node);
    return ancestors;
  }

  //  All ancestors excluding ancestor.
  //  Example: If ancestors(n) = [root, n1, n2, ..., ni, n(i+1) ..., n]
  //  then ancestors(n, ni) = [n(i+1), ..., n]
  public List<T> getAllAncestorsUpto(T node, T ancestor) throws Exception {
    List<T> ancestors = this.getAllAncestors(node);
    while(ancestors.isEmpty() == false) {
      T anc = ancestors.remove(0);
      if(anc.equals(ancestor)) {
        return ancestors;
      }
    }
    throw new Exception("Tree::getAllAncestorsUpto: the given ancestor is " +
      "not an actual ancestor.");
  }

  public Set<T> getLeafNodes() {
    Set<T> leaves = new HashSet<>();
    for(T node : this.getAllNodes()) {
      if(this.getChildren(node).isEmpty()) {
        leaves.add(node);
      }
    }
    return leaves;
  }

  public T lub(T a, T b) throws Exception {
    List<T> a1 = this.getAllAncestors(a);
    List<T> a2 = this.getAllAncestors(b);
    T cca = null;
    while(a1.size() != 0 && a2.size() != 0) {
      if(a1.get(0).equals(a2.get(0))) {
        cca = a1.get(0);
        a1.remove(0);
        a2.remove(0);
      }
      else {
        break;
      }
    }
    return cca;
  }

  public T lub(Set<T> children) throws Exception {
    if(children.size() < 2) {
      throw new Exception("Tree.lub: children set has insufficient number of nodes.");
    }
    List<T> list = new ArrayList<>(children);
    T cca = list.get(0);
    for(int i = 1; i < list.size(); i++) {
      cca = this.lub(cca, list.get(i));
    }
    return cca;
  }

  public Tree<T> getSubtree(T node) throws Exception {
    Tree<T> tree = new Tree<>(node);
    Queue<T> queue = new LinkedList<T>();
    queue.add(node);
    while(queue.isEmpty() == false) {
      T currentNode = queue.remove();
      for(T child : this.getChildren(currentNode)) {
        tree.addChild(currentNode, child);
        queue.add(child);
      }
    }
    return tree;
  }

  public Tree<T> copy() throws Exception {
    TreeMap<T, T> map = new TreeMap<>();
    Function<T, T> function = new Function<>() {
      public T apply(T input) {
        return input;
      }
    };
    return map.map(function, this);
  }

  // Add 'tree' as a subtree of 'node'
  // The node set in 'tree' should be disjoint with the node set of this tree.
  public void addSubtree(T node, Tree<T> tree) throws Exception {
    Queue<T> queue = new LinkedList<T>();
    this.addChild(node, tree.root);
    queue.add(tree.root);
    while(queue.isEmpty() == false) {
      T currentNode = queue.remove();
      for(T child : tree.getChildren(currentNode)) {
        this.addChild(currentNode, child);
        queue.add(child);
      }
    }
  }

  public Tree<T> getSlicedSubtree(T node, Set<T> leaves) throws Exception {
    Tree<T> tree = this.getSubtree(node);
    Set<List<T>> allAncestors = new HashSet<>();
    for(T leaf : leaves) {
      allAncestors.add(tree.getAllAncestors(leaf));
    }
    Tree<T> slicedTree = new Tree<>(node);
    for(List<T> path : allAncestors) {
      slicedTree.addPath(path);
    }
    return slicedTree;
  }


  public String toString() {
    String s = "Tree ";
    for(T node : this.map.keySet()) {
      s += node.toString() + " : ";
      for(T child : this.map.get(node)) {
        s += child.toString() + ", ";
      }
      s += "\n";
    }
    return s;
  }
}
