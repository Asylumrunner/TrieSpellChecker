# TrieSpellChecker
A spell-checking program in Java, which uses a Trie data structure to define a dictionary of words, and compare input against that dictionary

## What Is A Trie?

A Trie is a tree-based data structure in which the position of a node, rather than the node itself, determines the key associated with that node. Because of this, and the fact that the descendants of a key all have that key as a prefix, these trees are sometimes called Prefix Trees.

[Learn more about tries here](https://en.wikipedia.org/wiki/Trie)

## Why Utilize A Trie?

By the nature of a trie, lookup of words is extremely fast. It is O(n) (linear worst-case time compelxity), as all a program has to to is navigate down a series of edges representing each additional character added to the key. As a result, a lookup method will only traverse a number of edges equal to the number of characters in the key.

On top of this, a simple prefix navigation of the trie is capable of producing every single key in the trie in alphabetical order, which is extremely useful for language-based applications.

## Downsides Of Tries

The downside of the trie as a data structure for spellchecking is that it can be incredibly space-intensive, as each node can have a maximum number of outgoing edges equal to the number of valid characters in the alphabet (in English, 26). As a result, a trie representing English can have as many as 26 times as many nodes at depth n as at depth n-1.

## Optimal Trie Conditions

To use a trie optimally, it should be used with a language where knowing the prefix of a key limits the possible characters that follow it. There are cases in English where this is true (for instance, if the most recently-checked character in a key is 'q', there is a strong probability the next character will be a 'u'), but this is situational

Furthermore, using a trie is extremely useful for languages where most words can be defined as another, shorter word, plus a character. To have a language following this structure would mean that the trie would be able to define multiple words using largely the same memory space. Again, English has situations in which it can conform to this rule (especially when it comes to pluralization), but not always.

## Further Optimizations

The trie could be optimized further through compression. For instance, say we have the following subtrie:

	d->o->g
	 ->a->m

In this subtrie, d is the root, which then branches into two children, o and a. Again, none of these values are stored in the nodes themselves, but are rather defined by the positions of the nodes. We can call a node *redundant* if it has the following properties

1. It is NOT the root node
2. It has an outdegree of one (including it's own termination, if applicable)

In our example, o and a are both redundant. Neither is the root, and both have only one child. Recognizing these redundant nodes is the key to compression, as now we can compress by simply merging all redundant nodes with their singular children. In the case of our example trie, the result would look like this.
	
	d->og
	 ->am

To describe it more technically, what we have done is completely removed the leaf nodes, and the edges which define them, entirely. We then redefined the edges leading to o and a, indicating that these positions now correspond to "og" and "am", respectively. In doing so, we cut down on our memory usage, and also reduce average time complexity for word lookup (instead of traversing two edges to get to either word, we now only need to progress through one).

To utlize this compression method in the trie as it stands now, we would need to overhaul the definition of the node (as right now it uses node pointers to represent the 26 letters of the alphabet, with the assumption that every outgoing edge will only comprise a single additional letter), and it would also require a new way to compare the user-inputted string with the trie (as right now it only compares one letter at a time).

However, this compression method may not be optimal for our trie. For starters, it assumes that there are going to be no insertions or deletions in the trie. Otherwise, we may have the situation below:

	d->og
	 ->am
	>insert "doe"

In this instance, we see that we're inserting a word into the trie which shares a prefix ("do") with a word that already exists in the trie ("dog"). However, because we labeled that o node redundant earlier, we can't simply branch off of the o node as we could have before. Instead, we either need to split that node back apart into two seperate nodes, rendering our previous optimization moot, or do the following:

	d->og
	 ->am
	 ->o->e

This is inefficient, though. We could save memory by collapsing the similar prefixes together. No matter which solution we would pick, however, we end up with a lot more work on our hands, for maybe not that much optimality.

## Uses Of This Implementation

This trie specifically allows for modification and manipulation of the dictionary which it stores, and in fact sacrifices some optimality in favor of it. Therefore, this implementation is most useful in instances where it needs to keep track of a mutable set of keys. For example:

* This trie could be used to verify the user IDs of network users of a changing userbase, such as members of a project team or students of a school. As members enter or exit the team, or as students begin school or graduate, their user IDs could be added and removed from the trie. It would also allow users to change their ID with relative ease, and would allow the system to verify these IDs quickly. By storing IDs for verification separate from passwords, it also increases security.

* This trie could be used to form the spell-checker for a language in development. While natural languages such as English have a "proper" dictionary which is largely static over time, [conlangs](https://en.wikipedia.org/wiki/Constructed_language) are constantly being developed for fun, for scholarly purposes, for aesthetic purposes, or to supplement works of fiction, such as Tolkien's Elvish, or Martin's Dothraki. For the linguist building one of these languages, a malleable spell-checker would be useful to ensure that writings are "up to date" with the latest versions of their language.

* This trie could also be used as a sorting tool, especially for words and other keys described as a configuration of chracters of the alphabet. Simply insert all of the keys to be sorted into the trie, then use a pre-order traversal to output the keys in alphabetical order. While not optimal, this sorting method does allow for the set of keys to be sorted to change for little cost (O(Ln), where L is the length of the largest key to be added/removed, and n is the number of keys added/removed. For a large number of modifications, L is going to remain relatively constant whilst n subsumes it, leading to linear time complexity. The longest non-neologistic, non-technical word in the English language is [only 28 letters long](https://en.wikipedia.org/wiki/Antidisestablishmentarianism_(word))). 
