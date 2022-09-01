package com.wix.mysql.distribution.setup;

import com.wix.mysql.config.MysqldConfig;
import com.wix.mysql.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.extract.IExtractedFileSet;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class Mysql57Initializer implements Initializer {
    @Override
    public boolean matches(Version version) {
        return version.getMajorVersion().equals("5.7");
    }

    @Override
    public void apply(IExtractedFileSet files, IRuntimeConfig runtimeConfig, MysqldConfig config) throws IOException {
        File baseDir = files.baseDir();
        FileUtils.deleteDirectory(new File(baseDir, "data"));

        List<String> systemVariables = new ArrayList<>();
        systemVariables.add(files.executable().getAbsolutePath());
        systemVariables.add("--no-defaults");
        systemVariables.add("--initialize-insecure");
        systemVariables.add("--ignore-db-dir");
        systemVariables.add(format("--basedir=%s", baseDir));
        systemVariables.add(format("--datadir=%s/data", baseDir));

        List<String> configSystemVariables = config.getServerVariables().stream()
                .filter(MysqldConfig.ServerVariable::isInitializer)
                .map(MysqldConfig.ServerVariable::toCommandLineArgument)
                .collect(Collectors.toList());
        systemVariables.addAll(configSystemVariables);

        Process p = Runtime.getRuntime().exec(systemVariables.toArray(new String[0]));

        new ProcessRunner(files.executable().getAbsolutePath()).run(p, runtimeConfig, config.getTimeout(NANOSECONDS));
    }
}
