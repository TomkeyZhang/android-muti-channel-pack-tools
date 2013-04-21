import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Main {
	private static final String OUT_DIR = "apk-release/";

	public static void main(String[] args) {
//		args=new String[]{""}; 
		String projectName = getProjectName();
		new File(OUT_DIR).mkdir();
		String unsignPath="bin/" + projectName + "-release-unsigned.apk";
		String signPath=OUT_DIR
				+ projectName + "-" + args[0] + "-" + getVerCodeApkName();
//		signedApk(unsignPath, signPath);
		try {
			copyFile(new File(unsignPath), new File(signPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public static void signedApk(String unsignPath, String signPath) {
//		try {
//			Process process = null;
//			String jarsigner;
//			jarsigner = "jarsigner -keystore " + "xxx.keystore"
//					+ " -storepass xxx -keypass xxx "
//					+ "-signedjar " + signPath + " " + unsignPath + " gmusic"; // 签名apk
//			process = Runtime.getRuntime().exec(jarsigner); // 对apk进行签名
//			new MyThread(process.getErrorStream()).start();
//
//			new MyThread(process.getInputStream()).start();
//			process.waitFor();
//			process.destroy();
//			System.out.println(" signed apk over"); // 一条渠道的打包完成。文件会输出到指定目录
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public static String getVerCodeApkName() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse("AndroidManifest.xml");
			Element manifest = doc.getDocumentElement();
			return manifest.getAttribute("android:versionCode") + "-"
					+ manifest.getAttribute("android:versionName") + ".apk";
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("project name not found！");
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

	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	public static class MyThread extends Thread {
		BufferedReader bf;

		public MyThread(InputStream input) {
			bf = new BufferedReader(new InputStreamReader(input));
		}

		public void run() {
			String line;
			try {
				line = bf.readLine();
				while (line != null) {
					System.out.println(line);
					line = bf.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
