package com.avereon.xenos;

import com.avereon.event.EventWatcher;
import com.avereon.log.Log;
import com.avereon.util.*;
import com.avereon.xenon.ProgramEvent;
import com.avereon.xenon.Xenon;
import com.avereon.xenon.test.ProgramTestConfig;
import com.avereon.xenon.workpane.Workpane;
import com.avereon.xenon.workpane.WorkpaneEvent;
import com.avereon.zerra.event.FxEventWatcher;
import com.avereon.zerra.javafx.Fx;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opentest4j.AssertionFailedError;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.TimeUnit;

import static com.avereon.xenon.test.ProgramTestConfig.LONG_TIMEOUT;
import static com.avereon.xenon.test.ProgramTestConfig.TIMEOUT;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class is a duplicate of com.avereon.zenna.BaseXenonUiTestCase which is
 * intended to be visible for mod testing but is not available to Xenon to
 * avoid a circular dependency. Attempts at making this
 * class publicly available have run in to various challenges, with the most
 * recent being with Surefire not putting JUnit 5 on the module path at test
 * time if it is also on the module path at compile time.
 */
@ExtendWith( ApplicationExtension.class )
public abstract class BaseFullXenonTestCase extends BaseXenonTestCase {

	/*
	 * This limit is intentionally very small to detect when the JVM has actually
	 * allocated the memory for the program.
	 */
	private static final long MIN_INITIAL_MEMORY = 32 * SizeUnitBase2.MiB.getSize();

	private EventWatcher programWatcher;

	private FxEventWatcher workpaneWatcher;

	private long initialMemoryUse;

	private long finalMemoryUse;

	/**
	 * Overrides setup() in ApplicationTest and does not call super.setup().
	 */
	@BeforeEach
	protected void setup() throws Exception {
		super.setup();

		// For the parameters to be available using Java 9 or later, the following
		// needs to be added to the test JVM command line parameters because
		// com.sun.javafx.application.ParametersImpl is not exposed, nor is there
		// a "proper" way to access it:
		//
		// --add-opens=javafx.graphics/com.sun.javafx.application=ALL-UNNAMED

		Xenon xenon = getProgram();
		xenon.setProgramParameters( Parameters.parse( ProgramTestConfig.getParameters() ) );
		xenon.register( ProgramEvent.ANY, programWatcher = new EventWatcher( LONG_TIMEOUT ) );

		// NOTE This starts the application so all setup needs to be done by this point
		FxToolkit.setupApplication( () -> xenon );

		programWatcher.waitForEvent( ProgramEvent.STARTED, LONG_TIMEOUT );
		Fx.waitForWithExceptions( LONG_TIMEOUT );

		// Get initial memory use after program is started
		initialMemoryUse = getMemoryUse();
		long initialMemoryUseTimeLimit = System.currentTimeMillis() + TIMEOUT;
		while( initialMemoryUse < MIN_INITIAL_MEMORY && System.currentTimeMillis() < initialMemoryUseTimeLimit ) {
			initialMemoryUse = getMemoryUse();
		}

		assertThat( getProgram() ).withFailMessage( "Program is null" ).isNotNull();
		assertThat( getProgram().getWorkspaceManager() ).withFailMessage( "Workspace manager is null" ).isNotNull();
		assertThat( getProgram().getWorkspaceManager().getActiveWorkspace() ).withFailMessage( "Active workspace is null" ).isNotNull();
		assertThat( getProgram().getWorkspaceManager().getActiveWorkspace().getActiveWorkarea() ).withFailMessage( "Active workarea is null" ).isNotNull();

		Workpane workpane = getProgram().getWorkspaceManager().getActiveWorkspace().getActiveWorkarea();
		workpane.addEventHandler( WorkpaneEvent.ANY, workpaneWatcher = new FxEventWatcher() );
	}

	/**
	 * Override cleanup in FxPlatformTestCase and does not call super.cleanup().
	 */
	@AfterEach
	protected void teardown() throws Exception {
		FxToolkit.cleanupStages();

		Xenon program = getProgram();
		if( program != null ) {
			FxToolkit.cleanupApplication( program );
			programWatcher.waitForEvent( ProgramEvent.STOPPED );
			program.unregister( ProgramEvent.ANY, programWatcher );
		}

		Log.reset();

		// Pause to let things wind down
		//ThreadUtil.pause( TIMEOUT );

		finalMemoryUse = getMemoryUse();
		assertSafeMemoryProfile();

		super.teardown();
	}

	protected void closeProgram() throws Exception {
		closeProgram( false );
	}

	protected void closeProgram( boolean force ) throws Exception {
		Fx.run( () -> getProgram().requestExit( force ) );
		Fx.waitForWithExceptions( 5, TimeUnit.SECONDS );
	}

	protected EventWatcher getProgramEventWatcher() {
		return programWatcher;
	}

	protected Workpane getWorkarea() {
		return getProgram().getWorkspaceManager().getActiveWorkspace().getActiveWorkarea();
	}

	protected FxEventWatcher getWorkareaEventWatcher() {
		return workpaneWatcher;
	}

	protected double getAllowedMemoryGrowthSize() {
		return 20;
	}

	protected double getAllowedMemoryGrowthPercent() {
		return 0.50;
	}

	protected double getAllowedMemoryTotal() {
		return 64;
	}

	private long getMemoryUse() {
		System.gc();

		// Wait for the FX events to finish
		WaitForAsyncUtils.waitForFxEvents();

		// Pause a moment to let the memory use settle down
		ThreadUtil.pause( 50 );

		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	private void assertSafeMemoryProfile() {
		long increaseSize = finalMemoryUse - initialMemoryUse;
		double increaseAbsolute = ((double)increaseSize / (double)SizeUnitBase10.MB.getSize());
		double increasePercent = ((double)finalMemoryUse / (double)initialMemoryUse) - 1.0;

		//		String direction = "";
		//		if( increaseSize > 0 ) direction = "more";
		//		if( increaseSize < 0 ) direction = "less";
		//		increaseSize = Math.abs( increaseSize );
		//System.out.printf( "Memory use: %s -> %s = %s %s%n", FileUtil.getHumanSizeBase2( initialMemoryUse ), FileUtil.getHumanSizeBase2( finalMemoryUse ), FileUtil.getHumanSizeBase2( increaseSize ), direction );

		if( initialMemoryUse > SizeUnitBase2.MiB.getSize() && increaseAbsolute > getAllowedMemoryGrowthSize() ) {
			throw new AssertionFailedError( String.format(
				"Absolute memory growth too large %s -> %s > %s : %s",
				FileUtil.getHumanSizeBase2( initialMemoryUse ),
				FileUtil.getHumanSizeBase2( finalMemoryUse ),
				getAllowedMemoryGrowthSize(),
				FileUtil.getHumanSizeBase2( increaseSize )
			) );
		}
		if( initialMemoryUse > SizeUnitBase2.MiB.getSize() && increasePercent > getAllowedMemoryGrowthPercent() ) {
			throw new AssertionFailedError( String.format(
				"Relative memory growth too large %s -> %s > %s : %.2f%%",
				FileUtil.getHumanSizeBase2( initialMemoryUse ),
				FileUtil.getHumanSizeBase2( finalMemoryUse ),
				getAllowedMemoryGrowthPercent() * 100,
				increasePercent * 100
			) );
		}
	}

}
