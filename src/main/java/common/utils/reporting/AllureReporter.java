package common.utils.reporting;

import io.qameta.allure.Allure;

import java.io.*;

/** Reporter backed by Allure attachments. */
public class AllureReporter implements EvidenceReporter {
    @Override
    public void logScreenshot(File file, String message) {
        attach(file, message, "image/png", "png");
    }

    @Override
    public void logTrace(File file, String message) {
        attach(file, message, "application/zip", "zip");
    }

    private void attach(File file, String name, String type, String extension) {
        try (InputStream in = new FileInputStream(file)) {
            Allure.addAttachment(name, type, in, extension);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

