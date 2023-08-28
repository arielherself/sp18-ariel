package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Ariel
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (key.compareTo(p.key) == 0) {
            return p.value;
        } else if (key.compareTo(p.key) < 0) {
            return getHelper(key, p.left);
        } else {
            return getHelper(key, p.right);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            return new Node(key, value);
        }
        if (key.compareTo(p.key) == 0) {
            throw new IllegalArgumentException();
        } else if (key.compareTo(p.key) < 0) {
            p.left = putHelper(key, value, p.left);
        } else {
            p.right = putHelper(key, value, p.right);
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
        ++size;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /**
     * Fills a Set with the keys from p, using postorder traversal.
     */
    public void keySetHelper(Set<K> s, Node p) {
        if (p == null) {
            return ;
        }
        keySetHelper(s, p.left);
        keySetHelper(s, p.right);
        s.add(p.key);
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> result = new HashSet<>();
        keySetHelper(result, root);
        return result;
    }

    private static <K extends Comparable<K>, V> void swap(BSTMap<K, V>.Node p, BSTMap<K, V>.Node q) {
        final K pKey = p.key;
        final V pValue = p.value;
        p.key = q.key;
        p.value = q.value;
        q.key = pKey;
        q.value = pValue;
    }

    private static <K extends Comparable<K>, V> void sink(BSTMap<K, V>.Node p) {
        while (true) {
            if (p.left != null && p.key.compareTo(p.left.key) < 0) {
                swap(p, p.left);
                p = p.left;
            } else if (p.right != null && p.key.compareTo(p.right.key) > 0) {
                swap(p, p.right);
                p = p.right;
            } else {
                break;
            }
        }

    }

    private static <K extends Comparable<K>, V> BSTMap<K, V>.Node findRightmostFather(BSTMap<K, V>.Node p) {
        BSTMap<K, V>.Node result;
        if (p.right != null) {
            result = findRightmostFather(p.right);
        } else if (p.left != null) {
            result = findRightmostFather(p.left);
        } else {
            return null;
        }
        if (result == null) {
            return p;
        } else {
            return result;
        }
    }

    private static <K extends Comparable<K>, V> BSTMap<K, V>.Node remove(BSTMap<K, V>.Node p) {
        BSTMap<K, V>.Node rightmostFather = findRightmostFather(p);
        if (rightmostFather == null) {
            return null;
        }
        if (rightmostFather.right != null) {
            p.key = rightmostFather.right.key;
            p.value = rightmostFather.right.value;
            rightmostFather.right = null;
        } else {
            p.key = rightmostFather.left.key;
            p.value = rightmostFather.left.value;
            rightmostFather.left = null;
        }
        sink(p);
        return p;
    }

    public V removeHelper(K key, Node parent, Node current) {
        if (current == null) {
            return null;
        }
        if (key.compareTo(current.key) == 0) {
            V resultValue = current.value;
            Node resultNode = remove(current);
            if (parent != null) {
                if (parent.left == current) {
                    parent.left = resultNode;
                } else {
                    parent.right = resultNode;
                }
            } else {
                root = resultNode;
            }
            return resultValue;
        } else if (key.compareTo(current.key) < 0) {
            return removeHelper(key, current, current.left);
        } else {
            return removeHelper(key, current, current.right);
        }
    }

    public V removeHelper(K key, V value, Node parent, Node current) {
        if (current == null) {
            return null;
        }
        if (key.compareTo(current.key) == 0 && value != null && value.equals(current.value)) {
            V resultValue = current.value;
            Node resultNode = remove(current);
            if (parent != null) {
                if (parent.left == current) {
                    parent.left = resultNode;
                } else {
                    parent.right = resultNode;
                }
            } else {
                root = resultNode;
            }
            return resultValue;
        } else if (key.compareTo(current.key) < 0) {
            return removeHelper(key, value, current, current.left);
        } else {
            return removeHelper(key, value, current, current.right);
        }
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        V result = removeHelper(key, null, root);
        if (result != null) {
            --size;
        }
        return result;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        V result = removeHelper(key, value, null, root);
        if (result != null) {
            --size;
        }
        return result;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
