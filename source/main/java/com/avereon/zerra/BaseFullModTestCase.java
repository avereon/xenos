package com.avereon.zerra;

import com.avereon.product.ProductCard;
import com.avereon.xenon.Mod;

public class BaseFullModTestCase extends BaseFullXenonTestCase {

	private Mod mod;

	protected void initMod( ProductCard card ) {
		this.mod = CommonModTestStuff.initMod( getProgram(), card );
	}

	protected Mod getMod() {
		return mod;
	}

}
