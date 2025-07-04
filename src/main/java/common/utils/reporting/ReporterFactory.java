package common.utils.reporting;

/**
 * Discovers the best available {@link EvidenceReporter} based on class‑path inspection.
 */
public final class ReporterFactory {
    private ReporterFactory() {}

    public static EvidenceReporter resolve() {
        if (isPresent("io.qameta.allure.Allure")) {
            return new AllureReporter();
        }
        if (isPresent("com.epam.reportportal.utils.files.Utils")) {
            return new ReportPortalReporter();
        }
        return new ConsoleReporter();
    }

    private static boolean isPresent(String fqcn) {
        try { Class.forName(fqcn); return true; } catch (ClassNotFoundException e) { return false; }
    }
}
