package com.avereon.zerra;

import com.avereon.product.ProductCard;
import com.avereon.xenon.Mod;

public class BaseModUiTestCase extends BaseXenonUiTestCase{

	private Mod mod;

	protected void initMod( ProductCard card ) {
		this.mod = getProgram().getProductManager().getMod( card.getProductKey() );

		getProgram().getProductManager().setModEnabled( card, true );
		//Assertions.assertThat( getProgram().getProductManager().isEnabled( card ) ).withFailMessage( "Module not ready for testing: " + mod ).isTrue();
	}

	protected Mod getMod() {
		return mod;
	}

}
