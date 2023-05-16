package com.avereon.zerra;

import com.avereon.util.OperatingSystem;
import com.avereon.xenon.Xenon;
import org.junit.jupiter.api.BeforeEach;

import java.util.logging.Level;

/**
 * This class is a duplicate of com.avereon.xenon.BaseXenonTestCase which is
 * not visible due to the fact it is in the test folder. Attempts at making the
 * class publicly available have run in to various challenges with the most
 * recent being with Surefire not putting JUnit 5 on the module path.
 */
public abstract class BaseXenonTestCase {

	protected Xenon program;

	@BeforeEach
	protected void setup() throws Exception {
		// Be sure that the OperatingSystem class is properly set
		OperatingSystem.reset();
		program = new Xenon();
		java.util.logging.Logger.getLogger( "" ).setLevel( Level.OFF );
	}

	protected Xenon getProgram() {
		return program;
	}

}
