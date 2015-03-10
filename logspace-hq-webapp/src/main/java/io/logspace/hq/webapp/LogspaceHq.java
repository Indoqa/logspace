package io.logspace.hq.webapp;

import java.io.IOException;

import spark.utils.IOUtils;

import com.indoqa.spark.AbstractSparkApplication;

public class LogspaceHq extends AbstractSparkApplication {

    public static void main(String[] args) {
        new LogspaceHq().invoke();
    }

    @Override
    public void beforeInitialization() {
        this.printLogo();
    }

    @Override
    protected String getApplicationName() {
        return "Logspace";
    }

    protected String getAsciiLogo() throws IOException {
        return IOUtils.toString(LogspaceHq.class.getResourceAsStream("/logspace.io.txt"));
    }

    @Override
    protected String getComponentScanBasePackage() {
        return LogspaceHq.class.getPackage().getName();
    }

    @Override
    protected void initializeSpringBeans() {
        // nothing to do yet
    }

    @Override
    protected boolean isDevEnvironment() {
        return true;
    }

    private void printLogo() {
        try {
            String asciiLogo = IOUtils.toString(LogspaceHq.class.getResourceAsStream("/logspace.io.txt"));
            if (asciiLogo == null) {
                return;
            }
            this.getInitialzationLogger().info(asciiLogo);
        } catch (IOException e) {
            throw new ApplicationInitializationException("Error while reading ASCII logo.", e);
        }
    }
}
