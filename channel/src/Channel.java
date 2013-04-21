import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Channel {
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// String oldChannel = args[0];
		// String newChannel = args[1];
		// replaceChannel("channel_name", "appchina");
		replaceChannel(args[0]);
	}

	private static void replaceChannel(String newChannel) {
		try {
			String outPath = "AndroidManifest.xml"; // 输出文件位置
			String content = read(outPath);
			String tmpContent = content;
			String oldChannel = "channel_name";
			String tmp = "tmp";
			if (new File(tmp).exists()) {
				oldChannel = read(tmp).trim();
			}
			if (!tmpContent.contains(oldChannel)) {
				throw new RuntimeException(
						"replace channel error替换渠道出错，在Manifest文件中不存在旧渠道");
			}
			tmpContent = tmpContent.replaceFirst(oldChannel, newChannel);
			write(tmpContent, outPath);
			write(newChannel, tmp);
			System.out.println("replace channel " + oldChannel + "to "
					+ newChannel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String read(String path) {
		StringBuffer res = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(path), "UTF-8"));
			while ((line = reader.readLine()) != null) {
				res.append(line + "\n");
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res.toString();
	}

	private static boolean write(String cont, String dist) {
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
