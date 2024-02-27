package com.avereon.zerra;

import com.avereon.product.ProductCard;
import com.avereon.xenon.Mod;
import org.junit.jupiter.api.BeforeEach;

// FIXME This should not extend BaseFullXenonTestCase in order to be a unit test base class
public class BaseModTestCase<T extends Mod> extends BaseFullXenonTestCase {

	private final Class<T> type;

	private Mod mod;

	protected BaseModTestCase( Class<T> type ) {
		this.type = type;
	}

	@BeforeEach
	protected void setup() throws Exception{
		super.setup();
		this.mod = CommonModTestStuff.initMod( getProgram(), ProductCard.card( type ) );
	}

	protected Mod getMod() {
		return mod;
	}

}
