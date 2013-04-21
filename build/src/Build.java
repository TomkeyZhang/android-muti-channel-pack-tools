import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.ObjectInputStream.GetField;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Build {
	private static String target = "17";
	 private static final String DIR=new File("").getAbsolutePath();
//	private static final String DIR = "D:\\android\\git-repository\\gmusic";
	private static final Set<String> set = new HashSet<String>();

	public static void main(String[] args) {
		if (args != null && args.length > 0) {
			target = args[0];
		}
		buildAll();
		// System.out.println("..\\");
	}

	private static String getAbsPath(String dir, String path) {
		String absPath = dir;
		path = path.replaceAll("/", "\\\\");
		while (path.startsWith("..\\")) {
			path = path.replaceFirst("..\\\\", "");
			absPath = absPath.substring(0, absPath.lastIndexOf("\\"));
		}
		absPath = absPath + "\\" + path;
		System.out.println(absPath + ":" + new File(absPath).exists());
		return absPath;
	}

	private static void buildAll() {
		StringBuilder sb = new StringBuilder("call android update project");
		sb.append(" --target ").append(target);
		sb.append(" --name ").append(getProjectName());
		sb.append(" --path ").append(DIR);
		buildLibProjectsCmd(DIR, sb, getProperties("project.properties"));
		write(sb.toString(), "build.bat");
		System.out.println("success generate build.bat");
	}

	private static void buildLibProjectsCmd(String dir, StringBuilder sb,
			Properties p) {
		for (Object object : p.keySet()) {
			String key = object.toString();
			if (key.startsWith("android.library.reference")) {
				buildLibProjectCmd(dir, sb, p.getProperty(key));
			}
		}
	}

	private static void buildLibProjectCmd(String dir, StringBuilder sb,
			String path) {
		path = getAbsPath(dir, path);
		if (!set.contains(path)) {
			set.add(path);
			sb.append("\ncall android update lib-project");
			sb.append(" --target ").append(target);
			sb.append(" --path ").append(path);
			buildLibProjectsCmd(path, sb, getProperties(path
					+ "\\project.properties"));
			System.out.println("add path cmd: "+path);
		}
	}

	public static Properties getProperties(String fileName) {
		Properties p = new Properties();
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(fileName));
			p.load(in);
		} catch (FileNotFoundException e) {
			System.err.println(fileName + " 文件不存在！");
		} catch (IOException e) {
			System.err.println("读取" + fileName + " 文件失败！");
		}
		return p;
	}

	public static String getProjectName() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(".project");
			return doc.getElementsByTagName("name").item(0).getTextContent();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("project name not found！");
	}

	public static boolean write(String cont, String dist) {
		try {
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(new File(dist)), "utf-8");
			writer.write(cont);
			writer.flush();
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
