package com.avereon.xenos;

import com.avereon.product.ProductCard;
import com.avereon.product.Rb;
import com.avereon.settings.MapSettings;
import com.avereon.xenon.*;
import com.avereon.xenon.Module;
import com.avereon.xenon.asset.ResourceManager;
import com.avereon.xenon.index.IndexService;
import com.avereon.xenon.notice.NoticeManager;
import com.avereon.xenon.task.TaskManager;
import javafx.beans.property.SimpleStringProperty;
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
	protected ResourceManager resourceManager;

	@Mock
	protected SettingsManager settingsManager;

	@Mock
	protected ToolManager toolManager;

	@Mock
	protected IndexService indexService;

	@Mock
	protected NoticeManager noticeManager;

	@Mock
	protected WorkspaceManager workspaceManager;

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
		lenient().when( program.getResourceManager() ).thenReturn( resourceManager );
		lenient().when( program.getSettingsManager() ).thenReturn( settingsManager );
		lenient().when( program.getWorkspaceManager() ).thenReturn( workspaceManager );
		lenient().when( program.getToolManager() ).thenReturn( toolManager );
		lenient().when( program.getIndexService() ).thenReturn( indexService );
		lenient().when( program.getNoticeManager() ).thenReturn( noticeManager );
		lenient().when( program.getSettings() ).thenCallRealMethod();

		// Settings Manager
		lenient().when( settingsManager.getSettings( any( String.class ) ) ).thenReturn( new MapSettings() );
		lenient().when( settingsManager.getProductSettings( any( ProductCard.class ) ) ).thenReturn( new MapSettings() );

		// Product Manager
		//ProductManager productManager = getProgram().getProductManager();
		//lenient().when( program.getProductManager() ).thenReturn( productManager );

		// Workspace Manager
		ThemeMetadata themeMetadata = new ThemeMetadata("test", "Test Theme", true, "file:/test");
		lenient().when( workspaceManager.getThemeMetadata() ).thenReturn( themeMetadata );
		lenient().when( workspaceManager.themeIdProperty() ).thenReturn( new SimpleStringProperty( WorkspaceManager.DEFAULT_THEME_ID ) );

		// Load the module if it has not already been loaded
		if( module == null ) {
			module = type.getDeclaredConstructor().newInstance();
			module.init( program, module.getCard() );
			module.setParent( program );
			module.startup();
		}
	}

	@Override
	protected Xenon getProgram() {
		// Overridden to use the mock program
		return program;
	}

	protected Module getMod() {
		return module;
	}

}
