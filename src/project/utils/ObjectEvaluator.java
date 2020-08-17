package project.utils;

import project.model.Model;
import project.model._Attribute;

public abstract class ObjectEvaluator {
	
	 Model model;
	 SqlGenerator gen;
	 final String oneone = "OneToOne";
	 final String onemany = "OneToMany";
	 final String manymany = "ManyToMany";
	 final String nary = "Nary";
	 

	public String identiferNames(_Attribute attr) {
			String names = "";
			int k = 1;
			for(int j =0; j<attr.getIdentifiers().size();j++) {
				if(k+j>= attr.getIdentifiers().size()) {
					names += attr.getIdentifiers().get(j).getName();
				}
				else {
					names += attr.getIdentifiers().get(j).getName() + " ";
				}
			}
			return names;
			
		}

}
