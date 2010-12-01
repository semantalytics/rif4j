package at.sti2.rif4j;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

public class TestUtils {

	public static Reader getFileReader(String fileName) {
		return new InputStreamReader(TestUtils.class.getClassLoader()
				.getResourceAsStream(fileName));

	}

	public static String[] getRIFTestFiles(String path) {
		File dir = new File(path);
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.endsWith("rif") || name.endsWith("xml"));
			}
		};

		return dir.list(filter);
	}

	public static URI getFileURI(String fileName) {
		try {
			return TestUtils.class.getClassLoader().getResource(fileName)
					.toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return null;
	}

}