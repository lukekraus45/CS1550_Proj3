
public class PageTableEntry {
	
	boolean valid;
	boolean dirty;
	boolean referenced;
	int index;
	int frame;
	
	public PageTableEntry(){
		//create the PTE object 
		//just set the vlaues to false and index to 0 and frame to -1
		
		
		this.valid = false;
		this.dirty = false;
		this.referenced = false;
		this.index = 0;
		this.frame = 0;
	}
	
	public PageTableEntry(PageTableEntry copy){
		//create the PTE object 
		//just set the vlaues to false and index to 0 and frame to -1
		
		
		 this.valid = copy.valid;
		this.dirty = copy.dirty;
		this.referenced = copy.referenced;
		this.index = copy.index;
		this.frame = copy.frame;
	}

	
	
}
