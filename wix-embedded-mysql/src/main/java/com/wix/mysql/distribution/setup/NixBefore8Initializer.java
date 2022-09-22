package com.wix.mysql.distribution.setup;

import com.wix.mysql.config.MysqldConfig;
import com.wix.mysql.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.distribution.Platform;
import de.flapdoodle.embed.process.extract.IExtractedFileSet;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NixBefore8Initializer implements Initializer {

    private final static Logger logger = LoggerFactory.getLogger(NixBefore8Initializer.class);
    private static final String SEP = File.separator;

    @Override
    public boolean matches(Version version) {
        return Platform.detect().isUnixLike() &&
            version.getMajorVersion().equals("8.0");
    }

    @Override
    public void apply(IExtractedFileSet files, IRuntimeConfig runtimeConfig, MysqldConfig config) throws IOException {
        File baseDir = files.baseDir();
        File libDir = new File(baseDir + SEP + "lib");
        FileFilter filter = new RegexFileFilter("^[a-z]+\\.so(\\.[0-9]+)+");
        File[] soFiles = libDir.listFiles(filter);
        for (File file : soFiles) {
            Files.createSymbolicLink(Paths.get(baseDir + SEP + "bin" + SEP + file.getName()), Paths.get(file.getPath()));
            logger.debug("Symlink " + file.getName());
        }
    }
}
