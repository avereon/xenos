module com.avereon.zerra {

	// Compile-time only
	requires static lombok;

	// Both compile-time and run-time
	requires com.avereon.xenon;
	requires java.logging;
	requires javafx.graphics;
	requires org.assertj.core;
	requires org.junit.jupiter.api;
	requires org.testfx;
	requires org.testfx.junit5;
	requires org.mockito.junit.jupiter;
	requires org.mockito;

	exports com.avereon.zerra;

	opens com.avereon.zerra to org.testfx.junit5;
}
