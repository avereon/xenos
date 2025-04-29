module com.avereon.xenos {

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

	exports com.avereon.xenos;

	opens com.avereon.xenos to org.testfx.junit5;
}
