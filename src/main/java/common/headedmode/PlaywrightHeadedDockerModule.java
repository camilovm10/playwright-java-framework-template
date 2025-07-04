package common.headedmode;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import common.browser.OverridableBrowserConfiguration;
import common.shared.BrowserContextHolder;
import common.shared.PlaywrightPageProvider;
import common.shared.PlaywrightProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlaywrightHeadedDockerModule extends AbstractModule {

  private final OverridableBrowserConfiguration configuration;


  public PlaywrightHeadedDockerModule(OverridableBrowserConfiguration overridableBrowserConfiguration) {
    this.configuration = overridableBrowserConfiguration;
    log.debug("Headed module with Configuration setting,  {}", configuration);
  }

  @Override
  public void configure() {
    log.trace("Setting up docker-headed playwright module");
    bind(Playwright.class).toProvider(PlaywrightProvider.class).asEagerSingleton();
    bindCustomValues();
    bind(Browser.class).toProvider(BrowserProvider.class).asEagerSingleton();
    bind(BrowserContextHolder.class).toProvider(BrowserContextProvider.class).asEagerSingleton();
    bind(Page.class).toProvider(PlaywrightPageProvider.class).asEagerSingleton();
  }

  private void bindCustomValues() {
    bind(OverridableBrowserConfiguration.class).annotatedWith(Names.named("configuration")).toInstance(configuration);
  }
}
