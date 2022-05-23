package assignment3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DogShelter implements Iterable<Dog> {
	public DogNode root;

	public DogShelter(Dog d) {
		this.root = new DogNode(d);
	}

	private DogShelter(DogNode dNode) {
		this.root = dNode;
	}


	// add a dog to the shelter
	public void shelter(Dog d) {
		if (root == null) 
			root = new DogNode(d);
		else
			root = root.shelter(d);
	}

	// removes the dog who has been at the shelter the longest
	public Dog adopt() {
		if (root == null)
			return null;

		Dog d = root.d;
		root =  root.adopt(d);
		return d;
	}
	
	// overload adopt to remove from the shelter a specific dog
	public void adopt(Dog d) {
		if (root != null)
			root = root.adopt(d);
	}


	// get the oldest dog in the shelter
	public Dog findOldest() {
		if (root == null)
			return null;
		
		return root.findOldest();
	}

	// get the youngest dog in the shelter
	public Dog findYoungest() {
		if (root == null)
			return null;
		
		return root.findYoungest();
	}
	
	// get dog with highest adoption priority with age within the range
	public Dog findDogToAdopt(int minAge, int maxAge) {
		return root.findDogToAdopt(minAge, maxAge);
	}

	// Returns the expected vet cost the shelter has to incur in the next numDays days
	public double budgetVetExpenses(int numDays) {
		if (root == null)
			return 0;
		
		return root.budgetVetExpenses(numDays);
	}
	
	// returns a list of list of Dogs. The dogs in the list at index 0 need to see the vet in the next week. 
	// The dogs in the list at index i need to see the vet in i weeks. 
	public ArrayList<ArrayList<Dog>> getVetSchedule() {
		if (root == null)
			return new ArrayList<ArrayList<Dog>>();
			
		return root.getVetSchedule();
	}


	public Iterator<Dog> iterator() {
		return new DogShelterIterator();
	}


	public class DogNode {
		public Dog d;
		public DogNode younger;
		public DogNode older;
		public DogNode parent;

		public DogNode(Dog d) {
			this.d = d;
			this.younger = null;
			this.older = null;
			this.parent = null;
		}

		public DogNode shelter (Dog d) {
            // ADD YOUR CODE HERE
			DogNode goodboy = new DogNode(d);
			DogNode rootpointer = null;
			DogNode pointer = this;
				
			while (pointer != null) {
				rootpointer = pointer;
				if(d.getAge()<pointer.d.getAge()) {
					pointer = pointer.younger;
				}else {
					pointer = pointer.older;
				}
			}
				
			if(d.getAge()<rootpointer.d.getAge()) {
				rootpointer.younger = goodboy;
				goodboy.parent = rootpointer;
					
			}else {
				rootpointer.older=goodboy;
				goodboy.parent = rootpointer;
			}
			
			DogNode parentpointer = goodboy.parent;
			DogNode grandparentpointer = goodboy.parent.parent;
			
			while(goodboy.d.getDaysAtTheShelter()>goodboy.parent.d.getDaysAtTheShelter() ) {
				
				//if goodboy is on left node or if a right rotation is required
				if(goodboy.parent.younger==goodboy) {
					parentpointer.younger=goodboy.older;
					if(parentpointer.younger!=null) parentpointer.younger.parent = parentpointer;
					
					goodboy.older = parentpointer;
					parentpointer.parent = goodboy;
					goodboy.parent = grandparentpointer;
					if(grandparentpointer != null) {
						if(grandparentpointer.younger == parentpointer) 
							grandparentpointer.younger = goodboy;
						else
							grandparentpointer.older = goodboy;
					}
					
					parentpointer = goodboy.parent;
					if (goodboy.parent!=null) grandparentpointer = goodboy.parent.parent;
					if(goodboy.parent == null) break;
				}
				
				
				//if goodboy is on right node or if a left rotation is required
				else if (goodboy.parent.older == goodboy) {
					parentpointer.older = goodboy.younger;
					if(parentpointer.older!=null) parentpointer.older.parent = parentpointer;
					
					goodboy.younger = parentpointer;
					parentpointer.parent = goodboy;
					goodboy.parent = grandparentpointer;
					
					if(grandparentpointer != null) {
						if(grandparentpointer.younger == parentpointer) 
							grandparentpointer.younger = goodboy;
						else
							grandparentpointer.older = goodboy;
					}
					
					parentpointer = goodboy.parent;
					if(goodboy.parent!= null) grandparentpointer = goodboy.parent.parent;
					if(goodboy.parent == null) break;
				}
			}
			
			
			DogNode out = this;
			while(this.parent!=null) {
				if(out.parent==null) break;
				out = out.parent;
			}
			
            return out; 
		}

		@SuppressWarnings("unused")
		public DogNode adopt(Dog d) {
            DogNode curr = this;
            DogNode prev = null;
            DogNode toRemove = null;
            while(!curr.d.equals(d) || curr == null) {
            	if(d.getAge()<curr.d.getAge()) {
            		prev = curr;
            		curr = curr.younger;
            		if(curr== null) break;
            	}else if(d.getAge()>curr.d.getAge()) {
            		prev = curr;
            		curr = curr.older;
            		if(curr == null) break;
            	}
            	
            }
            toRemove = curr;
            DogNode out = null;
            
            //if node to remove isn't in the tree
            if(toRemove==null) {
            	out = this;
            	
        //if it is in the tree, first check if either left or right node is null, then if both nodes are not null
        //remove node and replace with oldest from left subtree and then perform tree rotations if necessary            
            }else {
            	if(toRemove.younger == null && toRemove.older == null) {
            		if(toRemove.parent==null) {
            			out = toRemove.parent;
            		}else {
	            		if(toRemove.parent.younger == toRemove) {
	            			toRemove.parent.younger = null;
	            			out = toRemove.parent;
	            			toRemove.parent = null;
	            		}else if(toRemove.parent.older == toRemove) {
	            			toRemove.parent.older = null;
	            			out=toRemove.parent;
	            			toRemove.parent = null;
	            		}
	            		
	            		while(true) {
	            			if(out.parent==null)break;
	            			out=out.parent;
	            		}
            		}
            	}else if(toRemove.older == null) {
            		
            		toRemove.younger.parent = toRemove.parent;
  
            		if(toRemove.parent!= null) {
	            		if(toRemove.parent.younger == toRemove) {
	            			toRemove.parent.younger = toRemove.younger;
	            		}else {
	            			toRemove.parent.older = toRemove.younger;
	            		}
            		}	
            		toRemove = toRemove.younger;
            		out = toRemove;
            		
            		while(true) {
            			if(out.parent==null)break;
            			out=out.parent;
            		}
            		
            	}else if(toRemove.younger == null) {
            		toRemove.older.parent = toRemove.parent;
            		if(toRemove.parent!= null) {
	            		if(toRemove.parent.younger == toRemove) {
	            			toRemove.parent.younger = toRemove.older;
	            		}else {
	            			toRemove.parent.older = toRemove.older;
	            		}
            		}
            		toRemove = toRemove.older;
            		out = toRemove;
            		
            		while(true) {
            			if(out.parent==null)break;
            			out=out.parent;
            		}
            	}else {
            		//placing old D in place of node to remove
            		DogNode oldD = findOldest(toRemove.younger);
            		
            		if(oldD.parent == toRemove) {
            			oldD.parent = toRemove.parent;
            			if(toRemove.parent!= null) {
            				if(oldD.parent.younger == toRemove) {
            					oldD.parent.younger = oldD;
            				}else {
            					oldD.parent.older = oldD;
            				}
            			}
            			toRemove.parent = null;
            			oldD.older = toRemove.older;
            			if (toRemove.older!=null)toRemove.older.parent = oldD;
            			toRemove.older = null;
            		}else {
            			
            			oldD.parent.older = oldD.younger;
            			if(oldD.younger!= null) oldD.younger.parent = oldD.parent;
            			
            			oldD.parent = toRemove.parent;
            			if(toRemove.parent!= null) {
            				if(oldD.parent.younger == toRemove) {
            					oldD.parent.younger = oldD;
            				}else {
            					oldD.parent.older = oldD;
            				}
            			}
            			toRemove.parent = null;
            			oldD.younger = toRemove.younger;
            			toRemove.younger = null;
            			oldD.older = toRemove.older;
            			toRemove.older = null;
            			if(oldD.younger!= null) oldD.younger.parent = oldD;
            			if(oldD.older!= null) oldD.older.parent = oldD;
            		}
            		//end of replacing phase
            		//now to adjust heap by implementing rotations
            		
            		DogNode goodboy;
            		DogNode s = oldD;
            		while(true) {
            			//if goodboy is on left node and a right notation is required(both nodes are not null)
            			if(s.younger!=null && s.older!= null && s.younger.d.getDaysAtTheShelter()>s.older.d.getDaysAtTheShelter()) {
            				goodboy = s.younger;
            				s.younger = goodboy.older;
            				if(s.younger!= null) s.younger.parent = s;
            				goodboy.parent = s.parent;
            				s.parent = goodboy;
            				goodboy.older = s;
            				if(goodboy.parent!=null) {
            					if(goodboy.parent.younger == s) {
            						goodboy.parent.younger = goodboy;
            					}else {
            						goodboy.parent.older = goodboy;
            					}
            				}
            				
            				if(s.younger == null && s.older == null) break;
            			
            				
            			//if goodboy is on right node and a left rotation is required(both nodes are not null)	
            			}else if(s.younger != null && s.older != null && s.older.d.getDaysAtTheShelter()>s.d.getDaysAtTheShelter()) {
            				goodboy = s.older;
            				s.older = goodboy.younger;
            				if(s.older!=null) s.older.parent = s;
            				goodboy.parent = s.parent;
            				s.parent = goodboy;
            				goodboy.younger = s;
            				if(goodboy.parent!= null) {
            					if(goodboy.parent.younger == s) {
            						goodboy.parent.younger = goodboy;
            					}else {
            						goodboy.parent.older = goodboy;
            					}
            				}
            				
            				if(s.younger == null && s.older == null) break;
            			
            				//if goodboy is on right node and a left rotation is required(only one node is not null)
            			}else if(s.younger == null && s.older != null && s.older.d.getDaysAtTheShelter()>s.d.getDaysAtTheShelter()){
            				goodboy = s.older;
            				s.older = goodboy.younger;
            				if(s.older!=null) s.older.parent = s;
            				goodboy.parent = s.parent;
            				s.parent = goodboy;
            				goodboy.younger = s;
            				if(goodboy.parent!= null) {
            					if(goodboy.parent.younger == s) {
            						goodboy.parent.younger = goodboy;
            					}else {
            						goodboy.parent.older = goodboy;
            					}
            				}
            				
            				if(s.younger == null && s.older == null) break;
            				
            				//if goodboy is on left node and a right rotation is required(only one node is not null)
            			}else if(s.older == null && s.younger != null && s.younger.d.getDaysAtTheShelter()>s.d.getDaysAtTheShelter()) {
            				goodboy = s.younger;
            				s.younger = goodboy.older;
            				if(s.younger!= null) s.younger.parent = s;
            				goodboy.parent = s.parent;
            				s.parent = goodboy;
            				goodboy.older = s;
            				if(goodboy.parent!=null) {
            					if(goodboy.parent.younger == s) {
            						goodboy.parent.younger = goodboy;
            					}else {
            						goodboy.parent.older = goodboy;
            					}
            				}
            				
            				if(s.younger == null && s.older == null) break;
            			}
            			
            			
            			
            			//check if heap order is restored yet
            			if(s.younger == null && s.older == null) {
            				break;
            			}else if(s.younger == null) {
            				if(s.d.getDaysAtTheShelter()>s.older.d.getDaysAtTheShelter()) break;
            			}else if(s.older == null) {
            				if(s.d.getDaysAtTheShelter()>s.younger.d.getDaysAtTheShelter()) break;
            			}else {
            				if(s.d.getDaysAtTheShelter()>s.younger.d.getDaysAtTheShelter() && s.d.getDaysAtTheShelter()>s.older.d.getDaysAtTheShelter()) break;
            			}
            		}
            		out=s;   
            		while(true) {
            			if(out.parent == null)break;
            			out = out.parent;
            		}
            	}
            	
            	
            	
            }
            
            return out;
            
		}

		@SuppressWarnings("unused")
		public Dog findOldest() {
            if (this == null) {
            	return null;
            }else if(this.older == null) {
            	return this.d;
            }else {
            	return this.older.findOldest();
            }
		}
		
		
		public DogNode findOldest(DogNode root) {
			if(root == null) {
				return root;
			}else if(root.older == null) {
				return root;
			}else {
				return findOldest(root.older);
			}
		}

		@SuppressWarnings("unused")
		public Dog findYoungest() {
            if(this == null) {
            	return null;
            }else if(this.younger==null){
            	return this.d;
            }else {
            	return this.younger.findYoungest();
            }
            
		}

		@SuppressWarnings("unused")
		public Dog findDogToAdopt(int minAge, int maxAge) {
           
			DogNode node = this;
			DogNode minpointer=node;
			DogNode maxpointer=node;
			
			while(true) {
				if(node.d.getAge()>minAge) {
					node = node.younger;
					if(node==null)break;
					minpointer=node;
				}else if(node.d.getAge()<minAge) {
					if(node.older==null || node.older.d.getAge()>minAge) {
						if(node.older==null) {
							node = node.parent;
							minpointer=node;
							break;
						}else {
							node=node.older;
							minpointer = node;
						}
					}
				}else if(node.d.getAge()==minAge) {
					break;
				}
			}
			node=this;
			while(true) {
				if(node.d.getAge()<maxAge) {
					node = node.older;
					if(node == null) break;
					maxpointer = node;
				}else if(node.d.getAge()>maxAge) {
					if(node.younger==null || node.younger.d.getAge()<maxAge) {
						if(node.younger==null) {
							node=node.parent;
							maxpointer = node;
							break;
						}else {
							node=node.younger;
							maxpointer=node;
						}	
					}
				}else if(node.d.getAge()==maxAge) {
					break;
				}
			}
			
			while(!(minpointer.d.getAge()>maxpointer.d.getAge())) {
				if(minpointer.parent == null) break;
				if(minpointer.parent.d.getAge()>maxpointer.d.getAge())break;
				minpointer=minpointer.parent;
			}
			return minpointer.d;
		}
		
		
		public double budgetVetExpenses(int numDays) {
            return budgetVetExpenses(numDays, this);	
		}
		
		public double budgetVetExpenses(int numDays, DogNode root) {
			if(root==null) 
            	return 0;
            if(root.d.getDaysToNextVetAppointment()>numDays)
            	return 0+budgetVetExpenses(numDays, root.younger)+budgetVetExpenses(numDays, root.older);
            return (root.d.getExpectedVetCost()+budgetVetExpenses(numDays, root.younger)+budgetVetExpenses(numDays, root.older));
			 
		}

		public ArrayList<ArrayList<Dog>> getVetSchedule() {
            ArrayList<ArrayList<Dog>> out= new ArrayList<ArrayList<Dog>>();
            ArrayList<DogNode> traverse = new ArrayList<DogNode>();
            DogNode curr=this;

            while(curr!=null || traverse.size()>0) {
            	
            	while(curr != null) {
            		traverse.add(curr);
            		curr = curr.younger;
            	}
            	curr = traverse.get(traverse.size()-1);
            	int x = (curr.d.getDaysToNextVetAppointment()/7);
            	if(out.size()<(x+1)) {
            		ArrayList<Dog> list = new ArrayList<Dog>();
            		list.add(curr.d);
            		
            		if(out.size()==0) {
            			int n=0;
            			while(!(n>x)) {
            				ArrayList<Dog> list1= new ArrayList<Dog>();
            				out.add(n, list1);
            				n++;
            			}
            		}else {
            			int n = out.size()-1;
            			while(n!=x) {
            				ArrayList<Dog> list1= new ArrayList<Dog>();
            				out.add(n+1, list1);
            				n++;
            			}
            		}
            		out.get(x).add(curr.d);
            	}else {
            		out.get(x).add(curr.d);
            	}
            	
            	traverse.remove(curr);
            	curr=curr.older;
            }
            
            return out;
		}

		public String toString() {
			String result = this.d.toString() + "\n";
			if (this.younger != null) {
				result += "younger than " + this.d.toString() + " :\n";
				result += this.younger.toString();
			}
			if (this.older != null) {
				result += "older than " + this.d.toString() + " :\n";
				result += this.older.toString();
			}
			/*if (this.parent != null) {
				result += "parent of " + this.d.toString() + " :\n";
				result += this.parent.d.toString() +"\n";
			}*/
			return result;
		}
		
	}


	private class DogShelterIterator implements Iterator<Dog> {
		int current;
		ArrayList<Dog> treelist;
		
		
		private DogShelterIterator() {
			this.treelist = new ArrayList<Dog>();
			DogNode start = root;
			this.current=-1;	
			this.traverse(start);
		}
		
		public void traverse(DogNode x) {
			
			if(x == null) {
				return;
			}
			
			this.traverse(x.younger);
			
			this.treelist.add(x.d);
	
			this.traverse(x.older);
			
		}

		public Dog next(){
			this.current++;
            return this.treelist.get(this.current);
		}

		public boolean hasNext() {
            return this.current+1<this.treelist.size();
		}

	}
	
	

}
