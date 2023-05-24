package com.avereon.zerra;

import com.avereon.product.ProductCard;
import com.avereon.xenon.Mod;

public abstract class BaseModUIT extends BaseXenonUIT {

	private Mod mod;

	protected void initMod( ProductCard card ) {
		this.mod = CommonModTestStuff.initMod( getProgram(), card );
	}

	protected Mod getMod() {
		return mod;
	}

}
