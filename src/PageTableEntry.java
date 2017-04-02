
public class PageTableEntry {
	
	boolean valid;
	boolean dirty;
	boolean referenced;
	int index;
	int frame;
	
	public PageTableEntry(){
		//create the PTE object 
		//just set the vlaues to false and index to 0 and frame to 0
		
		
		this.valid = false;
		this.dirty = false;
		this.referenced = false;
		this.index = 0;
		this.frame = 0;
	}
	


	
	
}
