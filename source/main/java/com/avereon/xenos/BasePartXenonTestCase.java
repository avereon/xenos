package com.avereon.xenos;

import com.avereon.util.Parameters;
import com.avereon.xenon.Xenon;
import com.avereon.xenon.test.ProgramTestConfig;
import org.junit.jupiter.api.BeforeEach;

/**
 * This class is a duplicate of com.avereon.zenna.BaseXenonUiTestCase which is
 * intended to be visible for mod testing but is not available to Xenon to
 * avoid a circular dependency. Attempts at making this
 * class publicly available have run in to various challenges with the most
 * recent being with Surefire not putting JUnit 5 on the module path.
 */
public abstract class BasePartXenonTestCase extends BaseXenonTestCase {

	@BeforeEach
	protected void setup() throws Exception {
		super.setup();

		// Create the program
		Xenon xenon = getProgram();
		xenon.setProgramParameters( Parameters.parse( ProgramTestConfig.getParameters() ) );
		xenon.init();
	}

}
