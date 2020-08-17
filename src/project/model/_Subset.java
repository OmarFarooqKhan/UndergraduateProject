package project.model;

import java.io.Serializable;

public class _Subset implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2427454064507005111L;
	
	private _Entity mine;
	private _Generalise gen;
	public _Subset(_Entity mine, _Generalise gen) {
		this.setMine(mine);
		this.setGen(gen);
	}
	public _Entity getMine() {
		return mine;
	}
	private void setMine(_Entity mine) {
		this.mine = mine;
	}
	public _Generalise getGen() {
		return gen;
	}
	private void setGen(_Generalise gen) {
		this.gen = gen;
	}

}
