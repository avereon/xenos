package com.avereon.zerra;

import com.avereon.product.ProductCard;
import com.avereon.xenon.Mod;

public abstract class BasePartModTestCase extends BasePartXenonTestCase {

	// NOTE This class is pretty much useless because in order to register the mod
	// the ProductManager must be created and that doesn't happen in Xenon.init().

	private Mod mod;

	protected void initMod( ProductCard card ) {
		this.mod = CommonModTestStuff.initMod( getProgram(), card );
	}

	protected Mod getMod() {
		return mod;
	}

}
