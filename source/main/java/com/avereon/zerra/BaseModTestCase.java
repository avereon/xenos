package com.avereon.zerra;

import com.avereon.product.ProductCard;
import com.avereon.xenon.Module;
import org.junit.jupiter.api.BeforeEach;

// FIXME This should not extend BaseFullXenonTestCase in order to be a unit test base class
public class BaseModTestCase<T extends Module> extends BaseFullXenonTestCase {

	private final Class<T> type;

	private Module module;

	protected BaseModTestCase( Class<T> type ) {
		this.type = type;
	}

	@BeforeEach
	protected void setup() throws Exception{
		super.setup();
		this.module = CommonModTestStuff.initMod( getProgram(), ProductCard.card( type ) );
	}

	protected Module getMod() {
		return module;
	}

}
