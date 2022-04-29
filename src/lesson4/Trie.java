package lesson4;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    private static class Node {
        SortedMap<Character, Node> children = new TreeMap<>();
    }

    private final Node root = new Node();

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    private String withZero(String initial) {
        return initial + (char) 0;
    }

    @Nullable
    private Node findNode(String element) {
        Node current = root;
        for (char character : element.toCharArray()) {
            if (current == null) return null;
            current = current.children.get(character);
        }
        return current;
    }

    @Override
    public boolean contains(Object o) {
        String element = (String) o;
        return findNode(withZero(element)) != null;
    }

    @Override
    public boolean add(String element) {
        Node current = root;
        boolean modified = false;
        for (char character : withZero(element).toCharArray()) {
            Node child = current.children.get(character);
            if (child != null) {
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                current.children.put(character, newChild);
                current = newChild;
            }
        }
        if (modified) {
            size++;
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        String element = (String) o;
        Node current = findNode(element);
        if (current == null) return false;
        if (current.children.remove((char) 0) != null) {
            size--;
            return true;
        }
        return false;
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new TrieIterator(root);
    }

    public class TrieIterator implements  Iterator<String> {
        Stack<String> stack = new Stack<>();
        String currentString;

        private TrieIterator(Node root) {
            pushToStack(root, "");
        }

        @Override
        public boolean hasNext() {
            return !stack.empty();
        }
        // Трудоёмкость - O(1)
        // Ресурсоёмкость - O(1)

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            currentString = stack.pop();
            return currentString;
        }
        // Трудоёмкость - O(1)
        // Ресурсоёмкость - O(1)

        @Override
        public void remove() {
            if (currentString == null) {
                throw new IllegalStateException();
            }
            Trie.this.remove(currentString);
            currentString = null;
        }
        // Трудоёмкость - O(string length)
        // Ресурсоёмкость - O(string length)

        private void pushToStack(Node root, String key) {
            Set<Character> set = new HashSet<>(root.children.keySet());
            for (char child: set) {
                if (child == '\0') {
                    stack.push(key);
                }
                else {
                    pushToStack(root.children.get(child), key + child);
                }
            }
        }
        // Трудоёмкость - O(n)
        // Ресурсоёмкость - O(n)

    }
}