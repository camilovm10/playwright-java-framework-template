package common;

import com.google.inject.Inject;
import com.microsoft.playwright.Page;
import common.exceptions.PageCreationException;
import common.utils.TimeOuts;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class BasePage {

    protected Page page;

    @Inject
    public BasePage(Page page) {
        this.page = page;
        this.page.setDefaultNavigationTimeout(TimeOuts.ONE_MINUTE.getValue());
    }

    public <T extends BasePage> T createWebPageInstance(Class<T> tClass) {
        try {
            return tClass.getDeclaredConstructor(Page.class).newInstance(page);
        } catch (Exception ex) {
            log.error("Error creating Page instance", ex);
            throw new PageCreationException("Error creating Webpage Instance.", ex);
        }
    }

    public <T extends BasePage> T createWebPageInstance(Class<T> tClass, Page newPage) {
        try {
            return tClass.getDeclaredConstructor(Page.class).newInstance(newPage);
        } catch (ReflectiveOperationException e) {
            throw new PageCreationException("Error creating Page Instance with page", e);
        }
    }
}
