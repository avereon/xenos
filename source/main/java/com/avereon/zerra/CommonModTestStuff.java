package com.avereon.zerra;

import com.avereon.product.ProductCard;
import com.avereon.product.Rb;
import com.avereon.xenon.Mod;
import com.avereon.xenon.Xenon;

import static org.assertj.core.api.Assertions.assertThat;

public interface CommonModTestStuff {

	static Mod initMod( Xenon program, ProductCard card ) {
		Mod mod = program.getProductManager().getMod( card.getProductKey() );

		program.getProductManager().setModEnabled( card, true );
		assertThat( program.getProductManager().isEnabled( card ) ).withFailMessage( "Module not ready for testing: " + mod ).isTrue();

		mod.init( program, ProductCard.card( mod ) );
		Rb.init( mod );

		return mod;
	}

}
