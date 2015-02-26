package io.logspace.hq.webapp;

import java.io.IOException;

import spark.utils.IOUtils;

import com.indoqa.spark.AbstractSparkApplication;

public class LogspaceHq extends AbstractSparkApplication {

    public static void main(String[] args) throws IOException {
        new LogspaceHq().invoke();
    }

    @Override
    protected String getApplicationName() {
        return "Logspace";
    }

    @Override
    protected String getAsciiLogo() throws IOException {
        return IOUtils.toString(LogspaceHq.class.getResourceAsStream("/logspace.io.txt"));
    }

    @Override
    protected String getComponentScanBasePackage() {
        return LogspaceHq.class.getPackage().getName();
    }

    @Override
    protected void initializeSpringBeans() {
        // nothing to to yet
    }

    @Override
    protected boolean isDevEnvironment() {
        return true;
    }
}
