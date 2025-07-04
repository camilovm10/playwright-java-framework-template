package common.utils.reporting;

import java.io.File;

/**
 * Abstraction for plugging any evidence/attachment mechanism (Allure, ReportPortal, etc.)
 */
public interface EvidenceReporter {
    void logScreenshot(File file, String message);
    void logTrace(File file, String message);
}
