package com.avereon.zerra;

import com.avereon.product.ProductCard;
import com.avereon.product.Rb;
import com.avereon.settings.MapSettings;
import com.avereon.xenon.Module;
import com.avereon.xenon.*;
import com.avereon.xenon.asset.AssetManager;
import com.avereon.xenon.index.IndexService;
import com.avereon.xenon.notice.NoticeManager;
import com.avereon.xenon.task.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith( MockitoExtension.class )
public class BaseModTestCase<T extends Module> extends BasePartXenonTestCase {

	// Keep this static so it is shared across all tests
	protected static Module module;

	protected static ProductCard programCard;

	protected static ActionLibrary actionLibrary;

	@Mock
	protected Xenon program;

	@Mock
	protected TaskManager taskManager;

	@Mock
	protected IconLibrary iconLibrary;

	@Mock
	protected AssetManager assetManager;

	@Mock
	protected SettingsManager settingsManager;

	@Mock
	protected ToolManager toolManager;

	@Mock
	protected IndexService indexService;

	@Mock
	protected NoticeManager noticeManager;

	private final Class<T> type;

	protected BaseModTestCase( Class<T> type ) {
		this.type = type;
	}

	@BeforeEach
	protected void setup() throws Exception {
		super.setup();

		if( programCard == null ) programCard = ProductCard.info( program.getClass() );
		lenient().when( program.getCard() ).thenReturn( programCard );
		Rb.init( program );
		lenient().when( program.getTaskManager() ).thenReturn( taskManager );
		lenient().when( program.getIconLibrary() ).thenReturn( iconLibrary );
		if( actionLibrary == null ) actionLibrary = new ActionLibrary( program );
		lenient().when( program.getActionLibrary() ).thenReturn( actionLibrary );
		lenient().when( program.getAssetManager() ).thenReturn( assetManager );
		lenient().when( program.getSettingsManager() ).thenReturn( settingsManager );
		lenient().when( program.getToolManager() ).thenReturn( toolManager );
		lenient().when( program.getIndexService() ).thenReturn( indexService );
		lenient().when( program.getNoticeManager() ).thenReturn( noticeManager );

		lenient().when( settingsManager.getProductSettings( any( ProductCard.class ) ) ).thenReturn( new MapSettings() );

		if( module == null ) {
			module = type.getDeclaredConstructor().newInstance();
			module.init( program, module.getCard() );
			module.setParent( program );
			module.startup();
		}
	}

	protected Module getMod() {
		return module;
	}

}
