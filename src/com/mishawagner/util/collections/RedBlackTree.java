package com.mishawagner.util.collections;

public class RedBlackTree<T extends Comparable<T>> extends Collection<T> {
	private class Node<T extends Comparable<T>> {
		T item;
		Node<T> left, right, parent;
		boolean isRed = true;

		public Node(T item, Node<T> parent) {
			this(item, null, null, parent);
		}

		public Node(T item, Node<T> left, Node<T> right, Node<T> parent) {
			this.item = item;
			this.left = left;
			this.right = right;
			this.parent = parent;
		}

		@Override
		public String toString() {
			return item + "-" + (isRed ? "R" : "B") + "(" + left + ", " + right + ")";
		}
	}

	Node<T> root;

	public RedBlackTree() {
		root = null;
	}

	@Override
	public void insert(T t) {
		if (root == null) {
			root = new Node<T>(t, null);
			root.isRed = false;
		} else {
			Node<T> inserted = insert(t, root);
			handleRedBlack(inserted);
		}
	}

	public void handleRedBlack(Node<T> inserted) {
		Node<T> grandParent = getGrandParent(inserted);
		Node<T> uncle = getUncle(inserted);

		if (inserted.parent == null) {
			inserted.isRed = false;
			return;
		} else if (!inserted.parent.isRed) {
			return;
		} else if (uncle != null && uncle.isRed) {
			inserted.parent.isRed = false;
			uncle.isRed = false;
			grandParent.isRed = true;
			handleRedBlack(grandParent);
		} else {
			if (!isLeftSubtree(inserted) && isLeftSubtree(inserted.parent)) {
				rotateLeft(inserted.parent);
				inserted = inserted.left;
			} else if (isLeftSubtree(inserted) && !isLeftSubtree(inserted.parent)) {
                rotateRight(inserted.parent);
                inserted = inserted.right;
            }

			grandParent = getGrandParent(inserted);
			
			inserted.parent.isRed = false;
			grandParent.isRed = true;
			
			if (isLeftSubtree(inserted)) {
				rotateRight(grandParent);
			} else {
				rotateLeft(grandParent);
			}
		}
	}

	private Node<T> insert(T t, Node<T> tree) {
		double comparison = tree.item.compareTo(t);

		if (comparison == 0) {
			System.out.println("Items match, can't add.");
			return null;
		} else if (comparison > 0) {
			if (tree.left != null) {
				return insert(t, tree.left);
			} else {
				tree.left = new Node<T>(t, tree);
				return tree.left;
			}
		} else {
			if (tree.right != null) {
				return insert(t, tree.right);
			} else {
				tree.right = new Node<T>(t, tree);
				return tree.right;
			}
		}
	}

	@Override
	public boolean remove(T t) {
		if (root != null) {
			return remove(t, root);
		} else {
			return false;
		}
	}

	private boolean remove(T t, Node<T> tree) {
		if (tree.item == t) {
			remove(tree);
			return true;
		} else {
			if (remove(t, tree.left)) {
				return true;
			} else {
				return remove(t, tree.right);
			}
		}
	}

	private void remove(Node<T> tree) {
		if (tree.left == null && tree.right == null) {
			// If no children, just remove it from parent
			if (tree.parent.left == tree) {
				tree.parent.left = null;
			} else {
				tree.parent.right = null;
			}
		} else if (tree.right == null) {
			if (tree.parent.left == tree) {
				tree.parent.left = tree.left;
			} else {
				tree.parent.right = tree.left;
			}
		} else if(tree.left == null) {
			if (tree.parent.left == tree) {
				tree.parent.left = tree.right;
			} else {
				tree.parent.right = tree.right;
			}
		} else {
			Node<T> replacer = getMostRight(tree.left);

			tree.item = replacer.item;
			replacer.right = null;
		}

	}

	private Node<T> getMostRight(Node<T> tree) {
		if (tree.right == null) {
			return tree;
		} else {
			return getMostRight(tree.right);
		}
	}

	@Override
	public int size() {
		return size(root);
	}

	private int size(Node<T> tree) {
		if (tree == null) {
			return 0;
		} else {
			return 1 + size(tree.left) + size(tree.right);
		}
	}

	@Override
	public boolean contains(T t) {
		return contains(t, root);
	}

	private boolean contains(T t, Node<T> tree) {
		if (tree == null) {
			return false;
		} else {
			return t == tree.item
				|| contains(t, tree.left)
				|| contains(t, tree.right);
		}
	}

	public int depth() {
		return depth(root);
	}

	private int depth(Node<T> tree) {
		if (tree == null) {
			return 0;
		} else {
			return 1 + Math.max(
				depth(tree.left),
				depth(tree.right)
			);
		}
	}
	
	private void rotateRight(Node<T> tree) {
		if (tree.left == null) {
			System.err.println("Can't right-rotate a tree with no left");
			return;
		}

		T item = tree.item;
		T leftItem = tree.left.item;
		Node<T> leftLeft = tree.left.left;
		Node<T> leftRight = tree.left.right;
		Node<T> left = tree.left;
		Node<T> right = tree.right;

		tree.item = leftItem;
		tree.left = leftLeft;
		if (tree.left != null) {
			tree.left.parent = tree;
		}
		tree.right = left;
		tree.right.item = item;
		tree.right.left = leftRight;
		if (tree.right.left != null) {
			tree.right.left.parent = tree.right;
		}
		tree.right.right = right;
		if (tree.right.right != null) {
			tree.right.right.parent = tree.right;
		}

		System.out.println(checkParentsCorrect(root));
	}

	private void rotateLeft(Node<T> tree) {
		if (tree.left == null) {
			System.err.println("Can't right-rotate a tree with no left");
			return;
		}

		T item = tree.item;
		T rightItem = tree.right.item;
		Node<T> rightLeft = tree.right.left;
		Node<T> rightRight = tree.right.right;
		Node<T> left = tree.left;
		Node<T> right = tree.right;

		tree.item = rightItem;
		tree.left = right;
		tree.right = rightRight;
		tree.right.parent = tree;
		tree.left.item = item;
		tree.left.left = left;
		tree.left.left.parent = tree.left;
		tree.left.right = rightLeft;

		if (tree.left.right != null) {
			tree.left.right.parent = tree.left;
		}

		System.out.println(checkParentsCorrect(root));
	}

	private Node<T> getGrandParent(Node<T> tree) {
		if (tree.parent == null) {
			return null;
		} else {
			return tree.parent.parent;
		}
	}

	private Node<T> getUncle(Node<T> tree) {
		if (tree.parent == null) {
			return null;
		}

		if (isLeftSubtree(tree)) {
			return tree.parent.right;
		} else {
			return tree.parent.left;
		}
	}

	private boolean isLeftSubtree(Node<T> tree) {
		if (tree.parent == null) {
			return false;
		}

		return tree.parent.left == tree;
	}

	private boolean isRed(Node<T> tree) {
		if (tree == null) {
			return false;
		} else {
			return tree.isRed;
		}
	}

	public int blackDepth() {
		return blackDepth(root);
	}

	private int blackDepth(Node<T> tree) {
		if (tree == null) {
			return 1;
		} else {
			return (tree.isRed ? 0 : 1) + Math.max(
				blackDepth(tree.left),
				blackDepth(tree.right));
		}
	}

	private boolean checkParentsCorrect(Node<T> tree) {
		return  (tree.left == null || (tree.left.parent == tree && checkParentsCorrect(tree.left))) &&
				(tree.right == null || (tree.right.parent == tree && checkParentsCorrect(tree.right)));
	}

	@Override
	public String toString() {
		return root.toString();
	}

	public static void main(String[] args) {
		RedBlackTree<Integer> t = new RedBlackTree<Integer>();

		t.insert(5);
		t.insert(4);
		t.insert(3);
		t.insert(2);
		t.insert(1);

		System.out.println(t);
	
		System.out.println("Contains: " + t.contains(100));
		System.out.println("Size: " + t.size());
		System.out.println("Depth: " + t.depth());
		System.out.println("Black depth: " + t.blackDepth());
	}
}

