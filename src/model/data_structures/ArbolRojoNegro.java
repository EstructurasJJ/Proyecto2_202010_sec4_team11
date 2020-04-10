package model.data_structures;

public class ArbolRojoNegro <Key extends Comparable<Key>, Value extends Comparable<Value>> 
{
	private Node root;
	private static final boolean RED = true;
	private static final boolean BLACK = false;
	
	private class Node
	{
		Key key;
		Value val;
		Node left; 
		Node right;
		int N;
		boolean color;
		
		Node (Key nKey, Value nVal, int nN, boolean nColor)
		{
			this.key = nKey;
			this.val = nVal;
			this.N = nN;
			this.color = nColor;
		}
	}
	
	public int size()
	{
		return size(root);
	}
	
	public int size (Node h)
	{
		if (h == null) 	return 0;
		else 			return h.N;
	}
	
	private boolean isRed (Node h)
	{
		if (h == null) 	return false;		
		return h.color == RED;
	}
	
	private Node rotateLeft (Node h)
	{
		Node x = h.right;
		h.right = x.left;
		x.left = h;
		
		x.color = h.color;
		h.color = RED;
		
		x.N = h.N;
		h.N = 1 + size(h.left) + size(h.right);
		
		return x;
	}

	private Node rotateRight (Node h)
	{
		Node x = h.left;
		h.left = x.right;
		x.right = h;
		
		x.color = h.color;
		h.color = RED;
		
		x.N = h.N;
		h.N = 1 + size(h.left) + size(h.right);
		
		return x;
	}
	
	private void flipColors (Node h)
	{
		h.color = RED;
		h.left.color = BLACK;
		h.right.color = BLACK;
	}
	
	public void put (Key key, Value val)
	{
		root = put(root, key, val);
		root.color = BLACK;
	}
	
	public Node put (Node h, Key key, Value val)
	{
		if (h == null)
			return new Node(key, val, 1, RED);
		
		int cmp = key.compareTo(h.key);
		
		if (cmp < 0) 			h.left = put(h.left, key, val);
		else if (cmp > 0)		h.right = put(h.right, key, val);
		else 					h.val = val;
		
		if (isRed(h.right) && !isRed(h.left)) 			h = rotateLeft(h);
		if (isRed(h.left) && isRed(h.left.left)) 		h = rotateRight(h);
		if (isRed(h.left) && isRed(h.right)) 			flipColors(h);
		
		h.N = size(h.left) + size(h.right) + 1;
		return h;
	}
	
	public Value get (Key key)
	{
		return get (root, key);
	}
	
	private Value get (Node h, Key key)
	{
		if (h == null) return null;
		
		int cmp = key.compareTo(h.key);
		
		if (cmp < 0) 		return get(h.left, key);
		else if (cmp > 0) 	return get(h.right, key);
		else				return h.val;
		
	}
	
	public Key min()
	{
		return min(root).key;
	}
	
	public Node min (Node h)
	{
		if(h.left == null) return h;
		return min(h.left);
	}
	
	public Key max()
	{
		return max(root).key;
	}
	
	public Node max (Node h)
	{
		if(h.right == null) return h;
		return max(h.right);
	}
	
    public int height() 
    {
        return height(root);
    }
    public int height(Node h) 
    {
        if (h == null) return -1;
        return 1 + Math.max(height(h.left), height(h.right));
    }
	
    /////////////////////////////////////////////////////////////////////////////////// ITERADORES
    
    public Iterable<Key> keys()
    {
    	return keys(min(), max());
    }
    
    public Iterable<Key> keys (Key lo, Key hi)
    {
    	ListaEnlazadaQueue<Key> lista = new ListaEnlazadaQueue<Key>();
    	keys(root, lista, lo, hi);
    	return lista;
    }
    
    private void keys (Node h, ListaEnlazadaQueue<Key> lista, Key lo, Key hi)
    {
    	if (h == null) return;
    	
    	int cmplo = lo.compareTo(h.key);
    	int cmphi = hi.compareTo(h.key);
    	
    	if (cmplo < 0) 					keys(h.left, lista, lo, hi);
    	if (cmplo <= 0 && cmphi >= 0) 	lista.enqueue(h.key);
    	if (cmphi > 0) 					keys(h.right, lista, lo, hi);
    }
    
    
    public Iterable<Value> values (Key lo, Key hi)
    {
    	ListaEnlazadaQueue<Value> lista = new ListaEnlazadaQueue<Value>();
    	values(root, lista, lo, hi);
    	return lista;
    }
    
    private void values (Node h, ListaEnlazadaQueue<Value> lista, Key lo, Key hi)
    {
    	if (h == null) return;
    	
    	int cmplo = lo.compareTo(h.key);
    	int cmphi = hi.compareTo(h.key);
    	
    	if (cmplo < 0) 					values(h.left, lista, lo, hi);
    	if (cmplo <= 0 && cmphi >= 0) 	lista.enqueue(h.val);
    	if (cmphi > 0) 					values(h.right, lista, lo, hi);
    }
    
    /////////////////////////////////////////////////////////////////////////////////// EXTRA
	
    public boolean isEmpty()
	{
		return root == null;
	}
	
	public boolean contains(Key key)
	{
		return get(key) != null;
	}
	
	//////////////////////////////////////////////////////// REVISAR TODAS LAS COSAS DE UN ARBOL ROJO NEGRO. 	
	
	public boolean check ()
	{
		return  BST() && RojoOjo() && NegroOjo();
	}
	
	// La llave de cada nodo sea mayor que cualquiera de su sub-árbol izquierdo.
	// La llave de cada nodo sea menor que cualquiera de su sub-árbol derecho.
	
	private boolean BST()
	{
		return BST(root, null, null);
	}
	
	private boolean BST (Node h, Key min, Key max)
	{
		if (h == null) return true;
		
        if (min != null && h.key.compareTo(min) <= 0) return false;
        if (max != null && h.key.compareTo(max) >= 0) return false;
        
        return BST(h.left, min, h.key) && BST(h.right, h.key, max);
	}
	
	// Un nodo NO puedetener enlace rojo a su hijo derecho. 
	// No puede haber dos enlaces rojos consecutivos a la izquierda. 
	
	private boolean RojoOjo()
	{
		return RojoOjo(root);
	}
	
	private boolean RojoOjo (Node h)
	{
		if(h == null) return true;
		
		if (isRed(h.right)) 						return false;
		if (h != root && isRed(h) && isRed(h.left)) return false;
		
		return RojoOjo(h.left) && RojoOjo(h.right);
	}
	
	// Todas las ramas tienen el mismo número de enlaces negros.
	
	private boolean NegroOjo()
	{
		int negro = 0;
		
		Node h = root;
		
		while (h != null)
		{
			if (!isRed(h)) negro++;
			h = h.left;
		}
		
		return NegroOjo(root, negro);
	}
	
	private boolean NegroOjo(Node h, int negro)
	{
		if (h == null) return negro == 0;
		if(!isRed(h)) negro--;
		
		return NegroOjo(h.left, negro) && NegroOjo(h.right, negro);
	}


   

	
}
