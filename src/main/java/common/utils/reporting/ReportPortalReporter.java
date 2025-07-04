package common.utils.reporting;

import common.utils.LoggingUtils;

import java.io.File;

/** Reporter that delegates to existing ReportPortal logging helper. */
public class ReportPortalReporter implements EvidenceReporter {
    @Override
    public void logScreenshot(File file, String message) {
        LoggingUtils.log(file, message);
    }

    @Override
    public void logTrace(File file, String message) {
        LoggingUtils.log(file, message);
    }
}
