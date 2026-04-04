package simulator2.tree;

import java.util.Queue;
import java.util.LinkedList;

public class TreeMap<T1, T2> {

  public Tree<T2> map(Function<T1, T2> function, Tree<T1> tree) throws Exception {
    T2 newroot = function.apply(tree.root);
    Tree<T2> newtree = new Tree<>(newroot);
    Queue<T1> queue = new LinkedList<>();
    queue.add(tree.root);
    while(queue.isEmpty() == false) {
      T1 currentNode = queue.remove();
      for(T1 child : tree.getChildren(currentNode)) {
        newtree.addChild(function.apply(currentNode), function.apply(child));
        queue.add(child);
      }
    }
    return newtree;

  }
}
