package org.xhxin.common.util;

import ch.qos.logback.core.util.FileUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件类型判断工具类
 *
 * <p>此工具根据文件的前几位bytes猜测文件类型，对于文本、zip判断不准确，对于视频、图片类型判断准确</p>
 *
 * <p>需要注意的是，xlsx、docx等Office2007格式，全部识别为zip，因为新版采用了OpenXML格式，这些格式本质上是XML文件打包为zip</p>
 *
 * @author Looly
 */
public class FileTypeUtil {

	private static final Map<String, String> fileTypeMap;
	/**
	 * 用于建立十六进制字符的输出的小写字符数组
	 */
	private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	/**
	 * 用于建立十六进制字符的输出的大写字符数组
	 */
	private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	static {
		fileTypeMap = new ConcurrentHashMap<>();

		fileTypeMap.put("ffd8ff", "jpg"); // JPEG (jpg)
		fileTypeMap.put("89504e47", "png"); // PNG (png)
		fileTypeMap.put("4749463837", "gif"); // GIF (gif)
		fileTypeMap.put("4749463839", "gif"); // GIF (gif)
		fileTypeMap.put("49492a00227105008037", "tif"); // TIFF (tif)
		fileTypeMap.put("424d228c010000000000", "bmp"); // 16色位图(bmp)
		fileTypeMap.put("424d8240090000000000", "bmp"); // 24位位图(bmp)
		fileTypeMap.put("424d8e1b030000000000", "bmp"); // 256色位图(bmp)
		fileTypeMap.put("41433130313500000000", "dwg"); // CAD (dwg)
		fileTypeMap.put("7b5c727466315c616e73", "rtf"); // Rich Text Format (rtf)
		fileTypeMap.put("38425053000100000000", "psd"); // Photoshop (psd)
		fileTypeMap.put("46726f6d3a203d3f6762", "eml"); // Email [Outlook Express 6] (eml)
		fileTypeMap.put("5374616E64617264204A", "mdb"); // MS Access (mdb)
		fileTypeMap.put("252150532D41646F6265", "ps");
		fileTypeMap.put("255044462d312e", "pdf"); // Adobe Acrobat (pdf)
		fileTypeMap.put("2e524d46000000120001", "rmvb"); // rmvb/rm相同
		fileTypeMap.put("464c5601050000000900", "flv"); // flv与f4v相同
		fileTypeMap.put("00000020667479706d70", "mp4");
		fileTypeMap.put("49443303000000002176", "mp3");
		fileTypeMap.put("000001ba210001000180", "mpg"); //
		fileTypeMap.put("3026b2758e66cf11a6d9", "wmv"); // wmv与asf相同
		fileTypeMap.put("52494646e27807005741", "wav"); // Wave (wav)
		fileTypeMap.put("52494646d07d60074156", "avi");
		fileTypeMap.put("4d546864000000060001", "mid"); // MIDI (mid)
		fileTypeMap.put("526172211a0700cf9073", "rar");// WinRAR
		fileTypeMap.put("235468697320636f6e66", "ini");
		fileTypeMap.put("504B03040a0000000000", "jar");
		fileTypeMap.put("504B0304140008000800", "jar");
		// MS Excel 注意：word、msi 和 excel的文件头一样
		fileTypeMap.put("d0cf11e0a1b11ae10", "xls");
		fileTypeMap.put("504B0304", "zip");
		fileTypeMap.put("4d5a9000030000000400", "exe");// 可执行文件
		fileTypeMap.put("3c25402070616765206c", "jsp");// jsp文件
		fileTypeMap.put("4d616e69666573742d56", "mf");// MF文件
		fileTypeMap.put("7061636b616765207765", "java");// java文件
		fileTypeMap.put("406563686f206f66660d", "bat");// bat文件
		fileTypeMap.put("1f8b0800000000000000", "gz");// gz文件
		fileTypeMap.put("cafebabe0000002e0041", "class");// bat文件
		fileTypeMap.put("49545346030000006000", "chm");// bat文件
		fileTypeMap.put("04000000010000001300", "mxp");// bat文件
		fileTypeMap.put("6431303a637265617465", "torrent");
		fileTypeMap.put("6D6F6F76", "mov"); // Quicktime (mov)
		fileTypeMap.put("FF575043", "wpd"); // WordPerfect (wpd)
		fileTypeMap.put("CFAD12FEC5FD746F", "dbx"); // Outlook Express (dbx)
		fileTypeMap.put("2142444E", "pst"); // Outlook (pst)
		fileTypeMap.put("AC9EBD8F", "qdf"); // Quicken (qdf)
		fileTypeMap.put("E3828596", "pwl"); // Windows Password (pwl)
		fileTypeMap.put("2E7261FD", "ram"); // Real Audio (ram)
	}

	/**
	 * 增加文件类型映射<br>
	 * 如果已经存在将覆盖之前的映射
	 *
	 * @param fileStreamHexHead 文件流头部Hex信息
	 * @param extName           文件扩展名
	 * @return 之前已经存在的文件扩展名
	 */
	public static String putFileType(String fileStreamHexHead, String extName) {
		return fileTypeMap.put(fileStreamHexHead.toLowerCase(), extName);
	}

	/**
	 * 移除文件类型映射
	 *
	 * @param fileStreamHexHead 文件流头部Hex信息
	 * @return 移除的文件扩展名
	 */
	public static String removeFileType(String fileStreamHexHead) {
		return fileTypeMap.remove(fileStreamHexHead.toLowerCase());
	}

	/**
	 * 根据Url获取网络资源获得文件类型
	 * @param url 网络资源地址
	 * @return 文件类型，未找到为<code>jpg</code>
	 * @throws IOException 读取流引起的异常
	 */
	public static String getUrlType(String url)throws IOException{
		try (InputStream inputStream = new URL(url).openStream()){
			String type = getType(inputStream);
			return type == null ? "jpg" : type;
		}
	}


	/**
	 * 根据文件流的头部信息获得文件类型
	 *
	 * @param fileStreamHexHead 文件流头部16进制字符串
	 * @return 文件类型，未找到为<code>null</code>
	 */
	public static String getType(String fileStreamHexHead) {
		for (Map.Entry<String, String> fileTypeEntry : fileTypeMap.entrySet()) {
			if (StringUtils.startsWithIgnoreCase(fileStreamHexHead, fileTypeEntry.getKey())) {
				return fileTypeEntry.getValue();
			}
		}
		return null;
	}

	/**
	 * 根据文件流的头部信息获得文件类型
	 *
	 * @param in {@link InputStream}
	 * @return 类型，文件的扩展名，未找到为<code>null</code>
	 * @throws IOException 读取流引起的异常
	 */
	public static String getType(InputStream in) throws IOException {
		return getType(readHex28Upper(in));
	}

	/**
	 * 从流中读取前28个byte并转换为16进制，字母部分使用大写
	 *
	 * @param in {@link InputStream}
	 * @return 16进制字符串
	 */
	public static String readHex28Upper(InputStream in) throws IOException {
		return readHex(in, 28, false);
	}
	/**
	 * 读取16进制字符串
	 *
	 * @param in {@link InputStream}
	 * @param length 长度
	 * @param toLowerCase true 传换成小写格式 ， false 传换成大写格式
	 * @return 16进制字符串
	 * @throws IOException IO异常
	 */
	public static String readHex(InputStream in, int length, boolean toLowerCase) throws IOException {
		return encodeHexStr(readBytes(in, length), toLowerCase);
	}
	/**
	 * 读取指定长度的byte数组，不关闭流
	 *
	 * @param in {@link InputStream}，为null返回null
	 * @param length 长度，小于等于0返回空byte数组
	 * @return bytes
	 * @throws IOException IO异常
	 */
	public static byte[] readBytes(InputStream in, int length) throws IOException {
		if (null == in) {
			return null;
		}
		if (length <= 0) {
			return new byte[0];
		}

		byte[] b = new byte[length];
		int readLength;
		try {
			readLength = in.read(b);
		} catch (IOException e) {
			throw new IOException(e);
		}
		if (readLength > 0 && readLength < length) {
			byte[] b2 = new byte[length];
			System.arraycopy(b, 0, b2, 0, readLength);
			return b2;
		} else {
			return b;
		}
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 *
	 * @param data        byte[]
	 * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
	 * @return 十六进制String
	 */
	public static String encodeHexStr(byte[] data, boolean toLowerCase) {
		return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}
	/**
	 * 将字节数组转换为十六进制字符串
	 *
	 * @param data     byte[]
	 * @param toDigits 用于控制输出的char[]
	 * @return 十六进制String
	 */
	private static String encodeHexStr(byte[] data, char[] toDigits) {
		return new String(encodeHex(data, toDigits));
	}

	/**
	 * 将字节数组转换为十六进制字符数组
	 *
	 * @param data     byte[]
	 * @param toDigits 用于控制输出的char[]
	 * @return 十六进制char[]
	 */
	private static char[] encodeHex(byte[] data, char[] toDigits) {
		final int len = data.length;
		final char[] out = new char[len << 1];//len*2
		// two characters from the hex value.
		for (int i = 0, j = 0; i < len; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];// 高位
			out[j++] = toDigits[0x0F & data[i]];// 低位
		}
		return out;
	}
}
