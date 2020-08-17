package project.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class _Generalise implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 4782484985200467838L;
	List<_Subset> subsets; 
	public List<_Subset> getSubsets() {
		return subsets;
	}
	private _Entity ent;
	public void removeSubset (_Subset sub) {
		if(subsets.contains(sub)) {
			subsets.remove(sub);
		}
	}
	
	public void addSubset (_Subset sub) {
		subsets.add(sub);
	}
	public _Generalise(_Entity ent) {
		this.ent = ent;
		subsets = new ArrayList<_Subset>();
	}
	public _Entity getEnt() {
		return ent;
	}
}
