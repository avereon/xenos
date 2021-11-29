module com.avereon.zerra {

	// Compile-time only
	requires static lombok;

	// Both compile-time and run-time
	requires com.avereon.xenon;
	requires org.assertj.core;
	requires org.junit.jupiter.api;
	requires org.testfx;
	requires org.testfx.junit5;

	requires java.logging;
	requires javafx.graphics;

	//exports com.avereon.zerra.test;

}
