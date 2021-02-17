package util;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.HashMap;

public class Tree<T> implements Iterable<T> {
    public Node
        root;

    public Tree() {
        this.root = null;
    }

    public Tree(T datum) {
        this.root = new Node(datum);
    }

    public void setRoot(Node node) {
        this.root = node;
    }

    public <S> Tree<S> transform(BiFunction<Tree<T>.Node, S, S> transformationHelper) {
        Tree<S>
            tree = new Tree<>();
        HashMap<Tree<T>.Node, Tree<S>.Node>
            map = new HashMap<>();
        if (this.root != null) {
            Tree<S>.Node
                sRoot = tree.new Node(transformationHelper.apply(this.root, null));
            map.put(this.root, sRoot);
            tree.setRoot(sRoot);
            this.iterationHelperSkipRoot((Tree<T>.Node tNode) -> {
                Tree<S>.Node
                    sNodeParent = map.get(tNode.parent),
                    sNode = tree.new Node(sNodeParent, transformationHelper.apply(tNode, sNodeParent.datum));
                map.put(tNode, sNode);
            });
        }
        return tree;
    }

    public <S> Tree<S> transform(Function<Tree<T>.Node, S> transformationHelper) {
        return this.transform((Tree<T>.Node node, S parentDatum) -> transformationHelper.apply(node));
    }
    
    @Override
    public Iterator<T> iterator() {
        ArrayList<T>
            temp = new ArrayList<T>();
        if (this.root != null) {
            this.iterationHelper(this.root, (Node n) -> temp.add(n.datum));
        }
        return temp.iterator();
    }

    public void iterationHelper(Node root, Consumer<Node> helper) {
        helper.accept(root);
        for (Node child : root.children) {
            iterationHelper(child, helper);
        }
    }

    public <S> void iterationHelper(Node root, BiConsumer<Node, S> helper, S s, Function<S, S> sTransformer) {
        helper.accept(root, s);
        for (Node child : root.children) {
            iterationHelper(child, helper, sTransformer.apply(s), sTransformer);
        }
    }

    public void iterationHelperSkipRoot(Consumer<Node> helper) {
        for (Node rootChild : this.root.children) {
            iterationHelper(rootChild, helper);
        }
    }

    public <S> void iterationHelperSkipRoot(BiConsumer<Node, S> helper, S s, Function<S, S> sTransformer) {
        for (Node child : this.root.children) {
            iterationHelper(child, helper, sTransformer.apply(s), sTransformer);
        }
    }

    public Iterator<T> breadthFirstIterator() {
        Stack<Node>
            back = new Stack<Node>();
        ArrayList<T>
            temp = new ArrayList<T>();
        if (this.root != null) {
            back.add(this.root);
            while (back.size() > 0) {
                Node
                    firstVisited = back.pop();
                temp.add(firstVisited.datum);
                back.addAll(firstVisited.children);
            }
        }
        return temp.iterator();
    }

    public class Node {
        public T
            datum;
        public ArrayList<Node>
            children;
        public final Node
            parent;

        private Node(T datum) {
            this.datum = datum;
            this.children = new ArrayList<>();
            this.parent = null;
        }

        public Node(Node parent, T datum) {
            parent.children.add(this);
            this.datum = datum;
            this.children = new ArrayList<>();
            this.parent = parent;
        }
    }
}
