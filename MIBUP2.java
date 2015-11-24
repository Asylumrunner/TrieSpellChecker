/* Trie Spell-Checker by Michael Burdick
 * Contrary to what may be intuitive, tries model a dictionary by considering EDGES to represent letters, instead of nodes. The number of nodes in a path consituting a word represents the number of letters in the word -1.
 * The downside of the trie is that it is incredibly space-intensive. In the worst-case the trie will be a tree in which every node has an outdegree of 26, and thus, every level of the trie has 26 times more nodes than the last.
 * On the upside, the trie is extremely fast (O(n) for most operations, where n is the length of the word being inserted/searched/deleted/etc).
 * 
 * */

import java.util.*;

class Node {
	boolean terminal;
	int outDegree;
	Node[] children;
	
	Node(boolean term){
		terminal = term;//determines if this particular node represents the last letter of a word in the dictionary
		outDegree = 0;//in the context of the Trie, this number represents how many words contain the path up to this node as a substring. When a node is first added, this is zero
		Node[] temp = {null, null, null, null, null, null, null, null, null, null,
		            null, null, null, null, null, null, null, null, null, null,
		            null, null, null, null, null, null};//when you construct the node, the constructor generates a 26-part array for storing letters, with children[0] being A, children[1] being B, etc.
		children = temp;
				
	}
}

class Trie {
	Node root;
	
	Trie(){
		root = new Node(false);//all the Trie constructor has to do is create a root node, which is not terminal, as there are no words with 0 letters. Remember that nodes don't represent letters, edges do.
	}
	boolean insert(String x){
		String s = x.toLowerCase();//ensures that the Trie is case-insensitive
		Node lookingAt = root;//the insert procedure begins by looking at the root
		int index;
		for(int i = 0; i < s.length()-1; i++){//for every letter of the word EXCEPT the last
			index = letterToNumber(s.charAt(i));//the letter is used to calculate an equivalent number index (a=0, b=1, and so on)
			if (lookingAt.children[index] == null) {//if there is not yet a connection equivalent to that letter
				lookingAt.children[index] = new Node(false);//create a new node connection equivalent to that letter
				lookingAt.outDegree++;//and increase the outdegree of the current node by 1
			}
			lookingAt = lookingAt.children[index];//then progress to the next node
			
		}
		
		index = letterToNumber(s.charAt(s.length()-1));//once you hit the last letter, determine the index of the last letter of the word
		if(lookingAt.children[index] == null){//if there does not exist a link for that last letter
			lookingAt.children[index] = new Node(true);//create a link for the last letter (which is terminal)
			lookingAt.outDegree++;//increase the outdegree of that second to last node by 1
			return true;//then return true, as the word has been archived
		}
		else {//if there IS a link for the last letter of the word
			if((lookingAt.children[index]).terminal){//check if it's terminal
				return false;//if it is, that word's already been placed, so just return false
			}
			else {//if it isn't terminal
				(lookingAt.children[index]).terminal = true;//just make it terminal
				return true;//and return true
			}
		}
		
		
	}
	
	boolean isPresent(String x){
		String s = x.toLowerCase();
		Node lookingAt = root;//start the search at the root node
		for(int i = 0; i < s.length(); i++){//for every letter of the word
			int index = letterToNumber(s.charAt(i));//create an index based on the letter
			if (lookingAt.children[index] == null) {//if there isn't a link to the next letter of the word
				return false;//return false, as the word does not exist
			}
			else if(i == s.length()-1 && !lookingAt.children[index].terminal){
				return false;//if it finds all of the letters of the word, but the last letter isn't terminal, return false
			}
			else {
				lookingAt = lookingAt.children[index];//otherwise, continue moving down the word and down the Trie
			}
		}
		return true;//if you make it all the way to the end of the word without reaching a dead end, return true, as the word exists
	}
	
	boolean delete(String x){
		String s = x.toLowerCase();
		boolean canYouDelete = isPresent(s);//before deletion begins, check to see if the word is even there
		if(!canYouDelete){//if it isn't
			return false;//just go ahead and bail
		}
		boolean deleted = recursiveDelete(root, s);//otherwise, begin the recursive deletion calls down the Trie (the deleted bool doesn't actually do anything, I just needed to catch the last recursive call)
		return true;//once deletion is complete, return true to indicate so
	}
	
	int membership(){//Membership is a bare-bones wrapper class, which keeps the main method a bit cleaner, and saves me the need to either set the Trie's root to be public, or to establish a getter function for the root
		return recursiveRollCall(root, 0);//membership begins the first call of the recursive membership checker function
	}
	
	void listAll(){
		this.massPrint(root, "");//listAll simply contains the call for the recursive printing function. Again, this mostly keeps the main method clean.
	}
	int letterToNumber(char c){//this code requires enough translation of letters to numbers and vice versa that I coded in the translators
		int num = c - 'a';//this simply subtracts the value "a" from the value of the desired char to get a number, 0-25, representing that character
		return num;
	}
	
	char numberToLetter(int i){//number to letter, meanwhile, does the exact opposite as letterToNumber to extract a letter value from a number, 0-25
		char c = (char) (i + 'a');
		return c;
	}
	
	/* 
	 * You'll notice neighter letterToNumber nor numberToLetter do case-checking. That's because neither is called on a letter that hasn't already been case-corrected by one of the trie's methods.
	 * In a more general application, it might be integral to edit these functions to ensure that their character input/output is of the proper case.
	 * */
	
	void massPrint(Node n, String s){//massPrint is a recursive function which basically carries parts of a string down the Trie, adding letters to the end of the string when necessary
		if (n.terminal){//when massPrint hits a node that's terminal, it prints the word it already has saved up
			System.out.println(s);
		}
		for(int i = 0; i < 26; i++){//then, massPrint checks each Node in the children array, to see if more words branch off of this word
			if (n.children[i] != null){//if any do
				String temp = s + numberToLetter(i);
				massPrint(n.children[i], temp);//then massPrint calls a new instance of itself on that next node, while also concatenating the appropriate letter to the string it's holding
			}
		}
	}
	
	int recursiveRollCall(Node n, int count){//recursiveRollCall simply uses a pre-order traversal of the Trie to count the number of terminal nodes, and thus, words
		if(n.terminal){//if the node rRC is currently on is terminal, add one to the count of words
			count++;
		}
		for(int i = 0; i < 26; i++){//then for each possible child branch of the node
			if (n.children[i] != null){//if there is a branch
				count = recursiveRollCall(n.children[i], count);//call rRC on that branch to check for any words down there
			}
		}
		return count;//since count is being passed along through all of the calls of rRC, one constant running tally is constantly being used
	}
	
	boolean recursiveDelete(Node n, String x){
		String s = x.toLowerCase();
		if(s.length() > 1){//if you are not looking at the very last letter of the string
			int i = letterToNumber(s.charAt(0));//create a number representing the next letter of the string
			if(n.children[i] == null){//if you don't have a link to the next letter, the delete effort is not going to work, so return false
				return false;
			}
			else{//if you do have a link to the next letter of the word
				boolean cutTies = recursiveDelete(n.children[i], s.substring(1));//ask if you should cut ties with the next letter
				if(cutTies){//if so
					n.children[i] = null;//sever the tie with the node in n's children array
					n.outDegree--;//and decrease n's outdegree by 1
				}
				if(n.outDegree == 0 && !n.terminal){//if the outdegree of THIS node is now 0, and it's not terminal
					return true;//indicate to the node above this one that you should delete it
				}
				else{//otherwise, indicate this node should not be deleted
					return false;
				}
			}
		}
		else{
			int i = letterToNumber(s.charAt(0));//create a number representing the next letter of the string
			if(n.children[i] == null){//if you don't have a link to the next letter, the delete effort is not going to work, so return false
				return false;
			}
			else{
				if((n.children[i]).outDegree == 0){
					n.children[i] = null;
					n.outDegree--;
				}
				else{
					n.children[i].terminal = false;
				}
			}
			
			if(n.outDegree == 0 && !n.terminal){//if the outdegree of THIS node is now 0, and it's not terminal
				return true;//indicate to the node above this one that you should delete it
			}
			else{//otherwise, indicate this node should not be deleted
				return false;
		}
	}
}
	
}
public class MIBUP2 {
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);//opening up a scanner using standard input to catch user commands
		boolean done = false;//done is the simple bool I use to determine if the program needs to keep running
		String line = "";//line will hold the assorted inputs the user generates
		
		Trie trie = new Trie();
		
		while (!done){
			System.out.println("Select an option from the following:");
			System.out.println("1. Display author details");
			System.out.println("2. Insert a word into the dictionary");
			System.out.println("3. List all words in the dictionary");
			System.out.println("4. Count the number of words in the dictionary");
			System.out.println("5. Check the membership of a word in the dictionary");
			System.out.println("6. Check the correctness of a list of words");
			System.out.println("7. Delete a word from the dictionary");
			System.out.println("8. Exit");
			System.out.print("Enter a selection: "); 
			
			/*This is my simple text UI. The original spec for this project used some arcane inputs (usually single characters followed by modifiers, in a pseudo-UNIX style) 
			 * I decided that a more traditional menu system would be more user-friendly, and fairly simple to implement
			 * */
			
			String choice = scanner.nextLine();
			String[] tokens;
			
			/* You might notice that the switch uses a String, instead of an int, to determine the case. By doing so, error checking is far simplified (type checking of user input is now unimportant).
			 * This reduces complexity of code, while ensuring this switch is air-tight, and at the same time, usability isn't affected at all.
			 * */
			
			switch(choice){
			case "1"://Option 1 simply prints the authorial information of the code to the screen
				System.out.println("Code Authored by Michael Burdick");
				System.out.println("Originally Written for Ivor Page's Data Structures and Introduction to Algorithm Analysis Class");
				System.out.println("As Offered at UT Dallas in Spring 2015");
				break;
			case "2"://Option 2 has the user enter a word, and then attempts to enter that word into the dictionary as defined by the trie
				System.out.print("Enter a word: ");
				line = scanner.nextLine();
				tokens = line.split(" ");
				boolean inserted = trie.insert(tokens[0]);
				/*Syntax like this is fairly common throughout the main method, where I take the input and split it into tokens. This insight came from Ivor Page, instructor of the course which orginally assigned this project.
				 * By splitting input like this, it makes this program insensitive to multi-word inputs, and instead allows it to simply care about the first word inputted by the user.
				 * This technique also allows cases which only require a one-word input, such as Cases 2 and 5, to use the same general input method as case 6, which takes a list of words as input.
				 * */
				
				if (inserted){
					System.out.println("Word inserted");
				}
				else {
					System.out.println("Word already exists");
				}
				break;
			case "3"://Case 3 prints all of the words currently defined by the trie in alphabetical order
				trie.listAll();
				break;
			case "4"://Case 4 simply counts the number of words defined by the trie
				int i = trie.membership();
				System.out.println("Membership is " + i);
				break;
			case "5"://Case 5 takes in a word and determines if it's in the trie
				System.out.print("Enter a word: ");
				line = scanner.nextLine();
				tokens = line.split(" ");
				boolean there = trie.isPresent(tokens[0]);
				if(there){
					System.out.println("Word found");
				}
				else {
					System.out.println("Word not found");
				}
				break;
			case "6"://Case 6 is unique, in that it takes in a list of words. It will then check each word to determine if it exists in the trie, singling out those that are not
				System.out.print("Enter a list of words, divided by spaces: ");
				line = scanner.nextLine();
				tokens = line.split(" ");
				for (int j = 0; j < tokens.length-1; j++){//for each word
					String temp = tokens[j];
					boolean found = trie.isPresent(temp);//check to see if that word exists in the Trie
					if (!found){//if it isn't
						System.out.println("Spelling mistake " + temp);//print out the "Spelling Mistake" prompt
					}
				}
				break;
			case "7"://Case 7 deletes a word from the trie
				System.out.print("Enter a word: ");
				line = scanner.nextLine();
				tokens = line.split(" ");
				boolean deleted = trie.delete(tokens[0]);
				if(!deleted){
					System.out.println("Word deleted");
				}
				else{
					System.out.println("Word not present");
				}
				break;
			case "8"://Case 8 breaks the input loop, and then shuts this sucker down.
				done = true;
				break;
			default://The default option for the switch serves as a simple catch-all error message
				System.out.println("Invalid menu option");
				break;
			}
			
			System.out.print("Press any key to continue.");
			String dumpVariable = scanner.nextLine();
			
			/*This simple set of lines at the bottom waits for user input to loop back around and display the menu again. Without it, the menu is automatically appended to the bottom of the program output after any operation, possibly pushing
			 * the output that the user actually cares about above the top of the window, causing a need for unnecessary scrolling.
			 */
					
		}
		
		scanner.close();
		
		
	}
}
