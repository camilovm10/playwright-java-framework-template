package common.utils;

import com.microsoft.playwright.Page;
import common.utils.reporting.EvidenceReporter;
import common.utils.reporting.ReporterFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;

import static common.utils.TimeOuts.TWENTY_SECONDS;

/**
 * Utility for capturing screenshots and Playwright tracing artefacts.  Internally delegates to whichever
 * {@link EvidenceReporter} is detected on the class‑path (Allure, ReportPortal, or a console fallback).
 */
@Slf4j
public final class ReportUtils {

    private ReportUtils() {}

    private static final String BUILD_REPORTS_RELATIVE_PATH = "build/reports";
    private static final Path   TRACE_DIR = Paths.get(BUILD_REPORTS_RELATIVE_PATH, "traces");
    private static final EvidenceReporter REPORTER = ReporterFactory.resolve();

    public static void removeOldTraceResults() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(TRACE_DIR, path -> !path.getFileName().toString().equals(".DS_Store"))) {
            for (Path p : stream) Files.deleteIfExists(p);
            log.trace("Removed all old trace results");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void uploadEvidenceOfPage(Page page) {
        takeScreenshotOfPage(page, "Screenshot – " + page.title());
    }

    public static void takeScreenshotOfPage(Page page, String message) {
        try {
            bringPageToFront(page);
            Path screenshotPath = Paths.get(BUILD_REPORTS_RELATIVE_PATH, "screenshot-" + System.currentTimeMillis() + ".png");
            page.screenshot(new Page.ScreenshotOptions().setTimeout(TWENTY_SECONDS.getValue()).setFullPage(false).setPath(screenshotPath));
            REPORTER.logScreenshot(screenshotPath.toFile(), message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void bringPageToFront(Page page) {
        try {
            page.bringToFront();
        } catch (Exception e) {
            log.debug("Could not bring page to front", e);
        }
    }

    public static void uploadTracingZip() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(TRACE_DIR, path -> !path.getFileName().toString().equals(".DS_Store"))) {
            for (Path p : stream) {
                REPORTER.logTrace(p.toFile(), "Trace – " + p.getFileName());
            }
            log.trace("Uploaded all traces");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
