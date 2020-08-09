module com.avereon.zarra {

	requires transitive org.junit.jupiter.api;
	requires transitive org.testfx;
	requires transitive org.testfx.junit5;
	requires javafx.graphics;
	requires org.hamcrest;

	exports com.avereon.zarra.test;

}