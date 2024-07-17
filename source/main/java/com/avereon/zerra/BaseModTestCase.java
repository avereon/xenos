package com.avereon.zerra;

import com.avereon.xenon.Module;
import org.junit.jupiter.api.BeforeEach;

public class BaseModTestCase<T extends Module> extends BasePartXenonTestCase {

	private final Class<T> type;

	private Module module;

	protected BaseModTestCase( Class<T> type ) {
		this.type = type;
	}

	@BeforeEach
	protected void setup() throws Exception{
		super.setup();

		// FIXME This works when its the full application, not for part
		// But we want it to work for part application tests
		//this.module = CommonModTestStuff.initMod( getProgram(), ProductCard.card( type ) );
	}

	protected Module getMod() {
		return module;
	}

}
