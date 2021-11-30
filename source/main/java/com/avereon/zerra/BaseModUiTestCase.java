package com.avereon.zerra;

import com.avereon.product.ProductCard;
import com.avereon.xenon.Mod;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseModUiTestCase extends BaseXenonUiTestCase{

	private Mod mod;

	protected void initMod( ProductCard card ) {
		this.mod = getProgram().getProductManager().getMod( card.getProductKey() );

		getProgram().getProductManager().setModEnabled( card, true );
		assertThat( getProgram().getProductManager().isEnabled( card ) ).withFailMessage( "Module not ready for testing: " + mod ).isTrue();
	}

	protected Mod getMod() {
		return mod;
	}

}
