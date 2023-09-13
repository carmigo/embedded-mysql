package com.wix.mysql.distribution.fileset;

import com.wix.mysql.distribution.Version;
import de.flapdoodle.embed.process.config.store.FileSet;
import de.flapdoodle.embed.process.distribution.Platform;

import java.util.Objects;

import static de.flapdoodle.embed.process.config.store.FileType.Executable;
import static de.flapdoodle.embed.process.config.store.FileType.Library;

public class Win80_20FileSetEmitter implements FileSetEmitter {
    @Override
    public boolean matches(Platform platform, Version version) {
        return !platform.isUnixLike() && Objects.equals(version.getMajorVersion(), "8.0")
                && version.getMinorVersion() > 17 && version.getMinorVersion() <= 20;
    }

    @Override
    public FileSet emit() {
        return FileSet.builder()
                .addEntry(Executable, "bin/mysqld.exe")
                .addEntry(Library, "bin/mysql.exe")
                .addEntry(Library, "bin/mysqladmin.exe")
                .addEntry(Library, "share/english/errmsg.sys")
                .addEntry(Library, "bin/libcrypto-1_1-x64.dll")
                .addEntry(Library, "bin/libprotobuf-lite.dll")
                .addEntry(Library, "bin/libprotobuf.dll")
                .addEntry(Library, "bin/libssl-1_1-x64.dll")
                .build();
    }
}
