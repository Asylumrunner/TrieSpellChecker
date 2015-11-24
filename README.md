# TrieSpellChecker
A spell-checking program in Java, which uses a Trie data structure to define a dictionary of words, and compare input against that dictionary

A Trie is a tree-based data structure in which the position of a node, rather than the node itself, determines the key associated with that node. Because of this, and the fact that the edge leading into the node represents the last element of the node's key, these trees are sometimes called Prefix Trees.

[Learn more about tries here](https://en.wikipedia.org/wiki/Trie)


By the nature of a trie, lookup of words is extremely fast. It is O(n) (linear worst-case time compelxity), as all a program has to to is navigate down a series of edges representing each additional character added to the key. As a result, a lookup method will only traverse a number of edges equal to the number of characters in the key.

On top of this, a simple prefix navigation of the trie is capable of producing every single key in the trie in alphabetical order, which is extremely useful for language-based applications.

The downside of the trie as a data structure for spellchecking is that it can be incredibly space-intensive, as each node can have a maximum number of outgoing edges equal to the number of valid characters in the alphabet (in English, 26). As a result, a trie representing English can have as many as 26 times as many nodes at depth n as at depth n-.

To use a trie optimally, it should be used with a language where knowing a character in a key extremely limits the possible characters that follow it. There are cases in English where this is true (for instance, is the most recently-checked character in a key is 'q', there is a strong probability the next character will be a 'u'), but this is situational

Furthermore, using a trie is extremely useful for languages where most words can be defined as another, shorter word, plus a character. To have a language following this structure would mean that the trie would be able to define multiple words using largely the same memory space. Again, English has situations in which it can conform to this rule (especially when it comes to pluralization), but not always.