package common.utils.reporting;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/** Fallback reporter that just logs file locations to the console. */
@Slf4j
public class ConsoleReporter implements EvidenceReporter {
    @Override
    public void logScreenshot(File file, String message) {
        log.info("[ConsoleReporter] Screenshot '{}' stored at {}", message, file.getAbsolutePath());
    }

    @Override
    public void logTrace(File file, String message) {
        log.info("[ConsoleReporter] Trace '{}' stored at {}", message, file.getAbsolutePath());
    }
}
