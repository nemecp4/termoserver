package sensorServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {

	public static InputStream tryOpenResource(String resource) {
		InputStream is = null;
		// 1. classpath
		if (is == null) {
			is = Utils.class.getClassLoader().getResourceAsStream(resource);
			if (is != null) {
				log.info("found {} on classpath", resource);
			}
		}
		// 2. FQ Path
		if (is == null) {
			File f = Paths.get(resource).toFile();

			try {
				is = new FileInputStream(f);
				log.info("found {} on path", resource);
			} catch (FileNotFoundException e) {
				is = null;
			}
		}
		if (is == null) {
			File f = Paths.get(System.getProperty("user.dir"), resource).toFile();
			try {
				is = new FileInputStream(f);
				log.info("found {} in user home", resource);
			} catch (FileNotFoundException e) {
				is = null;
			}
		}
		return is;

	}
}
