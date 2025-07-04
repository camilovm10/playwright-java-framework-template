package common.shared;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.microsoft.playwright.Page;

public class PlaywrightPageProvider implements Provider<Page> {

    @Inject
    private BrowserContextHolder contextHolder;

    @Override
    public Page get() {
        return contextHolder.getDefault().getNewPage();
    }
}
